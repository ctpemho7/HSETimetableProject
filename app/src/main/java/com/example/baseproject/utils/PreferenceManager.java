package com.example.baseproject.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.baseproject.R;

public class PreferenceManager {
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

        Toast.makeText(context,
                R.string.DataChangedRus,
                Toast.LENGTH_SHORT).show();
    }

    public String loadValue(String key) {
////          // sPref = getPreferences(MODE_PRIVATE);
//            // имя и значение по умолчанию
//            // String savedText = sPref.getString(SAVED_NAME, "");
//            // editText.setText(savedText);

        Toast.makeText(context,
                R.string.DataGetRus,
                Toast.LENGTH_SHORT).show();
        return sPref.getString(key, "");
    }
}
