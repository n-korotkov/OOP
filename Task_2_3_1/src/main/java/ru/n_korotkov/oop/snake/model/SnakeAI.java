package ru.n_korotkov.oop.snake.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import ru.n_korotkov.oop.snake.model.Field.Tile;

public abstract class SnakeAI {

    protected Field field;
    protected Snake snake;

    public SnakeAI(Field field, Snake snake) {
        this.field = field;
        this.snake = snake;
    }

    protected Queue<Direction> findPath(Point target) {
        Queue<Point> queue = new ArrayDeque<>();
        Direction[][] prev = new Direction[field.width()][field.height()];
        Point head = snake.getHeadPoint();
        prev[head.x()][head.y()] = Direction.NONE;
        queue.add(head);
        while (!queue.isEmpty() && prev[target.x()][target.y()] == null) {
            Point cur = queue.remove();
            for (Direction dir : Direction.values()) {
                Point neigh = cur.addModulo(dir.toPoint(), field.width(), field.height());
                if (prev[neigh.x()][neigh.y()] == null &&
                        field.getTile(neigh) != Tile.WALL && field.getTile(neigh) != Tile.SNAKE) {
                    prev[neigh.x()][neigh.y()] = dir;
                    queue.add(neigh);
                }
            }
        }
        if (prev[target.x()][target.y()] == null) {
            return null;
        }
        List<Direction> path = new ArrayList<>();
        Point p = target;
        while (!p.equals(head)) {
            Direction dir = prev[p.x()][p.y()];
            path.add(dir);
            p = p.addModulo(dir.opposite().toPoint(), field.width(), field.height());
        }
        Collections.reverse(path);
        return new ArrayDeque<>(path);
    }

    public Snake getSnake() {
        return snake;
    }

    public abstract void makeTurn();

}
