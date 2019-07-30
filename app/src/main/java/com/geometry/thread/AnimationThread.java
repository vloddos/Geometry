package com.geometry.thread;

import android.os.Handler;
import android.os.Looper;

public class AnimationThread extends Thread {

    private EntityThread entityThread;
    private IntersectionThread intersectionThread;
    private Looper looper;
    private Handler handler;

    public AnimationThread(Runnable onGameOver) {
        entityThread = new EntityThread();
        intersectionThread = new IntersectionThread(onGameOver);
    }

    public void run() {
        Looper.prepare();
        looper = Looper.myLooper();

        handler = new Handler();//await???

        entityThread.handler = handler;// FIXME: 30.07.2019 все равно как то криво
        entityThread.start();
        intersectionThread.handler = handler;
        intersectionThread.start();

        Looper.loop();
    }

    public void cancel() {
        entityThread.cancel();
        intersectionThread.cancel();

        boolean retry = true;
        while (retry)
            try {
                entityThread.join();//join >1 times???????
                intersectionThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        looper.quit();
    }
}