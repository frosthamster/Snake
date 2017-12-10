package Menu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class Editor extends GridPane {
    public Button SaveButton;
    private EditorTile[][] tiles;
    public final int Width;
    public final int Height;
    public final int Scale;
    public Editor(int width, int height, int scale){
        setAlignment(Pos.TOP_LEFT);
        tiles = new EditorTile[width/scale][height/scale];
        for (int i = 0; i < width / scale; i++) {
            for (int j = 0; j < height / scale; j++) {
                EditorTile tile = new EditorTile(scale, scale, i, j);
                add(tile, i, j);
                tiles[i][j] = tile;
            }
        }
        SaveButton = new Button("save");
        SaveButton.setPrefSize(2 * scale, scale);
        add(SaveButton, 0, height / scale + 1, 2, 1);
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
}
