package com.geometry.thread;

import android.os.Handler;

import com.geometry.Global;
import com.geometry.entity.Bonus;
import com.geometry.entity.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class EntityThread extends Thread {

    private boolean running;
    private FutureTask<Handler> handlerFutureTask;

    public void setHandlerFutureTask(FutureTask<Handler> handlerFutureTask) {
        this.handlerFutureTask = handlerFutureTask;
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

        Global.enemiesLock.writeLock().lock();
        for (int i = 0; i < Global.entityGenerator.ENEMY_COUNT; ++i) {
            Enemy enemy = Global.entityGenerator.generateEnemy(
                    Global.width,
                    Global.height,
                    Global.entityGenerator.PLAYER_BASE_SQUARE
            );
            Global.enemies.add(enemy);
            handler.post(enemy.animator::start);
        }
        Global.enemiesLock.writeLock().unlock();

        running = true;
        while (running) {
            //enemies
            //=============================================
            float square;
            List<Enemy> enemiesForRemove = new ArrayList<>();
            List<Enemy> enemiesForAdd = new ArrayList<>();

            Global.enemiesLock.readLock().lock();
            Global.enemies.forEach(
                    enemy -> {
                        enemy.lock.lock();
                        if (!enemy.alive)
                            enemiesForRemove.add(enemy);
                        enemy.lock.unlock();
                    }
            );
            Global.enemiesLock.readLock().unlock();

            if (enemiesForRemove.size() > 0) {
                while (enemiesForAdd.size() < enemiesForRemove.size()) {
                    Global.player.lock.lock();
                    square = Global.player.figure.getSquare();
                    Global.player.lock.unlock();

                    Enemy enemy = Global.entityGenerator.generateEnemy(
                            Global.width,
                            Global.height,
                            square
                    );
                    enemiesForAdd.add(enemy);
                    handler.post(enemy.animator::start);
                }

                Global.enemiesLock.writeLock().lock();
                Global.enemies.removeAll(enemiesForRemove);
                Global.enemies.addAll(enemiesForAdd);
                Global.enemiesLock.writeLock().unlock();
            }

            //bonuses
            //=============================================
            List<Bonus> bonusesForRemove = new ArrayList<>();
            List<Bonus> bonusesForAdd = new ArrayList<>();

            Global.bonusesLock.readLock().lock();
            Global.bonuses.forEach(
                    bonus -> {
                        bonus.lock.lock();
                        if (!bonus.alive)
                            bonusesForRemove.add(bonus);
                        bonus.lock.unlock();
                    }
            );
            Global.bonusesLock.readLock().unlock();


            Global.player.lock.lock();
            square = Global.player.figure.getSquare();
            Global.player.lock.unlock();
            if (square / Global.fieldSquare > Global.entityGenerator.SCREEN_PERCENTAGE_MAX)
                while (
                        Global.bonuses.size() - bonusesForRemove.size() + bonusesForAdd.size()
                                < Global.entityGenerator.BONUS_COUNT
                ) {
                    Global.player.lock.lock();
                    square = Global.player.figure.getSquare();
                    Global.player.lock.unlock();

                    Bonus bonus = Global.entityGenerator.generateBonus(
                            Global.width,
                            Global.height,
                            square
                    );
                    bonusesForAdd.add(bonus);
                    handler.post(bonus.animator::start);
                }

            Global.bonusesLock.writeLock().lock();
            Global.bonuses.removeAll(bonusesForRemove);
            Global.bonuses.addAll(bonusesForAdd);
            Global.bonusesLock.writeLock().unlock();
        }
    }

    public void cancel() {
        running = false;
    }
}