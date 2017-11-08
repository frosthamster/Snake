package model;

import lombok.Getter;
import lombok.val;
import utils.Point;

public class ShadowWall extends GameObject implements TimeWatcher {
    public ShadowWall(Map map, Point location) {
        super(map, location);
    }

    @Getter
    private boolean isUp = true;

    @Override
    public void onTimeChanged(int iteration, int applesEatenCount) {
        if (map.get(location) instanceof SnakePart || map.get(location) instanceof Apple)
            return;
        val reminder = iteration % 60;


        if (reminder >= 0 && reminder < 15) {
            map.add(location, this);
            isUp = true;
        } else {
            map.add(location, new Space(map, new Point(location.getX(), location.getY())));
            isUp = false;
        }
    }
}
