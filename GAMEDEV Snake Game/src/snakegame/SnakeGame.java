package snakegame;

import java.awt.Dimension;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameLoader;
import com.golden.gamedev.GameObject;

public class SnakeGame extends GameEngine {
	@Override
	public GameObject getGame(int gameID) {
		switch (gameID) {
		case 0: // main menu
			return new GameFrame(this);
		case 1: // main game
			return new GameOverFrame(this);
		case 2: // game over
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
