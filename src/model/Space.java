package model;

import utils.Point;

public class Space extends GameObject {
    public static final Character representation = '.';
    public Space(Map map, Point location) {
        super(map, location);
    }
}
