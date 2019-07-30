package com.geometry.figure;

import android.graphics.PointF;

public class Line {

    float A, B, C;

    public Line(PointF a, PointF b) {
        A = a.y - b.y;
        B = b.x - a.x;
        C = a.x * b.y - b.x * a.y;
    }

    public boolean isContain(PointF p) {
        return Math.abs(A * p.x + B * p.y + C) <= 1e-4;
    }
}