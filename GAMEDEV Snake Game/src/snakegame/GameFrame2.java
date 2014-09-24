package snakegame;

import java.util.ArrayList;

import com.golden.gamedev.GameEngine;

public class GameFrame2 extends GameFrame {

	public GameFrame2(GameEngine parent) {
		super(parent);
	}

	@Override
	public void createWall() {
		walls = new ArrayList<Block>();
		for (int x = MIN; x <= MAX; x++) {
			for (int y = MIN; y <= MAX; y++) {
				if (x == MIN || x == MAX || y == MIN || y == MAX || x >= 11
						&& x <= 29 && y >= 19 && y <= 20 || y >= 11
						&& y <= 29 && x >= 19 && x <= 20 || x >= 4 
						&& x <= 7 && y >= 4 && y <= 7 || x >= 4 
						&& x <= 7 && y >= 32 && y <= 35 || x >= 32 
						&& x <= 35 && y >= 4 && y <= 7 || x >= 32 
						&& x <= 35 && y >= 32 && y <= 35 ) {
					Block tempWall = new Block(getImage(WALLBLOCK),
							normalize((double) x), normalize((double) y));
					walls.add(tempWall);
				}
			}
		}
	}
}