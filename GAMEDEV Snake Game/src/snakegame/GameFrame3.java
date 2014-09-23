package snakegame;

import java.util.ArrayList;

import com.golden.gamedev.GameEngine;

public class GameFrame3 extends GameFrame {

	public GameFrame3(GameEngine parent) {
		super(parent);
	}

	@Override
	public void createWall() {
		walls = new ArrayList<Block>();
		for (int x = MIN; x <= MAX; x++) {
			for (int y = MIN; y <= MAX; y++) {
				if (x == MIN || x == MAX || y == MIN || y == MAX || x == 9
						&& y >= 11 && y <= 28 || x == 30 && y >= 11 && y <= 28) {
					Block tempWall = new Block(getImage(WALLBLOCK),
							normalize((double) x), normalize((double) y));
					walls.add(tempWall);
				}
			}
		}
	}
}