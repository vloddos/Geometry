package com.geometry.thread;

import android.graphics.Canvas;
import android.os.Handler;

import com.geometry.Global;
import com.geometry.Record;
import com.geometry.entity.Bonus;
import com.geometry.entity.Enemy;
import com.geometry.figure.Figure;
import com.geometry.figure.Intersector;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

public class IntersectionThread extends Thread {

    private static final String LOG_TAG = IntersectionThread.class.getSimpleName();

    private boolean running;// FIXME: 30.07.2019 redundant?
    private FutureTask<Handler> handlerFutureTask;
    private FutureTask<Record> recordFutureTask;
    private Consumer<Record> onGameOver;
    private Consumer<Figure> count;

    public void setHandlerFutureTask(FutureTask<Handler> handlerFutureTask) {
        this.handlerFutureTask = handlerFutureTask;
    }

    public void setRecordFutureTask(FutureTask<Record> recordFutureTask) {
        this.recordFutureTask = recordFutureTask;
    }

    public void setOnGameOver(Consumer<Record> onGameOver) {
        this.onGameOver = onGameOver;
    }

    public void setCount(Consumer<Figure> count) {
        this.count = count;
    }

    @Override
    public void run() {
        Handler handler;
        try {
            handler = handlerFutureTask.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

        Intersector intersector = new Intersector();

        running = true;
        while (running) {
            float square;

            //enemies
            //=============================================
            Global.enemiesLock.readLock().lock();
            for (Enemy enemy : Global.enemies) {
                enemy.lock.lock();
                if (enemy.alive) {
                    Global.player.lock.lock();
                    if (
                            intersector.areIntersecting(
                                    Global.player.figure,
                                    enemy.figure
                            )
                    ) {
                        square = Global.player.figure.getSquare();
                        if (square > enemy.figure.getSquare()) {
                            enemy.alive = false;
                            count.accept(enemy.figure);
                            Figure figure = enemy.figure.clone();
                            figure.paint = Global.player.figure.paint;//цвет остается белым
                            figure.setSquare((float) (square + 20 * Math.sqrt(square)));
                            Global.player.figure = figure;
                            handler.post(enemy.animator::end);
                        } else {
                            Global.player.lock.unlock();
                            enemy.lock.unlock();
                            Global.enemiesLock.readLock().unlock();

                            count.accept(
                                    new Figure() {

                                        @Override
                                        protected void updateComponents() {
                                        }

                                        @Override
                                        public void draw(Canvas canvas) {
                                        }
                                    }
                            );
                            try {
                                onGameOver.accept(recordFutureTask.get());
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    Global.player.lock.unlock();
                }
                enemy.lock.unlock();
            }
            Global.enemiesLock.readLock().unlock();

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
                                count.accept(bonus.numerableFigure.figure);
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