package ru.startandroid.develop.lab7;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private Paint paint;
    private Canvas canvas;
    private Bitmap canvasBitmap;
    private MyDrawingView drawingView;
    private float startX, startY;
    private boolean drawing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawingView = new MyDrawingView(this);
        drawingView.setOnTouchListener(this);
        setContentView(drawingView);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                drawing = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (drawing) {
                    float endX = event.getX();
                    float endY = event.getY();
                    canvas.drawLine(startX, startY, endX, endY, paint);
                    startX = endX;
                    startY = endY;
                    drawingView.invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                drawing = false;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                startX = event.getX(event.getActionIndex());
                startY = event.getY(event.getActionIndex());
                drawing = true;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                drawing = false;
                break;
        }
        return true;
    }

    private class MyDrawingView extends View {
        public MyDrawingView(MainActivity context) {
            super(context);
            setBackgroundColor(Color.WHITE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(canvasBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(canvasBitmap, 0, 0, null);
        }
    }
}
