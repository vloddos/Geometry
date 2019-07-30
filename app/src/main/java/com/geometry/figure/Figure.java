package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public abstract class Figure implements Cloneable {

    protected Paint paint;
    public PointF cpoint;
    protected float square;

    protected abstract void updateComponents();

    public Figure(PointF cpoint, float square, Paint paint) {
        this.cpoint = cpoint;
        this.square = square;
        this.paint = paint;
    }

    public float getSquare() {
        return square;
    }

    public void setSquare(float square) {// TODO: 30.07.2019 update components here??????
        this.square = square;
    }

    public abstract void draw(Canvas canvas);

    @Override
    public Figure clone() {
        try {
            Figure clone = (Figure) super.clone();
            clone.paint = new Paint(paint);
            clone.cpoint = new PointF(cpoint.x, cpoint.y);
            clone.square = square;
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}