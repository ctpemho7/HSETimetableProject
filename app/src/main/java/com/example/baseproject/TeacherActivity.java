package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TeacherActivity extends AppCompatActivity {
    private TextView time, status, subject, cabinet, corp, teacher;
    Date currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        final Spinner spinner = findViewById(R.id.activity_teacher_groupList);
        List<StudentActivity.Group> groups = new ArrayList<>();
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
        initTime();

        status = findViewById(R.id.activity_teacher_status);
        subject = findViewById(R.id.activity_teacher_subject);
        cabinet = findViewById(R.id.activity_teacher_cabinet);
        corp = findViewById(R.id.activity_teacher_building);
        teacher = findViewById(R.id.activity_teacher_teacher);

        initData();
    }


    private void initGroupList(List<StudentActivity.Group> groups)
    {
        groups.add(new StudentActivity.Group(1, "Преподаватель 1"));
        groups.add(new StudentActivity.Group(2, "Преподаватель 2"));
        groups.add(new StudentActivity.Group(2, "Преподаватель 3"));
    }


    private void initTime(){
        Date date  = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm, EEEE", Locale.forLanguageTag("ru"));

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        time.setText(simpleDateFormat.format(date));
    }



    private void initData(){
        status.setText(R.string.status);
        subject.setText(R.string.subject);
        cabinet.setText(R.string.cab);
        corp.setText(R.string.corp);
        teacher.setText(R.string.teacher);
    }
}
