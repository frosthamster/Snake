package model;


import utils.Point;

public class Apple extends GameObject {
    public static final Character representation = '@';
    public Apple(Map map, Point location) {
        super(map, location);
    }
}
