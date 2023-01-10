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
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class StudentActivity extends AppCompatActivity {
    private TextView time, status, subject, cabinet, corp, teacher;
    Date currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
//        enumeration(course, year, groupNumber);
        final Spinner spinner = findViewById(R.id.activity_student_groupList);

        List<Group> groups = new ArrayList();
        initGroupList(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

        time = findViewById(R.id.activity_student_time);
        initTime();

        status = findViewById(R.id.activity_student_status);
        subject = findViewById(R.id.activity_student_subject);
        cabinet = findViewById(R.id.activity_student_cabinet);
        corp = findViewById(R.id.activity_student_building);
        teacher = findViewById(R.id.activity_student_teacher);

        initData();
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

    private void initGroupList(List<StudentActivity.Group> groups)
    {
        List<List<String>> lists = Arrays.asList(
                Arrays.asList("ПИ","БИ"),
                Arrays.asList("19", "20"),
                Arrays.asList("1", "2", "3"));

        List<String> result = new ArrayList<>();

        generatePermutations(lists, result, 0, "");

        for (int i = 1; i <= result.size(); i++)
            groups.add(new StudentActivity.Group(i, result.get(i-1).substring(1)));
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


    static class Group {
        private Integer id;
        private String name;

        public Group(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}