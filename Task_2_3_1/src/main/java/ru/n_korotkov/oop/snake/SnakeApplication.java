package ru.n_korotkov.oop.snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.n_korotkov.oop.snake.controller.GameController;
import ru.n_korotkov.oop.snake.view.Drawer;

public class SnakeApplication extends Application {

    private final int WINDOW_WIDTH = 400;
    private final int WINDOW_HEIGHT = 400;

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Drawer drawer = new Drawer(gc);
        GameController controller = new GameController(scene, drawer);
        controller.start();

        pane.getChildren().add(canvas);
        stage.setScene(scene);
        stage.setTitle("Snake");
        stage.setResizable(false);
        stage.show();
    }

}
