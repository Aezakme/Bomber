package com.example.kirill.coursework;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Game extends AppCompatActivity {

    ArrayList<Integer> bombsArray;
    static ArrayList<Slot> slotArray;
    BombCounter bombCounter;
    Context context;

    static int FIELD_ROWS = 1;
    static int FIELD_COLUMNS = 1;
    int NUMBER_OF_BOMBS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = this;

        FIELD_ROWS = Settings.getSize();
        FIELD_COLUMNS = Settings.getSize();
        NUMBER_OF_BOMBS = Settings.getNumberOfBombs();

        bombsArray = new ArrayList<>();
        slotArray = new ArrayList<>();
        bombCounter = new BombCounter(NUMBER_OF_BOMBS, this);


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.HORIZONTAL);
        p.weight = 1;

        //Генерация бомб
        for (int i = 0; i < NUMBER_OF_BOMBS; i++) {

            //TODO: make equals IF
            int randNum = (int) (Math.random() * FIELD_COLUMNS * FIELD_ROWS);


            bombsArray.add(randNum);

            Log.v("==bomb at", bombsArray.get(i) + "");

        }

        //Создание кнопок
        for (int i = 0; i < FIELD_ROWS; i++) {

            LinearLayout lin = new LinearLayout(this);
            lin.setLayoutParams(p);


            for (int j = 0; j < FIELD_COLUMNS; j++) {

                Slot slot = new Slot(i * FIELD_ROWS + j, this, false);

                slotArray.add(slot);

                if (bombsArray.contains(slotArray.get(i * FIELD_ROWS + j).getId())) {
                    slotArray.get(i * FIELD_ROWS + j).setHaveBomb(true);
                }

                lin.addView(slot.getSlotButton(), j);
            }

            linearLayout.addView(lin, i);

        }


        for (int i = 0; i < FIELD_COLUMNS; i++) {
            for (int j = 0; j < FIELD_COLUMNS; j++) {

                //Заполнение единицами
                if (bombsArray.contains(slotArray.get(i * FIELD_COLUMNS + j).getId())) {

                    if (j < FIELD_COLUMNS - 1) {
                        slotArray.get(i * FIELD_COLUMNS + j + 1).upCount();
                        if (i < FIELD_COLUMNS - 1) {
                            slotArray.get((i + 1) * FIELD_COLUMNS + j + 1).upCount();
                        }
                        if (i > 0) {
                            slotArray.get((i - 1) * FIELD_COLUMNS + j + 1).upCount();
                        }
                    }

                    if (j > 0) {
                        slotArray.get(i * FIELD_COLUMNS + j - 1).upCount();
                        if (i < FIELD_COLUMNS - 1) {
                            slotArray.get((i + 1) * FIELD_COLUMNS + j - 1).upCount();
                        }

                        if (i > 0) {
                            slotArray.get((i - 1) * FIELD_COLUMNS + j - 1).upCount();
                        }
                    }
                    if (i < FIELD_COLUMNS - 1)
                        slotArray.get((i + 1) * FIELD_COLUMNS + j).upCount();
                    if (i > 0)
                        slotArray.get((i - 1) * FIELD_COLUMNS + j).upCount();


                }
            }
        }


    }

    void openNear(int id) {

        if (id % FIELD_COLUMNS != 0) {
            if (!slotArray.get(id - 1).isShowed())
                slotArray.get(id - 1).open();

            if (id > FIELD_COLUMNS) {
                if (!slotArray.get(id - FIELD_COLUMNS - 1).isShowed())
                    slotArray.get(id - FIELD_COLUMNS - 1).open();
            }

            if (id < (FIELD_COLUMNS - 1) * FIELD_COLUMNS) {
                if (!slotArray.get(id + FIELD_COLUMNS - 1).isShowed())
                    slotArray.get(id + FIELD_COLUMNS - 1).open();
            }


        }

        if (id % FIELD_COLUMNS != FIELD_COLUMNS - 1) {
            if (!slotArray.get(id + 1).isShowed()) {
                slotArray.get(id + 1).open();
            }
            if (id > FIELD_COLUMNS) {
                if (!slotArray.get(id - FIELD_COLUMNS + 1).isShowed())
                    slotArray.get(id - FIELD_COLUMNS + 1).open();
            }

            if (id < (FIELD_COLUMNS - 1) * FIELD_COLUMNS) {
                if (!slotArray.get(id + FIELD_COLUMNS + 1).isShowed())
                    slotArray.get(id + FIELD_COLUMNS + 1).open();
            }


        }

        if (id < (FIELD_COLUMNS - 1) * FIELD_COLUMNS) {
            if (!slotArray.get(id + FIELD_COLUMNS).isShowed())
                slotArray.get(id + FIELD_COLUMNS).open();
        }

        if (id > FIELD_COLUMNS) {
            if (!slotArray.get(id - FIELD_COLUMNS).isShowed())
                slotArray.get(id - FIELD_COLUMNS).open();
        }


    }
}

