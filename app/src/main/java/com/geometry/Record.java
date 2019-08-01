package com.geometry;

public class Record {

    public float square;
    public int circles;
    public int squares;
    public int triangles;
    public int total;

    public Record() {
    }

    public Record(float square, int circles, int squares, int triangles, int total) {
        this.square = square;
        this.circles = circles;
        this.squares = squares;
        this.triangles = triangles;
        this.total = total;
    }
}
