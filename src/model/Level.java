package model;

import lombok.Getter;

import java.util.ArrayList;

public class Level {
    @Getter
    private Map map;
    @Getter
    private SnakeHead snakeHead;
    @Getter
    private int appleCount;
    @Getter
    private ArrayList<TimeWatcher> timeWatchers = new ArrayList<>();

    public Level(Map map, SnakeHead snakeHead, int appleCount) {
        this.map = map;
        this.snakeHead = snakeHead;
        this.appleCount = appleCount;
        map.toStream().filter(e -> e instanceof TimeWatcher).forEach(e -> timeWatchers.add((TimeWatcher) e));
    }
}
