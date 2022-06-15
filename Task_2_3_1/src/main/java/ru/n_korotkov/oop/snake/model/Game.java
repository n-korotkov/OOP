package ru.n_korotkov.oop.snake.model;

import ru.n_korotkov.oop.snake.model.Field.Tile;

public class Game {

    public enum State {
        RUNNING,
        WIN,
        LOSE
    };

    public final int FIELD_WIDTH = 21;
    public final int FIELD_HEIGHT = 21;
    public final int INIT_SNAKE_X = 10;
    public final int INIT_SNAKE_Y = 10;
    public final int INIT_SNAKE_LENGTH = 3;
    public final int FOOD_ON_FIELD = 3;
    public final int WIN_LENGTH = 20;

    private Field field;
    private Snake snake;
    private State state;

    public void init() {
        field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
        snake = new Snake(INIT_SNAKE_LENGTH, INIT_SNAKE_X, INIT_SNAKE_Y, field);
        state = State.RUNNING;
        placeFood(FOOD_ON_FIELD);
        placeWalls(10);
    }

    public State getState() {
        return state;
    }

    public Field getField() {
        return field;
    }

    public void tick() {
        Tile tileInFront = field.getTile(snake.getPointInFront());
        switch (tileInFront) {
            case EMPTY:
                snake.advance();
                break;
            case FOOD:
                snake.grow();
                snake.advance();
                placeFood(1);
                if (snake.getLength() == WIN_LENGTH) {
                    state = State.WIN;
                }
                break;
            case SNAKE:
            case WALL:
                state = State.LOSE;
                break;
        }
    }

    public void placeFood(int foods) {
        for (int i = 0; i < foods; i++) {
            field.setTile(field.pickEmptyPoint(), Tile.FOOD);
        }
    }

    public void placeWalls(int walls) {
        for (int i = 0; i < walls; i++) {
            Point point = field.pickEmptyPoint();
            if (Math.abs(point.x() - INIT_SNAKE_X) > 3 || Math.abs(point.y() - INIT_SNAKE_Y) > 3) {
                field.setTile(point, Tile.WALL);
            }
        }
    }

    public int getSnakeLength() {
        return snake.getLength();
    }

    public void changeSnakeDirection(Direction direction) {
        snake.changeDirection(direction);
    }

}
