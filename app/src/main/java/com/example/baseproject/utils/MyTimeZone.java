package com.example.baseproject.utils;

import com.google.gson.annotations.SerializedName;

public class MyTimeZone {
    @SerializedName("current_time")
    private String currentTime;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
