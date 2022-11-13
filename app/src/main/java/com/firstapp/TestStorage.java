package com.firstapp;

import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TestStorage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_storage);
        try {
            SharedPreferences prefs = getSharedPreferences("com." + R.string.app_name + ".localstorage", MODE_PRIVATE);
            Editor editor = prefs.edit();
            editor.putString("basePath", "https://api.day.app/AYuom7SiruyhppdPtUdpMR/");
            editor.commit();
            String basePath = prefs.getString("basePath", null);
            Toast.makeText(TestStorage.this, "" + basePath, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}