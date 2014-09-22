package snakegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.golden.gamedev.Game;
import com.golden.gamedev.object.Sprite;
import com.golden.gamedev.object.SpriteGroup;
import com.golden.gamedev.object.Timer;
import com.golden.gamedev.object.collision.BasicCollisionGroup;

public class GameFrame extends Game {
	protected final static int DIMENSION = 16; // 16x16 sized blocks
	protected static final int MIN = 0;
	protected static final int MAX = 39;
	private static  Timer delay;
	private final static int speed = 100;
	protected static boolean isAlive = true; // flag for checking if snake is alive
	protected static boolean isStart = false;
	protected static boolean isEat = false;
	protected static int snakeSize = 5;

	protected static Block snakeHead;
	protected static ArrayList<Block> snakeBody;
	private ArrayList<Block> walls;
	private Block food;
	private Block gameover;

	private double foodX, foodY;
	private double gameoverX, gameoverY;
	protected static int snakeDirection; // 1 up, 2 right, 3 down, 4 left

	private BasicCollisionGroup collisionDeath;
	private BasicCollisionGroup collisionFood;
	private SpriteGroup PLAYER;
	private SpriteGroup ENEMY;
	private SpriteGroup FOOD;

	@Override
	public void initResources() {
		delay = new Timer (speed);

		// create snake
		snakeDirection = 2;
		snakeBody = new ArrayList<Block>();
		for (int i = snakeSize; i > 0; i--) {
			Block snakepart = new Block(getImage("./img/snakeblock.png"),
					normalize((double) i+5), normalize((double) 1+5));
			if (i == snakeSize) {
				snakeHead = snakepart;
			} else {				
				snakeBody.add(snakepart);
			}
		}
		PLAYER = new SpriteGroup("The Snake");
		PLAYER.add(snakeHead);
		ENEMY = new SpriteGroup("The Cause of Snake Death");
		for (Block temp : snakeBody) {
			ENEMY.add(temp);
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

		// create game over
		gameoverX = 12;
		gameoverY = 20;
		gameover = new Block(getImage("./img/gameover.png"), normalize(gameoverX),
				normalize(gameoverY));

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
		//gameover.render(gd);
		//gameover.setActive(false);
	}

	@Override
	public void update(long l) {
		readInput();

		if (delay.action(l) && isAlive) {
			moveSnake();
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
		//gameover.update(l);
	}

	/* USER DEFINED FUNCTIONS */
	public static double normalize(double val) {
		return val * DIMENSION;
	}

	public void readInput() {
		if (keyPressed(KeyEvent.VK_UP) && snakeDirection != 3) {
			snakeDirection = 1;
			//moveSnake();
		}
		if (keyPressed(KeyEvent.VK_RIGHT) && snakeDirection != 4) {
			snakeDirection = 2;
			//moveSnake();
		}
		if (keyPressed(KeyEvent.VK_DOWN) && snakeDirection != 1){
			snakeDirection = 3;
			//moveSnake();
		}
		if (keyPressed(KeyEvent.VK_LEFT) && snakeDirection != 2){
			snakeDirection = 4;
			//moveSnake();
		}

		isStart = true;
	}
	
	public void moveHead() {
		if(snakeDirection == 1)
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
		
		Block newBlock = new Block(getImage("./img/snakeblock.png"), newX, newY);
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
		int max = GameFrame.MAX-1;
		int min = GameFrame.MIN+1;
		int range = max-min+1;

		int newX = ((int)(Math.random() * range) + min);
		int newY = ((int)(Math.random() * range) + min);
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
