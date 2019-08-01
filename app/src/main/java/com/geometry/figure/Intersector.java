package com.geometry.figure;

import android.graphics.PointF;
import android.util.Log;

// TODO: 30.07.2019 меньше операций
// TODO: 31.07.2019 учитывать касания
// TODO: 31.07.2019 сделать собственный класс PointD
// TODO: 02.08.2019 use jni
public class Intersector {

    public boolean areIntersecting(Figure a, Figure b) {
        if (a instanceof Circle && b instanceof Circle)
            return circleWithCircle((Circle) a, (Circle) b);
        else if (a instanceof Circle && b instanceof Square)
            return circleWithSquare((Circle) a, (Square) b);
        else if (a instanceof Square && b instanceof Circle)
            return circleWithSquare((Circle) b, (Square) a);
        else if (a instanceof Circle && b instanceof Triangle)
            return circleWithTriangle((Circle) a, (Triangle) b);
        else if (a instanceof Triangle && b instanceof Circle)
            return circleWithTriangle((Circle) b, (Triangle) a);
        else if (a instanceof Square && b instanceof Square)
            return squareWithSquare((Square) a, (Square) b);
        else if (a instanceof Square && b instanceof Triangle)
            return squareWithTriangle((Square) a, (Triangle) b);
        else if (a instanceof Triangle && b instanceof Square)
            return squareWithTriangle((Square) b, (Triangle) a);
        else if (a instanceof Triangle && b instanceof Triangle)
            return triangleWithTriangle((Triangle) a, (Triangle) b);

        return false;
    }

    public boolean segmentWithSegment(Segment a, Segment b) {
        float
                d = a.A * b.B - b.A * a.B,
                x = -a.C * b.B + b.C * a.B,
                y = -a.A * b.C + b.A * a.C;

        if (Math.abs(d) > 0) {
            PointF p = new PointF(x / d, y / d);
            //Log.i("Intersector", p.toString());
            return a.isContain(p) && b.isContain(p);
        } else
            return false;
    }

    // TODO: 31.07.2019 check
    // TODO: 31.07.2019 multiplication instead of cast???
    //усложнять модель дороже чем создать еще 1 объект (но это не точно)
    public boolean circleWithSegment(Circle circle, Segment segment) {
        segment = new Segment(//параллельный перенос отрезка
                new PointF(segment.a.x - circle.cpoint.x, segment.a.y - circle.cpoint.y),
                new PointF(segment.b.x - circle.cpoint.x, segment.b.y - circle.cpoint.y)
        );

        double
                r2 = (double) circle.radius * circle.radius,
                a = (double) segment.A * segment.A + (double) segment.B * segment.B,
                b = 2. * segment.A * segment.C,
                c = (double) segment.C * segment.C - r2 * segment.B * segment.B,
                d = b * b - 4 * a * c;

        //Log.i("circleWithSegment", a + " " + b + " " + c + " " + d);

        if (d >= 0) {
            float
                    x1 = (float) ((-b + Math.sqrt(d)) / (2 * a)),
                    x2 = (float) ((-b - Math.sqrt(d)) / (2 * a)),
                    y1 = (float) Math.sqrt(r2 - x1 * x1),
                    y2 = -y1,
                    y3 = (float) Math.sqrt(r2 - x2 * x2),
                    y4 = -y3;

            PointF[] pointFS = {
                    new PointF(x1, y1), new PointF(x1, y2), new PointF(x2, y3), new PointF(x2, y4)
            };

            for (PointF point : pointFS) {
                //Log.i("points", point.toString());
                if (segment.isContain(point))
                    return true;
            }
        }

        return false;
    }

    public boolean circleWithCircle(Circle a, Circle b) {
        float x = b.cpoint.x - a.cpoint.x, y = b.cpoint.y - a.cpoint.y;
        return Math.sqrt(x * x + y * y) < a.radius + b.radius;
    }

    public boolean circleWithSquare(Circle a, Square b) {
        Segment[] segments = new Segment[]{
                new Segment(//top
                        new PointF(b.cpoint.x - b.halfSide, b.cpoint.y - b.halfSide),
                        new PointF(b.cpoint.x + b.halfSide, b.cpoint.y - b.halfSide)
                ),
                new Segment(//right
                        new PointF(b.cpoint.x + b.halfSide, b.cpoint.y - b.halfSide),
                        new PointF(b.cpoint.x + b.halfSide, b.cpoint.y + b.halfSide)
                ),
                new Segment(//bottom
                        new PointF(b.cpoint.x - b.halfSide, b.cpoint.y + b.halfSide),
                        new PointF(b.cpoint.x + b.halfSide, b.cpoint.y + b.halfSide)
                ),
                new Segment(//left
                        new PointF(b.cpoint.x - b.halfSide, b.cpoint.y - b.halfSide),
                        new PointF(b.cpoint.x - b.halfSide, b.cpoint.y + b.halfSide)
                )
        };

        for (Segment segment : segments)
            if (circleWithSegment(a, segment))
                return true;

        return false;
    }

    public boolean circleWithTriangle(Circle a, Triangle b) {
        Segment[] segments = new Segment[]{
                new Segment(//left
                        new PointF(b.cpoint.x - b.side / 2, b.cpoint.y + b.h / 3),
                        new PointF(b.cpoint.x, b.cpoint.y - 2 * b.h / 3)
                ),
                new Segment(//right
                        new PointF(b.cpoint.x, b.cpoint.y - 2 * b.h / 3),
                        new PointF(b.cpoint.x + b.side / 2, b.cpoint.y + b.h / 3)
                ),
                new Segment(//bottom
                        new PointF(b.cpoint.x - b.side / 2, b.cpoint.y + b.h / 3),
                        new PointF(b.cpoint.x + b.side / 2, b.cpoint.y + b.h / 3)
                )
        };

        for (Segment segment : segments)
            if (circleWithSegment(a, segment))
                return true;

        return false;
    }

    public boolean squareWithSquare(Square a, Square b) {
        Segment[] segments1 = new Segment[]{
                new Segment(//top
                        new PointF(a.cpoint.x - a.halfSide, a.cpoint.y - a.halfSide),
                        new PointF(a.cpoint.x + a.halfSide, a.cpoint.y - a.halfSide)/*,
                        "s1 top"*/
                ),
                new Segment(//right
                        new PointF(a.cpoint.x + a.halfSide, a.cpoint.y - a.halfSide),
                        new PointF(a.cpoint.x + a.halfSide, a.cpoint.y + a.halfSide)/*,
                        "s1 right"*/
                ),
                new Segment(//bottom
                        new PointF(a.cpoint.x - a.halfSide, a.cpoint.y + a.halfSide),
                        new PointF(a.cpoint.x + a.halfSide, a.cpoint.y + a.halfSide)/*,
                        "s1 bottom"*/
                ),
                new Segment(//left
                        new PointF(a.cpoint.x - a.halfSide, a.cpoint.y - a.halfSide),
                        new PointF(a.cpoint.x - a.halfSide, a.cpoint.y + a.halfSide)/*,
                        "s1 left"*/
                )
        };
        Segment[] segments2 = new Segment[]{
                new Segment(//top
                        new PointF(b.cpoint.x - b.halfSide, b.cpoint.y - b.halfSide),
                        new PointF(b.cpoint.x + b.halfSide, b.cpoint.y - b.halfSide)/*,
                        "s2 top"*/
                ),
                new Segment(//right
                        new PointF(b.cpoint.x + b.halfSide, b.cpoint.y - b.halfSide),
                        new PointF(b.cpoint.x + b.halfSide, b.cpoint.y + b.halfSide)/*,
                        "s2 right"*/
                ),
                new Segment(//bottom
                        new PointF(b.cpoint.x - b.halfSide, b.cpoint.y + b.halfSide),
                        new PointF(b.cpoint.x + b.halfSide, b.cpoint.y + b.halfSide)/*,
                        "s2 bottom"*/
                ),
                new Segment(//left
                        new PointF(b.cpoint.x - b.halfSide, b.cpoint.y - b.halfSide),
                        new PointF(b.cpoint.x - b.halfSide, b.cpoint.y + b.halfSide)/*,
                        "s2 left"*/
                )
        };

        for (Segment s1 : segments1)
            for (Segment s2 : segments2)
                if (segmentWithSegment(s1, s2))
                    return true;

        /*for (Segment s1 : segments1)
            for (Segment s2 : segments2) {
                Log.i("Segments", s1.toString() + ";;;" + s2.toString());

                float
                        d = s1.A * s2.B - s2.A * s1.B,
                        x = -s1.C * s2.B + s2.C * s1.B,
                        y = -s1.A * s2.C + s2.A * s1.C;

                if (Math.abs(d) > 0) {
                    PointF p = new PointF(x / d, y / d);
                    Log.i("Intersector", p.toString());
                    if (s1.isContain(p) && s2.isContain(p))
                        return true;
                }
            }*/

        return false;
    }

    public boolean squareWithTriangle(Square a, Triangle b) {
        Segment[] segments1 = new Segment[]{
                new Segment(//top
                        new PointF(a.cpoint.x - a.halfSide, a.cpoint.y - a.halfSide),
                        new PointF(a.cpoint.x + a.halfSide, a.cpoint.y - a.halfSide)
                ),
                new Segment(//right
                        new PointF(a.cpoint.x + a.halfSide, a.cpoint.y - a.halfSide),
                        new PointF(a.cpoint.x + a.halfSide, a.cpoint.y + a.halfSide)
                ),
                new Segment(//bottom
                        new PointF(a.cpoint.x - a.halfSide, a.cpoint.y + a.halfSide),
                        new PointF(a.cpoint.x + a.halfSide, a.cpoint.y + a.halfSide)
                ),
                new Segment(//left
                        new PointF(a.cpoint.x - a.halfSide, a.cpoint.y - a.halfSide),
                        new PointF(a.cpoint.x - a.halfSide, a.cpoint.y + a.halfSide)
                )
        };

        Segment[] segments2 = new Segment[]{
                new Segment(//left
                        new PointF(b.cpoint.x - b.side / 2, b.cpoint.y + b.h / 3),
                        new PointF(b.cpoint.x, b.cpoint.y - 2 * b.h / 3)
                ),
                new Segment(//right
                        new PointF(b.cpoint.x, b.cpoint.y - 2 * b.h / 3),
                        new PointF(b.cpoint.x + b.side / 2, b.cpoint.y + b.h / 3)
                ),
                new Segment(//bottom
                        new PointF(b.cpoint.x - b.side / 2, b.cpoint.y + b.h / 3),
                        new PointF(b.cpoint.x + b.side / 2, b.cpoint.y + b.h / 3)
                )
        };

        for (Segment s1 : segments1)
            for (Segment s2 : segments2)
                if (segmentWithSegment(s1, s2))
                    return true;

        return false;
    }

    public boolean triangleWithTriangle(Triangle a, Triangle b) {
        Segment[] segments1 = new Segment[]{
                new Segment(//left
                        new PointF(a.cpoint.x - a.side / 2, a.cpoint.y + a.h / 3),
                        new PointF(a.cpoint.x, a.cpoint.y - 2 * a.h / 3)
                ),
                new Segment(//right
                        new PointF(a.cpoint.x, a.cpoint.y - 2 * a.h / 3),
                        new PointF(a.cpoint.x + a.side / 2, a.cpoint.y + a.h / 3)
                ),
                new Segment(//bottom
                        new PointF(a.cpoint.x - a.side / 2, a.cpoint.y + a.h / 3),
                        new PointF(a.cpoint.x + a.side / 2, a.cpoint.y + a.h / 3)
                )
        };

        Segment[] segments2 = new Segment[]{
                new Segment(//left
                        new PointF(b.cpoint.x - b.side / 2, b.cpoint.y + b.h / 3),
                        new PointF(b.cpoint.x, b.cpoint.y - 2 * b.h / 3)
                ),
                new Segment(//right
                        new PointF(b.cpoint.x, b.cpoint.y - 2 * b.h / 3),
                        new PointF(b.cpoint.x + b.side / 2, b.cpoint.y + b.h / 3)
                ),
                new Segment(//bottom
                        new PointF(b.cpoint.x - b.side / 2, b.cpoint.y + b.h / 3),
                        new PointF(b.cpoint.x + b.side / 2, b.cpoint.y + b.h / 3)
                )
        };

        for (Segment s1 : segments1)
            for (Segment s2 : segments2)
                if (segmentWithSegment(s1, s2))
                    return true;

        return false;
    }
}