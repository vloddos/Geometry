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

    @NonNull
    @Override
    public String toString() {
        return square + " " + circles + " " + squares + " " + triangles + " " + total;
    }
}
