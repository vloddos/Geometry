package com.geometry.thread;

import android.os.Handler;
import android.os.Looper;

public class AnimationThread extends Thread {

    public Handler mHandler;

    public void run() {
        Looper.prepare();

        mHandler = new Handler();

        Looper.loop();
    }

    public void cancel() {
        Looper.myLooper().quit();
    }
}