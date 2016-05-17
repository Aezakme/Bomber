package com.example.kirill.coursework;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

/**
 * Activity настроек. На нем находятся два слайдера кол-во клеток и мин
 */

public class Settings extends AppCompatActivity {

    //Размер поля
    static int size = 5;
    //Кол-во мин
    static int numberOfBombs = 5;

    //Слайдер для изменение размера поля
    NumberPicker fieldsSize = null;
    //Слайдер для изменение размера поля
    NumberPicker bombNum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Блокировка поворота экрана
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Инициализация слайдеров
        fieldsSize = (NumberPicker) findViewById(R.id.numberPicker1);
        bombNum = (NumberPicker) findViewById(R.id.numberPicker2);

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
    }

    //Геттер размера
    public static int getSize() {
        return size;
    }
    //Геттер кол-ва бомб
    public static int getNumberOfBombs() {
        return numberOfBombs;
    }
}
