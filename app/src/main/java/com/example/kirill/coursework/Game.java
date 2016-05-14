package com.example.kirill.coursework;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Game extends AppCompatActivity {

    static ArrayList<Integer> bombsArray;
    static ArrayList<Slot> slotArray;
    static ImageButton button;
    static int openCounter;
    static Vibrator vibrator;
    Context context;

    //Счетчик оставшихся бомб
    static TextView counter;
    //Количество бомб, которые определил пользователь
    static int flagBombs = 0;
    static int goodBooms;

    static int NUMBER_OF_BOMBS;
    static int FIELD_ROWS;
    static int FIELD_COLUMNS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

        context = this;
        FIELD_ROWS = Settings.getSize();
        FIELD_COLUMNS = Settings.getSize();
        NUMBER_OF_BOMBS = Settings.getNumberOfBombs();
        button = (ImageButton) findViewById(R.id.smile);

        slotArray = new ArrayList<>();
        flagBombs = NUMBER_OF_BOMBS;
        counter = (TextView) findViewById(R.id.bombsCounter);
        counter.setText(flagBombs + "");


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.HORIZONTAL);
        p.weight = 1;

        //Создание кнопок
        for (int i = 0; i < FIELD_ROWS; i++) {

            LinearLayout lin = new LinearLayout(this);
            lin.setLayoutParams(p);


            for (int j = 0; j < FIELD_COLUMNS; j++) {

                Slot slot = new Slot(i * FIELD_ROWS + j, this, false);

                slotArray.add(slot);
                lin.addView(slot.getSlotButton(), j);
            }

            linearLayout.addView(lin, i);

        }
        newGame();
    }

    void upBombs(boolean good) {

        if (good) {
            goodBooms++;
            if (goodBooms == 0) {
                win();
            }
            //oneMoreOpen();
        }
        flagBombs++;
        counter.setText(flagBombs + "");

    }

    void downBombs(boolean good) {
        if (good) {
            goodBooms--;
            if (goodBooms == 0) {
                win();
            }
        }
        flagBombs--;
        counter.setText(flagBombs + "");
    }

    void newGame() {

        goodBooms = NUMBER_OF_BOMBS;
        flagBombs = NUMBER_OF_BOMBS;
        openCounter = FIELD_COLUMNS * FIELD_ROWS - NUMBER_OF_BOMBS;
        counter.setText(flagBombs + "");
        bombsArray = new ArrayList<>();

        int randNum = (int) (Math.random() * FIELD_COLUMNS * FIELD_ROWS);
        bombsArray.add(randNum);
        Log.v("==bomb at", bombsArray.get(0) + "");
        //Генерация бомб
        for (int i = 1; i < NUMBER_OF_BOMBS; i++) {

            randNum = (int) (Math.random() * FIELD_COLUMNS * FIELD_ROWS);
            while (bombsArray.contains(randNum))
                randNum = (int) (Math.random() * FIELD_COLUMNS * FIELD_ROWS);
            bombsArray.add(randNum);

            Log.v("==bomb at", bombsArray.get(i) + "");

        }

        for (int i = 0; i < FIELD_ROWS; i++) {
            for (int j = 0; j < FIELD_COLUMNS; j++) {

                slotArray.get(i * FIELD_ROWS + j).setCount(0);
                slotArray.get(i * FIELD_ROWS + j).setShowed(false);
                slotArray.get(i * FIELD_ROWS + j).setFlagUp(false);
                slotArray.get(i * FIELD_ROWS + j).setHaveBomb(false);


                slotArray.get(i * FIELD_ROWS + j).slotButton.setEnabled(true);
                slotArray.get(i * FIELD_ROWS + j).slotButton.setLongClickable(true);
                slotArray.get(i * FIELD_ROWS + j).slotButton.setBackgroundResource(R.drawable.slot);


                if (bombsArray.contains(slotArray.get(i * FIELD_ROWS + j).getId())) {
                    slotArray.get(i * FIELD_ROWS + j).setHaveBomb(true);
                }
            }
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

    void boomAll() {

        for (int i = 0; i < FIELD_ROWS; i++) {
            for (int j = 0; j < FIELD_COLUMNS; j++) {


                if (bombsArray.contains(slotArray.get(i * FIELD_ROWS + j).getId())) {
                    slotArray.get(i * FIELD_ROWS + j).slotButton.setBackgroundResource(R.drawable.bomb);
                } else {
                    if (slotArray.get(i * FIELD_ROWS + j).flagUp) {
                        slotArray.get(i * FIELD_ROWS + j).slotButton.setBackgroundResource(R.drawable.frong_bomb);
                    }
                }
                slotArray.get(i * FIELD_ROWS + j).slotButton.setEnabled(false);
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

    void oneMoreOpen() {
        openCounter--;
        if (openCounter == 0) {
            win();
        }
    }

    public void startNew(View view) {
        button.setBackgroundResource(R.drawable.smile);
        newGame();
    }

    public void win() {
        button.setBackgroundResource(R.drawable.win);
        for (int i = 0; i < FIELD_ROWS; i++) {
            for (int j = 0; j < FIELD_COLUMNS; j++) {
                if (bombsArray.contains(slotArray.get(i * FIELD_ROWS + j).getId())) {
                    slotArray.get(i * FIELD_ROWS + j).slotButton.setBackgroundResource(R.drawable.flag);
                }
                slotArray.get(i * FIELD_ROWS + j).slotButton.setEnabled(false);
            }
        }
    }
}

