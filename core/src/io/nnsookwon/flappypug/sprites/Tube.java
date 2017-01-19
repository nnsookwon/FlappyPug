package io.nnsookwon.flappypug.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import io.nnsookwon.flappypug.FlappyPug;

/**
 * Created by nnsoo on 1/1/2017.
 */

public class Tube {

    public static final int TUBE_WIDTH = 99;

    private static final int FLUCTUATION = 300;

    private static final int TUBE_GAP_EASY = 260;
    private static final int TUBE_GAP_MED = 225;
    private static final int TUBE_GAP_HARD = 195;

    private static final int LOWEST_OPENING = 130;
    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBottomTube;
    private Random rand;
    private Rectangle boundsTop, boundsBottom;
    private boolean playerPassed;
    private int gap;

    public Tube(float x, int difficulty) {
        setDifficulty(difficulty);
        topTube = new Texture("frenchfrytop.png");
        bottomTube = new Texture("frenchfrybottom.png");
        rand = new Random();
        playerPassed = false;
        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + gap + LOWEST_OPENING);
        posBottomTube = new Vector2(x, posTopTube.y - gap - bottomTube.getHeight());

        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
        boundsBottom = new Rectangle(posBottomTube.x, posBottomTube.y, bottomTube.getWidth(), bottomTube.getHeight());
    }

    public void setDifficulty(int difficulty) {
        switch (difficulty) {
            case FlappyPug.EASY:
                gap = TUBE_GAP_EASY;
                break;
            case FlappyPug.MED:
                gap = TUBE_GAP_MED;
                break;
            case FlappyPug.HARD:
                gap = TUBE_GAP_HARD;
                break;
            default:
                gap = TUBE_GAP_MED;
        }
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBottomTube() {
        return posBottomTube;
    }

    public void reposition(float x) {
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + gap + LOWEST_OPENING);
        posBottomTube.set(x, posTopTube.y - gap - bottomTube.getHeight());
        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBottom.setPosition(posBottomTube.x, posBottomTube.y);
        playerPassed = false;
    }

    public boolean collides (Rectangle player) {
        return player.overlaps(boundsTop) || player.overlaps(boundsBottom);
    }

    public boolean playerJustPassed(Rectangle player) {
        if (posTopTube.x <= player.x &&
                posTopTube.x + topTube.getWidth() >=  player.x &&
                !playerPassed) {
            playerPassed = true;
            return true;
        }
        return false;
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();
    }
}
