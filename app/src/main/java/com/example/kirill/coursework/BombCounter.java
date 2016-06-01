package com.example.kirill.coursework;

import android.app.Activity;
import android.widget.ImageView;

/**
 * Класс счетчика мин, содержит 3 ImageView, по одному на каждые разряд счетчика
 * Кол-во мин делится на разряды и устанавливается в счетчик
 */
class BombCounter {

    //Разряд сотен
    private final ImageView first;
    //Разряд десятков
    private final ImageView second;
    //Разряд единиц
    private final ImageView third;

    public BombCounter(int Bombs, Activity activity) {

        //Инициалиация ImageView
        first = (ImageView) activity.findViewById(R.id.first);
        second = (ImageView) activity.findViewById(R.id.second);
        third = (ImageView) activity.findViewById(R.id.third);

        //Установка начального значения
        setNumber(Bombs);

    }

    void setNumber(int value) {

        //Если значение мин неотрицтельное, число разбивается на цифры и выводится поразрядно
        if (value >= 0) {
            //Получение сотен
            int one = value / 100;
            //Вывод
            changeNumber(one, first);

            //Получение десятков
            int two = (value / 10) % 10;
            //Вывод
            changeNumber(two, second);

            //Получение единиц
            int three = value % 10;
            //Вывод
            changeNumber(three, third);
        }
        //Если значение отрицательное, то знак сотен устанавливается как "-"
        else {
            first.setImageResource(R.drawable.minus_table);

            //Получение десятков
            int two = (value / 10) % 10;
            //Инвертирование значения (т.к при деление, получилось отрицательное значение)
            two *= -1;
            //Вывод
            changeNumber(two, second);
            //Получение единиц
            int three = value % 10;

            //Инвертирование значения (т.к при деление, получилось отрицательное значение)
            three *= -1;
            //Вывод
            changeNumber(three, third);
        }

    }

    //Метод вывода значения цифры, входные параметры: цифра и в какой слот вставить цифру
    private void changeNumber(int value, ImageView imageView) {
        //Каждой цифре привязана соответствующая картинка слота
        switch (value) {
            case 0: {
                imageView.setImageResource(R.drawable.zero_table);
                break;
            }
            case 1: {
                imageView.setImageResource(R.drawable.one_table);
                break;
            }
            case 2: {
                imageView.setImageResource(R.drawable.two_table);
                break;
            }
            case 3: {
                imageView.setImageResource(R.drawable.three_table);
                break;
            }
            case 4: {
                imageView.setImageResource(R.drawable.four_table);
                break;
            }
            case 5: {
                imageView.setImageResource(R.drawable.five_table);
                break;
            }
            case 6: {
                imageView.setImageResource(R.drawable.six_table);
                break;
            }
            case 7: {
                imageView.setImageResource(R.drawable.seven_table);
                break;
            }
            case 8: {
                imageView.setImageResource(R.drawable.eight_table);
                break;
            }
            case 9: {
                imageView.setImageResource(R.drawable.nine_table);
                break;
            }
        }
    }
}
