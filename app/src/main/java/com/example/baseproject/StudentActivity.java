package com.example.baseproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SafeBrowsingResponse;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.example.baseproject.database.entities.GroupEntity;
import com.example.baseproject.database.entities.TeacherEntity;
import com.example.baseproject.database.entities.TimeTableEntity;
import com.example.baseproject.database.entities.TimeTableWithTeacherEntity;
import com.example.baseproject.shedulefiles.ScheduleMode;
import com.example.baseproject.shedulefiles.ScheduleType;
import com.example.baseproject.utils.Group;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudentActivity extends BaseActivity {
    private TextView status, subject, cabinet, corp, teacher;
    private Spinner spinner;
    private ArrayAdapter<Group> adapter;

    private String TAG = "StudentActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);


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




        spinner = findViewById(R.id.activity_student_groupList);

        initGroupList();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedID) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d("TAG", "selectedItem: " + item);
                showTime(timeViewModel.getDate().getValue());

            }

            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        initTime();
        initData();
    }



    private void initGroupList(){
        mainViewModel.getGroups().observe(this, new Observer<List<GroupEntity>>() {
            @Override
            public void onChanged(@Nullable List<GroupEntity> groupEntities) {
                List<Group> groupsResult = new ArrayList<>();
                for (GroupEntity listEntity : groupEntities) {
                    groupsResult.add(new Group(listEntity.id, listEntity.name));
                }
                adapter.clear();
                adapter.addAll(groupsResult);
                Log.d(TAG, groupsResult.toString());
            }
        });
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
            Toast.makeText(this, "Сейчас у этой группы нет пар!", Toast.LENGTH_SHORT).show();
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

        Group selectedGroup = getSelectedGroup();

        // по id группы и текущему времени надо обратиться к getTimeTableByDateAndGroupId и после этого вывести в initDataFromTimeTable
        if (selectedGroup != null && dateTime != null) {

            mainViewModel.getTimeTableByDateAndGroupId(dateTime, selectedGroup.getId())
                    .observe(this, timeTableWithTeacherEntity -> {
                        initDataFromTimeTable(timeTableWithTeacherEntity);

                    });

        }
    }

    private Group getSelectedGroup(){
        Object selectedItem = spinner.getSelectedItem();

        if (!(selectedItem instanceof Group)){
            return null;
        }

        return (Group) selectedItem;
    }
}