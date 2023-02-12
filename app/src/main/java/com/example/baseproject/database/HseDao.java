package com.example.baseproject.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.baseproject.database.entities.GroupEntity;
import com.example.baseproject.database.entities.TeacherEntity;
import com.example.baseproject.database.entities.TimeTableEntity;
import com.example.baseproject.database.entities.TimeTableWithTeacherEntity;

import java.util.List;

@Dao
public interface HseDao {

    @Query("SELECT * FROM `group`")
    LiveData<List<GroupEntity>> getAllGroups();

    @Insert
    void insertGroup(List<GroupEntity> data);

    @Delete
    void deleteGroup(GroupEntity group);

    @Query("SELECT * FROM `teacher`")
    LiveData<List<TeacherEntity>> getAllTeachers();

    @Insert
    void insertTeacher(List<TeacherEntity> data);

    @Delete
    void deleteTeacher(TeacherEntity teacher);

    @Query("SELECT * FROM `time_table`")
    LiveData<List<TimeTableEntity>> getAllTimeTable();

    @Transaction
    @Query("SELECT * FROM `time_table`")
    LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacher();

    @Insert
    void insertTimeTable(List<TimeTableEntity> data);
}
