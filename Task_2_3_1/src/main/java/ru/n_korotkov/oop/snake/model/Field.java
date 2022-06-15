package ru.n_korotkov.oop.snake.model;

import java.util.Arrays;
import java.util.Random;

public class Field {

    public enum Tile {
        EMPTY,
        SNAKE,
        FOOD,
        WALL
    }

    private Tile[][] grid;

    public Field(int width, int height) {
        grid = new Tile[width][height];
        for (Tile[] row : grid) {
            Arrays.fill(row, Tile.EMPTY);
        }
    }

    public int width() {
        return grid.length;
    }

    public int height() {
        return grid[0].length;
    }

    private void checkPointArgument(Point point) {
        if (point.x() < 0 || point.x() >= width() || point.y() < 0 || point.y() >= height()) {
            throw new IllegalArgumentException("Given point is outside the field");
        }
    }

    public Tile getTile(Point point) {
        checkPointArgument(point);
        return grid[point.x()][point.y()];
    }

    public void setTile(Point point, Tile tile) {
        checkPointArgument(point);
        grid[point.x()][point.y()] = tile;
    }

    public Point pickEmptyPoint() {
        Random rng = new Random();
        Point point;
        do {
            int x = rng.nextInt(width());
            int y = rng.nextInt(height());
            point = new Point(x, y);
        } while (getTile(point) != Tile.EMPTY);
        return point;
    }

}
