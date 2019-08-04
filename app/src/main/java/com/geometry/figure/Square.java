package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Square extends Figure {

    float halfSide;

    @Override
    protected void updateComponents() {
        halfSide = (float) (Math.sqrt(square) / 2);
    }

    public Square(PointF cpoint, float square, Paint paint) {
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
        canvas.drawRect(
                cpoint.x - halfSide,
                cpoint.y - halfSide,
                cpoint.x + halfSide,
                cpoint.y + halfSide,
                paint
        );
    }

    @Override
    public Square clone() {
        Square clone = (Square) super.clone();
        clone.halfSide = halfSide;
        return clone;
    }

    public float getHalfSide() {
        return halfSide;
    }
}