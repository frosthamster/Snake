package model;

import utils.Point;

public class Wall extends GameObject {
    public static final Character representation = 'x';
    public Wall(Map map, Point location) {
        super(map, location);
    }
}
