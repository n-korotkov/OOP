package ru.n_korotkov.oop.snake.model;

public record Point(int x, int y) {

    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    public Point addModulo(Point other, int modX, int modY) {
        int sx = ((this.x + other.x) % modX + modX) % modX;
        int sy = ((this.y + other.y) % modY + modY) % modY;
        return new Point(sx, sy);
    }

}
