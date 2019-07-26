package com.geometry;

import com.geometry.entity.Enemy;
import com.geometry.entity.EntityGenerator;
import com.geometry.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Global {

    public static Player player;
    public static EntityGenerator entityGenerator = new EntityGenerator();
    public static List<Enemy> enemies = new ArrayList<>();
}
