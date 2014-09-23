package SnakeG;

import java.awt.Dimension;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameLoader;
import com.golden.gamedev.GameObject;

public class SnakeGame extends GameEngine {
	@Override
	public GameObject getGame(int gameID) {
		switch (gameID) {
		case 0: // Level 1
			return new GameFrame1(this);
		case 1: // Game Over
			return new GameOverFrame(this);
                case 2: // Level 2
                        return new GameFrame2(this);
                case 3: // Level 3
                        return new GameFrame3(this);
                case 4: // Level 4
			return new GameFrame4(this);
		case 5: // game over
			break;
		}
		return null;
	}
	
	public static void main(String[] args) {
		GameLoader game = new GameLoader();
		game.setup(new SnakeGame(), new Dimension(640, 640), false);
		game.start();
	}
}
