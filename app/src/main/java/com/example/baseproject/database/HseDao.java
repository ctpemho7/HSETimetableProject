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

import java.util.Date;
import java.util.List;

@Dao
public interface HseDao {

    /// Group
    @Query("SELECT * FROM `group`")
    LiveData<List<GroupEntity>> getAllGroups();

    @Insert
    void insertGroup(List<GroupEntity> data);

    @Delete
    void deleteGroup(GroupEntity group);



    /// Teacher
    @Query("SELECT * FROM `teacher`")
    LiveData<List<TeacherEntity>> getAllTeachers();

    @Insert
    void insertTeacher(List<TeacherEntity> data);

    @Delete
    void deleteTeacher(TeacherEntity teacher);



    /// TimeTableEntity
    @Query("SELECT * FROM `time_table`")
    LiveData<List<TimeTableEntity>> getAllTimeTable();

    @Transaction
    @Query("SELECT * FROM `time_table`")
    LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacher();

    @Insert
    void insertTimeTable(List<TimeTableEntity> data);



    // get By Date and ID
    @Transaction
    @Query("SELECT * FROM `time_table` " +
            " WHERE :date BETWEEN time_start AND time_end " +
            " AND group_id = :group_id ")
    LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndGroupId(Date date, int group_id);

    @Transaction
    @Query("SELECT * FROM `time_table` " +
            " WHERE teacher_id = :teacher_id " +
            " AND :date BETWEEN time_start AND time_end")
    LiveData<List<TimeTableWithTeacherEntity>> getTimeTableByDateAndTeacherId(Date date, int teacher_id);



    // get in Data Range
    @Transaction
    @Query("SELECT * FROM `time_table` " +
            " WHERE group_id = :group_id " +
            " AND :start <= time_end " +
            " AND :end >= time_end" +
            " ORDER BY time_start ASC")
    LiveData<List<TimeTableWithTeacherEntity>> getTimeTableStudentInRange(Date start, Date end,  int group_id);

    @Transaction
    @Query("SELECT * FROM `time_table` " +
            " WHERE teacher_id = :teacher_id " +
            " AND :start <= time_end " +
            " AND :end >= time_end " +
            " ORDER BY time_start ASC")
    LiveData<List<TimeTableWithTeacherEntity>> getTimeTableTeacherInRange(Date start, Date end, int teacher_id);

}
