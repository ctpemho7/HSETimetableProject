package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.baseproject.shedulefiles.ItemAdapter;
import com.example.baseproject.shedulefiles.OnItemClick;
import com.example.baseproject.shedulefiles.ScheduleItem;
import com.example.baseproject.shedulefiles.ScheduleItemHeader;
import com.example.baseproject.shedulefiles.ScheduleMode;
import com.example.baseproject.shedulefiles.ScheduleType;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends BaseActivity {
    private String TAG = "ScheduleActivity";

    private ScheduleType type;
    private ScheduleMode mode;
    private String name;
    private TextView title;

    public static String ARG_TYPE = "ARG_TYPE";
    public static String ARG_MODE = "ARG_MODE";
    public static String ARG_NAME = "ARG_NAME";


    RecyclerView recyclerView;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shedule);


        type = (ScheduleType) getIntent().getSerializableExtra(ARG_TYPE);
        mode = (ScheduleMode) getIntent().getSerializableExtra(ARG_MODE);
        name = getIntent().getStringExtra(ARG_NAME);

        title = findViewById(R.id.title);
        time = findViewById(R.id.time);

        recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(new OnItemClick() {
            public void onClick(ScheduleItem data) {
            }
        });
        recyclerView.setAdapter(adapter);

        initData();
    }



    private void initData() {
        //        initTitle
        initTitle();
        //        initTime
        initTime();
        //        ScheduleItems
        initScheduleItems();
    }

    private void initTitle() {
        title.setText(name);
    }


    private void initScheduleItems() {
        List<ScheduleItem> list = null;

        switch (type){
            case DAY:
            {
                list = getDaySchedule();
                Log.d(TAG,  "we are in DAY");
                break;
            }

            case WEEK: {
                list = getWeekSchedule();
                Log.d(TAG, "we are in WEEK");
                break;
            }

            default: {
                Log.d(TAG, type.toString() + " we don't support it");
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