package ru.startandroid.develop.lab7;
// Объявление пакета для класса MainActivity

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
// Импорт необходимых Android библиотек

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    // Определение класса MainActivity, который расширяет AppCompatActivity и реализует View.OnTouchListener

    private Paint paint;
    private Canvas canvas;
    private Bitmap canvasBitmap;
    private MyDrawingView drawingView;
    private SparseArray<Float> startX = new SparseArray<>();
    private SparseArray<Float> startY = new SparseArray<>();
    private boolean drawing = false;
    // Объявление переменных класса

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Переопределение метода onCreate, вызывается при создании активности

        drawingView = new MyDrawingView(this);
        drawingView.setOnTouchListener(this);
        setContentView(drawingView);
        // Инициализация drawingView и установка его в качестве контентного представления

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        // Инициализация объекта paint для рисования
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Переопределение метода onTouch, вызывается при возникновении события касания

        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        // Получение деталей события касания

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                startX.put(pointerId, event.getX(pointerIndex));
                startY.put(pointerId, event.getY(pointerIndex));
                drawing = true;
                break;
            // Обработка событий нажатия на экран

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    float endX = event.getX(i);
                    float endY = event.getY(i);
                    canvas.drawLine(startX.get(id), startY.get(id), endX, endY, paint);
                    startX.put(id, endX);
                    startY.put(id, endY);
                }
                drawingView.invalidate();
                break;
            // Обработка событий перемещения по экрану

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                startX.remove(pointerId);
                startY.remove(pointerId);
                if (startX.size() == 0) {
                    drawing = false;
                }
                break;
            // Обработка событий отпускания экрана
        }
        return true;
    }

    private class MyDrawingView extends View {
        // Внутренний класс MyDrawingView, который расширяет View

        public MyDrawingView(MainActivity context) {
            super(context);
            setBackgroundColor(Color.WHITE);
            // Конструктор для MyDrawingView, установка цвета фона
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(canvasBitmap);
            // Вызывается при изменении размера представления, создание битмапа и холста
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(canvasBitmap, 0, 0, null);
            // Вызывается для рисования представления, рисование битмапа на холсте
        }
    }
}
