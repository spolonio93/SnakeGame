package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Joypad implements InputProcessor {

    class Button {

        Rectangle rect;
        String action;
        boolean pressed;


        Button(int x, int y, int sx, int sy, String action)
        {
            rect = new Rectangle(x, y, sx, sy);
            this.action = action;
            pressed = false;
        }
    }

    Map<String,Button> buttons;
    Map<Integer,Button> pointers;
    final OrthographicCamera camera;

    public Joypad(OrthographicCamera camera)
    {
        this.camera = camera;
        buttons = new HashMap<>();
        pointers = new HashMap<>();

        Gdx.input.setInputProcessor(this);

    }

    public void addButton(int x, int y, int sx, int sy, String action)
    {
        Button b = new Button(x, y, sx, sy, action);
        buttons.put(action, b);
    }

    boolean isPressed(String action)
    {
        if(buttons.get(action) != null)
            return buttons.get(action).pressed;
        else
            return false;
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(String i:buttons.keySet())
        {
            Button b = buttons.get(i);
            shapeRenderer.setColor(b.pressed ? Color.YELLOW : Color.GREEN);
            shapeRenderer.ellipse(b.rect.x, b.rect.y, b.rect.width, b.rect.height, 2);
            shapeRenderer.rect(b.rect.x, b.rect.y, b.rect.width, b.rect.height);
        }
        shapeRenderer.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {

        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);

        for(String i:buttons.keySet())
        //for(int i = 0; i < buttons.size(); i++)
        {
            if(buttons.get(i).rect.contains(touchPos.x,touchPos.y))
            {
                pointers.put(pointer,buttons.get(i));
                buttons.get(i).pressed = true;
            }
        }

        return true; // return true to indicate the event was handled
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        // your touch up code here
        if(pointers.get(pointer) != null)
        {
            pointers.get(pointer).pressed = false;
            pointers.remove(pointer);
        }
        return true; // return true to indicate the event was handled
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);

        if(pointers.get(pointer) != null)
        {
            if(!pointers.get(pointer).rect.contains(touchPos.x, touchPos.y))
            {
                pointers.get(pointer).pressed = false;
            }
        }

        for(String i:buttons.keySet())
        //for(int i = 0; i < buttons.size(); i++)
        {
            if(buttons.get(i).rect.contains(touchPos.x,touchPos.y))
            {
                if(pointers.get(pointer) != null)
                {
                    pointers.get(pointer).pressed = false;
                }
                pointers.put(pointer,buttons.get(i));
                buttons.get(i).pressed = true;
            }
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}