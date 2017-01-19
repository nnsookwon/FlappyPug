package io.nnsookwon.flappypug.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.nnsookwon.flappypug.FlappyPug;

/**
 * Created by nnsoo on 1/3/2017.
 */

public class BaseState extends State {

    public static final String SETTINGS_PREFERENCE = "SETTINGS_PREFERENCE";
    public static final String SOUND_SETTING = "SOUND_SETTING";
    public static final String DIFFICULTY_SETTING = "DIFFICULTY_SETTING";

    protected Texture background;
    protected Preferences pref;
    protected boolean soundOn;
    protected int difficulty;

    public BaseState(GameStateManager gsm) {
        super(gsm);
        pref = Gdx.app.getPreferences(SETTINGS_PREFERENCE);
        soundOn = pref.getBoolean(SOUND_SETTING, true);
        difficulty = pref.getInteger(DIFFICULTY_SETTING, FlappyPug.MED);
        background = new Texture("background.png");
        camera.setToOrtho(false, FlappyPug.WIDTH, FlappyPug.HEIGHT);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, camera.position.x - camera.viewportWidth/2, 0,
                FlappyPug.WIDTH, FlappyPug.HEIGHT);
        batch.end();

    }

    @Override
    public void dispose() {
        pref.putBoolean(SOUND_SETTING, soundOn);
        pref.putInteger(DIFFICULTY_SETTING, difficulty);
        pref.flush();
        background.dispose();
    }
}
