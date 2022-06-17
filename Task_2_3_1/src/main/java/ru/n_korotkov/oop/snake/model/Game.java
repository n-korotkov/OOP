package ru.n_korotkov.oop.snake.model;

import java.util.ArrayList;
import java.util.List;

import ru.n_korotkov.oop.snake.model.Field.Tile;

public class Game {

    public enum State {
        RUNNING,
        WIN,
        LOSE
    };

    public final int INIT_SNAKE_LENGTH = 3;
    public final int FOOD_ON_FIELD = 3;
    public final int WALLS_ON_FIELD = 10;
    public final int SAVEZONE_RADIUS = 3;
    public final int WIN_LENGTH = 20;
    public final int HUNGRY_ENEMIES = 1;
    public final int OBLIVIOUS_ENEMIES = 2;

    private Field field;
    private Snake playerSnake;
    private List<Snake> snakes = new ArrayList<>();
    private List<SnakeAI> enemies = new ArrayList<>();
    private State state;

    public void init(int fieldWidth, int fieldHeight) {
        field = new Field(fieldWidth, fieldHeight);
        playerSnake = new Snake(INIT_SNAKE_LENGTH, fieldWidth / 2, fieldHeight / 2, field);
        snakes.add(playerSnake);
        for (int i = 0; i < HUNGRY_ENEMIES; i++) {
            Point startingPoint = field.pickEmptyPoint();
            Snake enemySnake = new Snake(INIT_SNAKE_LENGTH, startingPoint.x(), startingPoint.y(), field);
            snakes.add(enemySnake);
            enemies.add(new HungryAI(field, enemySnake));
        }
        for (int i = 0; i < OBLIVIOUS_ENEMIES; i++) {
            Point startingPoint = field.pickEmptyPoint();
            Snake enemySnake = new Snake(INIT_SNAKE_LENGTH, startingPoint.x(), startingPoint.y(), field);
            snakes.add(enemySnake);
            enemies.add(new ObliviousAI(field, enemySnake));
        }
        state = State.RUNNING;
        placeFood(FOOD_ON_FIELD);
        placeWalls(WALLS_ON_FIELD);
    }

    public State getState() {
        return state;
    }

    public Field getField() {
        return field;
    }

    public void moveSnake(Snake snake) {
        Point pointInFront = snake.getPointInFront();
        Tile tileInFront = field.getTile(pointInFront);
        switch (tileInFront) {
            case EMPTY:
                snake.advance();
                break;
            case FOOD:
                snake.grow();
                snake.advance();
                placeFood(1);
                break;
            case SNAKE:
                for (Snake otherSnake : snakes) {
                    if (otherSnake.isAlive()) {
                        if (otherSnake.shrink(pointInFront)) {
                            break;
                        }
                    }
                }
                snake.advance();
                break;
            case WALL:
                snake.die();
                break;
        }
    }

    public void tick() {
        moveSnake(playerSnake);
        for (SnakeAI enemy : enemies) {
            if (enemy.getSnake().isAlive()) {
                enemy.makeTurn();
                moveSnake(enemy.getSnake());
            }
        }
        if (!playerSnake.isAlive()) {
            state = State.LOSE;
        } else if (playerSnake.getLength() == WIN_LENGTH) {
            state = State.WIN;
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
            int dx = Math.abs(point.x() - field.width() / 2);
            int dy = Math.abs(point.y() - field.height() / 2);
            if (dx > SAVEZONE_RADIUS || dy > SAVEZONE_RADIUS) {
                field.setTile(point, Tile.WALL);
            }
        }
    }

    public int getSnakeLength() {
        return playerSnake.getLength();
    }

    public void changeSnakeDirection(Direction direction) {
        playerSnake.changeDirection(direction);
    }

}
