package Menu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.*;

import java.util.HashMap;
import java.util.Map;

public class EditorTile extends StackPane {
    public final int X;
    public final int Y;
    private ImageView tileImage;
    private int clickCounter = -1;
    public EditorTile(double height, double width, int x, int y) {
        X = x;
        Y = y;
        textures = new HashMap<Class, Image>() {{
//            put(Apple.class, new Image("./apple.png", width, height, false, false));
            put(Wall.class, new Image("./wall.png", width, height, false, false));
            put(ShadowWall.class, new Image("./shadowWall.png", width, height, false, false));
            put(Space.class, new Image("./space.png", width, height, false, false));
//            put(Mushroom.class, new Image("./mushroom.png", width, height, false, false));
            put(SnakeHead.class, new Image("./blueHeadStraight.png", width, height, false, false));
            put(SnakeBodyPart.class, new Image("./blueBodyStraight.png", width, height, false, false));
            put(Portal.class, new Image("./portal.png", width, height, false, false));
        }};
        tileImage = new ImageView();
        updateImage();
        getChildren().add(tileImage);
        setOnMouseClicked(e -> updateImage());
    }
    private Map<Class, Image> textures;

    private static Class[] typeOrder =
            new Class[]{Space.class, Wall.class, ShadowWall.class, SnakeHead.class, SnakeBodyPart.class, Portal.class};

    private void updateImage(){
        clickCounter++;
        if (clickCounter == typeOrder.length)
            clickCounter = 0;
        tileImage.setImage(textures.get(typeOrder[clickCounter]));
    }

    public Class getCurrentClass(){
        return typeOrder[clickCounter];
    }
}
