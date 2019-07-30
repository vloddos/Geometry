package com.geometry.thread;

import android.os.Handler;
import android.util.Log;

import com.geometry.Global;
import com.geometry.entity.Enemy;
import com.geometry.figure.Figure;
import com.geometry.figure.Intersector;

public class IntersectionThread extends Thread {

    private static final String LOG_TAG = IntersectionThread.class.getSimpleName();

    private boolean running;// FIXME: 30.07.2019 redundant?
    private Runnable onGameOver;
    Handler handler;

    public IntersectionThread(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    @Override
    public void run() {
        Intersector intersector = new Intersector();

        running = true;
        while (running) {
            Global.enemiesLock.readLock().lock();
            try {
                for (Enemy enemy : Global.enemies) {
                    enemy.lock.lock();
                    try {
                        if (enemy.alive) {
                            //Log.i(LOG_TAG, "check intersection " + enemy.figure.cpoint.toString());//debug

                            Global.player.lock.lock();
                            try {
                                if (
                                        intersector.areIntersecting(
                                                Global.player.figure,
                                                enemy.figure
                                        )
                                ) {
                                    Log.i(LOG_TAG, "intersection " + enemy.figure.cpoint.toString());//debug

                                    float square = Global.player.figure.getSquare();
                                    if (square > enemy.figure.getSquare()) {
                                        enemy.alive = false;
                                        Figure figure = enemy.figure.clone();//тут также меняется цвет, мб убрать???
                                        figure.setSquare((float) (square + 3 * Math.sqrt(square)));
                                        Global.player.figure = figure;
                                        handler.post(enemy.animator::end);
                                    } else {
                                        /*Global.player.lock.unlock();
                                        enemy.lock.unlock();
                                        Global.enemiesLock.readLock().unlock();*/
                                        onGameOver.run();
                                        return;
                                    }
                                }
                            } finally {
                                Global.player.lock.unlock();
                            }
                        }
                    } finally {
                        enemy.lock.unlock();
                    }
                }
            } finally {
                Global.enemiesLock.readLock().unlock();
            }
        }
    }

    public void cancel() {
        running = false;
    }
}