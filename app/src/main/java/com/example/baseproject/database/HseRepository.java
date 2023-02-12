package com.example.baseproject.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.baseproject.database.entities.GroupEntity;
import com.example.baseproject.database.entities.TeacherEntity;
import com.example.baseproject.database.entities.TimeTableWithTeacherEntity;

import java.util.Date;
import java.util.List;

public class HseRepository {

    private DatabaseManager databaseManager;
    private HseDao dao;

    public HseRepository(Context context){
        databaseManager = DatabaseManager.getInstance(context);
        dao = databaseManager.getHseDao();
    }

    public LiveData<List<GroupEntity>> getGroups(){
        return dao.getAllGroups();
    }

    public LiveData<List<TeacherEntity>> getTeachers(){
        return dao.getAllTeachers();
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacherByDate(Date date){
        return dao.getTimeTableTeacher();
    }
}
