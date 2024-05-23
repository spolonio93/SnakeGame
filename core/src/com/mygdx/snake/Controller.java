package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Controller {
    private int score;

    // PC
    /*public Direction queryInput() {
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (rightPressed)
            return Direction.RIGHT;
        if (leftPressed)
            return Direction.LEFT;
        if (upPressed)
            return Direction.UP;
        if (downPressed)
            return Direction.DOWN;
        return Direction.NONE;
    }*/

    public Direction queryInput(Joypad joypad) {
        if (joypad.isPressed("RIGHT"))
            return Direction.RIGHT;
        if (joypad.isPressed("LEFT"))
            return Direction.LEFT;
        if (joypad.isPressed("UP"))
            return Direction.UP;
        if (joypad.isPressed("DOWN"))
            return Direction.DOWN;
        return Direction.NONE;
    }

    // PC
    /*public boolean checkForRestart() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            return true;
        } else {
            return false;
        }
    }*/

    public boolean checkForRestart() {
        return Gdx.input.justTouched();
    }

    public void increaseScore() {
        this.score++;
    }

    public void resetScore() {
        this.score = 0;
    }
    public int getScore() {
        return this.score;
    }
}
