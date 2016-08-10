package com.example.kirill.coursework;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Activity с инфой обо мне и проекте, так же тут спряталсь пасхалочка
 */

class About extends AppCompatActivity {

    //Кол-во нажатий на кнопку
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //Блокировка поворота экрана
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Обнуление счетчка нажатий, каждый раз, как создаем окно
        count = 0;
    }

    //Метод от ImageView (5 раз нажать сменится background)
    @SuppressWarnings({"unused", "UnusedParameters"})
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
