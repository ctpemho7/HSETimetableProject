package com.example.baseproject.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.baseproject.database.HseRepository;
import com.example.baseproject.database.entities.GroupEntity;
import com.example.baseproject.database.entities.TeacherEntity;
import com.example.baseproject.database.entities.TimeTableEntity;
import com.example.baseproject.database.entities.TimeTableWithTeacherEntity;


import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel  {
    private HseRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new HseRepository(application);
    }

    public LiveData<List<GroupEntity>> getGroups() { return repository.getGroups(); }

    public LiveData<List<TeacherEntity>> getTeachers() { return repository.getTeachers(); }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacherByDate(Date date){
        return repository.getTimeTableTeacherByDate(date);
    }

}
