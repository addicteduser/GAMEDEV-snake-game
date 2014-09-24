package snakegame;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;

public class GameOverFrame extends GameObject {
	private static final String GAMEOVER = "./resources/img/gameover.png";
	private static Block gameover;
	
	/* CONSTRUCTOR */
	public GameOverFrame(GameEngine parent) {
		super(parent);
	}

	/* ABSTRACT METHODS */
	@Override
	public void initResources() {
		gameover = new Block(getImage(GAMEOVER), 0, 0);
	}

	@Override
	public void render(Graphics2D gd) {
		gameover.render(gd);

	}

	@Override
	public void update(long l) {
		gameover.update(l);
		if (keyPressed(KeyEvent.VK_ENTER)) {
			System.out.println("END");
			parent.nextGameID = 0;
			finish();
		}
	}
}
