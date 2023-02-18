package com.example.baseproject.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class ScheduleSupportLibrary {

    public String getType(int type) {
        String lessonType = "Пара";
        switch (type) {
            case 0:
                lessonType = "Лекция";
                break;
            case 1:
                lessonType = "Семинар";
                break;
            case 2:
                lessonType = "Консультация";
                break;
        }
        return lessonType;
    }

    public Date getStartDay(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public Date getEndDay(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }


    public Date getStartWeek(Date date){
        return getStartDay(date);
    }

    public Date getEndWeek(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        // неделя у нормальных людей начинается с понедельника
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //перемещаемся на воскресенье
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

}
