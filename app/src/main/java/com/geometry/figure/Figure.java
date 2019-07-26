package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public abstract class Figure {

    protected Paint paint;
    public PointF cpoint;
    public float square;

    public Figure(PointF cpoint, float square, Paint paint) {
        this.cpoint = cpoint;
        this.square = square;
        this.paint = paint;
    }

    public abstract void draw(Canvas canvas);
}