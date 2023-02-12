package com.example.baseproject.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.baseproject.database.entities.TeacherEntity;
import com.example.baseproject.database.entities.TimeTableEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class DatabaseManager {

    private DatabaseHelper db;

    private static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context){
        if (instance == null)
            instance = new DatabaseManager(context.getApplicationContext());
        return instance;
    }

    public HseDao getHseDao(){
        return db.hseDao();
    }

    private DatabaseManager(Context context){
        db = Room.databaseBuilder(context,
                        DatabaseHelper.class, DatabaseHelper.DATABASE_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                initData(context);
                            }
                        });
                    }
                })
                .build();
    }

    private void initData(Context context){
        List<TeacherEntity> teachers = new ArrayList<>();
        TeacherEntity teacher = new TeacherEntity();
        teacher.id = 1;
        teacher.fio = "Плаксин Михаил Александрович";
        teachers.add(teacher);
        teacher = new TeacherEntity();
        teacher.id = 2;
        teacher.fio = "Жегалкина Афросинья Полиномовна";
        teachers.add(teacher);
        DatabaseManager.getInstance(context).getHseDao().insertTeacher(teachers);

        List<TimeTableEntity> timeTables = new ArrayList<>();
        TimeTableEntity timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 1;
        timeTableEntity.cabinet = "Ауд. 505";
        timeTableEntity.subGroup = "ПИ";
        timeTableEntity.subjName = "Философия";
        timeTableEntity.corp = "бульвар Гагарина 37а";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = dateFromString("2023-27-01 10:00");
        timeTableEntity.timeEnd = dateFromString("2023-27-01 11:30");
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 1;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 2;
        timeTableEntity.cabinet = "Кабинет 2";
        timeTableEntity.subGroup = "ПИ";
        timeTableEntity.subjName = "Мобильная разработка";
        timeTableEntity.corp = "K1";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = dateFromString("2023-27-01 13:00");
        timeTableEntity.timeEnd = dateFromString("2023-27-01 14:30");
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);
        DatabaseManager.getInstance(context).getHseDao().insertTimeTable(timeTables);
    }

    private Date dateFromString(String val){
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        try{
            return dataFormat.parse(val);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
