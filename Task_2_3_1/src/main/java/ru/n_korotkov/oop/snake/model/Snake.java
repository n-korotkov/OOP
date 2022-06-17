package ru.n_korotkov.oop.snake.model;

import java.util.ArrayDeque;
import java.util.Deque;

import ru.n_korotkov.oop.snake.model.Field.Tile;

public class Snake {

    private Field field;
    private Deque<Point> segments;
    private Direction headDirection = Direction.UP;
    private Direction lastMoveDirection = Direction.UP;

    public Snake(int length, int x, int y, Field field) {
        this.field = field;
        segments = new ArrayDeque<>();
        for (int i = 0; i < length; i++) {
            segments.add(new Point(x, y));
        }
        field.setTile(new Point(x, y), Tile.SNAKE);
    }

    public int getLength() {
        return segments.size();
    }

    public Point getHeadPoint() {
        return segments.getLast();
    }

    public Point getPointInFront() {
        Point head = getHeadPoint();
        int x = (headDirection.toPoint().x() + head.x() + field.width()) % field.width();
        int y = (headDirection.toPoint().y() + head.y() + field.height()) % field.height();
        return new Point(x, y);
    }

    public void changeDirection(Direction direction) {
        if (lastMoveDirection.opposite() != direction) {
            headDirection = direction;
        }
    }

    public void advance() {
        Point newSegment = getPointInFront();
        Point oldSegment = segments.removeFirst();
        lastMoveDirection = headDirection;
        segments.addLast(newSegment);
        field.setTile(newSegment, Tile.SNAKE);
        field.setTile(oldSegment, Tile.EMPTY);
        field.setTile(segments.getFirst(), Tile.SNAKE);
    }
    
    public void grow() {
        segments.addFirst(segments.getFirst());
    }

}
