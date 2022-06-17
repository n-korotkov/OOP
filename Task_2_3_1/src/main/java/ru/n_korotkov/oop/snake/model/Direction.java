package ru.n_korotkov.oop.snake.model;

public enum Direction {
    RIGHT(new Point(1, 0)),
    DOWN(new Point(0, 1)),
    LEFT(new Point(-1, 0)),
    UP(new Point(0, -1)),
    NONE(new Point(0, 0));

    private Point point;
    private Direction opposite;

    private Direction(Point point) {
        this.point = point;
    }

    static {
        RIGHT.opposite = LEFT;
        DOWN.opposite = UP;
        LEFT.opposite = RIGHT;
        UP.opposite = DOWN;
        NONE.opposite = NONE;
    }

    public Point toPoint() {
        return point;
    }

    public Direction opposite() {
        return opposite;
    }

}
