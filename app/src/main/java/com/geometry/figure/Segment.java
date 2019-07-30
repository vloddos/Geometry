package com.geometry.figure;

import android.graphics.PointF;

public class Segment extends Line {

    PointF a, b;

    public Segment(PointF a, PointF b) {
        super(a, b);
        this.a = a;
        this.b = b;
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
                        && left < p.x && p.x < right
                        && top < p.y && p.y < bottom;
    }
}
