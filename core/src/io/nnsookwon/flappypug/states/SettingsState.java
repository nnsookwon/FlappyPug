package io.nnsookwon.flappypug.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import io.nnsookwon.flappypug.FlappyPug;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by nnsoo on 1/3/2017.
 */

public class SettingsState extends BaseState {

    private static final int MARGIN = 20;
    private Texture soundLabel;
    private Texture backLabel;
    private Texture checkBoxEmpty;
    private Texture checkBoxMarked;
    private Texture levelLabel;
    private Texture easyLabel;
    private Texture mediumLabel;
    private Texture hardLabel;
    private Texture checkBox;
    private Texture levelSelected;
    private Texture menu;
    private int soundRowWidth;
    private Vector3 lastTouched;


    public SettingsState(GameStateManager gsm) {
        super(gsm);
        soundLabel = new Texture("sound_label.png");
        backLabel = new Texture("back_label.png");
        checkBoxEmpty = new Texture("checkbox_empty.png");
        checkBoxMarked = new Texture("checkbox_marked.png");
        menu = new Texture("settings_menu.png");
        levelLabel = new Texture("level_label.png");
        easyLabel = new Texture("easy_label.png");
        mediumLabel = new Texture("medium_label.png");
        hardLabel = new Texture("hard_label.png");
        camera.setToOrtho(false, FlappyPug.WIDTH, FlappyPug.HEIGHT);
        soundRowWidth = soundLabel.getWidth() + checkBoxEmpty.getWidth() + 2* MARGIN;
        lastTouched = new Vector3();
        determineSelect(soundOn, difficulty);
    }

    @Override
    public void handleInput() {
        if(input.justTouched()) {
            lastTouched.set(input.getX(), input.getY(), 0);
            lastTouched = camera.unproject(lastTouched);

            if (lastTouched.x >= camera.position.x - soundRowWidth/2 &&
                    lastTouched.x <= camera.position.x + soundRowWidth/2 &&
                    lastTouched.y >= camera.position.y + MARGIN &&
                    lastTouched.y <= camera.position.y + MARGIN + checkBoxEmpty.getHeight()) {
                // checkbox was touched
                soundOn = !soundOn;

                pref.putBoolean(SOUND_SETTING, soundOn);
                pref.flush();
            }

            if (lastTouched.x >= camera.position.x - soundRowWidth/2 + levelLabel.getWidth() &&
                    lastTouched.x <= camera.position.x - soundRowWidth/2 + levelLabel.getWidth() + levelSelected.getWidth() &&
                    lastTouched.y <= camera.position.y - MARGIN &&
                    lastTouched.y >= camera.position.y - (MARGIN + levelLabel.getHeight())) {
                // level label was touched
                difficulty++;
                if (difficulty > FlappyPug.HARD)
                    difficulty = FlappyPug.EASY;


                pref.putInteger(DIFFICULTY_SETTING, difficulty);
                pref.flush();
            }

            if (lastTouched.x >= camera.position.x - backLabel.getWidth()/2 &&
                    lastTouched.x <= camera.position.x + backLabel.getWidth()/2 &&
                    lastTouched.y >= camera.position.y - menu.getHeight()/2 + MARGIN &&
                    lastTouched.y <= camera.position.y - menu.getHeight()/2 + MARGIN + backLabel.getHeight()) {
                // back was touched
                gameStateManager.set(new MenuState(gameStateManager));
            }
        }


    }

    private void determineSelect(boolean sound, int level) {
        if (sound)
            checkBox = checkBoxMarked;
        else
            checkBox = checkBoxEmpty;

        switch (level) {
            case FlappyPug.EASY:
                levelSelected = easyLabel;
                break;
            case FlappyPug.MED:
                levelSelected = mediumLabel;
                break;
            case FlappyPug.HARD:
                levelSelected = hardLabel;
                break;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        determineSelect(soundOn, difficulty);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        batch.begin();
        batch.draw(menu, camera.position.x - menu.getWidth()/2, camera.position.y - menu.getHeight()/2);
        batch.draw(backLabel, camera.position.x - backLabel.getWidth()/2, camera.position.y - menu.getHeight()/2 + MARGIN);
        batch.draw(checkBox, camera.position.x - soundRowWidth/2, camera.position.y + MARGIN);
        batch.draw(levelLabel, camera.position.x - soundRowWidth/2, camera.position.y - (MARGIN + levelLabel.getHeight()));
        batch.draw(levelSelected, camera.position.x - soundRowWidth/2 + levelLabel.getWidth(),
                camera.position.y - (MARGIN + levelLabel.getHeight()));


        batch.draw(soundLabel, camera.position.x + soundRowWidth/2 - soundLabel.getWidth(), camera.position.y + MARGIN );


        batch.end();

    }

    @Override
    public void dispose() {
        super.dispose();
        checkBoxEmpty.dispose();
        checkBoxMarked.dispose();
        soundLabel.dispose();
        backLabel.dispose();
        menu.dispose();
        easyLabel.dispose();
        mediumLabel.dispose();
        hardLabel.dispose();
        levelLabel.dispose();
    }
}
