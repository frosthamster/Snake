package Menu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import model.*;

import java.util.HashMap;
import java.util.Map;

public class Editor extends GridPane {
    public Button PlayButton;
    public Button BackButton;
    public Button ResetButton;
    private EditorTile[][] tiles;
    public final int Width;
    public final int Height;
    public final int Scale;
    public Editor(int width, int height, int scale){
        Map<Class, Image> textureMaps = new HashMap<Class, Image>() {{
            put(Wall.class, new Image("./wall.png", scale, scale, false, false));
            put(ShadowWall.class, new Image("./shadowWall.png", scale, scale, false, false));
            put(Space.class, new Image("./space.png", scale, scale, false, false));
            put(SnakeHead.class, new Image("./blueHeadStraight.png", scale, scale, false, false));
            put(SnakeBodyPart.class, new Image("./blueBodyStraight.png", scale, scale, false, false));
            put(Portal.class, new Image("./portal.png", scale, scale, false, false));
        }};
        setAlignment(Pos.TOP_LEFT);
        tiles = new EditorTile[width/scale][height/scale];
        for (int i = 0; i < width / scale; i++) {
            for (int j = 0; j < height / scale; j++) {
                EditorTile tile = new EditorTile(scale, scale, i, j, textureMaps);
                add(tile, i, j);
                tiles[i][j] = tile;
            }
        }
        PlayButton = new Button("play");
        PlayButton.setPrefSize(2 * scale, scale);
        add(PlayButton, 0, height / scale + 1, 2, 1);
        ResetButton = new Button("reset");
        ResetButton.setPrefSize(2 * scale, scale);
        ResetButton.setOnMouseClicked(e -> resetAllTiles());
        add(ResetButton, 2, height / scale + 1, 2, 1);
        BackButton = new Button("back");
        BackButton.setPrefSize(2 * scale, scale);
        add(BackButton, 4, height / scale + 1, 2, 1);
        Scale = scale;
        Height = height;
        Width = width;
    }

    public Class[][] getTiles(){
        Class[][] res = new Class[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                res[i][j] = tiles[i][j].getCurrentClass();
            }
        }
        return res;
    }

    private void resetAllTiles(){
        for (EditorTile[] line : tiles) {
            for (EditorTile tile : line) {
                tile.resetImage();
            }
        }
    }
}
