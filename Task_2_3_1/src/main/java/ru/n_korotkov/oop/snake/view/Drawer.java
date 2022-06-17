package ru.n_korotkov.oop.snake.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import ru.n_korotkov.oop.snake.model.Field;
import ru.n_korotkov.oop.snake.model.Game;
import ru.n_korotkov.oop.snake.model.Point;
import ru.n_korotkov.oop.snake.model.Game.State;

public class Drawer {

    private final Paint PAINT_EMPTY = Paint.valueOf("black");
    private final Paint PAINT_SNAKE = Paint.valueOf("green");
    private final Paint PAINT_FOOD  = Paint.valueOf("red");
    private final Paint PAINT_WALL  = Paint.valueOf("white");
    private final Paint PAINT_TEXT  = Paint.valueOf("lightgrey");
    private final Paint PAINT_TEXT_OUTLINE = Paint.valueOf("black");

    private final int PROGRESS_MSG_X = 10;
    private final int PROGRESS_MSG_Y = 20;
    private final int STATE_MSG_X = 10;
    private final int STATE_MSG_Y = 40;

    private final String WIN_MESSAGE = "You won.";
    private final String LOSE_MESSAGE = "You lost.";

    private GraphicsContext gc;

    public Drawer(GraphicsContext gc) {
        this.gc = gc;
    }

    public void draw(Game game) {
        drawField(game);
        drawUI(game);
    }

    private void drawField(Game game) {
        gc.setFill(PAINT_EMPTY);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        Field field = game.getField();
        double cellWidth = gc.getCanvas().getWidth() / field.width();
        double cellHeight = gc.getCanvas().getHeight() / field.height();
        for (int i = 0; i < field.width(); i++) {
            for (int j = 0; j < field.height(); j++) {

                Paint p = switch (field.getTile(new Point(i, j))) {
                    case EMPTY -> PAINT_EMPTY;
                    case SNAKE -> PAINT_SNAKE;
                    case FOOD  -> PAINT_FOOD;
                    case WALL  -> PAINT_WALL;
                };
                gc.setFill(p);
                gc.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
            }
        }
    }

    private void drawUI(Game game) {
        State state = game.getState();
        String state_msg = switch (state) {
            case LOSE -> LOSE_MESSAGE;
            case WIN -> WIN_MESSAGE;
            default -> "";
        };
        String progress_msg = String.format("%d/%d", game.getSnakeLength(), game.WIN_LENGTH);
        drawText(state_msg, STATE_MSG_X, STATE_MSG_Y);
        drawText(progress_msg, PROGRESS_MSG_X, PROGRESS_MSG_Y);
    }

    private void drawText(String text, int x, int y) {
        gc.setStroke(PAINT_TEXT_OUTLINE);
        gc.strokeText(text, x, y);
        gc.setFill(PAINT_TEXT);
        gc.fillText(text, x, y);
    }

}
