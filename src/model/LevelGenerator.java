package model;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import utils.Config;
import utils.Point;
import utils.Utils;

import java.util.ArrayList;

public final class LevelGenerator {
    private static ArrayList<String> levelsRepresentations = new ArrayList<>();
    @Getter private static ArrayList<Level> levels = new ArrayList<>();

    static {
//        levelsRepresentations.add(
//                "xxxxxxxxxxxxxxxxx\n" +
//                        "x.....x.........x\n" +
//                        "x.....x.........x\n" +
//                        "x.....xxxx##x...x\n" +
//                        "x...........x...x\n" +
//                        "x.s...x.....x...x\n" +
//                        "x.o...x.....x...x\n" +
//                        "xxxxxxxxxxxxxxxxx"
//        );
//        levelsRepresentations.add(
//                "xxxxxxxxxxxxxxxxx\n" +
//                        "x...............x\n" +
//                        "x..xxxx...xxxx..x\n" +
//                        "x...............x\n" +
//                        "x..xxxxxxxxxxx..x\n" +
//                        "x.s...x...x.....x\n" +
//                        "x.o.............x\n" +
//                        "xxxxxxxxxxxxxxxxx"
//        );
//        levelsRepresentations.add(
//                "xxxxxxxxxxxxxxxxx\n" +
//                        "x...............x\n" +
//                        "x.xxxxxxxx......x\n" +
//                        "x...............x\n" +
//                        "x....xxxxxxxxxxxx\n" +
//                        "x.s.............x\n" +
//                        "xxo....x........x\n" +
//                        "xxxxxxxxxxxxxxxxx"
//        );
//        levelsRepresentations.add(
//                "xxxxxxxxxxxxxxxxxxxxxxxxxx\n" +
//                        "x...............#........x\n" +
//                        "x...............#........x\n" +
//                        "x....1..........#........x\n" +
//                        "x...............#........x\n" +
//                        "x...............#........x\n" +
//                        "x...............#........x\n" +
//                        "x........................x\n" +
//                        "x...............1........x\n" +
//                        "x....s...................x\n" +
//                        "x....o...................x\n" +
//                        "xxxxxxxxxxxxxxxxxxxxxxxxxx"
//        );

        for (int i = 0; i < levelsRepresentations.size(); i++)
            levels.add(getLevel(i));
    }

    @SneakyThrows
    private static Map parseLevel(String level) {
        val lines = level.split("\n");
        val width = lines[0].length();
        val result = new Map(lines.length, width);
        int snakeHeadCount = 0;
        int snakeBodyCount = 0;
        val portalManager = new PortalManager();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].length() != width)
                throw new InstantiationException();

            for (int j = 0; j < lines[i].length(); j++) {
                val currentChar = lines[i].toLowerCase().charAt(j);
                if (currentChar == Space.representation)
                    result.add(j, i, new Space(result, new Point(j, i)));
                else if (currentChar == Wall.representation)
                    result.add(j, i, new Wall(result, new Point(j, i)));
                else if (currentChar == ShadowWall.representation)
                    result.add(j, i, new ShadowWall(result, new Point(j, i)));
                else if (currentChar == SnakeHead.representation) {
                    result.add(j, i, new SnakeHead(result, new Point(j, i)));
                    snakeHeadCount++;
                } else if (currentChar == Apple.representation)
                    result.add(j, i, new Apple(result, new Point(j, i)));
                else if (currentChar == SnakeBodyPart.representation) {
                    result.add(j, i, new SnakeBodyPart(result, new Point(j, i), false));
                    snakeBodyCount++;
                } else if (Utils.tryParseChar(currentChar)) {
                    val portalId = Character.getNumericValue(currentChar);
                    val portal = new Portal(result, new Point(j, i), portalId);
                    portalManager.addPortal(portal);
                    result.add(j, i, portal);
                } else
                    throw new IllegalArgumentException();
            }
        }
        if (snakeBodyCount != 1 || snakeHeadCount != 1)
            throw new IllegalArgumentException("At start there should be 1 head and 1 body");
        portalManager.connectPortals();
        return result;
    }

    public static void addLevel(String levelRepresentation){
        levelsRepresentations.add(levelRepresentation);
        levels.add(getLevel(levels.size()));
    }

    public static Level getLevel(int number) {
        val map = parseLevel(levelsRepresentations.get(number));
        val snake = (SnakeHead) map.findFirst(SnakeHead.class);
        val snakeBody = (SnakeBodyPart) map.findFirst(SnakeBodyPart.class);

        if (!snake.isNeighbor(snakeBody))
            throw new IllegalArgumentException("Body is not in contact with head");

        snake.getBody().add(snakeBody);
        return new Level(map, snake, Config.getApplesCount(number));
    }

    private LevelGenerator() {
    }
}
