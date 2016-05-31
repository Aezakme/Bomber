package com.example.kirill.coursework;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * Класс для activity_main - запускается первым, содержит меню для перехода
 * Из названий понятно, что куда идет
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @SuppressWarnings({"unused", "UnusedParameters"})
    public void onStartClick(View view) {
        startActivity(new Intent(getApplicationContext(), Game.class));
    }

    @SuppressWarnings({"unused", "UnusedParameters"})
    public void onSettingsClick(View view) {
        startActivity(new Intent(getApplicationContext(), Settings.class));
    }

    @SuppressWarnings({"unused", "UnusedParameters"})
    public void onAboutClick(View view) {
        startActivity(new Intent(getApplicationContext(), About.class));
    }
}
