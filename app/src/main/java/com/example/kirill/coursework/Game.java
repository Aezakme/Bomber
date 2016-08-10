package com.example.kirill.coursework;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Класс самой игры, здесь организована основная механика и запуск game_layout
 * <p/>
 * Кнопки полей создаются программно и записываются в список для дальнейшего использования.
 * При запуске activity_game создается FIELD_ROW * FIELD_COLUMNS штук экземпляров класса Slot
 * в котором находится 3 поля необходимы в это классе: кнопка, кол-во мин вокруг и булевский флаг - есть ли мина.
 * Эти ячейки записываются в список slotArray.
 * Затем вызывается метод newGame(). В начале идет заполнение списка мин - случайные значения,
 * равные id некоторых слотов. После каждый слот проверяется на присутствия его id в списки мин, если это так,
 * то флаг о наличии мин поднимается.
 * После идет обнуление всех параметров, чтобы параметры старой игры не повлияли на новую.
 * Дальше идет заполнение поля единиц, шагая по списку слотов, ищем слот с бомбой, а наступая на мину
 * повышаем счетчик соседних клеток на 1.
 * После можно начинать играть, остальные методы необходимы для обработки действий игрока.
 * <p/>
 * Победа происходит в двух случаях:
 * 1) игрок открыл все слоты без мин
 * 2) игрок поставил флажки на все мины (если флажков больше, чем мин не считается)
 */

class Game extends AppCompatActivity {

    //Вибратор - телефон вибрирует, если игрок проиграл
    static Vibrator vibrator;
    static GameEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Блокировка поворота экрана
        if (Settings.isUgly()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        engine = new GameEngine(this);

        //Инициализация вибратора
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (true) {
            //Инициализация размеров поля и кол-во мин
            int FIELD_ROWS = Settings.getSize();
            int FIELD_COLUMNS = Settings.getSize();

            //Инициалзивая шаблона в котором хранятся списки кнопок
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);

            //Создание стиля для LinearLayout, горизонтальное расположение, размеры подстраивются под родителя
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.HORIZONTAL);
            //Все элементы равны между собой
            layoutParams.weight = 1;

            //Создание слотов
            for (int i = 0; i < FIELD_ROWS; i++) {

                //Создание LinearLayout в котором будут хранится слоты и установка настрое
                LinearLayout lin = new LinearLayout(this);
                lin.setLayoutParams(layoutParams);

                for (int j = 0; j < FIELD_COLUMNS; j++) {
                    lin.addView(engine.getSlot(i * FIELD_ROWS + j).getSlotButton(), j);
                }

                //Линейный шаблон вставляется в другой шаблон - получается строка
                linearLayout.addView(lin, i);
            }

            //Запуск новой игры
            engine.newGame();
        } else {

        }
    }

    //Нажатие на смайли запустит новую игру
    public void startNew(View view) {
        engine.newGame();
    }
}

   /* protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("bombsArray", bombsArray);

        //Список слотов поля
        //outState.putParcelableArrayList("slotArray", slotArray);
        //Счетчик закрытых слотов
        outState.putInt("closeCounter", closeCounter);
        //Количество бомб, которые определил пользователь
        outState.putInt("flagBombs", flagBombs);
        // Кол-во мин которые действительно осталось найти (игрок может ошибиться)
        outState.putInt("goodBombs", goodBombs);
        //Состояние кнопки со смайлом (0 - норма, 1 - win, 2 - dead) Простите за костыль:(
        outState.putByte("stateOfSmile", stateOfSmile);
        //Кол-во мин
        outState.putInt("NUMBER_OF_BOMBS", NUMBER_OF_BOMBS);
        //Высота и ширина поля (одинаковы, но сделаны раздельно - возможно потом пригодится)
        outState.putInt("FIELD_ROWS", FIELD_ROWS);
        outState.putInt("FIELD_COLUMNS", FIELD_COLUMNS);
        Log.v("Pop", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Список бомб
        bombsArray = savedInstanceState.getIntegerArrayList("bombsArray");
        //Список слотов поля
        //slotArray = savedInstanceState.getParcelableArrayList("slotArray");
        //Счетчик закрытых слотов
        closeCounter = savedInstanceState.getInt("closeCounter");
        //Количество бомб, которые определил пользователь
        flagBombs = savedInstanceState.getInt("flagBombs");
        // Кол-во мин которые действительно осталось найти (игрок может ошибиться)
        goodBombs = savedInstanceState.getInt("goodBombs");
        //Состояние кнопки со смайлом (0 - норма, 1 - win, 2 - dead) Простите за костыль:(
        stateOfSmile = savedInstanceState.getByte("stateOfSmile");
        //Кол-во мин
        NUMBER_OF_BOMBS = savedInstanceState.getInt("NUMBER_OF_BOMBS");
        //Высота и ширина поля (одинаковы, но сделаны раздельно - возможно потом пригодится)
        FIELD_ROWS = savedInstanceState.getInt("FIELD_ROWS");
        FIELD_COLUMNS = savedInstanceState.getInt("FIELD_COLUMNS");

        //Инициалзивая шаблона в котором хранятся списки кнопок
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);

        //Создание стиля для LinearLayout, горизонтальное расположение, размеры подстраивются под родителя
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.HORIZONTAL);
        //Все элементы равны между собой
        layoutParams.weight = 1;

      *//*  //Создание слотов
        for (int i = 0; i < FIELD_ROWS; i++) {

            //Создание LinearLayout в котором будут хранится слоты и установка настрое
            LinearLayout lina = new LinearLayout(this);

            lina.setLayoutParams(layoutParams);

            for (int j = 0; j < FIELD_COLUMNS; j++) {

                if (slotArray.get(i * FIELD_COLUMNS + j).getSlotButton().getParent() != null)
                    ((ViewGroup) slotArray.get(i * FIELD_COLUMNS + j).getSlotButton().getParent()).removeView(slotArray.get(i * FIELD_COLUMNS + j).getSlotButton());
                //Кнопка слота вставляется в линейный шаблон
                lina.addView(slotArray.get(i * FIELD_COLUMNS + j).getSlotButton(), j);
            }

            //Линейный шаблон вставляется в другой шаблон - получается строка
            linearLayout.addView(lina, i);
        }*//*

        Log.v("Pop", "onRestoreInstanceState");


    }
}*/

