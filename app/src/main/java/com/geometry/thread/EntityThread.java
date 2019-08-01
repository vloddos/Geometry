package com.geometry.thread;

import android.os.Handler;
import android.util.Log;

import com.geometry.Global;
import com.geometry.entity.Bonus;
import com.geometry.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

public class EntityThread extends Thread {

    private boolean running;
    Handler handler;

    @Override
    public void run() {
        running = true;

        Global.enemiesLock.writeLock().lock();
        //try {
        for (int i = 0; i < Global.entityGenerator.ENEMY_COUNT; ++i) {
            Enemy enemy = Global.entityGenerator.generateEnemy(
                    Global.width,
                    Global.height,
                    Global.entityGenerator.PLAYER_BASE_SQUARE
            );
            Global.enemies.add(enemy);
            handler.post(enemy.animator::start);
        }
        //} finally {
        Global.enemiesLock.writeLock().unlock();
        //}

        while (running) {
            //enemies
            //=============================================
            float square;
            List<Enemy> enemiesForRemove = new ArrayList<>();
            List<Enemy> enemiesForAdd = new ArrayList<>();

            Global.enemiesLock.readLock().lock();
            //try {
            Global.enemies.forEach(
                    enemy -> {
                        enemy.lock.lock();
                        //try {
                        if (!enemy.alive)
                            enemiesForRemove.add(enemy);
                        //} finally {
                        enemy.lock.unlock();
                        //}
                    }
            );
            //} finally {
            Global.enemiesLock.readLock().unlock();
            //}

            if (enemiesForRemove.size() > 0) {
                while (enemiesForAdd.size() < enemiesForRemove.size()) {
                    Global.player.lock.lock();
                    //try {
                    square = Global.player.figure.getSquare();
                    //} finally {
                    Global.player.lock.unlock();
                    //}

                    Enemy enemy = Global.entityGenerator.generateEnemy(
                            Global.width,
                            Global.height,
                            square
                    );
                    enemiesForAdd.add(enemy);
                    handler.post(enemy.animator::start);
                }

                Global.enemiesLock.writeLock().lock();
                //try {
                Global.enemies.removeAll(enemiesForRemove);
                Global.enemies.addAll(enemiesForAdd);
                //} finally {
                Global.enemiesLock.writeLock().unlock();
                //}
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