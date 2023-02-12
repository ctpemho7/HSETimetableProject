package com.example.baseproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.example.baseproject.database.entities.GroupEntity;
import com.example.baseproject.database.entities.TimeTableEntity;
import com.example.baseproject.database.entities.TimeTableWithTeacherEntity;
import com.example.baseproject.shedulefiles.ScheduleMode;
import com.example.baseproject.shedulefiles.ScheduleType;
import com.example.baseproject.utils.Group;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class StudentActivity extends BaseActivity {
    private TextView status, subject, cabinet, corp, teacher;
    private Spinner spinner;

    private ArrayAdapter<Group> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //enumeration(course, year, groupNumber);
        spinner = findViewById(R.id.activity_student_groupList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedID) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d("TAG", "selectedItem: " + item);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /////// все штуки
        status = findViewById(R.id.activity_student_status);
        subject = findViewById(R.id.activity_student_subject);
        cabinet = findViewById(R.id.activity_student_cabinet);
        corp = findViewById(R.id.activity_student_building);
        teacher = findViewById(R.id.activity_student_teacher);
        time = findViewById(R.id.activity_student_time);

        /// new
        Button scheduleDay = findViewById(R.id.activity_student_button_day);
        Button scheduleWeek = findViewById(R.id.activity_student_button_week);

        scheduleDay.setOnClickListener(v -> showSchedule(ScheduleType.DAY));
        scheduleWeek.setOnClickListener(v -> showSchedule(ScheduleType.WEEK));


        initTime();
        initData();
        initGroupList();
    }



    private void initGroupList(){
        mainViewModel.getGroups().observe(this, groupEntities -> {
            List<Group> groupResult = new ArrayList<>();
            for (GroupEntity groupEntity : groupEntities){
                groupResult.add(new Group(groupEntity.id, groupEntity.name));
            }
            adapter.clear();
            adapter.addAll(groupResult);
        });

//////        List<List<String>> lists = Arrays.asList(
//////                Arrays.asList("ПИ","БИ"),
//////                Arrays.asList("19", "20"),
//////                Arrays.asList("1", "2", "3"));
//////
//////        List<String> result = new ArrayList<>();
//////
//////        generatePermutations(lists, result, 0, "");
//////
//////        for (int i = 1; i <= result.size(); i++)
//////            groups.add(new Group(i, result.get(i-1).substring(1)));
    }

    static void generatePermutations(List<List<String>> lists, List<String> result, int depth, String current) {
//        только честное заимствование https://stackoverflow.com/questions/17192796/generate-all-combinations-from-multiple-lists
        if (depth == lists.size()) {
            result.add(current);
            return;
        }

        for (int i = 0; i < lists.get(depth).size(); i++) {
            generatePermutations(lists, result, depth + 1, current + "-" +lists.get(depth).get(i));
        }
    }


    private void initData(){
        initDataFromTimeTable(null);
    }


    private void showSchedule(ScheduleType type){
        Object selectedItem = spinner.getSelectedItem();
        if (!(selectedItem instanceof Group)){
            return;
        }
        showScheduleImpl(ScheduleMode.STUDENT, type, (Group)selectedItem);
    }


    private void initDataFromTimeTable(TimeTableWithTeacherEntity timeTableWithTeacherEntity){
        if (timeTableWithTeacherEntity == null){
            status.setText(R.string.status);
            subject.setText(R.string.subject);
            cabinet.setText(R.string.cab);
            corp.setText(R.string.corp);
            teacher.setText(R.string.teacher);
            return;
        }
            TimeTableEntity timeTableEntity = timeTableWithTeacherEntity.timeTableEntity;

            subject.setText(timeTableEntity.subjName);
            cabinet.setText(timeTableEntity.cabinet);
            corp.setText(timeTableEntity.corp);
            teacher.setText(timeTableWithTeacherEntity.teacherEntity.fio);

            status.setText(R.string.lesson_in_progress);
    }


    @Override
    protected void showTime(Date dateTime){
        super.showTime(dateTime);
        mainViewModel.getTimeTableTeacherByDate(dateTime).observe(this, new Observer<List<TimeTableWithTeacherEntity>>() {
            @Override
            public void onChanged(@Nullable List<TimeTableWithTeacherEntity> list) {
                for (TimeTableWithTeacherEntity listEntity : list){
                    Log.d("tag", listEntity.timeTableEntity.subjName + " " + listEntity.teacherEntity.fio);
                    if (getSelectedGroup() != null && getSelectedGroup().getId().equals(listEntity.timeTableEntity.groupId)){
                        initDataFromTimeTable(listEntity);
                    }
                }
            }
        });
    }

    private Group getSelectedGroup(){
        Object selectedItem = spinner.getSelectedItem();

        if (!(selectedItem instanceof Group)){
            return null;
        }

        return (Group) selectedItem;
    }

////    private void initTime(){
////        Date date  = new Date();
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm, EEEE", Locale.forLanguageTag("ru"));
////
////        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
////        time.setText(simpleDateFormat.format(date));
////    }
}