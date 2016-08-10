package com.example.kirill.coursework;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;

/**
 * Activity настроек. На нем находятся два слайдера кол-во клеток и мин
 */

class Settings extends AppCompatActivity {

    //Размер поля
    private static int size = 5;
    //Кол-во мин
    private static int numberOfBombs = 5;
    //Автоповорот экрана
    private static boolean isUgly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Блокировка поворота экрана
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Инициализация слайдеров
        final NumberPicker fieldsSize = (NumberPicker) findViewById(R.id.numberPicker1);
        final NumberPicker bombNum = (NumberPicker) findViewById(R.id.numberPicker2);

        Switch switcher = (Switch) findViewById(R.id.switch1);

        //Кол-во клеток фиксирована (от 5 до 10) иначе кнопки слишком маленькие
        fieldsSize.setMinValue(5);
        fieldsSize.setMaxValue(10);
        //Запрет на цикличность
        fieldsSize.setWrapSelectorWheel(false);

        //Кол-во мин также фиксирована (от 1 до обещго кол-ва клеток -1) иначе нет смысла играть
        bombNum.setMinValue(1);
        bombNum.setMaxValue(fieldsSize.getValue() - 1);
        //Разрешение на цикличность
        bombNum.setWrapSelectorWheel(true);

        //Установка первичных значений слайдеров
        fieldsSize.setValue(size);
        bombNum.setMaxValue((size * size) - 1);
        bombNum.setValue(numberOfBombs);
        switcher.setChecked(isUgly);

        //Слушатель изменений размеров слайдера с размером поля
        NumberPicker.OnValueChangeListener OnValueChangeListenerNumberPicker1 = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //При изменении величины слайдера изменяется максимальное значени для второго слайдера
                bombNum.setMaxValue(newVal * newVal - 1);
                //Размер поля принимается как действующие значение слайдера
                size = newVal;

            }
        };

        //Слушатель изменений размеров слайдера с кол-ом кнопок
        NumberPicker.OnValueChangeListener OnValueChangeListenerNumberPicker2 = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Кол-во мин принимается как действующие значение слайдера
                numberOfBombs = newVal;
            }
        };

        //Привязка слушателей на слайдеры
        fieldsSize.setOnValueChangedListener(OnValueChangeListenerNumberPicker1);
        bombNum.setOnValueChangedListener(OnValueChangeListenerNumberPicker2);

        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isUgly = isChecked;
            }
        });
    }


    //Геттер размера
    public static int getSize() {
        return size;
    }

    //Геттер кол-ва бомб
    public static int getNumberOfBombs() {
        return numberOfBombs;
    }

    //Геттер флага автоповорота
    public static boolean isUgly() {
        return !isUgly;
    }
}
