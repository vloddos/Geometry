package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends Figure {

    public float radius;

    public Circle(float cx, float cy, float square, Paint paint) {
        super(cx, cy, square, paint);
        radius = (float) Math.sqrt(square / Math.PI);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, paint);
    }
}
