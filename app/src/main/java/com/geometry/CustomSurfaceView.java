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
import com.geometry.figure.Figure;
import com.geometry.thread.AnimationThread;
import com.geometry.thread.DrawThread;
import com.geometry.thread.EntityThread;
import com.geometry.thread.IntersectionThread;
import com.geometry.thread.RecordThread;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

// TODO: 26.07.2019 как правильно делать lockCanvas???
// TODO: 26.07.2019 check float cast priority
// TODO: 23.07.2019 калибровка скорости движения
public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback,
        View.OnTouchListener {

    private static final String LOG_TAG = CustomSurfaceView.class.getSimpleName();

    private DrawThread drawThread;
    private AnimationThread animationThread;
    private EntityThread entityThread;
    private IntersectionThread intersectionThread;
    private RecordThread recordThread;
    private Consumer<Record> onGameOver;

    public CustomSurfaceView(Context context) {
        super(context);

        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public void setOnGameOver(Consumer<Record> onGameOver) {
        this.onGameOver = onGameOver;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // TODO: 30.07.2019 убрать зависимость от Global и убрать вообще подобные места
        Global.entityGenerator = new EntityGenerator();
        Global.enemiesLock = new ReentrantReadWriteLock();
        Global.enemies = new ArrayList<>();
        Global.bonusesLock = new ReentrantReadWriteLock();
        Global.bonuses = new ArrayList<>();

        boolean retry = true;
        while (retry) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                Global.player = Global.entityGenerator.generatePlayer(
                        new PointF(
                                (float) ((Global.width = canvas.getWidth()) / 2),
                                (float) ((Global.height = canvas.getHeight()) / 2)
                        )
                );
                Global.fieldSquare = Global.width * Global.height;
                retry = false;

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        Log.i(LOG_TAG, "width=" + Global.width + " height=" + Global.height);

        drawThread = new DrawThread();
        drawThread.setSurfaceHolder(getHolder());//fixme surfaceHolder???
        drawThread.start();

        animationThread = new AnimationThread();
        animationThread.start();

        entityThread = new EntityThread();
        entityThread.setHandlerFutureTask(animationThread.getHandlerFutureTask());
        entityThread.start();

        recordThread = new RecordThread();
        recordThread.start();

        intersectionThread = new IntersectionThread();
        intersectionThread.setHandlerFutureTask(animationThread.getHandlerFutureTask());
        intersectionThread.setRecordFutureTask(recordThread.getRecordFutureTask());
        intersectionThread.setOnGameOver(onGameOver);
        intersectionThread.setCount(recordThread::count);
        intersectionThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        intersectionThread.cancel();
        recordThread.count(// FIXME: 05.08.2019 как то не очень
                new Figure() {

                    @Override
                    protected void updateComponents() {
                    }

                    @Override
                    public void draw(Canvas canvas) {
                    }
                }
        );
        entityThread.cancel();
        animationThread.cancel();
        drawThread.cancel();

        boolean retry = true;
        while (retry)
            try {
                intersectionThread.join();
                recordThread.join();
                entityThread.join();
                animationThread.join();
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
            switch (event.getAction()) {
                default:
                    Global.player.lock.lock();
                    Global.player.figure.cpoint.x += event.getX() - x;
                    Global.player.figure.cpoint.y += event.getY() - y;
                    Global.player.lock.unlock();
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