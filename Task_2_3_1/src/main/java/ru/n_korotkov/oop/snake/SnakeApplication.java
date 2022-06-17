package ru.n_korotkov.oop.snake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.n_korotkov.oop.snake.controller.GameController;
import ru.n_korotkov.oop.snake.view.Drawer;

public class SnakeApplication extends Application {

    private final int WINDOW_WIDTH = 500;
    private final int CANVAS_WIDTH = 400;
    private final int WINDOW_HEIGHT = 400;

    @Override
    public void start(Stage stage) {
        var rootBox = new HBox();
        var settingsBox = new VBox();

        Button startButton = new Button("Start");

        Text widthLabel = new Text("Width");
        Text heightLabel = new Text("Height");
        Text gameSpeedLabel = new Text("Game speed");
        TextField widthInput = new TextField("20");
        TextField heightInput = new TextField("20");
        TextField gameSpeedInput = new TextField("1");

        Canvas canvas = new Canvas(CANVAS_WIDTH, WINDOW_HEIGHT);
        Scene scene = new Scene(rootBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Drawer drawer = new Drawer(gc);
        GameController controller = new GameController(scene, drawer);

        startButton.setOnMouseClicked(event -> {
            try {
                int fieldWidth = Integer.parseInt(widthInput.getText());
                int fieldHeight = Integer.parseInt(heightInput.getText());
                double gameSpeed = Double.parseDouble(gameSpeedInput.getText());
                controller.start(fieldWidth, fieldHeight, gameSpeed);
                canvas.requestFocus();
            } catch (NumberFormatException e) {
                widthInput.setText("");
                heightInput.setText("");
                gameSpeedInput.setText("");
            }
        });

        rootBox.getChildren().addAll(canvas, settingsBox);
        settingsBox.getChildren().addAll(
            widthLabel,
            widthInput,
            heightLabel,
            heightInput,
            gameSpeedLabel,
            gameSpeedInput,
            startButton
        );
        stage.setScene(scene);
        stage.setTitle("Snake");
        stage.setResizable(false);
        stage.show();
    }

}
