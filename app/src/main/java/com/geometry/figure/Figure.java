package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Figure {

    public float cx, cy;
    protected Paint paint;
    public float square;

    public Figure(float cx, float cy, float square, Paint paint) {
        this.cx = cx;
        this.cy = cy;
        this.square = square;
        this.paint = paint;
    }

    public abstract void draw(Canvas canvas);
}