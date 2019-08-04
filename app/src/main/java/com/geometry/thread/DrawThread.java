package com.geometry.thread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import com.geometry.Global;

public class DrawThread extends Thread {

    private boolean running;
    private SurfaceHolder surfaceHolder;

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.BLACK);

                Global.player.lock.lock();
                if (Global.player.figure.cpoint.x < 0)
                    Global.player.figure.cpoint.x = 0;
                if (Global.player.figure.cpoint.y < 0)
                    Global.player.figure.cpoint.y = 0;
                if (Global.player.figure.cpoint.x > Global.width)
                    Global.player.figure.cpoint.x = Global.width;
                if (Global.player.figure.cpoint.y > Global.height)
                    Global.player.figure.cpoint.y = Global.height;
                Global.player.figure.draw(canvas);
                Global.player.lock.unlock();

                Global.enemiesLock.readLock().lock();
                Global.enemies.forEach(
                        enemy -> {
                            enemy.lock.lock();
                            if (enemy.alive)
                                enemy.figure.draw(canvas);
                            enemy.lock.unlock();
                        }
                );
                Global.enemiesLock.readLock().unlock();

                Global.bonusesLock.readLock().lock();
                Global.bonuses.forEach(
                        bonus -> {
                            bonus.lock.lock();
                            if (bonus.alive)
                                bonus.numerableFigure.draw(canvas);
                            bonus.lock.unlock();
                        }
                );
                Global.bonusesLock.readLock().unlock();

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void cancel() {
        running = false;
    }
}