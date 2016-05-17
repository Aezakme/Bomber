package com.example.kirill.coursework;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Класс слота с миной
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
    //Открыта
    boolean showed = false;
    //Поднят флаг
    boolean flagUp = false;

    Slot(int id, final Activity activity, final boolean bomb) {

        //Создание стиля для LinearLayout, горизонтальное расположение, размеры подстраивются под родителя
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.HORIZONTAL);
        layoutParams.weight = 1;

        //Новая кнопка
        slotButton = new ImageButton(activity);
        //Настройка параметров
        slotButton.setLayoutParams(layoutParams);
        //Фон слота принимает вид закрытой кнопки
        slotButton.setBackgroundResource(R.drawable.slot);
        //Фот растягивается по всей площади
        slotButton.setScaleType(ImageView.ScaleType.FIT_XY);
        //Устновка id кнопки
        slotButton.setId(id);
        //Активирование долгого нажатия
        slotButton.setLongClickable(true);

        //Установка id слота
        this.id = id;
        //Установка мины
        this.haveBomb = bomb;

        //Слушатель короткого нажатия на слот
        slotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Нажимается только, если на слоте не стоит флажок
                if (!flagUp) {
                    //Если есть мина - взрыв!
                    if (haveBomb) {
                        //Смайлик умирает
                        ImageView view = (ImageView) activity.findViewById(R.id.smile);
                        view.setBackgroundResource(R.drawable.dead);

                        //Напоминание, какоой игрок неудачник
                        Toast.makeText(activity, "Booom!You lose!", Toast.LENGTH_SHORT).show();
                        //Вибрация
                        vibrator.vibrate(500);
                        //Подрыв всех мин
                        boomAll();
                    } else {
                        //открытие клетки
                        open();
                    }
                }
            }
        });

        //Слушатель долгого нажатия на слот
        slotButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Если флаг стоит - убиарется, и наборот
                if (flagUp) {
                    slotButton.setBackgroundResource(R.drawable.slot);
                    //Если мина есть, увеличиваются оба счетчика, если нет, только счетчик фейков
                    if (haveBomb) {
                        upBombs(true);
                    } else {
                        upBombs(false);
                    }
                    flagUp = false;
                } else {
                    slotButton.setBackgroundResource(R.drawable.flag);
                    //Если мина есть, уменьшаются оба счетчика, если нет, только счетчик фейков
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


    //Метод открытия слота
    void open() {

        //Слот открыт
        showed = true;
        //Если в слоте бомба - она показыватся,  иначе устнавливается картинка в соответсвии со счетчиком мин
        if (haveBomb) {
            slotButton.setBackgroundResource(R.drawable.bomb);
        } else {
            //Уменьшаем счетчик закрытых ячеек
            oneMoreOpen();
            switch (count) {
                //Если рядом нет мин, открывются соседние клетки
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
        slotButton.setEnabled(false);
    }


    //Повышение кол-ва мин вокруг слота
    public void upCount() {
        count++;
    }

    //Сеетр кол-ва мин вкоруг слота
    public void setCount(int count) {
        this.count = count;
    }
    //Сеттер флажка
    public void setFlagUp(boolean flagUp) {
        this.flagUp = flagUp;
    }
    //Сеттер нахождения мины в данном слоте
    public void setHaveBomb(boolean haveBomb) {
        this.haveBomb = haveBomb;
    }
    //Сеттер переменной открытия слота
    public void setShowed(boolean showed) {
        this.showed = showed;
    }

    //Геттер кнопки
    public ImageButton getSlotButton() {
        return slotButton;
    }
    //Геттер id
    public int getId() {
        return id;
    }
    //Геттер переменной открытия слота
    public boolean isShowed() {
        return showed;
    }
}
