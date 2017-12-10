package main;


import Menu.Editor;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.val;
import model.*;
import utils.Config;
import view.View;
import Menu.MainMenu;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Stage theStage;
    private Parent menu;
    private Editor editor;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Snake game");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        menu = createMainMenu();
        primaryStage.setScene(new Scene(menu, Color.BLACK));
        theStage = primaryStage;
        theStage.show();
    }

    private void playDefaultSnake(int difficulty){
        val builder = new LevelBuilder(11, 11);
        builder.add(6,3,  SnakeHead.class);
        builder.add(7,3,  SnakeBodyPart.class);
        builder.add(0,0, Wall.class);
        builder.add(10, 5, Portal.class);
        builder.add(10, 10, Portal.class);
        LevelGenerator.addLevel(builder.getLevelRepresentation());

        val game = new Game(difficulty);
        val view = new View(game, theStage);
        val scene = new Scene(view);
        scene.setOnKeyPressed(
                event -> {
                    val snake = game.getCurrentLevel().getSnakeHead();

                    if (view.isPaused())
                        view.resume();

                    if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D)
                        snake.rotate(Direction.RIGHT);
                    else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A)
                        snake.rotate(Direction.LEFT);
                    else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W)
                        snake.rotate(Direction.UP);
                    else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S)
                        snake.rotate(Direction.DOWN);
                    else if (event.getCode() == KeyCode.SHIFT)
                        view.pause();
                }
        );
        theStage.setOnCloseRequest(
                event -> view.closeTimer()
        );
        theStage.setScene(scene);
    }

    private void playSnake(int difficulty){
        Class[][] tiles = editor.getTiles();
        LevelBuilder bd = new LevelBuilder(tiles[0].length, tiles.length);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                bd.add(j, i, tiles[i][j]);
            }
        }
        LevelGenerator.addLevel(bd.getLevelRepresentation());

        val game = new Game(difficulty);
        val view = new View(game, theStage);
        val scene = new Scene(view);
        scene.setOnKeyPressed(
                event -> {
                    val snake = game.getCurrentLevel().getSnakeHead();

                    if (view.isPaused())
                        view.resume();

                    if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D)
                        snake.rotate(Direction.RIGHT);
                    else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A)
                        snake.rotate(Direction.LEFT);
                    else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W)
                        snake.rotate(Direction.UP);
                    else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S)
                        snake.rotate(Direction.DOWN);
                    else if (event.getCode() == KeyCode.SHIFT)
                        view.pause();
                }
        );
        theStage.setOnCloseRequest(
                event -> view.closeTimer()
        );
        theStage.setScene(scene);
    }


    private Parent createMainMenu(){
        val root = new StackPane();
        root.setPrefSize(400, 400);
        root.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.GREEN,
                                CornerRadii.EMPTY,
                                Insets.EMPTY)
                )
        );
        val mainMenu = new MainMenu();
        editor = new Editor(600, 600, 30);
        editor.SaveButton.setOnMouseClicked(e -> playSnake(Config.DIFFICULTY));
        mainMenu.getMenuButtons().get("play").setOnMouseClicked(e -> playDefaultSnake(Config.DIFFICULTY));
        mainMenu.getMenuButtons().get("editor").setOnMouseClicked(e -> theStage.setScene(new Scene(editor)));
        root.getChildren().add(mainMenu);
        return root;
    }
}

