package snakegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.golden.gamedev.Game;
import com.golden.gamedev.object.CollisionManager;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.collision.BasicCollisionGroup;
import com.golden.gamedev.object.collision.CollisionShape;

public class GameFrame extends Game {
	private final static int DIMENSION = 16; // 16x16 sized blocks
	private final int MIN = 0;
	private final int MAX = 39;
	private final double speed = 0.1;
	protected static boolean isAlive = true; // flag for checking if snake is
												// alive
	protected static int snakeSize = 2;

	// private Block player;
	private ArrayList<Block> snake;
	private ArrayList<Block> walls;
	private Block food;

	// private double playerX, playerY;
	private ArrayList<Double> snakeX, snakeY;
	private double foodX, foodY;
	private int snakeDirection; // 1 up, 2 right, 3 down, 4 left

	private BasicCollisionGroup collisionDeath;
	private BasicCollisionGroup collisionFood;
	private SpriteGroup PLAYER;
	private SpriteGroup ENEMY;
	private SpriteGroup FOOD;

	@Override
	public void initResources() {
		// create snake
		/*
		 * playerX = 5; playerY = 5; snakeDirection = 2; player = new
		 * Block(getImage("./img/snakeblock.png"), normalize(playerX),
		 * normalize(playerY)); PLAYER = new SpriteGroup("The Snake");
		 * PLAYER.add(player);
		 */

		snakeDirection = 2;
		snake = new ArrayList<Block>();
		for (int i = 1; i <= snakeSize; i++) {
			Block snakepart;
			snakepart = new Block(getImage("./img/snakeblock.png"),
					normalize((double) i), normalize((double) 1));
			snake.add(snakepart);
		}
		PLAYER = new SpriteGroup("The Snake");
		for (Block temp : snake) {
			PLAYER.add(temp);
		}

		// create wall
		walls = new ArrayList<Block>();
		for (int x = MIN; x <= MAX; x++) {
			for (int y = MIN; y <= MAX; y++) {
				if (x == MIN || x == MAX || y == MIN || y == MAX) {
					Block tempWall = new Block(getImage("img/wallblock.png"),
							normalize((double) x), normalize((double) y));
					walls.add(tempWall);
				}
			}
		}
		ENEMY = new SpriteGroup("The Wall");
		for (Block wall : walls) {
			ENEMY.add(wall);
		}

		// create food
		foodX = 20;
		foodY = 20;
		food = new Block(getImage("./img/foodblock.png"), normalize(foodX),
				normalize(foodY));
		FOOD = new SpriteGroup("The Food");
		FOOD.add(food);

		// initialize collisions
		collisionDeath = new SnakeDeath();
		collisionDeath.setCollisionGroup(PLAYER, ENEMY);
		collisionFood = new SnakeEat();
		collisionFood.setCollisionGroup(PLAYER, FOOD);
	}

	@Override
	public void render(Graphics2D gd) {
		gd.setColor(Color.GRAY);
		gd.fillRect(0, 0, getWidth(), getHeight());

		PLAYER.render(gd);
		ENEMY.render(gd);
		FOOD.render(gd);
	}

	@Override
	public void update(long l) {
		readInput();

		if (isAlive)
			moveSnake();

		PLAYER.update(l);
		FOOD.update(l);

		collisionDeath.checkCollision();
		collisionFood.checkCollision();
	}

	/* USER DEFINED FUNCTIONS */
	public static double normalize(double val) {
		return val * DIMENSION;
	}

	public void readInput() {
		if (keyPressed(KeyEvent.VK_UP) && snakeDirection != 3)
			snakeDirection = 1;
		if (keyPressed(KeyEvent.VK_RIGHT) && snakeDirection != 4)
			snakeDirection = 2;
		if (keyPressed(KeyEvent.VK_DOWN) && snakeDirection != 1)
			snakeDirection = 3;
		if (keyPressed(KeyEvent.VK_LEFT) && snakeDirection != 2)
			snakeDirection = 4;
	}

	public void moveSnake() {
		/*
		 * for (Block snakepart : snake) { double currX = snakepart.getX();
		 * double currY = snakepart.getY(); double newX = currX, newY = currY;
		 * 
		 * if (snakeDirection == 1) newY = currY - speed; else if
		 * (snakeDirection == 2) newX = currX + speed; else if (snakeDirection
		 * == 3) newY = currY + speed; else if (snakeDirection == 4) newX =
		 * currX - speed;
		 * 
		 * snakepart.setX(normalize(newX)); snakepart.setY(normalize(newY)); }
		 */

		for (Block snakepart : snake) {
			if (snakeDirection == 2) {
				snakepart.setX(snakepart.getX() + speed);
			}
		}
	}
}

class SnakeEat extends BasicCollisionGroup {
	public SnakeEat() {
		pixelPerfectCollision = false;
	}

	@Override
	public void collided(Sprite snake, Sprite food) {
		int newX, newY;
		newX = ((int) ((Math.random() * 100) % 40));
		newY = ((int) ((Math.random() * 100) % 40));
		food.setX(GameFrame.normalize((double) newX));
		food.setY(GameFrame.normalize((double) newY));
	}

}

class SnakeDeath extends BasicCollisionGroup {
	public SnakeDeath() {
		pixelPerfectCollision = false;
	}

	@Override
	public void collided(Sprite snake, Sprite causeOfDeath) {
		GameFrame.isAlive = false;
	}
}
