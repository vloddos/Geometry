package com.geometry.entity;

import android.animation.ValueAnimator;
import android.graphics.PointF;

import com.geometry.figure.Figure;

import java.util.concurrent.locks.ReentrantLock;

public class Enemy {

    public PointF start, end;
    public Figure figure;
    public ValueAnimator animator;
    public ReentrantLock lock;
    public boolean alive = true;

    public Enemy(PointF start, PointF end, Figure figure, ReentrantLock lock) {
        this.start = start;
        this.end = end;
        this.figure = figure;
        this.lock = lock;
    }
}