package com.example.baseproject.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.baseproject.database.entities.GroupEntity;
import com.example.baseproject.database.entities.TeacherEntity;
import com.example.baseproject.database.entities.TimeTableEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class DatabaseManager {
    private SimpleDateFormat dataFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

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
        String TAG = "Database Manager";
        Log.d(TAG, "init data");

        //группы
        List<GroupEntity> groups = new ArrayList<>();
        GroupEntity group = new GroupEntity();
        group.id = 1;
        group.name = "ПИ-20-1";
        groups.add(group);
        group = new GroupEntity();
        group.id = 2;
        group.name = "ПИ-20-2";
        groups.add(group);
        DatabaseManager.getInstance(context).getHseDao().insertGroup(groups);

        //учителя
        List<TeacherEntity> teachers = new ArrayList<>();
        TeacherEntity teacher = new TeacherEntity();
        teacher.id = 1;
        teacher.fio = "Плаксин Михаил Александрович";
        teachers.add(teacher);

        teacher = new TeacherEntity();
        teacher.id = 2;
        teacher.fio = "Жегалкина Афросинья Полиномовна";
        teachers.add(teacher);

        teacher = new TeacherEntity();
        teacher.id = 3;
        teacher.fio = "Яборов Андрей Владимирович";
        teachers.add(teacher);

        teacher = new TeacherEntity();
        teacher.id = 4;
        teacher.fio = "Соколов Евгений Андреевич";
        teachers.add(teacher);

        teacher = new TeacherEntity();
        teacher.id = 5;
        teacher.fio = "Лядова Людмила Николаевна";
        teachers.add(teacher);
        DatabaseManager.getInstance(context).getHseDao().insertTeacher(teachers);


        //расписание
        // сегодня по одной паре
        List<TimeTableEntity> timeTables = new ArrayList<>();
        TimeTableEntity timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 1;
        timeTableEntity.cabinet = "Ауд. 505";
        timeTableEntity.subGroup = "ПИ-20-2";
        timeTableEntity.subjName = "Обеспечение качества и Тестирование";
        timeTableEntity.corp = "бульвар Гагарина 37а";
        timeTableEntity.type = 0;
        timeTableEntity.timeStart = dateFromString(getLessonTime(0, 0, -5));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(0, 0, 30));
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 1;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 2;
        timeTableEntity.cabinet = "Ауд. 506";
        timeTableEntity.subGroup = "ПИ-20-1";
        timeTableEntity.subjName = "Мобильная разработка";
        timeTableEntity.corp = "бульвар Гагарина 37а";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = dateFromString(getLessonTime(0, 0, -5));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(0, 0, 30));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 3;
        timeTables.add(timeTableEntity);


        // завтра две пары
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 3;
        timeTableEntity.cabinet = "Ауд. 320";
        timeTableEntity.subGroup = "ПИ-20-1";
        timeTableEntity.subjName = "Экономика";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 2;
        timeTableEntity.timeStart = dateFromString(getLessonTime(1, 2, 0));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(1, 3, 20));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 4;
        timeTableEntity.cabinet = "online";
        timeTableEntity.subGroup = "ПИ-20-1";
        timeTableEntity.subjName = "Прикладные задачи анализа данных";
        timeTableEntity.corp = "ONLINE";
        timeTableEntity.type = 2;
        timeTableEntity.timeStart = dateFromString(getLessonTime(1, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(1, 5, 0));
        timeTableEntity.groupId = 1;
        timeTableEntity.teacherId = 4;
        timeTables.add(timeTableEntity);

        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 5;
        timeTableEntity.cabinet = "Ауд. 301";
        timeTableEntity.subGroup = "ПИ-20-2";
        timeTableEntity.subjName = "Экономика";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 2;
        timeTableEntity.timeStart = dateFromString(getLessonTime(1, 2, 0));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(1, 3, 20));
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 6;
        timeTableEntity.cabinet = "Ауд. 202";
        timeTableEntity.subGroup = "ПИ-20-2";
        timeTableEntity.subjName = "Правовая Грамотность";
        timeTableEntity.corp = "Студенческая 38";
        timeTableEntity.type = 1;
        timeTableEntity.timeStart = dateFromString(getLessonTime(1, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(1, 5, 0));
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 2;
        timeTables.add(timeTableEntity);

        // а у ПИ-20-2 ещё одна послезавтра :)
        timeTableEntity = new TimeTableEntity();
        timeTableEntity.id = 7;
        timeTableEntity.cabinet = "Ауд. 505";
        timeTableEntity.subGroup = "ПИ-20-2";
        timeTableEntity.subjName = "Базы данных";
        timeTableEntity.corp = "бульвар Гагарина 37а";
        timeTableEntity.type = 2;
        timeTableEntity.timeStart = dateFromString(getLessonTime(2, 3, 40));
        timeTableEntity.timeEnd = dateFromString(getLessonTime(2, 5, 0));
        timeTableEntity.groupId = 2;
        timeTableEntity.teacherId = 5;
        timeTables.add(timeTableEntity);

        DatabaseManager.getInstance(context).getHseDao().insertTimeTable(timeTables);
    }

    private Date dateFromString(String val){

        try{
            return dataFormat.parse(val);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("init data", "dateFromString ParseException ERROR");
        }

        return null;
    }


    private String getLessonTime(int days, int hours, int minutes){
        Calendar start = Calendar.getInstance();
        hours = 24 * days + hours;

        start.add(Calendar.HOUR, hours);
        start.add(Calendar.MINUTE, minutes);


        return dataFormat.format(start.getTime());
    }

}
