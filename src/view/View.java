package view;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.val;
import model.Game;
import model.SnakeBodyPart;
import utils.Config;
import utils.Utils;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class View extends Group {
    private Game game;
    private Canvas canvas;
    private Timer gameTimer = new Timer();
    private AnimationTimer animationTimer;
    private Stage stage;
    private int iteration;

    public View(Game game, Stage stage) {
        this.game = game;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        val map = game.getCurrentLevel().getMap();
        canvas = new Canvas(map.getWidth() * Config.GAME_OBJECT_SIZE, map.getHeight() * Config.GAME_OBJECT_SIZE);

        this.getChildren().add(canvas);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                animationTimerTick();
            }
        };
        animationTimer.start();

        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> game.makeGameIteration());
            }
        }, 0, 1000 / game.getDifficult());

        game.addEndGameHandler(this::onEndGame);
        game.addChangeLevelHandler(this::onLevelChanged);
    }

    private void onLevelChanged(int level) {
        resizeField();
    }

    private void onEndGame(int levelNumber, int snakeLength, boolean snakeIsDead) {
        gameTimer.cancel();
        animationTimer.stop();

        stage.hide();

        val alert = new Alert(Alert.AlertType.CONFIRMATION);

        val message = String.format(snakeIsDead
                        ? "Game over!\n Level: %d Apples: %d\n"
                        : "Success! Game finished!\n Level: %d Apples: %d\n"
                , levelNumber, snakeLength - 2);

        alert.setHeaderText(message);
        alert.setContentText("Do you want try again?\n");

        val yesButton = new ButtonType("Yes");
        val noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton) {
            refreshGame(!snakeIsDead);
            stage.show();
        } else if (result.get() == noButton) {
            Platform.exit();
        }
    }

    private void refreshGame(boolean startOver) {
        game.refreshGame(startOver);
        resizeField();

        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> game.makeGameIteration());
            }
        }, 0, 1000 / game.getDifficult());

        animationTimer.start();
    }

    private void resizeField() {
        val map = game.getCurrentLevel().getMap();

        canvas.setWidth(map.getWidth() * Config.GAME_OBJECT_SIZE);
        canvas.setHeight(map.getHeight() * Config.GAME_OBJECT_SIZE);
        stage.sizeToScene();
    }

    private void animationTimerTick() {
        val map = game.getCurrentLevel().getMap();
        val size = Config.GAME_OBJECT_SIZE;
        val graphicsContext = canvas.getGraphicsContext2D();

        for (int x = 0; x < map.getWidth(); x++)
            for (int y = 0; y < map.getHeight(); y++) {
                val gameObject = map.get(x, y);
                graphicsContext.setFill(Utils.getUnitsImages().get(gameObject.getClass()));
                if(gameObject instanceof SnakeBodyPart && ((SnakeBodyPart) gameObject).isSafePart())
                    graphicsContext.setFill(Color.BLUE);
                graphicsContext.fillRect(x * size, y * size, size, size);
            }
    }

    public void closeTimer() {
        gameTimer.cancel();
    }
}
