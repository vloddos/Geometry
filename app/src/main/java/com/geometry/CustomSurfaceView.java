package com.geometry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.geometry.thread.DrawThread;
import com.geometry.thread.EntityThread;

// TODO: 26.07.2019 как правильно делать lockCanvas???
// TODO: 23.07.2019 check borders
// TODO: 26.07.2019 check float cast priority
// TODO: 23.07.2019 калибровка скорости движения
public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback,
        View.OnTouchListener {

    private SurfaceHolder surfaceHolder;

    private DrawThread drawThread;
    private EntityThread entityThread;

    public CustomSurfaceView(Context context) {
        super(context);
//        setFocusable(true);//возможно без этого не будут открываться панели при смене ориентации

        if (surfaceHolder == null) {
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
        }

        setOnTouchListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null)
                try {
                    Global.player = Global.entityGenerator.generatePlayer(
                            new PointF(
                                    (float) ((Global.width = canvas.getWidth()) / 2),
                                    (float) ((Global.height = canvas.getHeight()) / 2)
                            )
                    );
                    retry = false;
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
        }

        drawThread = new DrawThread(getHolder());//fixme surfaceHolder???
        drawThread.start();
        entityThread = new EntityThread();
        entityThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        entityThread.cancel();
        drawThread.cancel();

        boolean retry = true;
        while (retry)
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    private float x, y;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof SurfaceView)
            switch (event.getAction()) {//todo check
                default:
                    Global.player.lock.lock();
                    try {
                        Global.player.figure.cpoint.x += event.getX() - x;
                        Global.player.figure.cpoint.y += event.getY() - y;
                    } finally {
                        Global.player.lock.unlock();
                    }
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                case MotionEvent.ACTION_UP:
                    return true;
            }
        else
            return false;
    }
}

/*
String PROPERTY_CX = "cx";
    String PROPERTY_CY = "cy";
    Lock clock = new ReentrantLock();
    float cx, cy, r = 20;
    Paint w, b;

    {
        w = new Paint();
        w.setColor(Color.WHITE);
        w.setStyle(Paint.Style.FILL_AND_STROKE);

        b = new Paint();
        b.setColor(Color.BLACK);
        b.setStyle(Paint.Style.FILL_AND_STROKE);
    }
 */
/*
ValueAnimator animator = new ValueAnimator();
        animator.setValues(
                PropertyValuesHolder.ofFloat(PROPERTY_CX, 350, 400, 600),
                PropertyValuesHolder.ofFloat(PROPERTY_CY, 330, 560, 560)
        );
        animator.setDuration(2000);
        animator.addUpdateListener(
                animation -> {
                    clock.lock();
                    try {
                        cx = (float) animation.getAnimatedValue(PROPERTY_CX);
                        cy = (float) animation.getAnimatedValue(PROPERTY_CY);
                    } finally {
                        clock.unlock();
                    }
                }
        );
        animator.start();
        new Thread(
                () -> {
                    while (true) {
                        Canvas canvas = surfaceHolder.lockCanvas();
                        if (canvas != null) {
                            clock.lock();
                            try {
                                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), b);
                                canvas.drawCircle(cx, cy, r, w);
                            } finally {
                                clock.unlock();
                                surfaceHolder.unlockCanvasAndPost(canvas);
                            }
                        }
                    }
                }
        ).start();
 */
/*Canvas canvas = surfaceHolder.lockCanvas();
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
        surfaceHolder.unlockCanvasAndPost(canvas);*/
//drawPlayer(x, y, 10);

/*
Thread draw = new Thread(
                () -> {
                    while (true) {
                        Canvas canvas = surfaceHolder.lockCanvas();

                        //draw black rect
                        Paint b = new Paint();
                        b.setColor(Color.BLACK);
                        b.setStyle(Paint.Style.FILL_AND_STROKE);
                        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), b);

                        //draw player
                        Paint w = new Paint();
                        w.setColor(Color.WHITE);
                        w.setStyle(Paint.Style.FILL_AND_STROKE);
                        canvas.drawCircle(
                                Player.getInstance().x,
                                Player.getInstance().y,
                                Player.getInstance().radius,
                                w
                        );

                        //draw enemies
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
        );

        Thread animation = new Thread(
                () -> {
                    //изменить координаты врагов и удалить если их не видно
                }
        );

        Thread entityGenerator = new Thread(
                () -> {
                    //если есть место сгенерировать врага и добавить в лист
                }
        );

        Thread intersection = new Thread(
                () -> {

                }
        );
 */