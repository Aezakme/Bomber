package com.example.kirill.coursework;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Kirill on 04.05.16.
 */
public class Slot extends Game {

    //Кнопка слота
    ImageButton slotButton;
    //ID слота
    int id;
    //Количество мин вокруг
    int count = 0;
    //Есть мина
    boolean haveBomb = false;
    //Поднят флаг
    boolean flagUp = false;
    //Activity от Game

    public void setFlagUp(boolean flagUp) {
        this.flagUp = flagUp;
    }

    private Activity activity;
    //Показана
    boolean showed = false;


    Slot(int id, final Activity activity, final boolean bomb) {


        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.HORIZONTAL);
        p.weight = 1;
        slotButton = new ImageButton(activity);
        slotButton.setLayoutParams(p);
        //slotButton.setBackgroundColor(Color.TRANSPARENT);
        slotButton.setBackgroundResource(R.drawable.slot);
        slotButton.setScaleType(ImageView.ScaleType.FIT_XY);
        slotButton.setId(id);
        slotButton.setLongClickable(true);
        this.id = id;
        this.haveBomb = bomb;
        this.activity = activity;

        slotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flagUp) {

                    Log.v("id", slotButton.getId() + "");
                    if (haveBomb) {
                        ImageView view = (ImageView) activity.findViewById(R.id.smile);
                        view.setBackgroundResource(R.drawable.dead);
                        Toast.makeText(activity, "Booom!You lose!", Toast.LENGTH_SHORT).show();


                        vibrator.vibrate(500);

                        boomAll();
                    } else {
                        open();
                    }
                }
            }
        });

        slotButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (flagUp) {
                    slotButton.setBackgroundResource(R.drawable.slot);
                    if (haveBomb) {
                        upBombs(true);
                    } else {
                        upBombs(false);
                    }
                    flagUp = false;
                } else {
                    slotButton.setBackgroundResource(R.drawable.flag);
                    if (haveBomb) {
                        downBombs(true);
                    } else {
                        downBombs(false);
                    }
                    flagUp = true;
                }
                return true;
            }
        });
    }

    void open() {


        showed = true;

        if (haveBomb) {
            slotButton.setBackgroundResource(R.drawable.bomb);
        } else {
            oneMoreOpen();
            switch (count) {
                case 0: {
                    slotButton.setBackgroundResource(R.drawable.zero);
                    openNear(slotButton.getId());
                    break;
                }
                case 1: {
                    slotButton.setBackgroundResource(R.drawable.one);
                    break;
                }
                case 2: {
                    slotButton.setBackgroundResource(R.drawable.two);
                    break;
                }
                case 3: {
                    slotButton.setBackgroundResource(R.drawable.three);
                    break;
                }
                case 4: {
                    slotButton.setBackgroundResource(R.drawable.four);
                    break;
                }
                case 5: {
                    slotButton.setBackgroundResource(R.drawable.five);
                    break;
                }
                case 6: {
                    slotButton.setBackgroundResource(R.drawable.six);
                    break;
                }
                case 7: {
                    slotButton.setBackgroundResource(R.drawable.seven);
                    break;
                }
                case 8: {
                    slotButton.setBackgroundResource(R.drawable.eight);
                    break;
                }


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

    public ImageButton getSlotButton() {
        return slotButton;
    }

    public void setShowed(boolean showed) {
        this.showed = showed;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
