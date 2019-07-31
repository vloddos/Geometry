package com.geometry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.geometry.entity.EntityGenerator;
import com.geometry.thread.AnimationThread;
import com.geometry.thread.DrawThread;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// TODO: 26.07.2019 как правильно делать lockCanvas???
// TODO: 26.07.2019 check float cast priority
// TODO: 23.07.2019 калибровка скорости движения
public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback,
        View.OnTouchListener {

    private static final String LOG_TAG = CustomSurfaceView.class.getSimpleName();

    private SurfaceHolder surfaceHolder;

    private DrawThread drawThread;
    private AnimationThread animationThread;

    public CustomSurfaceView(Context context, Runnable onGameOver) {
        super(context);
//        setFocusable(true);//возможно без этого не будут открываться панели при смене ориентации

        if (surfaceHolder == null) {
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
        }

        setOnTouchListener(this);
        animationThread = new AnimationThread(onGameOver);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // TODO: 30.07.2019 убрать зависимость от Global и убрать вообще подобные места
        Global.entityGenerator = new EntityGenerator();
        Global.enemiesLock = new ReentrantReadWriteLock();
        Global.enemies = new ArrayList<>();

        boolean retry = true;
        while (retry) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                //try {
                Global.player = Global.entityGenerator.generatePlayer(
                        new PointF(
                                (float) ((Global.width = canvas.getWidth()) / 2),
                                (float) ((Global.height = canvas.getHeight()) / 2)
                        )
                );
                retry = false;
                //} finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
                //}
            }
        }

        Log.i(LOG_TAG, "width=" + Global.width + " height=" + Global.height);

        drawThread = new DrawThread(getHolder());//fixme surfaceHolder???
        drawThread.start();
        animationThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        drawThread.cancel();
        animationThread.cancel();//join?

        boolean retry = true;
        while (retry)
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        Log.i(LOG_TAG, "threads are cancelled, surface destroyed");//debug
        /**TODO: 29.07.2019 some {@link Global} cleaning*/
    }

    private float x, y;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof SurfaceView)
            switch (event.getAction()) {//todo check
                default:
                    Global.player.lock.lock();
                    //try {
                    Global.player.figure.cpoint.x += event.getX() - x;
                    Global.player.figure.cpoint.y += event.getY() - y;
                    //} finally {
                    Global.player.lock.unlock();
                    //}
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