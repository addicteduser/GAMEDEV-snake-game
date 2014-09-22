package snakegame;

import java.awt.Graphics2D;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;

public class GameOverFrame extends GameObject {
	protected final static int DIMENSION = 16;
	private static Block gameover;
	private double gameoverX, gameoverY;
	
	/* CONSTRUCTOR */
	public GameOverFrame(GameEngine parent) {
		super(parent);
	}

	/* ABSTRACT METHODS */
	@Override
	public void initResources() {
		gameoverX = 1;
		gameoverY = 1;
		gameover = new Block(getImage("./img/gameover.png"), normalize(gameoverX), normalize(gameoverY));

	}

	@Override
	public void render(Graphics2D gd) {
		gameover.render(gd);

	}

	@Override
	public void update(long l) {
		gameover.update(l);
	}

	/* USER-DEFINED METHODS */
	public static double normalize(double val) {
		return val * DIMENSION;
	}
}
