package ru.n_korotkov.oop.snake.model;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import ru.n_korotkov.oop.snake.model.Field.Tile;

public class HungryAI extends SnakeAI {

    private Point targetFood = new Point(0, 0);
    private Queue<Direction> foodPath = new ArrayDeque<>();

    public HungryAI(Field field, Snake snake) {
        super(field, snake);
    }

    private Point findFood() {
        for (int i = 0; i < field.width(); i++) {
            for (int j = 0; j < field.height(); j++) {
                Point p = new Point(i, j);
                if (field.getTile(p) == Tile.FOOD) {
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public void makeTurn() {
        if (foodPath.isEmpty() || field.getTile(targetFood) != Tile.FOOD) {
            targetFood = findFood();
            if (targetFood == null) {
                targetFood = new Point(0, 0);
            }
            
            foodPath = findPath(targetFood);
            if (foodPath == null || foodPath.isEmpty()) {
                foodPath = new ArrayDeque<>(List.of(snake.getHeadDirection()));
            }
        }
        snake.changeDirection(foodPath.remove());
    }

}
