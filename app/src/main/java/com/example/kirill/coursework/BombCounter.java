package com.example.kirill.coursework;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Kirill on 12.05.16.
 */
public class BombCounter extends Game {

    //Счетчик оставшихся бомб
    TextView counter;
    //Количество бомб
    int Bombs;
    //Количество бомб, которые определил пользователь
    static int fakeBombs = 0;
    //Activity
    Activity activity;

    public BombCounter(int Bombs, Activity activity) {
        this.Bombs = Bombs;
        this.fakeBombs = Bombs;
        this.activity = activity;

        counter = (TextView) activity.findViewById(R.id.bombsCounter);
        counter.setText(Bombs + "");
    }

    void upFakeBombs() {
        counter = (TextView) activity.findViewById(R.id.bombsCounter);
        fakeBombs++;
        counter.setText(fakeBombs + "");
    }

    void downFakeBombs() {
        counter = (TextView) activity.findViewById(R.id.bombsCounter);
        fakeBombs--;
        counter.setText(fakeBombs + "");
    }

    public int getFakeBombs() {
        return fakeBombs;
    }
}
