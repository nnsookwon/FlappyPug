package io.nnsookwon.flappypug.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import io.nnsookwon.flappypug.Animation;
import io.nnsookwon.flappypug.FlappyPug;

/**
 * Created by nnsoo on 1/1/2017.
 */

public class Pug {
    private static final int GRAVITY = -30;
    private static final int JUMP = 500;
    private static final int JUMP_EASY = 350;
    private static final int MOVEMENT_EASY = 180;
    private static final int MOVEMENT_MED = 200;
    private static final int MOVEMENT_HARD = 400;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation animation;
    private int jumps;
    private int jump;
    private boolean canJump;
    private float minY;
    private int movementSpeed;

    public Pug(int x, int y, float minY, int difficulty) {
        setDifficulty(difficulty);
        position = new Vector3(x, y, 0);
        velocity = new Vector3(movementSpeed, 0, 0);
        animation = new Animation(0.5f);
        bounds = new Rectangle(x, y, animation.getFrame().getWidth(), animation.getFrame().getHeight());
        jumps = 0;
        canJump = false;
        this.minY = minY;
    }

    public void setDifficulty(int difficulty) {
        switch (difficulty) {
            case FlappyPug.EASY:
                movementSpeed = MOVEMENT_EASY;
                jump = JUMP_EASY;
                break;
            case FlappyPug.MED:
                movementSpeed = MOVEMENT_MED;
                jump = JUMP;
                break;
            case FlappyPug.HARD:
                movementSpeed = MOVEMENT_HARD;
                jump = JUMP;
                break;
            default:
                movementSpeed = MOVEMENT_MED;
                jump = JUMP;
        }
    }

    public void update(float dt, boolean alive) {
        animation.update(dt);
        if (!alive)
            velocity.x = 0;
        if (position.y > 0)
            velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);
        if (position.y < minY)
            position.y = minY;

        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public Texture getPugTexture() {
        return animation.getFrame();
    }

    public Vector3 getPosition() {
        return position;
    }

    public void jump(float dt){
        if (canJump && jumps < 1/(dt)/8) {
            velocity.y = JUMP;
            jumps++;
        } else {
            canJump = false;
        }
    }

    public void atRest() {
        jumps = 0;
        canJump = true;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        animation.dispose();
    }
}
