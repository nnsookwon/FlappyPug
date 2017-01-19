package io.nnsookwon.flappypug.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import io.nnsookwon.flappypug.FlappyPug;
import io.nnsookwon.flappypug.sprites.Pug;
import io.nnsookwon.flappypug.sprites.Tube;

/**
 * Created by nnsoo on 1/1/2017.
 */

public class PlayState extends BaseState {
    public static final String EASY_BEST_SCORE = "EASY_BEST_SCORE";
    public static final String MED_BEST_SCORE = "MED_BEST_SCORE";
    public static final String HARD_BEST_SCORE = "HARD_BEST_SCORE";
    public static final String BEST_SCORE = "BEST_SCORE"; //equivalent to medium
    private static final int TUBE_SPACING = 370;
    private static final int TUBE_COUNT = 4;
    private static final long INITIAL_PAUSE_MILLI = 2000;

    private Pug pug;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Sound fart;
    private Sound crash;
    private Music music;

    private int score;
    private String scoreString;
    private BitmapFont scoreFont;
    private BitmapFont bestScoreFont;
    private GlyphLayout layout;

    private Array<Tube> tubes;
    private Array<Texture> numbers;

    private boolean playerAlive;
    private int bestScore;
    private String maxScoreSring;
    private int countDown;

    private long startTimeMilli;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        countDown = 3;
        ground = new Texture("burger_floor.png");
        numbers = new Array<Texture>();
        numbers.add(new Texture("1.png"));
        numbers.add(new Texture("2.png"));
        numbers.add(new Texture("3.png"));
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth/2, -10);
        groundPos2 = new Vector2(camera.position.x - camera.viewportWidth/2 + ground.getWidth(),-10);

        pug = new Pug(160, 400, ground.getHeight()-20, difficulty);
        camera.setToOrtho(false, FlappyPug.WIDTH, FlappyPug.HEIGHT);
        tubes = new Array<Tube>();
        for(int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i* (TUBE_SPACING + Tube.TUBE_WIDTH), difficulty));
        }

        setBestScore(difficulty);

        score = 0;
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.WHITE);
        scoreFont.getData().scale(1.8f);
        scoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bestScoreFont = new BitmapFont();
        bestScoreFont.setColor(Color.BLACK);
        bestScoreFont.getData().scale(1.4f);
        bestScoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        maxScoreSring = "Best:\n" + bestScore;
        scoreString = "Score: " + score;
        layout = new GlyphLayout(scoreFont, scoreString);

        fart = Gdx.audio.newSound(Gdx.files.internal("fart.ogg"));
        crash = Gdx.audio.newSound(Gdx.files.internal("thud.ogg"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(0.8f);

        playerAlive = true;
        startTimeMilli = TimeUtils.millis();
    }

    private void handleBestScore(int level) {
        switch (level) {
            case FlappyPug.EASY:
                pref.putInteger(EASY_BEST_SCORE, bestScore);
                break;
            case FlappyPug.MED:
                pref.putInteger(MED_BEST_SCORE, bestScore);
                break;
            case FlappyPug.HARD:
                pref.putInteger(HARD_BEST_SCORE, bestScore);
                break;
        }
        pref.flush();
    }

    private void setBestScore(int level) {
        switch (level) {
            case FlappyPug.EASY:
                bestScore = pref.getInteger(EASY_BEST_SCORE, 0);
                break;
            case FlappyPug.MED:
                bestScore = pref.getInteger(MED_BEST_SCORE, pref.getInteger(BEST_SCORE, 0));
                break;
            case FlappyPug.HARD:
                bestScore = pref.getInteger(HARD_BEST_SCORE, 0);
                break;
        }
    }

    @Override
    public void handleInput() {

    }


    public void handleInput(float dt) {
        if (playerAlive) {
            if (soundOn && Gdx.input.justTouched())
                fart.play(0.4f);
            if (Gdx.input.isTouched())
                pug.jump(dt);
            else
                pug.atRest();
        }
    }

    @Override
    public void update(float dt) {

        if (countDown > 0) {
            countDown = 3 - (int) (TimeUtils.timeSinceMillis(startTimeMilli) / 1000);
            if (countDown > 0)
                return;
            else {
                if (soundOn)
                    music.play();
            }
        }

        handleInput(dt);

        pug.update(dt, playerAlive);
        upgradeGround();
        camera.position.x = pug.getPosition().x + 80;
        for (Tube tube : tubes) {
            if (camera.position.x - camera.viewportWidth / 2 >
                    tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + TUBE_COUNT * (Tube.TUBE_WIDTH + TUBE_SPACING));
            }

            if (playerAlive && ( tube.collides(pug.getBounds()) ||
                    pug.getBounds().y <= ground.getHeight())) {
                playerAlive = false;
                if (score > bestScore) {
                    bestScore = score;
                    handleBestScore(difficulty);
                }
                if (soundOn)
                    crash.play(0.7f);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        gameStateManager.set(new MenuState(gameStateManager));
                    }
                }, 2f);
                return;

            }

            if (tube.playerJustPassed(pug.getBounds())) {
                scoreString = "Score: " + String.valueOf(++score);
                layout.setText(scoreFont, scoreString);
            }
        }

//        gro
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        batch.begin();

        batch.draw(ground, groundPos1.x, groundPos1.y);
        batch.draw(ground, groundPos2.x, groundPos2.y);

        if (countDown > 0)
            batch.draw(numbers.get(countDown-1),
                    camera.position.x - numbers.get(countDown-1).getWidth()/2,
                    FlappyPug.HEIGHT - 250);

        for (Tube tube: tubes) {
            batch.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            batch.draw(tube.getBottomTube(), tube.getPosBottomTube().x, tube.getPosBottomTube().y);
        }
        batch.draw(pug.getPugTexture(), pug.getPosition().x, pug.getPosition().y, pug.getBounds().width, pug.getBounds().height);

        bestScoreFont.draw(batch, maxScoreSring,
                camera.position.x - camera.viewportWidth/2 + 25, FlappyPug.HEIGHT - 90);
        scoreFont.draw(batch, scoreString,
                camera.position.x - layout.width/2, FlappyPug.HEIGHT - 90);


        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        pug.dispose();
        for(Tube tube: tubes){
            tube.dispose();
        }
        for (Texture texture: numbers) {
            texture.dispose();
        }
        music.dispose();
        fart.dispose();
        crash.dispose();
        ground.dispose();
    }

    private void upgradeGround() {
        if (camera.position.x - camera.viewportWidth/2 > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth()*2, 0);
        if (camera.position.x - camera.viewportWidth/2 > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth()*2, 0);
    }
}
