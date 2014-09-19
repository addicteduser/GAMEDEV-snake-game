package snakegame;
import java.awt.Dimension;

import com.golden.gamedev.GameLoader;


public class SnakeGame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GameLoader game = new GameLoader();
		game.setup(new GameFrame(), new Dimension(640, 640), false);
		game.start();
	}

}
