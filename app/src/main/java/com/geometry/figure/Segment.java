package com.geometry.figure;

import android.graphics.PointF;
import android.util.Log;

import androidx.annotation.NonNull;

public class Segment extends Line {

    String name;
    PointF a, b;

    public Segment(PointF a, PointF b) {
        super(a, b);
        this.a = a;
        this.b = b;
    }

    public Segment(PointF a, PointF b, String name) {
        this(a, b);
        this.name = name;
    }

    @Override
    public boolean isContain(PointF p) {
        float
                left = Math.min(a.x, b.x),
                top = Math.min(a.y, b.y),
                right = Math.max(a.x, b.x),
                bottom = Math.max(a.y, b.y);

        return
                super.isContain(p)
                        && (left <= p.x && p.x <= right || Math.abs(left - p.x) <= 1e-3)
                        && (top <= p.y && p.y <= bottom || Math.abs(top - p.y) <= 1e-3);
    }

    @NonNull
    @Override
    public String toString() {
        return name + ":" + A + " " + B + " " + C + " " + a.toString() + " " + b.toString();
    }
}
