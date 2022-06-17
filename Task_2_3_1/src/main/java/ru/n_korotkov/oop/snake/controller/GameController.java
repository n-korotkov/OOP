package ru.n_korotkov.oop.snake.controller;

import javafx.util.Duration;
import ru.n_korotkov.oop.snake.model.Direction;
import ru.n_korotkov.oop.snake.model.Field;
import ru.n_korotkov.oop.snake.model.Game;
import ru.n_korotkov.oop.snake.model.Game.State;
import ru.n_korotkov.oop.snake.view.Drawer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class GameController {

    private final Duration KEYFRAME_DURATION = Duration.millis(100);

    private Drawer drawer;
    private Game game;
    private Timeline timeline;
    private boolean running = false;

    public GameController(Scene scene, Drawer drawer) {
        this.drawer = drawer;
        game = new Game();
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(KEYFRAME_DURATION, e -> update()));
        scene.setOnKeyPressed(this::onKeyPressed);
    }

    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case RIGHT:
                game.changeSnakeDirection(Direction.RIGHT);
                break;
            case DOWN:
                game.changeSnakeDirection(Direction.DOWN);
                break;
            case LEFT:
                game.changeSnakeDirection(Direction.LEFT);
                break;
            case UP:
                game.changeSnakeDirection(Direction.UP);
                break;
            case R:
                if (!running) {
                    start();
                }
                break;
            default:
        }
    }

    public Field getField() {
        return game.getField();
    }

    private void update() {
        game.tick();
        drawer.draw(game);
        if (game.getState() != State.RUNNING) {
            stop();
        }
    }

    public void start() {
        game.init();
        running = true;
        timeline.play();
    }

    public void stop() {
        running = false;
        timeline.stop();
    }

}
