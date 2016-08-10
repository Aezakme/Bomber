package com.example.kirill.coursework;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

class GameEngine {

    //Список мин
    private ArrayList<Integer> bombsArray;
    //Список слотов поля
    private ArrayList<Slot> slotArray;

    //Счетчик закрытых слотов
    private int closeCounter;
    //Количество бомб, которые определил пользователь
    private int flagBombs = 0;
    // Кол-во мин которые действительно осталось найти (игрок может ошибиться)
    private int goodBombs;

    //Какая-то кнопка хз если честно
    private final ImageButton button;

    //Экземпляр класса BombCounter отвечает за счетчик мин в левом верхнем углу экрана
    private final BombCounter bombCounter;

    //Секундомер
    private Clock clock;

    //Кол-во мин
    private int NUMBER_OF_BOMBS;
    //Высота и ширина поля (одинаковы, но сделаны раздельно - возможно потом пригодится)
    private int FIELD_ROWS;
    private int FIELD_COLUMNS;

    //Состояние кнопки со смайлом (0 - норма, 1 - win, 2 - dead) Простите за костыль:(
    private byte stateOfSmile = 0;


    GameEngine(Activity activity) {
        //Инициализация кнопки новой игры
        button = (ImageButton) activity.findViewById(R.id.smile);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button.setBackgroundResource(R.drawable.smile_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    button.setBackgroundResource(R.drawable.smile);
                }
                return false;
            }
        });

        //Инициализация счетчика мин
        bombCounter = new BombCounter(NUMBER_OF_BOMBS, activity);

        clock = new Clock(activity);

        //Инициальзация списка слотов
        slotArray = new ArrayList<>();
        //Инициализация размеров поля и кол-во мин
        FIELD_ROWS = Settings.getSize();
        FIELD_COLUMNS = Settings.getSize();
        NUMBER_OF_BOMBS = Settings.getNumberOfBombs();
        flagBombs = NUMBER_OF_BOMBS;


        //Создание стиля для LinearLayout, горизонтальное расположение, размеры подстраивются под родителя
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.HORIZONTAL);
        //Все элементы равны между собой
        layoutParams.weight = 1;

        //Создание слотов
        for (int i = 0; i < FIELD_ROWS; i++) {
            for (int j = 0; j < FIELD_COLUMNS; j++) {

                //Создание слот id - дается по порядку, мин нет поумолчанию
                Slot slot = new Slot(i * FIELD_ROWS + j, activity, false);
                //Запись в список слотов
                slotArray.add(slot);
            }
        }
    }

    Slot getSlot(int index) {
        return slotArray.get(index);
    }


    //Повышение счетчика мин
    //Флаг показывает, верно ли выставлен флаг
    void upBombs(boolean good) {

        //Если правильно - повышается счетчик правильных мин
        if (good) {
            goodBombs++;
            //Если счетчик равен нулу, а кол-во флажков не привышает кол-ва мин, выигрыш!
            if ((goodBombs == 0) && (flagBombs >= 0)) {
                win();
            }
            //oneMoreOpen();
        }
        //Счетчик флажков увеливается
        flagBombs++;
        //Изменяется кол-во оставшихся мин на табло
        bombCounter.setNumber(flagBombs);

    }

    //Понижение счетчика мин
    //Флаг показывает, верно ли выставлен флаг
    void downBombs(boolean good) {
        //Если правильно - понижается счетчик правильных мин
        if (good) {
            goodBombs--;
            //Если счетчик равен нулу, а кол-во флажков не привышает кол-ва мин, выигрыш!
            if ((goodBombs == 0) && (flagBombs >= 0)) {
                win();
            }
        }
        //Счетчик флажков уменьшается
        flagBombs--;
        //Изменяется кол-во оставшихся мин на табло
        bombCounter.setNumber(flagBombs);

    }

    //Метод для запуска новой игры
    void newGame() {

        clock.start();
        button.setBackgroundResource(R.drawable.smile);

        //Нормальный смайлик
        stateOfSmile = 0;

        //Кол-во действительно оставшихся мин
        goodBombs = NUMBER_OF_BOMBS;
        //Кол-во мин которые нужно найти
        flagBombs = NUMBER_OF_BOMBS;
        //Изменяется кол-во оставшихся мин на табло
        bombCounter.setNumber(NUMBER_OF_BOMBS);
        //Кол-во слотов, которые осталось открыть
        closeCounter = FIELD_COLUMNS * FIELD_ROWS - NUMBER_OF_BOMBS;

        //Список мин
        bombsArray = new ArrayList<>();
        //Случайный id слота, где лежит первая мина
        int randNum = (int) (Math.random() * FIELD_COLUMNS * FIELD_ROWS);
        //Первый слот с миной заполняется вне цикла, для исключения повторения
        bombsArray.add(randNum);

        //Генерация мин
        for (int i = 1; i < NUMBER_OF_BOMBS; i++) {

            //Случайный id слота, где лежит мина
            randNum = (int) (Math.random() * FIELD_COLUMNS * FIELD_ROWS);
            //Пока не получим новый id, которого нет в списке, повторяем выбор случайного id
            while (bombsArray.contains(randNum))
                randNum = (int) (Math.random() * FIELD_COLUMNS * FIELD_ROWS);
            //Запись id в список мин
            bombsArray.add(randNum);
        }

        //Обнуление параметров для каждого слота и его минирование
        for (int i = 0; i < FIELD_ROWS; i++) {
            for (int j = 0; j < FIELD_COLUMNS; j++) {

                //Мин вокруг 0
                slotArray.get(i * FIELD_ROWS + j).setCount(0);
                //Слот непоказан
                slotArray.get(i * FIELD_ROWS + j).setShowed(false);
                //Флажок опущен
                slotArray.get(i * FIELD_ROWS + j).setFlagUp(false);
                //Бомб нет
                slotArray.get(i * FIELD_ROWS + j).setHaveBomb(false);

                //Кнопка активна
                slotArray.get(i * FIELD_ROWS + j).slotButton.setEnabled(true);
                //Фон слота принимает вид закрытой кнопки
                slotArray.get(i * FIELD_ROWS + j).slotButton.setBackgroundResource(R.drawable.slot);

                //Если id кнопки есть в списке, минируется!
                if (bombsArray.contains(slotArray.get(i * FIELD_ROWS + j).getId())) {
                    slotArray.get(i * FIELD_ROWS + j).setHaveBomb(true);
                }
            }
        }

        //Заполнение счетчиков мин вокруг
        for (int i = 0; i < FIELD_COLUMNS; i++) {
            for (int j = 0; j < FIELD_COLUMNS; j++) {
                //Если id кнопки есть в списке, ближайшие клетки получают +1 к кол-ву мин вокруг
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

    //Метод, который в случае проигрыша подрывает все мины
    void boomAll() {

        //Смайлик умирает
        button.setBackgroundResource(R.drawable.dead);
        stateOfSmile = 2;

        for (int i = 0; i < FIELD_ROWS; i++) {
            for (int j = 0; j < FIELD_COLUMNS; j++) {

                //Если есть мина - подрыв
                if (bombsArray.contains(slotArray.get(i * FIELD_ROWS + j).getId())) {
                    slotArray.get(i * FIELD_ROWS + j).slotButton.setBackgroundResource(R.drawable.bomb);
                } else {
                    //Мины нет, но флаг поднят, фон меняется на перечеркнутую мину
                    if (slotArray.get(i * FIELD_ROWS + j).flagUp) {
                        slotArray.get(i * FIELD_ROWS + j).slotButton.setBackgroundResource(R.drawable.frong_bomb);
                    }
                }
                //Делаем слот неактивным
                slotArray.get(i * FIELD_ROWS + j).slotButton.setEnabled(false);
            }
        }
    }

    //Метод, который открывает слот и если он пустой, то его соседей
    void openNear(int id) {

        if (id % FIELD_COLUMNS != 0) {
            if (slotArray.get(id - 1).isNoShowed())
                slotArray.get(id - 1).open();

            if (id > FIELD_COLUMNS) {
                if (slotArray.get(id - FIELD_COLUMNS - 1).isNoShowed())
                    slotArray.get(id - FIELD_COLUMNS - 1).open();
            }

            if (id < (FIELD_COLUMNS - 1) * FIELD_COLUMNS) {
                if (slotArray.get(id + FIELD_COLUMNS - 1).isNoShowed())
                    slotArray.get(id + FIELD_COLUMNS - 1).open();
            }
        }

        if (id % FIELD_COLUMNS != FIELD_COLUMNS - 1) {
            if (slotArray.get(id + 1).isNoShowed()) {
                slotArray.get(id + 1).open();
            }
            if (id > FIELD_COLUMNS) {
                if (slotArray.get(id - FIELD_COLUMNS + 1).isNoShowed())
                    slotArray.get(id - FIELD_COLUMNS + 1).open();
            }

            if (id < (FIELD_COLUMNS - 1) * FIELD_COLUMNS) {
                if (slotArray.get(id + FIELD_COLUMNS + 1).isNoShowed())
                    slotArray.get(id + FIELD_COLUMNS + 1).open();
            }
        }

        if (id < (FIELD_COLUMNS - 1) * FIELD_COLUMNS) {
            if (slotArray.get(id + FIELD_COLUMNS).isNoShowed())
                slotArray.get(id + FIELD_COLUMNS).open();
        }

        if (id > FIELD_COLUMNS) {
            if (slotArray.get(id - FIELD_COLUMNS).isNoShowed())
                slotArray.get(id - FIELD_COLUMNS).open();
        }
    }

    //Вызывается, когда открыли новую клетку без мины
    void oneMoreOpen() {
        //Уменьшаем значение счетчика закрытых слотов
        closeCounter--;
        //Если счетчик равен 0, а кол-во флажков неотрицатеьно - победа!
        if ((closeCounter == 0) && (flagBombs >= 0)) {
            win();
        }
    }


    //Метод выигрыша, на всех слотах с минами ставит флажки и блокирует их
    private void win() {
        button.setBackgroundResource(R.drawable.win);
        stateOfSmile = 1;
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
