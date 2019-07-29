package com.geometry.entity;

import com.geometry.figure.Figure;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Player {

    public Lock lock = new ReentrantLock();
    public Figure figure;

    public Player(Figure figure) {
        this.figure = figure;
    }
}
