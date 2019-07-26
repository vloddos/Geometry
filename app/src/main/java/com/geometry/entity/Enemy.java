package com.geometry.entity;

import android.animation.ValueAnimator;
import android.graphics.PointF;

import com.geometry.figure.Figure;

public class Enemy {

    private PointF start, end;
    private Figure figure;
    private ValueAnimator animator;

    public Enemy(PointF start, PointF end, Figure figure, ValueAnimator animator) {
        this.start = start;
        this.end = end;
        this.figure = figure;
        this.animator = animator;
    }
}
