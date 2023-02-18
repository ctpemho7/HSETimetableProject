package com.example.baseproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baseproject.ViewModels.MainViewModel;
import com.example.baseproject.database.entities.TimeTableEntity;
import com.example.baseproject.database.entities.TimeTableWithTeacherEntity;
import com.example.baseproject.shedulefiles.ItemAdapter;
import com.example.baseproject.shedulefiles.ScheduleItem;
import com.example.baseproject.shedulefiles.ScheduleItemHeader;
import com.example.baseproject.shedulefiles.ScheduleMode;
import com.example.baseproject.shedulefiles.ScheduleType;
import com.example.baseproject.utils.ScheduleSupportLibrary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class ScheduleActivity extends BaseActivity {
    private final String TAG = "ScheduleActivity";
    private final ScheduleSupportLibrary library = new ScheduleSupportLibrary();

    private ScheduleType type;
    private ScheduleMode mode;
    private String name;
    private Integer id;
    private TextView title;
    private TextView time;
    private Date date;

    public static String ARG_TYPE = "ARG_TYPE";
    public static String ARG_MODE = "ARG_MODE";
    public static String ARG_NAME = "ARG_NAME";
    public static String ARG_DATE = "ARG_DATE";
    public static String ARG_ID = "ARG_ID";


    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    List<ScheduleItem> listOfScheduleItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shedule);


        type = (ScheduleType) getIntent().getSerializableExtra(ARG_TYPE);
        mode = (ScheduleMode) getIntent().getSerializableExtra(ARG_MODE);
        name = getIntent().getStringExtra(ARG_NAME);
        id = (Integer) getIntent().getSerializableExtra(ARG_ID);
        date = (Date) getIntent().getSerializableExtra(ARG_DATE);

        title = findViewById(R.id.title);
        time = findViewById(R.id.time);

        recyclerView = findViewById(R.id.listView);
        // DEFAULT_ORIENTATION = VERTICAL
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);

        initData();
    }


    private void initData() {
        //        initTitle
        initTitle();
        //        initTime
        initTime();
        //        ScheduleItems
        initScheduleItemsWithViewModel();
    }

    @Override
    protected void initTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM", new Locale("ru"));
        time.setText(simpleDateFormat.format(date));
    }


    private void initTitle() {
        title.setText(name);
    }


    private void initScheduleItemsWithViewModel() {

        Observer<List<TimeTableWithTeacherEntity>> observerForSchedule = new Observer<List<TimeTableWithTeacherEntity>>() {
            @Override
            public void onChanged(List<TimeTableWithTeacherEntity> timeTableWithTeacherEntities) {
                // создать расписание из timeTableWithTeacherEntities
                listOfScheduleItems = scheduleBuilder(timeTableWithTeacherEntities);
                // прикрепить его к адаптеру
                adapter.setDataList(listOfScheduleItems);
                adapter.notifyDataSetChanged();

            }
        };
        filterItem(observerForSchedule);

    }


    private List<ScheduleItem> scheduleBuilder(List<TimeTableWithTeacherEntity> timeTableWithTeacherEntities) {
        if (timeTableWithTeacherEntities == null ||
                timeTableWithTeacherEntities.isEmpty()) {
            Toast.makeText(this, "Нет пар на выбранный период!", Toast.LENGTH_SHORT).show();
            return null;
        }


        // для отображения даты
        SimpleDateFormat headerDateFormat = new SimpleDateFormat("EEEE, dd MMMM", new Locale("ru"));
        SimpleDateFormat hoursFormat = new SimpleDateFormat("HH:mm", new Locale("ru"));


        // сортируем по дате начала каждой пары
        // предполагается, что у одной группы не может быть одновременно две пары.
        List<TimeTableWithTeacherEntity> sortedList = timeTableWithTeacherEntities.stream()
                .sorted(Comparator.comparing(item -> item.timeTableEntity.timeStart))
                .collect(Collectors.toList());

        // здесь храним расписание
        List<ScheduleItem> list = new ArrayList<>();

        // для первого элемента
        String lastDate = "";
        String currentDate;

        for (TimeTableWithTeacherEntity entity : sortedList) {
            TimeTableEntity timeTableEntity = entity.timeTableEntity;

            // проверяем, нет ли такой хеадера, если нет, то создаём новый
            currentDate = headerDateFormat.format(timeTableEntity.timeStart);
            if (!currentDate.equals(lastDate)){

                ScheduleItemHeader header = new ScheduleItemHeader();
                header.setTitle(headerDateFormat.format(timeTableEntity.timeEnd));
                list.add(header);

                lastDate = currentDate;
            }

            String start = hoursFormat.format(timeTableEntity.timeStart);
            String end = hoursFormat.format(timeTableEntity.timeEnd);
            String type = library.getType(timeTableEntity.type);
            String name = timeTableEntity.subjName;
            String place = timeTableEntity.cabinet + ", " + timeTableEntity.corp;
            String teacher_fio = entity.teacherEntity.fio;

            ScheduleItem item = new ScheduleItem();
            item.setStart(start);
            item.setEnd(end);
            item.setType(type);
            item.setName(name);
            item.setPlace(place);
            item.setTeacher(teacher_fio);
            list.add(item);
        }
        return list;
    }

    private void filterItem(Observer<List<TimeTableWithTeacherEntity>> observer) {

        switch (type) {
            case DAY: {
                mainViewModel.getTimeTableForDay(date, id, mode).observe(this, observer);
                Log.d(TAG, "we are in DAY");
                break;
            }

            case WEEK: {
                mainViewModel.getTimeTableForWeek(date, id, mode).observe(this, observer);
                Log.d(TAG, "we are in WEEK");
                break;
            }

            default: {
                Log.d(TAG, type + " we don't support it");
                break;
            }
        }
    }



    private void initScheduleItems() {
        List<ScheduleItem> list = null;

        switch (type) {
            case DAY: {
                list = getDaySchedule();
                Log.d(TAG, "we are in DAY");
                break;
            }

            case WEEK: {
                list = getWeekSchedule();
                Log.d(TAG, "we are in WEEK");
                break;
            }

            default: {
                Log.d(TAG, type + " we don't support it");
                break;
            }
        }
        adapter.setDataList(list);

    }

    private List<ScheduleItem> getDaySchedule() {
        List<ScheduleItem> list = new ArrayList<>();

        ScheduleItemHeader header = new ScheduleItemHeader();
        header.setTitle("Вторник, 7 февраля");
        list.add(header);

        ScheduleItem item = new ScheduleItem();
        item.setStart("9:40");
        item.setEnd("11:00");
        item.setType("Практическое занятие");
        item.setName("НИС Разработка мобильных приложений");
        item.setPlace("Ауд. 504, Бульвар Гагарина, 37а");
        item.setTeacher("Пред. Яборов Андрей Владимирович");
        list.add(item);

        return list;
    }

    private List<ScheduleItem> getWeekSchedule() {
        List<ScheduleItem> list = new ArrayList<>();

        ScheduleItemHeader header = new ScheduleItemHeader();
        header.setTitle("Понедельник, 6 февраля");
        list.add(header);

        ScheduleItem item = new ScheduleItem();
        item.setStart("13:10");
        item.setEnd("14:30");
        item.setType("Практическое занятие");
        item.setName("Правовая грамотность");
        item.setPlace("Ауд. 315, Студенческая 38");
        item.setTeacher("Пред. Литвинова Юлия Андреевна");
        list.add(item);

        header = new ScheduleItemHeader();
        header.setTitle("Вторник, 7 февраля");
        list.add(header);

        item = new ScheduleItem();
        item.setStart("9:40");
        item.setEnd("11:00");
        item.setType("Практическое занятие");
        item.setName("НИС Разработка мобильных приложений");
        item.setPlace("Ауд. 504, Бульвар Гагарина 37а");
        item.setTeacher("Пред. Яборов Андрей Владимирович");
        list.add(item);

        header = new ScheduleItemHeader();
        header.setTitle("Вторник, 9 февраля");
        list.add(header);

        item = new ScheduleItem();
        item.setStart("13:10");
        item.setEnd("14:30");
        item.setType("Практическое занятие");
        item.setName("Прикладные задачи анализа данных (рус)");
        item.setPlace("online, ONLINE");
        item.setTeacher("Пред. Кантонистова Елена Олеговна");
        list.add(item);

        item = new ScheduleItem();
        item.setStart("15:00");
        item.setEnd("16:20");
        item.setType("Лекция");
        item.setName("Прикладные задачи анализа данных (рус)");
        item.setPlace("online, ONLINE");
        item.setTeacher("Доц. Соколов Евгений Андреевич");
        list.add(item);

        return list;
    }


}