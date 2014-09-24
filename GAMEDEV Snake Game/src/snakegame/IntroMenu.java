package snakegame;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;

public class IntroMenu extends GameObject { // change Game to GameObject
	private static final String BACKGROUND = "./resources/img/MainBG.png";
	private static final String LEVELS1 = "./resources/img/levels1.png";
	private static final String LEVELS2 = "./resources/img/levels2.png";
	private static final String LEVELS3 = "./resources/img/levels3.png";
	private static final String LEVELS4 = "./resources/img/levels4.png";
	private static final String SPEED1 = "./resources/img/speed1.png";
	private static final String SPEED2 = "./resources/img/speed2.png";
	private static final String SPEED3 = "./resources/img/speed3.png";

	private Block background;
	private Block levels;
	private Block speed;

	private int levelPressed = 1;
	private int speedPressed = 1;
	private boolean play = false;

	/* CONSTRUCTOR */
	public IntroMenu(GameEngine parent) {
		super(parent);
	}

	/* ABSTRACT METHODS */
	@Override
	public void initResources() {
		background = new Block(getImage(BACKGROUND), 0, 0);
		levels = new Block(getImage(LEVELS1), 0, 0);
		speed = new Block(getImage(SPEED1), 0, 0);
	}

	@Override
	public void render(Graphics2D gd) {
		background.render(gd);
		levels.render(gd);
		speed.render(gd);
	}

	@Override
	public void update(long l) {
		background.update(l);
		ReadInput();
		changeImage();

		if (play == true) {
			switch(speedPressed) {
			case 1:
				SnakeGame.speed = 250;
				break;
			case 2:
				SnakeGame.speed = 100;
				break;
			case 3:
				SnakeGame.speed = 50;
				break;
			}
			
			switch(levelPressed) {
			case 1:
				parent.nextGameID = 1;
				break;
			case 2:
				parent.nextGameID = 2;
				break;
			case 3:
				parent.nextGameID = 3;
				break;
			case 4:
				parent.nextGameID = 4;
				break;
			}
			System.out.println("LEVEL: "+levelPressed);
			finish();
		}
	}

	public void ReadInput() {
		if (keyPressed(KeyEvent.VK_UP)) {
			play = false;
			if (levelPressed == 1) {
				levelPressed = 1;
			} else if (levelPressed == 2) {
				levelPressed = 1;
			} else if (levelPressed == 3) {
				levelPressed = 2;
			} else {
				levelPressed = 3;
			}
		} else if (keyPressed(KeyEvent.VK_DOWN)) {
			play = false;
			if (levelPressed == 1) {
				levelPressed = 2;
			} else if (levelPressed == 2) {
				levelPressed = 3;
			} else if (levelPressed == 3) {
				levelPressed = 4;
			} else {
				levelPressed = 4;
			}
		} else if (keyPressed(KeyEvent.VK_RIGHT)) {
			play = false;
			if (speedPressed == 1) {
				speedPressed = 2;
			} else if (speedPressed == 2) {
				speedPressed = 3;
			} else {
				speedPressed = 3;
			}
		} else if (keyPressed(KeyEvent.VK_LEFT)) {
			play = false;
			if (speedPressed == 1) {
				speedPressed = 1;
			} else if (speedPressed == 2) {
				speedPressed = 1;
			} else {
				speedPressed = 2;
			}
		} else if (keyPressed(KeyEvent.VK_ENTER)) {
			play = true;
		}
	}

	public void changeImage() {
		if (levelPressed == 1) {
			levels.setImage(getImage(LEVELS1));
			// levels = new Block(getImage(LEVELS1),0,0);
		} else if (levelPressed == 2) {
			levels.setImage(getImage(LEVELS2));
			// levels = new Block(getImage("levels2.png"),0,0);
		} else if (levelPressed == 3) {
			levels.setImage(getImage(LEVELS3));
			// levels = new Block(getImage("levels3.png"),0,0);
		} else {
			levels.setImage(getImage(LEVELS4));
			// levels = new Block(getImage("levels4.png"),0,0);
		}

		if (speedPressed == 1) {
			speed.setImage(getImage(SPEED1));
			// speed = new Block(getImage("speed.png"),0,0);
		} else if (speedPressed == 2) {
			speed.setImage(getImage(SPEED2));
			// speed = new Block(getImage("speed2.png"),0,0);
		} else {
			speed.setImage(getImage(SPEED3));
			// speed = new Block(getImage("speed3.png"),0,0);
		}
	}

}
