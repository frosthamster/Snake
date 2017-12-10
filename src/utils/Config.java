package utils;

public final class Config {
    private Config() {
    }
    public static final int DIFFICULTY = 8;
    public static final int GAME_OBJECT_SIZE = 30;
    public static final int MUSHROOM_ITERATION_PERIOD = 5;
    public static int getApplesCount(int levelNumber) {
        return 40 + 2 * levelNumber;
    }
}
