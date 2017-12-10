package Menu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends GridPane {
    @Getter
    private Map<String, Button> menuButtons;

    public MainMenu(){
        val play = new Button("play");
        play.setPrefSize(50, 50);
        menuButtons = new HashMap<>();
        menuButtons.put("play", play);
        val editor = new Button("editor");
        editor.setPrefSize(50, 50);
        menuButtons.put("editor", editor);
        this.add(play, 0, 0);
        this.add(editor, 0, 1);
//        getChildren().add(play);
//        getChildren().add(editor);
        setAlignment(Pos.CENTER);
    }
}
