package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Square extends Figure {

    private float halfSide;

    public Square(PointF cpoint, float square, Paint paint) {
        super(cpoint, square, paint);
        halfSide = (float) (Math.sqrt(square) / 2);
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
}
