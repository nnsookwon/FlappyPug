package io.nnsookwon.flappypug.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by nnsoo on 1/1/2017.
 */

public abstract class State {
    protected OrthographicCamera camera;
    protected Vector3 mouse;
    protected GameStateManager gameStateManager;


    public State(GameStateManager gsm) {
        gameStateManager = gsm;
        camera = new OrthographicCamera();
        mouse = new Vector3();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();

}
