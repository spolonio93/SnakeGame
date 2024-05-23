package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
    private GlyphLayout layout;
    private BitmapFont bitmapFont;
    private Batch batch;
    ShapeRenderer shapeRenderer;
    Camera camera;
    Viewport viewport;
    Snake snake;
    Food food;
    Controller controller;
    private Joypad joypad;
    private STATE state = STATE.PLAYING;
    private static final int SNAKE_SIZE = 32;
    private static final int SNAKE_STEP = SNAKE_SIZE;
    private static final float SNAKE_SPEED = 1F;
    private static final int WORLD_WIDTH = 1920;
    private static final int WORLD_HEIGHT = 1080;
    private static final String GAME_OVER_TEXT = "Game Over... Tap space to restart!";

    @Override
    public void show() {
        camera = new OrthographicCamera(WORLD_WIDTH,WORLD_HEIGHT/*Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*/);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT /2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        batch = new SpriteBatch();
        layout = new GlyphLayout();
        bitmapFont = new BitmapFont();
        shapeRenderer = new ShapeRenderer();
        snake = new Snake(2, 20);
        controller = new Controller();
        food = new Food(snake, controller);
        snake.setViewport(viewport);
        food.setViewport(viewport);

        joypad = new Joypad((OrthographicCamera) camera);
        joypad.addButton(100, 250, 150, 150, "LEFT");
        joypad.addButton(350, 250, 150, 150, "RIGHT");
        joypad.addButton(224, 400, 150, 150, "UP");
        joypad.addButton(224, 100, 150, 150, "DOWN");

        Gdx.input.setInputProcessor(joypad);
    }

    @Override
    public void render(float delta) {
        switch (state) {
            case PLAYING: {
                snake.updateDirection(controller.queryInput(joypad));
                state = snake.update(delta);
                food.updatePosition();
                food.checkFoodCollision();
            }
            break;
            case GAME_OVER: {
                if (controller.checkForRestart()) {
                    restart();
                }
            }
            break;
        }
        clearScreen();
        drawScreen();
        
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }

    public void restart() {
        state = STATE.PLAYING;
        food.reset();
        snake.reset();
        controller.resetScore();
    }
    private void drawScreen() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        snake.draw(shapeRenderer);
        snake.drawBodyParts(shapeRenderer);
        food.draw(shapeRenderer);
        drawGrid();
        batch.end();
        batch.begin();
        if (state == STATE.GAME_OVER) {
            layout.setText(bitmapFont, GAME_OVER_TEXT);
            bitmapFont.draw(batch, GAME_OVER_TEXT, viewport.getWorldWidth() / 2 - layout.width / 2, viewport.getWorldHeight() / 2 - layout.height / 2);
        }
        drawScore();
        batch.end();
        joypad.render(shapeRenderer);
    }

    public void drawGrid() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.DARK_GRAY);

        int worldWidth = (int) viewport.getWorldWidth();
        int worldHeight = (int) viewport.getWorldHeight();


        worldWidth = (worldWidth / SNAKE_SIZE) * SNAKE_SIZE;
        worldHeight = (worldHeight / SNAKE_SIZE) * SNAKE_SIZE;

        for (int i = 0; i <= worldWidth; i += SNAKE_SIZE) {
            for (int j = 0; j <= worldHeight; j += SNAKE_SIZE) {
                shapeRenderer.rect(i, j, SNAKE_SIZE, SNAKE_SIZE);
            }
        }

        shapeRenderer.end();
    }

    public void drawScore() {
        if (state == STATE.PLAYING) {
            String scoreToString = "Score: " + controller.getScore();
            bitmapFont.getData().setScale(2.0f, 2.0f);
            layout.setText(bitmapFont, scoreToString);
            bitmapFont.draw(batch, scoreToString,
                    viewport.getWorldWidth() / 2 - layout.width / 2,
                    viewport.getWorldHeight() - layout.height / 2);
            bitmapFont.getData().setScale(1.0f, 1.0f);
        }
    }
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }


}
