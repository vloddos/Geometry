package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Circle extends Figure {

    float radius;

    @Override
    protected void updateComponents() {
        radius = (float) Math.sqrt(square / Math.PI);
    }

    public Circle(PointF cpoint, float square, Paint paint) {
        super(cpoint, square, paint);
        updateComponents();
    }

    @Override
    public void setSquare(float square) {
        super.setSquare(square);
        updateComponents();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(cpoint.x, cpoint.y, radius, paint);
    }

    @Override
    public Circle clone() {
        Circle clone = (Circle) super.clone();
        clone.radius = radius;
        return clone;
    }
}