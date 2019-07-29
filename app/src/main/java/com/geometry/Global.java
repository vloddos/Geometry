package com.geometry;

import com.geometry.entity.Enemy;
import com.geometry.entity.EntityGenerator;
import com.geometry.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Global {

    public static final int ENEMY_COUNT = 10;
    public static int width;
    public static int height;

    public static Player player;
    public static EntityGenerator entityGenerator = new EntityGenerator();

    public static ReadWriteLock enemiesLock = new ReentrantReadWriteLock();
    public static List<Enemy> enemies = new ArrayList<>();
}
