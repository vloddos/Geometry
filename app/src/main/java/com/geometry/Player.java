package com.geometry;

public class Player {

    private static Player instance;

    public static Player getInstance() {
        if (instance == null)
            instance = new Player();
        return instance;
    }

    public float x, y, radius;

    private Player() {
    }
}
