package io.nnsookwon.flappypug;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * Created by nnsoo on 1/2/2017.
 */

public class Animation {
    private Array<Texture> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;
    private int frame;

    public Animation(float cycleTime) {
        frames = new Array<Texture>();
        frames.add(new Texture("pug_down.png"));
        frames.add(new Texture("pug_up.png"));
        frameCount = frames.size;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
    }

    public void update(float dt) {
        currentFrameTime += dt;
        if (currentFrameTime > maxFrameTime) {
            frame++;
            currentFrameTime = 0;
        }
        if (frame >= frameCount)
            frame = 0;
    }

    public Texture getFrame() {
        return frames.get(frame);
    }

    public void dispose() {
        frames.clear();
    }
}
