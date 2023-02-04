package com.example.baseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.baseproject.shedulefiles.ScheduleMode;
import com.example.baseproject.shedulefiles.ScheduleType;
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
    private final static String TAG = "BaseActivity";
    public final static String URL = "http://api.ipgeolocation.io/ipgeo?apiKey=b03018f75ed94023a005637878ec0977";

    protected TextView time;
    protected Date currentTime;

    protected OkHttpClient client = new OkHttpClient();

    protected void initTime() {
        getTime();
    }

    protected void getTime() {
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


    private void parseResponse(Response response) throws IOException {
        Gson gson = new Gson();
        ResponseBody body = response.body();

        try {
            if (body == null)
                return;

            String string = body.string();
            Log.d(TAG, string);

            TimeResponse timeResponse = gson.fromJson(string, TimeResponse.class);

            String currentTimeVal = timeResponse.getTimeZone().getCurrentTime();
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.getDefault());
            Date dateTime = dataFormat.parse(currentTimeVal);

            //run on UI thread
            runOnUiThread(() -> showTime(dateTime));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    protected void showTime(Date date) {
        if (date == null)
            return;

        currentTime = date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM", new Locale("ru"));
        time.setText(simpleDateFormat.format(date));
    }


    protected void showScheduleImpl(ScheduleMode mode, ScheduleType type, Group group){
        Intent intent = new Intent(this, ScheduleActivity.class);

        intent.putExtra(ScheduleActivity.ARG_NAME, group.getName());
        intent.putExtra(ScheduleActivity.ARG_MODE, mode);
        intent.putExtra(ScheduleActivity.ARG_TYPE, type);
        startActivity(intent);
    }
}