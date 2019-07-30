package com.geometry.figure;

import android.graphics.PointF;

// TODO: 30.07.2019 меньше операций
// TODO: 31.07.2019 учитывать касания
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

        if (Math.abs(d) > 1e-4) {
            PointF p = new PointF(x / d, y / d);
            return a.isContain(p) && b.isContain(p);
        } else
            return false;
    }

    // TODO: 31.07.2019 check
    public boolean circleWithSegment(Circle circle, Segment segment) {
        float
                r2 = circle.radius * circle.radius,
                a = segment.A * segment.A + segment.B * segment.B,
                b = 2 * segment.A * segment.C,
                c = segment.C * segment.C - segment.B * segment.B * r2,
                d = b * b - 4 * a * c;

        if (d > 1e-4) {
            float
                    x1 = (float) ((-b + Math.sqrt(d)) / (2 * a)),
                    x2 = (float) ((-b - Math.sqrt(d)) / (2 * a)),
                    y1 = -(segment.A * x1 + segment.C) / segment.B,
                    y2 = -(segment.A * x2 + segment.C) / segment.B;

            return segment.isContain(new PointF(x1, y1)) || segment.isContain(new PointF(x2, y2));
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

        for (Segment s1 : segments1)
            for (Segment s2 : segments2)
                if (segmentWithSegment(s1, s2))
                    return true;

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