package com.geometry.thread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import com.geometry.Global;

public class DrawThread extends Thread {

    private boolean running;
    private SurfaceHolder surfaceHolder;

    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null)
                try {
                    canvas.drawColor(Color.BLACK);

                    Global.player.cpointLock.lock();
                    try {
                        if (Global.player.figure.cpoint.x < 0)
                            Global.player.figure.cpoint.x = 0;
                        if (Global.player.figure.cpoint.y < 0)
                            Global.player.figure.cpoint.y = 0;
                        if (Global.player.figure.cpoint.x > canvas.getWidth())
                            Global.player.figure.cpoint.x = canvas.getWidth();
                        if (Global.player.figure.cpoint.y > canvas.getHeight())
                            Global.player.figure.cpoint.y = canvas.getHeight();
                        Global.player.figure.draw(canvas);
                    } finally {
                        Global.player.cpointLock.unlock();
                    }
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
        }
    }

    public void cancel() {
        running = false;
    }
}
