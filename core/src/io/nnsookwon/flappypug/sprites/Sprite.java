package io.nnsookwon.flappypug.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by nnsoo on 1/1/2017.
 */

public class Sprite {

    private Texture image;
    private Vector2 position;
    private Rectangle bounds;

    public Sprite(String imagePath, float x, float y) {
        image = new Texture(imagePath);
        position = new Vector2(x, y);
        bounds = new Rectangle(position.x, position.y, image.getWidth(), image.getWidth());
    }

    public Texture getImage() {
        return image;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(position.x, position.y);
    }


    public boolean collidesWith (Sprite other) {
        return bounds.overlaps(other.getBounds());
    }
}
