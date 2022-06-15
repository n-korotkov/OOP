package ru.n_korotkov.oop.snake.model;

public enum Direction {
    RIGHT,
    DOWN,
    LEFT,
    UP;

    public Point toPoint() {
        return switch (this) {
            case RIGHT -> new Point(1, 0);
            case DOWN  -> new Point(0, 1);
            case LEFT  -> new Point(-1, 0);
            case UP    -> new Point(0, -1);
        };
    }

    public Direction opposite() {
        return switch (this) {
            case RIGHT -> LEFT;
            case DOWN  -> UP;
            case LEFT  -> RIGHT;
            case UP    -> DOWN;
        };
    }

}
