package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Circle extends Figure {

    private float radius;

    public Circle(PointF cpoint, float square, Paint paint) {
        super(cpoint, square, paint);
        radius = (float) Math.sqrt(square / Math.PI);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(cpoint.x, cpoint.y, radius, paint);
    }
}
