package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.baseproject.shedulefiles.ScheduleMode;
import com.example.baseproject.shedulefiles.ScheduleType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TeacherActivity extends BaseActivity {
    private TextView status, subject, cabinet, corp, teacher;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        spinner = findViewById(R.id.activity_teacher_groupList);
        List<Group> groups = new ArrayList<>();
        initGroupList(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d("TAG", "selectedItem" + item);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        time = findViewById(R.id.activity_teacher_time);

        status = findViewById(R.id.activity_teacher_status);
        subject = findViewById(R.id.activity_teacher_subject);
        cabinet = findViewById(R.id.activity_teacher_cabinet);
        corp = findViewById(R.id.activity_teacher_building);
        teacher = findViewById(R.id.activity_teacher_teacher);

        initData();
        initTime();

        /// new
        Button scheduleDay = findViewById(R.id.activity_teacher_button_day);
        Button scheduleWeek = findViewById(R.id.activity_teacher_button_week);

        scheduleDay.setOnClickListener(v -> showSchedule(ScheduleType.DAY));
        scheduleWeek.setOnClickListener(v -> showSchedule(ScheduleType.WEEK));
    }


    private void initGroupList(List<Group> groups)
    {
        groups.add(new Group(1, "Преподаватель 1"));
        groups.add(new Group(2, "Преподаватель 2"));
        groups.add(new Group(2, "Преподаватель 3"));
    }


    private void initData(){
        status.setText(R.string.status);
        subject.setText(R.string.subject);
        cabinet.setText(R.string.cab);
        corp.setText(R.string.corp);
        teacher.setText(R.string.teacher);
    }

    private void showSchedule(ScheduleType type){
        Object selectedItem = spinner.getSelectedItem();
        if (!(selectedItem instanceof Group)){
            return;
        }
        showScheduleImpl(ScheduleMode.TEACHER, type, (Group)selectedItem);
    }


////    private void initTime(){
////        Date date  = new Date();
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm, EEEE", Locale.forLanguageTag("ru"));
////
////        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
////        time.setText(simpleDateFormat.format(date));
////    }
}
