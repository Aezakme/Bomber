package com.example.kirill.coursework;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Класс самой игры, здесь организована основная механика и запуск game_layout
 * <p>
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
 * <p>
 * Победа происходит в двух случаях:
 * 1) игрок открыл все слоты без мин
 * 2) игрок поставил флажки на все мины (если флажков больше, чем мин не считается)
 */

public class Game extends AppCompatActivity {

    //Список мин
    static ArrayList<Integer> bombsArray;
    //Список слотов поля
    static ArrayList<Slot> slotArray;

    //Счетчик закрытых слотов
    static int closeCounter;
    //Количество бомб, которые определил пользователь
    static int flagBombs = 0;
    // Кол-во мин которые действительно осталось найти (игрок может ошибится)
    static int goodBooms;

    //Кнопка новой игры (смайлик), меняется на крутой смайли, если выиграем и мертвого, если проигрываем
    static ImageButton button;
    //Экземпляр класса BombCounter отвечает за счетчик мин в левом верхнем углу экрана
    static BombCounter bombCounter;
    //Вибратор - телефон вибрирует, если игрок проиграл
    static Vibrator vibrator;

    //Кол-во мин
    static int NUMBER_OF_BOMBS;
    //Высота и ширина поля (одинаковы, но сделаны раздельно - возможно потом пригодится)
    static int FIELD_ROWS;
    static int FIELD_COLUMNS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Блокировка поворота экрана
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Инициализация размеров поля и кол-во мин
        FIELD_ROWS = Settings.getSize();
        FIELD_COLUMNS = Settings.getSize();
        NUMBER_OF_BOMBS = Settings.getNumberOfBombs();
        flagBombs = NUMBER_OF_BOMBS;

        //Инициализация кнопки новой игры
        button = (ImageButton) findViewById(R.id.smile);
        //Инициализация вибратора
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //Инициализация счетчика мин
        bombCounter = new BombCounter(NUMBER_OF_BOMBS, this);

        //Инициальзация списка слотов
        slotArray = new ArrayList<>();

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

                //Создание слот id - дается по порядку, мин нет поумолчанию
                Slot slot = new Slot(i * FIELD_ROWS + j, this, false);
                //Запись в список слотов
                slotArray.add(slot);
                //Кнопка слота вставляется в линейный шаблон
                lin.addView(slot.getSlotButton(), j);
            }

            //Линейный шаблон вставляется в другой шаблон - получается строка
            linearLayout.addView(lin, i);
        }

        //Запуск новой игры
        newGame();
    }

    //Повышение счетчика мин
    //Флаг показывает, верно ли выставлен флаг
    void upBombs(boolean good) {

        //Если правильно - повышается счетчик правильных мин
        if (good) {
            goodBooms++;
            //Если счетчик равен нулу, а кол-во флажков не привышает кол-ва мин, выигрыш!
            if ((goodBooms == 0) && (flagBombs >= 0)) {
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
            goodBooms--;
            //Если счетчик равен нулу, а кол-во флажков не привышает кол-ва мин, выигрыш!
            if ((goodBooms == 0) && (flagBombs >= 0)) {
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

        //Кол-во действительно оставшихся мин
        goodBooms = NUMBER_OF_BOMBS;
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

    //Вызывается, когда открыли новую клетку без мины
    void oneMoreOpen() {
        //Уменьшаем значение счетчика закрытых слотов
        closeCounter--;
        //Если счетчик равен 0, а кол-во флажков неотрицатеьно - победа!
        if ((closeCounter == 0) && (flagBombs >= 0)) {
            win();
        }
    }

    //Нажатие на смайли запустит новую игру
    public void startNew(View view) {
        button.setBackgroundResource(R.drawable.smile);
        newGame();
    }

    //Метод выигрыша, на всех слотах с минами ставит флажки и блокирует их
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

