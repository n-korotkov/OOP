package ru.n_korotkov.oop.snake.model;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class ObliviousAI extends SnakeAI {

    private Queue<Direction> path = new ArrayDeque<>();

    public ObliviousAI(Field field, Snake snake) {
        super(field, snake);
    }

    @Override
    public void makeTurn() {
        if (path.isEmpty()) {
            path = findPath(field.pickEmptyPoint());
            if (path == null) {
                path = new ArrayDeque<>(List.of(snake.getHeadDirection()));
            }
        }
        snake.changeDirection(path.remove());
    }

}
