package com.geometry.entity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.geometry.figure.Circle;
import com.geometry.figure.Figure;
import com.geometry.figure.NumerableFigure;
import com.geometry.figure.Square;
import com.geometry.figure.Triangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class EntityGenerator {

    private final float SQUARE_COEF_MIN = 0.01f;
    private final float SQUARE_COEF_MAX = 4f;
    private final int RGB_MIN = 80;
    private final int RGB_MAX = 220;
    private final long DURATION_MIN = 4000;
    private final long DURATION_MAX = 7000;
    public final float PLAYER_BASE_SQUARE = 400;
    public final int ENEMY_COUNT = 10;
    public final int BONUS_COUNT = 4;
    public final float SCREEN_PERCENTAGE_MAX = 0.01f;

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

    public float generateBonusSquare(float square) {
        float result = (square - PLAYER_BASE_SQUARE) * random.nextFloat();
        if (result <= 0)
            throw new IllegalStateException("Generated bonus square<=0");
        return result;
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

    public void generateAndSetAnimator(Enemy enemy) {
        enemy.animator = new ValueAnimator();
        enemy.animator.setValues(
                PropertyValuesHolder.ofFloat("cx", enemy.start.x, enemy.end.x),
                PropertyValuesHolder.ofFloat("cy", enemy.start.y, enemy.end.y)
        );
        enemy.animator.setDuration(generateDuration());
        enemy.animator.addUpdateListener(
                animation -> {
                    enemy.lock.lock();

                    enemy.figure.cpoint.x = (float) animation.getAnimatedValue("cx");
                    enemy.figure.cpoint.y = (float) animation.getAnimatedValue("cy");

                    enemy.lock.unlock();
                }
        );
        enemy.animator.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        enemy.lock.lock();

                        enemy.alive = false;

                        enemy.lock.unlock();
                    }
                }
        );
    }

    // TODO: 01.08.2019 убрать повторяющийся код
    public void generateAndSetAnimator(Bonus bonus) {
        bonus.animator = new ValueAnimator();
        bonus.animator.setValues(
                PropertyValuesHolder.ofFloat("cx", bonus.start.x, bonus.end.x),
                PropertyValuesHolder.ofFloat("cy", bonus.start.y, bonus.end.y)
        );
        bonus.animator.setDuration(generateDuration());
        bonus.animator.addUpdateListener(
                animation -> {
                    bonus.lock.lock();

                    bonus.numerableFigure.figure.cpoint.x = (float) animation.getAnimatedValue("cx");
                    bonus.numerableFigure.figure.cpoint.y = (float) animation.getAnimatedValue("cy");

                    bonus.lock.unlock();
                }
        );
        bonus.animator.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        bonus.lock.lock();

                        bonus.alive = false;

                        bonus.lock.unlock();
                    }
                }
        );
    }

    public Player generatePlayer(PointF cpoint) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        return new Player(generateFigure(cpoint, PLAYER_BASE_SQUARE, paint));
    }

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
        ReentrantLock lock = new ReentrantLock();

        Enemy enemy = new Enemy(start, end, figure, lock);
        generateAndSetAnimator(enemy);

        return enemy;
    }

    public NumerableFigure generateNumerableFigure(PointF cpoint, float square) {
        int
                r = RGB_MIN + random.nextInt(RGB_MAX - RGB_MIN + 1),
                g = RGB_MIN + random.nextInt(RGB_MAX - RGB_MIN + 1),
                b = RGB_MIN + random.nextInt(RGB_MAX - RGB_MIN + 1);

        Paint paint = new Paint(), numberPaint = new Paint();

        paint.setColor(Color.rgb(r, g, b));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        numberPaint.setColor(Color.rgb(256 - r, 256 - g, 256 - b));//style??????
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setStrokeWidth(5);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setTextSize(50);// TODO: 01.08.2019 regarding square

        return new NumerableFigure(numberPaint, generateFigure(cpoint, square, paint));
    }

    public Bonus generateBonus(float width, float height, float square) {
        List<PointF> pointFS = new ArrayList<>();
        pointFS.add(new PointF(0, random.nextFloat() * height));
        pointFS.add(new PointF(random.nextFloat() * width, 0));
        pointFS.add(new PointF(width, random.nextFloat() * height));
        pointFS.add(new PointF(random.nextFloat() * width, height));

        PointF
                start = pointFS.remove(random.nextInt(4)),
                end = pointFS.remove(random.nextInt(3));

        NumerableFigure numerableFigure = generateNumerableFigure(start, generateBonusSquare(square));
        ReentrantLock lock = new ReentrantLock();

        Bonus bonus = new Bonus(start, end, numerableFigure, lock);
        generateAndSetAnimator(bonus);

        return bonus;
    }

    public List<PointF> generateTrajectory(float width, float height) {// TODO: 04.08.2019 более сложная траектория
        List<PointF> trajectory = new ArrayList<>();

        List<PointF> pointFS = new ArrayList<>();
        pointFS.add(new PointF(0, random.nextFloat() * height));
        pointFS.add(new PointF(random.nextFloat() * width, 0));
        pointFS.add(new PointF(width, random.nextFloat() * height));
        pointFS.add(new PointF(random.nextFloat() * width, height));

        trajectory.add(pointFS.remove(random.nextInt(4)));
        trajectory.add(pointFS.remove(random.nextInt(3)));

        return trajectory;
    }

    public void hideTrajectoryEnds(Figure figure, PointF start, PointF end, float width, float height) {
        if (figure instanceof Circle) {
            if (start.x == 0)
                start.x -= ((Circle) figure).getRadius();
            if (start.y == 0)
                start.y -= ((Circle) figure).getRadius();

            if (start.x == width)
                start.x += ((Circle) figure).getRadius();
            if (start.y == height)
                start.y += ((Circle) figure).getRadius();

            if (end.x == 0)
                end.x -= ((Circle) figure).getRadius();
            if (end.y == 0)
                end.y -= ((Circle) figure).getRadius();

            if (end.x == width)
                end.x += ((Circle) figure).getRadius();
            if (end.y == height)
                end.y += ((Circle) figure).getRadius();
        } else if (figure instanceof Square) {
            if (start.x == 0)
                start.x -= ((Square) figure).getHalfSide();
            if (start.y == 0)
                start.y -= ((Square) figure).getHalfSide();

            if (start.x == width)
                start.x += ((Square) figure).getHalfSide();
            if (start.y == height)
                start.y += ((Square) figure).getHalfSide();

            if (end.x == 0)
                end.x -= ((Square) figure).getHalfSide();
            if (end.y == 0)
                end.y -= ((Square) figure).getHalfSide();

            if (end.x == width)
                end.x += ((Square) figure).getHalfSide();
            if (end.y == height)
                end.y += ((Square) figure).getHalfSide();
        } else if (figure instanceof Triangle) {
            if (start.x == 0)
                start.x -= ((Triangle) figure).getSide() / 2;
            if (start.y == 0)
                start.y -= ((Triangle) figure).getH() / 3;

            if (start.x == width)
                start.x += ((Triangle) figure).getSide() / 2;
            if (start.y == height)
                start.y += 2 * ((Triangle) figure).getH() / 3;

            if (end.x == 0)
                end.x -= ((Triangle) figure).getSide() / 2;
            if (end.y == 0)
                end.y -= ((Triangle) figure).getH() / 3;

            if (end.x == width)
                end.x += ((Triangle) figure).getSide() / 2;
            if (end.y == height)
                end.y += 2 * ((Triangle) figure).getH() / 3;
        }
    }
}
