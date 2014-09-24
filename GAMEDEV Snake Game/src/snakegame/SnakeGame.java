package snakegame;

import java.awt.Dimension;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameLoader;
import com.golden.gamedev.GameObject;

public class SnakeGame extends GameEngine {
	public static int speed = 200; // delay of snake movement in milliseconds
	
	@Override
	public GameObject getGame(int gameID) {
		switch (gameID) {
		case 0: // Intro Menu
			return new IntroMenu(this);
		case 1: // Level 1
			return new GameFrame1(this);
		case 2: // Level 2
			return new GameFrame2(this);
		case 3: // Level 3
			return new GameFrame3(this);
		case 4: // Level 4
			return new GameFrame4(this);
		case 5: // Game Over
			return new GameOverFrame(this);
		}
		return null;
	}

	public static void main(String[] args) {
		GameLoader game = new GameLoader();
		game.setup(new SnakeGame(), new Dimension(640, 640), false);
		game.start();
	}
}
