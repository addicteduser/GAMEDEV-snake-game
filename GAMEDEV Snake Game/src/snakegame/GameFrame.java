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
	protected static final String SNAKEBLOCK = "./resources/img/snakeblock.png";
	protected static final String WALLBLOCK = "./resources/img/wallblock.png";
	protected static final String FOODBLOCK = "./resources/img/foodblock.png";

	protected int speed; // delay of snake movement in milliseconds
	private Timer delay;

	protected static boolean isAlive; // flag if snake is alive
	protected static boolean isEat; // flag for body increment

	protected ArrayList<Block> walls;
	private ArrayList<Block> snakeBody;
	private Block snakeHead;	
	private Block food;

	private int foodX, foodY;
	private static int snakeSize = 5;
	private static int snakeDirection; // 1 up, 2 right, 3 down, 4 left

	private BasicCollisionGroup collisionDeath;
	private BasicCollisionGroup collisionFood;
	private SpriteGroup PLAYER;
	private SpriteGroup ENEMY;
	private SpriteGroup FOOD;

	/* CONSTRUCTOR */
	public GameFrame(GameEngine parent) {
		super(parent);
	}

	/* ABSTRACT METHODS */
	public void initResources() {
		System.out.println("IM CALLED");
		isAlive = true;
		isEat = false;
		speed = SnakeGame.speed;
		System.out.println("SPEED: "+speed);
		delay = new Timer(speed);

		createWall(); // create wall
		createSnake(); // create snake
		createFood(); // create food
		createSpriteGroups(); // sprite groups
		initCollisions(); // initialize collisions
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

		if (delay.action(l) && isAlive) {
			moveSnake();
		} else if (!isAlive) {
			parent.nextGameID = 5;
			finish();
		}

		collisionDeath.checkCollision();
		collisionFood.checkCollision();

		if (isEat) {
			resetFood();
			incrementBody();
			isEat = false;
		}

		PLAYER.update(l);
		ENEMY.update(l);
		FOOD.update(l);
	}

	/* USER-DEFINED METHODS */
	public abstract void createWall();

	public void createSnake() {
		snakeDirection = 2;
		snakeBody = new ArrayList<Block>();
		for (int i = snakeSize; i > 0; i--) {
			Block snakepart = new Block(getImage(SNAKEBLOCK),
					normalize((double) i + 5), normalize((double) 2));
			if (i == snakeSize) {
				snakeHead = snakepart;
			} else {
				snakeBody.add(snakepart);
			}
		}
	}

	public void createFood() {
		foodX = foodY = 0;
		randomizeFoodXY();
		food = new Block(getImage(FOODBLOCK), normalize(foodX),
				normalize(foodY));
	}

	public void createSpriteGroups() {
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
	}

	public void initCollisions() {
		collisionDeath = new SnakeDeath();
		collisionDeath.setCollisionGroup(PLAYER, ENEMY);
		collisionFood = new SnakeEat();
		collisionFood.setCollisionGroup(PLAYER, FOOD);
	}

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

	public boolean isOccupiedByWall(double x, double y) {
		for (Block wall : walls) {
			if (wall.getX() == x && wall.getY() == y)
				return true;
		}
		return false;
	}
	
	public void randomizeFoodXY() {
		int max = GameFrame.MAX - 1;
		int min = GameFrame.MIN + 1;
		int range = max - min + 1;
		int newX = 0;
		int newY = 0;

		while (isOccupiedByWall(normalize(newX), normalize(newY))) {
			newX = ((int) (Math.random() * range) + min);
			newY = ((int) (Math.random() * range) + min);
		}
		
		foodX = newX;
		foodY = newY;
	}
	public void resetFood() {
		randomizeFoodXY();
		food.setX(GameFrame.normalize((double) foodX));
		food.setY(GameFrame.normalize((double) foodY));;
	}
}

class SnakeEat extends BasicCollisionGroup {
	public SnakeEat() {
		pixelPerfectCollision = false;
	}

	@Override
	public void collided(Sprite snake, Sprite food) {
		GameFrame.isEat = true;
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