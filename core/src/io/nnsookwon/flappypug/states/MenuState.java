package io.nnsookwon.flappypug.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by nnsoo on 1/1/2017.
 */

public class MenuState extends BaseState {
    private static int MARGIN = 50;
    private static int OFFSET = 100;
    private Texture playButton;
    private Texture settingsButton;
    private Vector3 lastTouched;
    private float play_bottom, play_top, settings_bottom, settings_top,
                button_right, button_left;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        playButton = new Texture("button_play.png");
        settingsButton = new Texture("button_settings.png");
        lastTouched = new Vector3();


        button_left = camera.position.x - playButton.getWidth()/2;
        button_right = button_left + playButton.getWidth();
        play_bottom = camera.position.y + MARGIN + OFFSET;
        play_top = play_bottom + playButton.getHeight();
        settings_bottom = camera.position.y - (settingsButton.getHeight() + MARGIN) + OFFSET;
        settings_top = settings_bottom + settingsButton.getHeight();

    }
    @Override
    public void handleInput() {
        if(input.justTouched()) {
            lastTouched.set(input.getX(), input.getY(), 0);
            lastTouched = camera.unproject(lastTouched);
            if (lastTouched.x >= button_left && lastTouched.x <= button_right) {
                if (lastTouched.y >= play_bottom && lastTouched.y <= play_top) {
                    //play button was pressed;
                    gameStateManager.set(new PlayState(gameStateManager));
                }
                if (lastTouched.y >= settings_bottom && lastTouched.y <= settings_top) {
                    //settings button was pressed;
                    gameStateManager.set(new SettingsState(gameStateManager));
                }
            }
        }
    }


    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch batch) {

        super.render(batch);
        batch.begin();
        batch.draw(playButton, button_left, play_bottom);
        batch.draw(settingsButton, button_left, settings_bottom);
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        playButton.dispose();
        settingsButton.dispose();
    }


}
