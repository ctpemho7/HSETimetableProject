package com.example.baseproject.utils;
import com.google.gson.annotations.SerializedName;

public class TimeResponse{
    @SerializedName("time_zone")
    private MyTimeZone myTimeZone;

    public MyTimeZone getTimeZone(){
        return myTimeZone;
    }

    public void setTimeZone(MyTimeZone myTimeZone) {
        this.myTimeZone = myTimeZone;
    }
}
