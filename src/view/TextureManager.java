package view;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import lombok.val;
import model.*;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

class TextureManager {
    private static String snakeColor = "blue";

    private final static Map<Class, Image> staticTextures = new HashMap<Class, Image>() {{
        put(Apple.class, new Image("apple.png"));
        put(Wall.class, new Image("wall.png"));
        put(ShadowWall.class, new Image("shadowWall.png"));
        put(Space.class, new Image("space.png"));
        put(Mushroom.class, new Image("mushroom.png"));
    }};

    private Map<GameObject, Image> dynamicTextures = new HashMap<>();

    TextureManager(Game game, int animationIterationCount) {
        fillDynamicTextures(game, animationIterationCount);
    }

    private void fillDynamicTextures(Game game, int animationIterationCount) {
        val snakeHead = game.getCurrentLevel().getSnakeHead();
        val snakeBody = snakeHead.getBody();
        val lastSnakeBodyPart = snakeBody.getLast();
        staticTextures.put(Portal.class, rotate(new Image("portal.png"), animationIterationCount));


        //HEAD
        dynamicTextures.put(snakeHead, getRotatedImage("Head", false, snakeHead.getDirection(),
                snakeHead.getDirectionTo(lastSnakeBodyPart)));


        //BODY
        val body = "Body";

        if (snakeBody.size() > 1)
            dynamicTextures.put(lastSnakeBodyPart, getRotatedImage(body, lastSnakeBodyPart.isSafePart(),
                    lastSnakeBodyPart.getDirectionTo(snakeHead),
                    lastSnakeBodyPart.getDirectionTo(snakeBody.get(snakeBody.size() - 2))));

        for (int i = snakeBody.size() - 2; i >= 1; i--) {
            val previousPart = snakeBody.get(i + 1);
            val nextPart = snakeBody.get(i - 1);

            val directionToPreviousPart = snakeBody.get(i).getDirectionTo(previousPart);
            val directionToNextPart = snakeBody.get(i).getDirectionTo(nextPart);

            dynamicTextures.put(snakeBody.get(i),
                    getRotatedImage(body, snakeBody.get(i).isSafePart(), directionToPreviousPart, directionToNextPart));
        }

        //TAIL
        dynamicTextures.put(snakeBody.getFirst(), getRotatedImage("Tail", snakeBody.getFirst().isSafePart(),
                snakeBody.getFirst().getDirectionTo(
                        snakeBody.size() > 1 ? snakeBody.get(1) : snakeHead
                ), null));
    }

    private Image getRotatedImage(String bodyPartsVarieties, boolean isSafeBodyPart,
                                  Direction directionToPreviousBodyPart, Direction directionToNextBodyPart) {

        val isStraightBodyPart = identy(directionToPreviousBodyPart, directionToNextBodyPart) || directionToNextBodyPart == null;


        val color = isSafeBodyPart ? "red" : snakeColor; //TODO
        val imageUrl = String.format("%s%s%s.png", color,
                bodyPartsVarieties, isStraightBodyPart ? "Straight" : "Turn");
        val image = new Image(imageUrl);


        Integer angleForTurnBodyPart = Utils.getAnglesForTurnBodyParts().get(new Pair<>(directionToPreviousBodyPart, directionToNextBodyPart));
        val angleForStraightPart = Utils.getAnglesForStraightBodyParts().get(directionToPreviousBodyPart);


        return rotate(image, isStraightBodyPart
                ? angleForStraightPart
                : angleForTurnBodyPart);
    }

    private boolean identy(Direction directionToPreviousBodyPart, Direction directionToNextBodyPart) {
        return (directionToNextBodyPart == Direction.UP && directionToPreviousBodyPart == Direction.DOWN) ||
                (directionToNextBodyPart == Direction.DOWN && directionToPreviousBodyPart == Direction.UP) ||
                (directionToNextBodyPart == Direction.RIGHT && directionToPreviousBodyPart == Direction.LEFT) ||
                (directionToNextBodyPart == Direction.LEFT && directionToPreviousBodyPart == Direction.RIGHT) ||
                (directionToNextBodyPart == directionToPreviousBodyPart);
    }

    private Image rotate(Image image, Integer angle) {
        val iv = new ImageView(image);
        iv.setRotate(angle);
        iv.setScaleX(angle > 0 ? 1 : -1);
        iv.setSmooth(true);
        val params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return iv.snapshot(params, null);
    }

    Image getTexture(GameObject gameObject) {
        return staticTextures.containsKey(gameObject.getClass())
                ? staticTextures.get(gameObject.getClass())
                : dynamicTextures.get(gameObject);
    }
}
