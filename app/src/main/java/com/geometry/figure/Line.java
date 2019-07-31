package com.geometry.figure;

import android.graphics.PointF;
import android.util.Log;

public class Line {

    float A, B, C;

    public Line(PointF a, PointF b) {
        A = a.y - b.y;
        B = b.x - a.x;
        C = a.x * b.y - b.x * a.y;
    }

    public boolean isContain(PointF p) {
        /*float tmp = Math.abs(A * p.x + B * p.y + C);
        Log.i("Line", A + " " + B + " " + C + " " + p.x + " " + p.y + " tmp=" + tmp);
        return tmp <= 1e-3;*/
        return Math.abs(A * p.x + B * p.y + C) <= 1e-3;
    }
}