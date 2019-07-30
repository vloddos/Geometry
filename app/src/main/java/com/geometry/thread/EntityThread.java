package com.geometry.thread;

import android.os.Handler;

import com.geometry.Global;
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
        try {
            for (int i = 0; i < Global.entityGenerator.ENEMY_COUNT; ++i) {
                Enemy enemy = Global.entityGenerator.generateEnemy(
                        Global.width,
                        Global.height,
                        Global.entityGenerator.PLAYER_BASE_SQUARE
                );
                Global.enemies.add(enemy);
                handler.post(enemy.animator::start);
            }
        } finally {
            Global.enemiesLock.writeLock().unlock();
        }

        while (running) {
            List<Enemy> forRemove = new ArrayList<>();
            List<Enemy> forAdd = new ArrayList<>();

            Global.enemiesLock.readLock().lock();
            try {
                Global.enemies.forEach(
                        enemy -> {
                            enemy.lock.lock();
                            try {
                                if (!enemy.alive)
                                    forRemove.add(enemy);
                            } finally {
                                enemy.lock.unlock();
                            }
                        }
                );
            } finally {
                Global.enemiesLock.readLock().unlock();
            }

            if (forRemove.size() > 0) {
                while (forAdd.size() < forRemove.size()) {
                    float square;

                    Global.player.lock.lock();
                    try {
                        square = Global.player.figure.getSquare();
                    } finally {
                        Global.player.lock.unlock();
                    }

                    Enemy enemy = Global.entityGenerator.generateEnemy(
                            Global.width,
                            Global.height,
                            square
                    );
                    forAdd.add(enemy);
                    handler.post(enemy.animator::start);
                }

                Global.enemiesLock.writeLock().lock();
                try {
                    Global.enemies.removeAll(forRemove);
                    Global.enemies.addAll(forAdd);
                } finally {
                    Global.enemiesLock.writeLock().unlock();
                }
            }
        }
    }

    public void cancel() {
        running = false;
    }
}