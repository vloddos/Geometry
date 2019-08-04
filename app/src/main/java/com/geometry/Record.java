package com.geometry;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Record implements Serializable {

    private static final long serialVersionUID = 8102953499901562921L;

    public double square;
    public int circles;
    public int squares;
    public int triangles;
    public int total;

    public Record() {
    }

    public Record(double square, int circles, int squares, int triangles, int total) {
        this.square = square;
        this.circles = circles;
        this.squares = squares;
        this.triangles = triangles;
        this.total = total;
    }

    @NonNull
    @Override
    public String toString() {
        return square + " " + circles + " " + squares + " " + triangles + " " + total;
    }
}
