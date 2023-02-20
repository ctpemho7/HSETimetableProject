package com.example.baseproject.ViewModels;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.baseproject.utils.TimeResponse;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TimeViewModel extends AndroidViewModel  {
    public MutableLiveData<Date> dateMutableLiveData;
    private String TAG = "TimeViewModel";

    private final static String URL = "http://api.ipgeolocation.io/ipgeo?apiKey=b03018f75ed94023a005637878ec0977";
    private OkHttpClient client = new OkHttpClient();


    public TimeViewModel(@NonNull Application application) {
        super(application);
        dateMutableLiveData = new MutableLiveData<>();
    }

    // дата
    public LiveData<Date> getDate(){
        return dateMutableLiveData;
    }


}
