package com.example.kirill.coursework;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Kirill on 04.05.16.
 */
public class Slot extends Game {

    //Кнопка слота
    Button slotButton;
    //ID слота
    int id;
    //Количество мин вокруг
    int count = 0;
    //Есть мина
    boolean haveBomb = false;
    //Activity от Game
    private Activity activity;
    //Показана
    boolean showed = false;


    Slot(int id, final Activity activity, final boolean bomb) {


        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.HORIZONTAL);
        p.weight = 1;
        slotButton = new Button(activity);
        slotButton.setLayoutParams(p);
        slotButton.setId(id);
        slotButton.setLongClickable(true);
        this.id = id;
        this.haveBomb = bomb;
        this.activity = activity;

        slotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.v("id", slotButton.getId() + "");
                open();
            }
        });

        slotButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                slotButton.setText("f");
                bombCounter.downFakeBombs();
                slotButton.setClickable(false);
                return true;
            }
        });
    }

    void open() {
        showed = true;
        if (haveBomb) {
            ImageView view = (ImageView) activity.findViewById(R.id.imageView2);
            view.setVisibility(View.INVISIBLE);
            Toast.makeText(activity, "Booom!You lose!", Toast.LENGTH_SHORT).show();
            slotButton.setText("*");
        } else {
            if (count > 0) {
                slotButton.setText("" + count);
            } else {
                slotButton.setText(".");
                openNear(slotButton.getId());
            }

        }
        slotButton.setLongClickable(false);


    }


    public void setHaveBomb(boolean haveBomb) {
        this.haveBomb = haveBomb;
    }

    public void upCount() {
        count++;
    }

    public boolean isShowed() {
        return showed;
    }

    public int getId() {
        return id;
    }

    public Button getSlotButton() {
        return slotButton;
    }


}
