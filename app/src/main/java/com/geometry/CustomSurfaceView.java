package com.geometry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

// TODO: 23.07.2019 check borders
public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback,
        View.OnTouchListener {

    private SurfaceHolder surfaceHolder;

    public CustomSurfaceView(Context context) {
        super(context);

        setFocusable(true);//???

        if (surfaceHolder == null) {
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
        }

        setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        Paint tp = new Paint();
        //tp.setStrokeWidth();???
        tp.setColor(Color.RED);
        tp.setStyle(Paint.Style.FILL_AND_STROKE);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(100, 100);
        path.lineTo(140, 140);
        path.lineTo(230, 250);
        path.close();

        canvas.drawPath(path, tp);

        float x = (float) canvas.getWidth() / 2;
        float y = (float) canvas.getHeight() / 2;
        surfaceHolder.unlockCanvasAndPost(canvas);
        //drawPlayer(x, y, 10);

        Thread draw = new Thread(
                () -> {
                    //draw black rect
                    //draw player
                    //draw enemies
                }
        );

        Thread animation = new Thread(
                () -> {
                    //изменить координаты врагов и удалить если их не видно
                }
        );

        Thread enemyGenerator = new Thread(
                () -> {
                    //если есть место сгенерировать врага и добавить в лист
                }
        );
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public void drawPlayer(float x, float y, float radius) {//must drawing in draw thread together with enemies
        Canvas canvas = surfaceHolder.lockCanvas();

        Paint b = new Paint(), w = new Paint();
        b.setColor(Color.BLACK);
        w.setColor(Color.WHITE);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), b);

        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        if (x > canvas.getWidth())
            x = canvas.getWidth();
        if (y > canvas.getHeight())
            y = canvas.getHeight();

        synchronized (Player.class) {
            canvas.drawCircle(x, y, radius, w);
            Player.getInstance().x = x;
            Player.getInstance().y = y;
            Player.getInstance().radius = radius;
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private float x, y;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof SurfaceView) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x = event.getX();
                y = event.getY();
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_UP)
                return true;

            drawPlayer(
                    Player.getInstance().x + event.getX() - x,// TODO: 23.07.2019 калибровка скорости движения
                    Player.getInstance().y + event.getY() - y,
                    Player.getInstance().radius
            );
            x = event.getX();
            y = event.getY();

            return true;
        } else
            return false;
    }
}