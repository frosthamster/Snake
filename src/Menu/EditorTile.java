package Menu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.Portal;
import model.ShadowWall;
import model.Space;
import model.Wall;
import model.SnakeHead;
import model.SnakeBodyPart;
import java.util.Map;

public class EditorTile extends StackPane {
    public final int X;
    public final int Y;
    private ImageView tileImage;
    private int clickCounter = -1;
    public EditorTile(double height, double width, int x, int y, Map<Class, Image> textureMap) {
        X = x;
        Y = y;
        textures = textureMap;
        tileImage = new ImageView();
        updateImage();
        getChildren().add(tileImage);
        setOnMouseClicked(this::handleClick);
    }

    private void handleClick(MouseEvent e){
        if (e.getButton() == MouseButton.SECONDARY) {
            resetImage();
        } else {
            updateImage();
        }
    }

    private Map<Class, Image> textures;

    private static Class[] typeOrder =
            new Class[]{Space.class, Wall.class, ShadowWall.class, Portal.class, SnakeHead.class, SnakeBodyPart.class};

    public void updateImage(){
        clickCounter++;
        if (clickCounter == typeOrder.length)
            clickCounter = 0;
        tileImage.setImage(textures.get(typeOrder[clickCounter]));
    }

    public void resetImage(){
        clickCounter = 0;
        tileImage.setImage(textures.get(typeOrder[clickCounter]));
    }

    public Class getCurrentClass(){
        return typeOrder[clickCounter];
    }
}
