package com.geometry.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.FutureTask;

public class AnimationThread extends Thread {

    private Looper looper;
    private Handler handler;
    private FutureTask<Handler> handlerFutureTask = new FutureTask<>(() -> handler);//Handler::new???

    public FutureTask<Handler> getHandlerFutureTask() {
        return handlerFutureTask;
    }

    public void run() {
        Looper.prepare();
        looper = Looper.myLooper();

        handler = new Handler();
        handlerFutureTask.run();

        Looper.loop();
    }

    public void cancel() {
        looper.quit();
    }
}