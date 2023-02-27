package com.example.baseproject.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.baseproject.database.HseRepository;
import com.example.baseproject.database.entities.GroupEntity;
import com.example.baseproject.database.entities.TeacherEntity;
import com.example.baseproject.database.entities.TimeTableWithTeacherEntity;
import com.example.baseproject.shedulefiles.ScheduleMode;
import com.example.baseproject.utils.ScheduleSupportLibrary;


import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final HseRepository repository;
    private final ScheduleSupportLibrary library;


    // параметр application – особенность AndroidViewModel.
    // это позваоляет нам обратиться к контексту
    // и не вызовет учечку памяти.
    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new HseRepository(application);
        library = new ScheduleSupportLibrary();
    }

    public LiveData<List<GroupEntity>> getGroups() {
        return repository.getGroups();
    }

    public LiveData<List<TeacherEntity>> getTeachers() {
        return repository.getTeachers();
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacherByDate(Date date) {
        return repository.getTimeTableTeacherByDate(date);
    }



    //  по дате и по ID
    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndGroupId(Date date, int groupId) {
        return repository.getTimeTableByDateAndGroupId(date, groupId);
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndTeacherId(Date date, int groupId) {
        return repository.getTimeTableByDateAndTeacherId(date, groupId);
    }


    //  Временной промежуток getTimeTableStudentInRange
    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableForDay(Date currentDate, int id, ScheduleMode mode) {
        // с начала текущего дня
        Date startDate = library.getStartDay(currentDate);
        // до конца этого дня
        Date endDate = library.getEndDay(currentDate);
        switch (mode) {
            case STUDENT:
                return repository.getTimeTableStudentInRange(startDate, endDate, id);
            case TEACHER:
                return repository.getTimeTableTeacherInRange(startDate, endDate, id);
        }
        return null;
    }

    public LiveData<List<TimeTableWithTeacherEntity>> getTimeTableForWeek(Date currentDate, int id, ScheduleMode mode){
        // с начала текущего дня
        Date startDate = library.getStartWeek(currentDate);
        // до конца текущей недели
        Date endDate = library.getEndWeek(currentDate);

        switch (mode) {
            case STUDENT:
                return repository.getTimeTableStudentInRange(startDate, endDate, id);
            case TEACHER:
                return repository.getTimeTableTeacherInRange(startDate, endDate, id);
        }
        return null;
    }

}
