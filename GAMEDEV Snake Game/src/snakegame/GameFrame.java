package snakegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.Timer;
import com.golden.gamedev.object.collision.BasicCollisionGroup;

public abstract class GameFrame extends GameObject {
	protected static final int DIMENSION = 16; // 16x16 sized blocks
	protected static final int MIN = 0;
	protected static final int MAX = 39;
	protected static final String SNAKEBLOCK = "./img/snakeblock.png";
	protected static final String WALLBLOCK = "./img/wallblock.png";
	protected static final String FOODBLOCK = "./img/foodblock.png";

	protected static int speed = 100;
	protected static Timer delay;

	protected static boolean isAlive = true; // flag if snake is alive
	protected static boolean isEat = false; // flag for body increment

	protected static Block snakeHead;
	protected static ArrayList<Block> snakeBody;
	protected static ArrayList<Block> walls;
	protected static Block food;

	protected static int snakeSize = 5;
	protected static double foodX, foodY;
	protected static int snakeDirection; // 1 up, 2 right, 3 down, 4 left

	protected static BasicCollisionGroup collisionDeath;
	protected static BasicCollisionGroup collisionFood;
	protected static SpriteGroup PLAYER;
	protected static SpriteGroup ENEMY;
	protected static SpriteGroup FOOD;

	/* CONSTRUCTOR */
	public GameFrame(GameEngine parent) {
		super(parent);
	}

	/* ABSTRACT METHODS */
	public void initResources() {
		delay = new Timer(speed);

		// create snake
		snakeDirection = 2;
		snakeBody = new ArrayList<Block>();
		for (int i = snakeSize; i > 0; i--) {
			Block snakepart = new Block(getImage(SNAKEBLOCK),
					normalize((double) i + 5), normalize((double) 1 + 5));
			if (i == snakeSize) {
				snakeHead = snakepart;
			} else {
				snakeBody.add(snakepart);
			}
		}

		// create wall
		createWall();

		// create food
		foodX = 20;
		foodY = 20;
		food = new Block(getImage(FOODBLOCK), normalize(foodX),
				normalize(foodY));

		// sprite groups
		PLAYER = new SpriteGroup("The Snake");
		PLAYER.add(snakeHead);

		ENEMY = new SpriteGroup("The Cause of Snake Death");
		for (Block temp : snakeBody) {
			ENEMY.add(temp);
		}
		for (Block wall : walls) {
			ENEMY.add(wall);
		}

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
		// Change Levels
		if (keyPressed(KeyEvent.VK_1)) {
			parent.nextGameID = 0;
			finish();
		} else if (keyPressed(KeyEvent.VK_2)) {
			parent.nextGameID = 2;
			finish();
		} else if (keyPressed(KeyEvent.VK_3)) {
			parent.nextGameID = 3;
			finish();
		} else if (keyPressed(KeyEvent.VK_4)) {
			parent.nextGameID = 4;
			finish();
		}

		readInput();

		if (delay.action(l) && isAlive) {
			moveSnake();
		} else if (!isAlive) {
			parent.nextGameID = 1;
			finish();
		}

		collisionDeath.checkCollision();
		collisionFood.checkCollision();

		if (isEat) {
			incrementBody();
			isEat = false;
		}

		PLAYER.update(l);
		ENEMY.update(l);
		FOOD.update(l);
	}

	/* USER-DEFINED METHODS */
	public abstract void createWall();

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

	public void moveHead() {
		if (snakeDirection == 1)
			snakeHead.setY(snakeHead.getY() - DIMENSION);
		else if (snakeDirection == 2)
			snakeHead.setX(snakeHead.getX() + DIMENSION);
		else if (snakeDirection == 3)
			snakeHead.setY(snakeHead.getY() + DIMENSION);
		else if (snakeDirection == 4)
			snakeHead.setX(snakeHead.getX() - DIMENSION);
	}

	public void moveSnake() {
		double pastX = snakeHead.getX();
		double pastY = snakeHead.getY();

		// move the head
		moveHead();

		// move body
		for (int i = 0; i < snakeBody.size(); i++) {
			double tempX = snakeBody.get(i).getX();
			double tempY = snakeBody.get(i).getY();
			snakeBody.get(i).setX(pastX);
			snakeBody.get(i).setY(pastY);
			pastX = tempX;
			pastY = tempY;
		}
	}

	public void incrementBody() {
		double newX = snakeHead.getX();
		double newY = snakeHead.getY();

		Block newBlock = new Block(getImage(SNAKEBLOCK), newX, newY);
		snakeBody.add(0, newBlock);
		ENEMY.add(newBlock);

		moveHead();
	}
}

class SnakeEat extends BasicCollisionGroup {
	public SnakeEat() {
		pixelPerfectCollision = false;
	}

	@Override
	public void collided(Sprite snake, Sprite food) {
		resetFood(food);
		GameFrame.isEat = true;
	}

	public void resetFood(Sprite food) {
		int max = GameFrame.MAX - 1;
		int min = GameFrame.MIN + 1;
		int range = max - min + 1;

		int newX = ((int) (Math.random() * range) + min);
		int newY = ((int) (Math.random() * range) + min);
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