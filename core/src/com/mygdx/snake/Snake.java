package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.w3c.dom.UserDataHandler;

import java.security.PrivateKey;

public class Snake {
    private Direction direction = Direction.RIGHT;
    private int x;
    private int y;
    private int xBeforeMove;
    private int yBeforeMove;
    private float timer;
    private final float MOVE_TIME;
    private final int SIZE;
    private final int STEP;
    private STATE state = STATE.PLAYING;
    private Array<BodyPart> bodyParts = new Array<>();
    Viewport viewport;


    public Snake(int size, int speed) {
        MOVE_TIME = 1f / speed;
        SIZE = 16 * size;
        STEP = SIZE;
        this.timer = MOVE_TIME;
    }

    private void move(Direction direction) {
        this.xBeforeMove = this.x;
        this.yBeforeMove = this.y;
        switch (direction) {
            case RIGHT: {
                this.x += STEP;
                return;
            }
            case LEFT: {
                this.x -= STEP;
                return;
            }
            case UP: {
                this.y += STEP;
                return;
            }
            case DOWN: {
                this.y -= STEP;
                return;
            }
        }
    }

    public STATE update(float delta) {
        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;
            move(direction);
            checkOutOfBounds();
            updateBodyParts();
            checkBodyCollision();
        }
        return state;
    }

    public void updateDirection(Direction newDirection) {
        if (this.direction != newDirection) {
            switch (newDirection) {
                case RIGHT: {
                    updateIfNotOpposite(newDirection, Direction.LEFT);
                }
                break;
                case LEFT: {
                    updateIfNotOpposite(newDirection, Direction.RIGHT);
                }
                break;
                case UP: {
                    updateIfNotOpposite(newDirection, Direction.DOWN);
                }
                break;
                case DOWN: {
                    updateIfNotOpposite(newDirection, Direction.UP);
                }
                break;
            }
        }
    }

    private void checkOutOfBounds() {
        if (x >= viewport.getWorldWidth()) {
            x = 0;
        }
        if (x < 0) {
            x = Math.round(viewport.getWorldWidth()) - STEP;
        }
        if (y >= viewport.getWorldHeight()) {
            y = 0;
        }
        if (y < 0) {
            y = Math.round(viewport.getWorldHeight()) - STEP;
        }
    }

    public void checkBodyCollision() {
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.x == this.x && bodyPart.y == this.y) {
                state = STATE.GAME_OVER;
            }
        }
    }
    private void updateIfNotOpposite(Direction newDirection, Direction oppositeDirection) {
        if (this.direction != oppositeDirection || bodyParts.size == 0) {
            this.direction = newDirection;
        }
    }
    public void draw(ShapeRenderer shapeRenderer) {
        drawHead(shapeRenderer);
    }
    public void drawHead(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(x, y, SIZE, SIZE);
        shapeRenderer.end();
    }
    public int getSIZE() {
        return this.SIZE;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void createBodyPart(int x, int y) {
        BodyPart bodyPart = new BodyPart();
        bodyPart.updateBodyPart(x, y);
        bodyParts.insert(0, bodyPart);
    }

    public void drawBodyParts(ShapeRenderer shapeRenderer) {
        for (BodyPart bodyPart : bodyParts) {
            bodyPart.draw(shapeRenderer);
        }
    }
    public void updateBodyParts() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
            bodyPart.updateBodyPart(xBeforeMove, yBeforeMove);
            bodyParts.add(bodyPart);
        }
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public void reset() {
        x = 0;
        y = 0;
        xBeforeMove = 0;
        yBeforeMove = 0;
        bodyParts.clear();
        timer = MOVE_TIME;
        direction = Direction.RIGHT;
        state = STATE.PLAYING;
    }
    private class BodyPart {
        int x;
        int y;
        public void updateBodyPart(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public void draw(ShapeRenderer shapeRenderer) {
            if (!(this.x == Snake.this.x && this.y == Snake.this.y)) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(Color.GOLD);
                shapeRenderer.rect(this.x, this.y, Snake.this.SIZE, Snake.this.SIZE);
                shapeRenderer.end();
            }
        }
    }
}
