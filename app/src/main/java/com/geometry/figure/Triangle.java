package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class Triangle extends Figure {

    float side, h;

    @Override
    protected void updateComponents() {
        side = (float) Math.sqrt(4 * square / Math.sqrt(3));
        h = (float) (Math.sqrt(3) / 2 * side);
    }

    public Triangle(PointF cpoint, float square, Paint paint) {
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
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(cpoint.x - side / 2, cpoint.y + h / 3);
        path.lineTo(cpoint.x, cpoint.y - 2 * h / 3);
        path.lineTo(cpoint.x + side / 2, cpoint.y + h / 3);
        path.close();

        canvas.drawPath(path, paint);
    }

    @Override
    public Triangle clone() {
        Triangle clone = (Triangle) super.clone();
        clone.side = side;
        clone.h = h;
        return clone;
    }
}