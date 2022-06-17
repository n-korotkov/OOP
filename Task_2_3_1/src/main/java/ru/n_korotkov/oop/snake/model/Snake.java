package ru.n_korotkov.oop.snake.model;

import java.util.ArrayDeque;
import java.util.Deque;

import ru.n_korotkov.oop.snake.model.Field.Tile;

public class Snake {

    private Field field;
    private Deque<Point> segments;
    private Direction headDirection = Direction.UP;
    private Direction lastMoveDirection = Direction.UP;
    private boolean alive = true;

    public Snake(int length, int x, int y, Field field) {
        this.field = field;
        segments = new ArrayDeque<>();
        for (int i = 0; i < length; i++) {
            segments.add(new Point(x, y));
        }
        field.setTile(new Point(x, y), Tile.SNAKE);
    }

    public boolean isAlive() {
        return alive;
    }

    public int getLength() {
        return segments.size();
    }

    public Point getHeadPoint() {
        return segments.peekLast();
    }

    public Direction getHeadDirection() {
        return headDirection;
    }

    public Point getPointInFront() {
        if (!alive) {
            return null;
        }
        return getHeadPoint().addModulo(headDirection.toPoint(), field.width(), field.height());
    }

    public boolean hasPoint(Point p) {
        return segments.contains(p);
    }

    public void changeDirection(Direction direction) {
        if (lastMoveDirection.opposite() != direction) {
            headDirection = direction;
        }
    }

    public void advance() {
        if (alive) {
            Point newSegment = getPointInFront();
            Point oldSegment = segments.removeFirst();
            lastMoveDirection = headDirection;
            segments.addLast(newSegment);
            field.setTile(newSegment, Tile.SNAKE);
            field.setTile(oldSegment, Tile.EMPTY);
            field.setTile(segments.getFirst(), Tile.SNAKE);
        }
    }
    
    public void grow() {
        if (alive) {
            segments.addFirst(segments.getFirst());
        }
    }

    public boolean shrink(Point cutPoint) {
        if (!segments.contains(cutPoint)) {
            return false;
        }
        Point removedSegment;
        do {
            removedSegment = segments.removeFirst();
            field.setTile(removedSegment, Tile.EMPTY);
        } while (!removedSegment.equals(cutPoint));

        if (segments.isEmpty()) {
            alive = false;
        }
        return true;
    }

    public void die() {
        shrink(getHeadPoint());
    }

}
