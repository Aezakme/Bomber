package com.example.kirill.coursework;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void onStartClick(View view) {
        startActivity(new Intent(getApplicationContext(), Game.class));

    }

    public void onSettingsClick(View view) {
        startActivity(new Intent(getApplicationContext(), Settings.class));
    }

    public void onHintClick(View view) {
        x++;
        if (x > 5) {
            x = 0;
            Toast.makeText(this, "ha-ha", Toast.LENGTH_SHORT).show();
        }
    }
}
