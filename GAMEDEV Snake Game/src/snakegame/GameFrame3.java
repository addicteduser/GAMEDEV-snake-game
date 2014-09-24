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
				if (x == MIN || x == MAX || y == MIN || y == MAX ||
						y == 9 && x >= 0 && x <= 9 ||
						y >= 0 && y <= 9 && x == 30 || y >= 30 && y <= 38 && x == 9 ||
						x >= 30 && x <= 38 && y == 30 ||
						x >= 12 && y >= 12 && x <= 27 && y <= 27 && x == y ||
						x == 12 && y == 27 || x == 13 && y == 26 || x == 14 && y == 25
						|| x == 15 && y == 24 || x == 16 && y == 23 || x == 17 && y == 22
						|| x == 18 && y == 21 || x == 19 && y == 20 || x == 20 && y == 19
						|| x == 21 && y == 18 || x == 22 && y == 17 || x == 23 && y == 16
						|| x == 24 && y == 15 || x == 25 && y == 14 || x == 26 && y == 13
						|| x == 27 && y == 12) {
					Block tempWall = new Block(getImage(WALLBLOCK),
							normalize((double) x), normalize((double) y));
					walls.add(tempWall);
				}
			}
		}
	}
}