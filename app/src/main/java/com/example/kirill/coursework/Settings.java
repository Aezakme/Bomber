package com.example.kirill.coursework;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

public class Settings extends AppCompatActivity {

    static int size = 5;
    static int numberOfBombs = 5;

    NumberPicker fieldsSize = null;
    NumberPicker bombNum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fieldsSize = (NumberPicker) findViewById(R.id.numberPicker1);
        bombNum = (NumberPicker) findViewById(R.id.numberPicker2);

        fieldsSize.setMinValue(5);
        fieldsSize.setMaxValue(10);
        fieldsSize.setWrapSelectorWheel(false);

        bombNum.setMinValue(1);
        bombNum.setMaxValue(fieldsSize.getValue() - 1);
        bombNum.setWrapSelectorWheel(true);

        fieldsSize.setValue(size);
        bombNum.setMaxValue((size * size) - 1);
        bombNum.setValue(numberOfBombs);


        NumberPicker.OnValueChangeListener OnValueChangeListenerNumberPicker1 = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                bombNum.setMaxValue(newVal * newVal - 1);
                size = newVal;

            }
        };
        NumberPicker.OnValueChangeListener OnValueChangeListenerNumberPicker2 = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numberOfBombs = newVal;
            }
        };

        fieldsSize.setOnValueChangedListener(OnValueChangeListenerNumberPicker1);
        bombNum.setOnValueChangedListener(OnValueChangeListenerNumberPicker2);
    }


    public static int getSize() {
        return size;
    }

    public static int getNumberOfBombs() {
        return numberOfBombs;
    }
}
