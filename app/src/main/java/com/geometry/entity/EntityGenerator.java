package com.geometry.entity;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.geometry.figure.Circle;
import com.geometry.figure.Figure;
import com.geometry.figure.Square;
import com.geometry.figure.Triangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO: 25.07.2019 fix square coefs
public class EntityGenerator {

    private final float SQUARE_COEF_MIN = 0.1f;
    private final float SQUARE_COEF_MAX = 5f;
    private final int RGB_MIN = 80;
    private final int RGB_MAX = 220;
    private final long DURATION_MIN = 2000;
    private final long DURATION_MAX = 7000;
    private final float PLAYER_BASE_SQUARE = 400;

    private Random random = new Random();

    public Figure generateFigure(PointF cpoint, float square, Paint paint) {
        switch (random.nextInt(3)) {
            case 0:
                return new Circle(cpoint, square, paint);
            case 1:
                return new Square(cpoint, square, paint);
            case 2:
                return new Triangle(cpoint, square, paint);
        }
        return null;
    }

    public float generateSquare(float square) {
        return (SQUARE_COEF_MIN + (SQUARE_COEF_MAX - SQUARE_COEF_MIN) * random.nextFloat()) * square;
    }

    public Paint generatePaint() {
        Paint paint = new Paint();
        paint.setColor(
                Color.rgb(
                        RGB_MIN + random.nextInt(RGB_MAX - RGB_MIN + 1),
                        RGB_MIN + random.nextInt(RGB_MAX - RGB_MIN + 1),
                        RGB_MIN + random.nextInt(RGB_MAX - RGB_MIN + 1)
                )
        );
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        return paint;
    }

    public long generateDuration() {
        return DURATION_MIN + random.nextInt((int) (DURATION_MAX - DURATION_MIN + 1));
    }

    public ValueAnimator generateAnimator(Figure figure, PointF start, PointF end) {
        ValueAnimator animator = new ValueAnimator();
        animator.setValues(
                PropertyValuesHolder.ofFloat("cx", start.x, end.x),
                PropertyValuesHolder.ofFloat("cy", start.y, end.y)
        );
        animator.setDuration(generateDuration());
        animator.addUpdateListener(
                animation -> {
                    figure.cpoint.x = (float) animation.getAnimatedValue("cx");
                    figure.cpoint.y = (float) animation.getAnimatedValue("cy");
                }
        );
        return animator;
    }

    public Player generatePlayer(PointF cpoint) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        return
                new Player(
                        generateFigure(cpoint, PLAYER_BASE_SQUARE, paint)
                );
    }

    // FIXME: 26.07.2019 как передавать width/height???
    public Enemy generateEnemy(float width, float height, float square) {
        List<PointF> pointFS = new ArrayList<>();
        pointFS.add(new PointF(0, random.nextFloat() * height));
        pointFS.add(new PointF(random.nextFloat() * width, 0));
        pointFS.add(new PointF(width, random.nextFloat() * height));
        pointFS.add(new PointF(random.nextFloat() * width, height));

        PointF
                start = pointFS.remove(random.nextInt(4)),
                end = pointFS.remove(random.nextInt(3));

        Figure figure = generateFigure(start, generateSquare(square), generatePaint());

        return
                new Enemy(
                        start,
                        end,
                        figure,
                        generateAnimator(figure, start, end)
                );
    }
}
