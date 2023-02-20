package com.example.baseproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.baseproject.ViewModels.MainViewModel;
import com.example.baseproject.ViewModels.TimeViewModel;
import com.example.baseproject.shedulefiles.ScheduleMode;
import com.example.baseproject.shedulefiles.ScheduleType;
import com.example.baseproject.utils.Group;
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

public abstract class BaseActivity extends AppCompatActivity {
    protected MainViewModel mainViewModel;
    protected TimeViewModel timeViewModel;

    private final static String TAG = "BaseActivity";
    public final static String URL = "http://api.ipgeolocation.io/ipgeo?apiKey=b03018f75ed94023a005637878ec0977";

    protected TextView time;

    protected OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                                            // owner и фабрика (второй параметр опционально)
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        timeViewModel = new ViewModelProvider(this).get(TimeViewModel.class);

        getTime();
        timeViewModel.getDate().observe(this, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                    showTime(date);
            }
        });
    }

    protected void initTime() {
//        only for testing
//        currentTime = Calendar.getInstance().getTime();

//        if (currentTime == null)
//            getTime();
//        else
//            showTime(currentTime);
    }


    protected void showTime(Date date) {
        if (date == null)
            return;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM", new Locale("ru"));
        time.setText(simpleDateFormat.format(date));
    }


    protected void showScheduleImpl(ScheduleMode mode, ScheduleType type, Group group){
        Intent intent = new Intent(this, ScheduleActivity.class);

        intent.putExtra(ScheduleActivity.ARG_NAME, group.getName());
        intent.putExtra(ScheduleActivity.ARG_ID, group.getId());
        intent.putExtra(ScheduleActivity.ARG_MODE, mode);
        intent.putExtra(ScheduleActivity.ARG_TYPE, type);
        intent.putExtra(ScheduleActivity.ARG_DATE, timeViewModel.getDate().getValue());
        startActivity(intent);
    }



    public void getTime() {
        Request request = new Request.Builder().url(URL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                parseResponse(response);
            }

            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "getTime", e);
            }
        });
    }


    private void parseResponse(Response response){
        Gson gson = new Gson();
        ResponseBody responseBody = response.body();
        try{
            if(responseBody==null)
                return;
            String string = responseBody.string();
            Log.d(TAG, string);
            TimeResponse timeResponse = gson.fromJson(string, TimeResponse.class);
            String currentTimeVal = timeResponse.getTimeZone().getCurrentTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSX", Locale.getDefault());
            Date dateTime = simpleDateFormat.parse(currentTimeVal);
            runOnUiThread(() -> {
                showTime(dateTime);
                timeViewModel.dateMutableLiveData.setValue(dateTime);
            });
        }
        catch (Exception e){
            Log.e(TAG, "", e);
        }
    }
}