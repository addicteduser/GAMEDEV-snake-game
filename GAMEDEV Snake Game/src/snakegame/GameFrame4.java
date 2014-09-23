package snakegame;

import java.util.ArrayList;

import com.golden.gamedev.GameEngine;

public class GameFrame4 extends GameFrame {

	public GameFrame4(GameEngine parent) {
		super(parent);
	}

	@Override
	public void createWall() {
		walls = new ArrayList<Block>();
		for (int x = MIN; x <= MAX; x++) {
			for (int y = MIN; y <= MAX; y++) {
				if (x == MIN || x == MAX || y == MIN || y == MAX || x == 9
						&& y >= 0 && y <= 9 || x >= 30 && x <= 38 && y == 9
						|| x >= 0 && x <= 9 && y == 30 || x == 30 && y >= 30
						&& y <= 38) {
					Block tempWall = new Block(getImage(WALLBLOCK),
							normalize((double) x), normalize((double) y));
					walls.add(tempWall);
				}
			}
		}
	}
}