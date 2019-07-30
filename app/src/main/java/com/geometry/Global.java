package com.geometry;

import com.geometry.entity.Enemy;
import com.geometry.entity.EntityGenerator;
import com.geometry.entity.Player;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

public class Global {

    public static int width;
    public static int height;

    public static Player player;
    public static EntityGenerator entityGenerator;

    public static ReadWriteLock enemiesLock;
    public static List<Enemy> enemies;// TODO: 30.07.2019 make array instead
}
