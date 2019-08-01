package com.geometry.entity;

import android.animation.ValueAnimator;
import android.graphics.PointF;

import com.geometry.figure.NumerableFigure;

import java.util.concurrent.locks.ReentrantLock;

public class Bonus {

    public PointF start, end;
    public NumerableFigure numerableFigure;
    public ValueAnimator animator;
    public ReentrantLock lock;
    public boolean alive = true;

    public Bonus(PointF start, PointF end, NumerableFigure numerableFigure, ReentrantLock lock) {
        this.start = start;
        this.end = end;
        this.numerableFigure = numerableFigure;
        this.lock = lock;
    }
}
