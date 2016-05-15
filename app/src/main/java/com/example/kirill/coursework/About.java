package com.example.kirill.coursework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class About extends AppCompatActivity {


    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        count = 0;
        setContentView(R.layout.activity_about);
    }

    public void startHint(View view) {
        count++;
        if (count > 4) {
            ImageButton image = (ImageButton) findViewById(R.id.imageView3);
            image.setBackgroundResource(R.drawable.hint);
        }
        if (count > 9) {
            ImageButton image = (ImageButton) findViewById(R.id.imageView3);
            image.setBackgroundResource(R.drawable.win);
            count = 0;

        }

    }
}
