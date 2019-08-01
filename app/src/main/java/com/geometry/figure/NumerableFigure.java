package com.geometry.figure;

import android.graphics.Canvas;
import android.graphics.Paint;

public class NumerableFigure {

    public Paint numberPaint;
    public Figure figure;

    public NumerableFigure(Paint numberPaint, Figure figure) {
        this.numberPaint = numberPaint;
        this.figure = figure;
    }

    public void draw(Canvas canvas) {
        figure.draw(canvas);
        canvas.drawText(
                "-" + (int) figure.square,//не знал что так можно
                figure.cpoint.x,
                figure.cpoint.y,
                numberPaint
        );
    }
}
