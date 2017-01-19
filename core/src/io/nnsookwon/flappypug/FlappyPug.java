package io.nnsookwon.flappypug;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.nnsookwon.flappypug.states.GameStateManager;
import io.nnsookwon.flappypug.states.MenuState;

public class FlappyPug extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	public static final int EASY = 1;
	public static final int MED = 2;
	public static final int HARD = 3;

	public static final String TITLE = "Flappy Pug";
	private GameStateManager gameStateManager;
	private SpriteBatch batch;

	AdHandler handler;

	public FlappyPug(AdHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gameStateManager = new GameStateManager();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		gameStateManager.push(new MenuState(gameStateManager));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameStateManager.update(Gdx.graphics.getDeltaTime());
		gameStateManager.render(batch);

	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
