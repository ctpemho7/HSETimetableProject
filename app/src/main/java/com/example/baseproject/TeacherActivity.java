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

import com.example.baseproject.database.entities.TeacherEntity;
import com.example.baseproject.database.entities.TimeTableEntity;
import com.example.baseproject.database.entities.TimeTableWithTeacherEntity;
import com.example.baseproject.shedulefiles.ScheduleMode;
import com.example.baseproject.shedulefiles.ScheduleType;
import com.example.baseproject.utils.Group;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherActivity extends BaseActivity {
    private TextView status, subject, cabinet, corp, teacher;
    private Spinner spinner;

    private ArrayAdapter<Group> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        spinner = findViewById(R.id.activity_teacher_groupList);
        initGroupList();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d("TAG", "selectedItem" + item);
                showTime(currentTime);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        /////// все штуки
        status = findViewById(R.id.activity_teacher_status);
        subject = findViewById(R.id.activity_teacher_subject);
        cabinet = findViewById(R.id.activity_teacher_cabinet);
        corp = findViewById(R.id.activity_teacher_building);
        teacher = findViewById(R.id.activity_teacher_teacher);
        time = findViewById(R.id.activity_teacher_time);

        /// new
        Button scheduleDay = findViewById(R.id.activity_teacher_button_day);
        Button scheduleWeek = findViewById(R.id.activity_teacher_button_week);

        scheduleDay.setOnClickListener(v -> showSchedule(ScheduleType.DAY));
        scheduleWeek.setOnClickListener(v -> showSchedule(ScheduleType.WEEK));


        initData();
        initTime();
    }


    private void initGroupList() {
        mainViewModel.getTeachers().observe(this, new Observer<List<TeacherEntity>>() {
            @Override
            public void onChanged(@Nullable List<TeacherEntity> list) {
                List<Group> groups = new ArrayList<>();
                for (TeacherEntity listEntity : list) {
                    groups.add(new Group(listEntity.id, listEntity.fio));
                }
                adapter.clear();
                adapter.addAll(groups);
            }
        });
    }


    private void initData() {
        initDataFromTimeTable(null);
    }

    private void initDataFromTimeTable(TimeTableWithTeacherEntity timeTableWithTeacherEntity) {
        if (timeTableWithTeacherEntity == null) {
            status.setText(R.string.status);
            subject.setText(R.string.subject);
            cabinet.setText(R.string.cab);
            corp.setText(R.string.corp);
            teacher.setText(R.string.teacher);
            return;
        }

        status.setText(R.string.lesson_in_progress);

        TimeTableEntity timeTableEntity = timeTableWithTeacherEntity.timeTableEntity;

        subject.setText(timeTableEntity.subjName);
        cabinet.setText(timeTableEntity.cabinet);
        corp.setText(timeTableEntity.corp);
        teacher.setText(timeTableWithTeacherEntity.teacherEntity.fio);
    }

    private void showSchedule(ScheduleType type) {
        Object selectedItem = spinner.getSelectedItem();
        if (!(selectedItem instanceof Group)) {
            return;
        }
        showScheduleImpl(ScheduleMode.TEACHER, type, (Group) selectedItem);
    }

    @Override
    protected void showTime(Date dateTime) {
        super.showTime(dateTime);

        Group teacher = getSelectedGroup();

        // по id учителя и текущему времени надо обратиться к getTimeTableByDateAndGroupId и после этого вывести в initDataFromTimeTable
        if (teacher != null) {
            mainViewModel.getTimeTableByDateAndTeacherId(dateTime, teacher.getId())
                    .observe(this, new Observer<TimeTableWithTeacherEntity>() {
                        @Override
                        public void onChanged(TimeTableWithTeacherEntity timeTableWithTeacherEntity) {
                            initDataFromTimeTable(timeTableWithTeacherEntity);
                        }
                    });

        }
    }

    private Group getSelectedGroup() {
        Object selectedItem = spinner.getSelectedItem();

        if (!(selectedItem instanceof Group)) {
            return null;
        }

        return (Group) selectedItem;
    }
}
