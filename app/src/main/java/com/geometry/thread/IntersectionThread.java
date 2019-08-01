package com.geometry.thread;

import android.os.Handler;

import com.geometry.Global;
import com.geometry.entity.Bonus;
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
        //Record record = new Record();// TODO: 01.08.2019 count in new thread???
        Intersector intersector = new Intersector();

        running = true;
        while (running) {
            float square;

            //enemies
            //=============================================
            Global.enemiesLock.readLock().lock();
            //try {
            //Log.i(LOG_TAG, "new loop of the checking intersection");//debug
            for (Enemy enemy : Global.enemies) {
                enemy.lock.lock();
                //try {
                if (enemy.alive) {
                    //Log.i(LOG_TAG, "check intersection " + enemy.figure.cpoint.toString());//debug

                    Global.player.lock.lock();
                    //try {
                    if (
                            intersector.areIntersecting(
                                    Global.player.figure,
                                    enemy.figure
                            )
                    ) {
                        //Log.i(LOG_TAG, "intersection " + enemy.figure.cpoint.toString());//debug
                        square = Global.player.figure.getSquare();
                        if (square > enemy.figure.getSquare()) {
                            enemy.alive = false;
                            Figure figure = enemy.figure.clone();
                            figure.paint = Global.player.figure.paint;//цвет остается белым
                            figure.setSquare((float) (square + 20 * Math.sqrt(square)));
                            Global.player.figure = figure;
                            handler.post(enemy.animator::end);
                        } else {
                            Global.player.lock.unlock();
                            enemy.lock.unlock();
                            Global.enemiesLock.readLock().unlock();
                            onGameOver.run();
                            return;
                        }
                    }
                    //} finally {
                    Global.player.lock.unlock();
                    //}
                }
                //} finally {
                enemy.lock.unlock();
                //}
            }
            //} finally {
            Global.enemiesLock.readLock().unlock();
            //}

            //bonuses
            //=============================================
            Global.player.lock.lock();
            square = Global.player.figure.getSquare();
            Global.player.lock.unlock();

            if (square / Global.fieldSquare > Global.entityGenerator.SCREEN_PERCENTAGE_MAX) {
                Global.bonusesLock.readLock().lock();
                for (Bonus bonus : Global.bonuses) {
                    bonus.lock.lock();
                    if (bonus.alive) {
                        Global.player.lock.lock();
                        if (
                                intersector.areIntersecting(
                                        Global.player.figure,
                                        bonus.numerableFigure.figure
                                )
                        ) {
                            square = Global.player.figure.getSquare() - bonus.numerableFigure.figure.getSquare();

                            if (square >= Global.entityGenerator.PLAYER_BASE_SQUARE) {
                                bonus.alive = false;
                                Figure figure = bonus.numerableFigure.figure.clone();
                                figure.paint = Global.player.figure.paint;//цвет остается белым
                                figure.setSquare(square);
                                Global.player.figure = figure;
                                handler.post(bonus.animator::end);
                            }
                        }
                        Global.player.lock.unlock();
                    }
                    bonus.lock.unlock();
                }
                Global.bonusesLock.readLock().unlock();
            }
        }
    }

    public void cancel() {
        running = false;
    }
}