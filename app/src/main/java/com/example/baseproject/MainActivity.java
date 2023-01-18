package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View buttonStudent = findViewById(R.id.buttonstud);
        View buttonTeacher = findViewById(R.id.buttonprof);
        View buttonSettings = findViewById(R.id.buttonSettings);

        buttonStudent.setOnClickListener(view -> {
//                getText(buttonStudent);
            showStudentActivity();
        });

        buttonTeacher.setOnClickListener(view -> {
//                getText(buttonTeacher);
            showTeacherActivity();
        });

        buttonSettings.setOnClickListener(view -> {
            showSettingsActivity();
        });
    }

    private void getText(View b){
        Button button = (Button) b;
        Toast.makeText(this, button.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private void showStudentActivity() {
        Intent intent = new Intent(this, StudentActivity.class);
        startActivity(intent);
    }
    private void showTeacherActivity() {
        Intent intent = new Intent(this, TeacherActivity.class);
        startActivity(intent);
    }
    private void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}