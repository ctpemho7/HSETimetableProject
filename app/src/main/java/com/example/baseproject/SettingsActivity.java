package com.example.baseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private static final String SAVED_NAME = "username";
    private static final String SAVED_PHOTO = "photo";
    private final Integer REQUEST_IMAGE_CAPTURE = 0;
    private final Integer REQUEST_PERMISSION_CODE = 1;
    private final String[] PERMISSION = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private File imageFilePath;
    private File imageFile;
    Uri photoUri;

    EditText editText;
    Button buttonSave, ButtonMakePhoto;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        PreferenceManager preferenceManager = new PreferenceManager(this);

        imageFile = new File(preferenceManager.loadValue(SAVED_PHOTO));
        if(imageFile.exists())
        {
//            чзх??????
//            Glide.with(this).load(imageFile).into(imageView);
        }
        else{
//            imageView.setImageResource(R.drawable.dzhek);
        }

        editText = findViewById(R.id.EditTextName);
        ButtonMakePhoto = findViewById(R.id.ButtonMakePhoto);
        buttonSave = findViewById(R.id.SaveSettingsButton);

        buttonSave.setOnClickListener(view -> {
            preferenceManager.saveValue(SAVED_NAME,
                    editText.getText().toString());

            preferenceManager.saveValue(SAVED_PHOTO, imageFile.getPath());
        });
        ButtonMakePhoto.setOnClickListener(view -> checkPermission());
        editText.setText(preferenceManager.loadValue(SAVED_NAME));
    }



    public void checkPermission() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, PERMISSION[0]);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION[0])) {
                showExplanation("Нужно предоставить права", "Для съёмки фото нужно предоставить права",
                        PERMISSION[0], REQUEST_PERMISSION_CODE);
            } else {
                requestPermissions(PERMISSION, REQUEST_PERMISSION_CODE);
            }
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, id) ->
                        requestPermissions(new String[]{permission}, permissionRequestCode));
        builder.create().show();
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

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "Create file", ex);
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Start activity", e);
                }
            }
        }
    }

    public File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "TMG " + timeStamp + " ";

        imageFilePath = new File(getFilesDir(), "Pictures");
        imageFilePath.mkdir();
        imageFile = new File(imageFilePath.getPath(), String.format("%s.img", imageFileName));

        return imageFile;
    }

    public static class PreferenceManager {
        // в файле можем указывать общие для нескольких activity preferences
        private final static String PREFERENCE_FILE = "org.hse.android.file";

        private final SharedPreferences sPref;
        private final Context context;

        public PreferenceManager(Context context) {
            sPref = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
            this.context = context;
        }

        //////////////////////

        public void saveValue(String key, String value) {
            // константа MODE_PRIVATE означает, что данные будут видны только приложению
//            sPref = getPreferences(MODE_PRIVATE);
            // создаём editor
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString(key, value);
            ed.apply();

            Toast.makeText(context, "Данные изменено", Toast.LENGTH_SHORT).show();
        }

        public String loadValue(String key) {
////            sPref = getPreferences(MODE_PRIVATE);
//            // имя и значение по умолчанию
//            String savedText = sPref.getString(SAVED_NAME, "");
//            editText.setText(savedText);

            Toast.makeText(context, "Данные считаны", Toast.LENGTH_SHORT).show();
            return sPref.getString(key, "");
        }
    }

}


