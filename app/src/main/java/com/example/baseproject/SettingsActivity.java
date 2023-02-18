package com.example.baseproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorManager;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.baseproject.utils.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "SettingsActivity";
    private static final String SAVED_NAME = "username";
    private static final String SAVED_PHOTO = "photo";
    private final Integer REQUEST_IMAGE_CAPTURE = 1;
    private final Integer REQUEST_PERMISSION_CODE = 100;

    private final String PERMISSION = Manifest.permission.CAMERA;
    private String imageFilePath;
    private SensorManager sensorManager;
    private Sensor lightSensor;

    EditText editText;
    Button buttonSave, ButtonMakePhoto;
    ImageView imageView;
    TextView lightSensorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lightSensorTextView = findViewById(R.id.TextViewLightSensor);
        imageView = findViewById(R.id.ProfilePic);
        editText = findViewById(R.id.EditTextName);
        ButtonMakePhoto = findViewById(R.id.ButtonMakePhoto);
        buttonSave = findViewById(R.id.SaveSettingsButton);


        PreferenceManager preferenceManager = new PreferenceManager(this);

        imageFilePath = preferenceManager.loadValue(SAVED_PHOTO);
        if (imageFilePath != null) {
            loadPhoto();
        }
        buttonSave.setOnClickListener(view -> {

            if (editText.getText().toString().length() != 0)
                preferenceManager.saveValue(SAVED_NAME,
                        editText.getText().toString());

            if (imageFilePath.length() != 0)
                preferenceManager.saveValue(SAVED_PHOTO, imageFilePath);
        });
        ButtonMakePhoto.setOnClickListener(view -> checkPermission());
        editText.setText(preferenceManager.loadValue(SAVED_NAME));


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        getAllSensors(sensorManager);
    }


    public void getAllSensors(SensorManager sensorManager){

        ListView listView = findViewById(R.id.listViewSensors);
        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        List<String> listSensorType = new ArrayList<>();
        for (int i = 0; i < deviceSensors.size(); i++) {
            listSensorType.add(deviceSensors.get(i).getName());
        }

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listSensorType);
        listView.setAdapter(adapter);

    }

//https://startandroid.ru/ru/blog/508-android-permissions.html
    public void checkPermission() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, PERMISSION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
//                shouldShowRequestPermissionRationale will return true only if
//                the application was launched earlier and the user "denied" the permission
//                WITHOUT checking "never ask again".

//                In other cases (app launched first time, or the app launched earlier too
//                and the user denied permission by checking "never ask again"),
//                the return value is false.
                showExplanation(getString(R.string.titleShowExplanation), getString(R.string.messageShowExplanation),
                        PERMISSION, REQUEST_PERMISSION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {PERMISSION}, REQUEST_PERMISSION_CODE);

//                requestPermissions(new String[] {PERMISSION}, REQUEST_PERMISSION_CODE);
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void showExplanation(String title, String message,final String permission,
                                 final int permissionRequestCode) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.GivePermission, (dialog, id) -> {
                    // дейстиве у кнопки
                    ActivityCompat.requestPermissions(this, new String[] {permission}, permissionRequestCode);
                });
        builder.create().show();
    }


    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        будет ли запущена activity для данного намерения
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
                imageFilePath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                Log.e(TAG, "Create file", ex);
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                Log.i(TAG, "Image's URI: " + photoUri.toString());

                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Start activity", e);
                }
            }
        }
    }

    public File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "img_" + timeStamp;

        File FilePath = new File(getFilesDir(), "Pictures");
        FilePath.mkdir();

//        temp file
        return new File(FilePath.getPath(),
                String.format("%s.img", imageFileName));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] result) {
        super.onRequestPermissionsResult(requestCode, permissions, result);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Фотография была сделана
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            loadPhoto();
            Log.i(TAG, "photo loaded from " + imageFilePath);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loadPhoto(){
        Glide.with(this).load(imageFilePath).into(imageView);

// //        imageFile = new File(imageFilePath);
// //        if(imageFile.exists()){
// //            Bitmap myBitmap = BitmapFactory.decodeFile(imageFilePath);
// //            Log.e(TAG, imageFilePath.toString());
// //            Log.e(TAG, imageFile.toString());
// //            Log.e(TAG, myBitmap.toString());
// //            imageView.setImageBitmap(myBitmap);
// //        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        lightSensorTextView.setText(String.format("%s lux", lux));
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}


