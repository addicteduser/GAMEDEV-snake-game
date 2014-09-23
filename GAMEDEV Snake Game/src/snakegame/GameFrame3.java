/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SnakeG;


import static SnakeG.GameFrame2.normalize;
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


public class GameFrame3 extends GameObject {
	protected final static int DIMENSION = 16; // 16x16 sized blocks
	protected static final int MIN = 0;
	protected static final int MAX = 39;
	private final static int speed = 100;
	private static  Timer delay;

	protected static boolean isAlive = true; // flag for checking if snake is alive
	protected static boolean isEat = false; // flag for body increment

	private static Block snakeHead;
	private static ArrayList<Block> snakeBody;
	private static ArrayList<Block> walls;
	private static Block food;

	private static int snakeSize = 5;
	private double foodX, foodY;
	private static int snakeDirection; // 1 up, 2 right, 3 down, 4 left

	private BasicCollisionGroup collisionDeath;
	private BasicCollisionGroup collisionFood;
	private SpriteGroup PLAYER;
	private SpriteGroup ENEMY;
	private SpriteGroup FOOD;

	/* CONSTRUCTOR */
	public GameFrame3(GameEngine parent) {
		super(parent);
	}

	/* ABSTRACT METHODS */
	@Override
	public void initResources() {
		delay = new Timer (speed);

		// create snake
		snakeDirection = 2;
		snakeBody = new ArrayList<Block>();
		for (int i = snakeSize; i > 0; i--) {
			Block snakepart = new Block(getImage("SnakeBlock.png"),
					normalize((double) i+5), normalize((double) 1+5));
			if (i == snakeSize) {
				snakeHead = snakepart;
			} else {				
				snakeBody.add(snakepart);
			}
		}		

		// create wall
		walls = new ArrayList<Block>();
		for (int x = MIN; x <= MAX; x++) {
			for (int y = MIN; y <= MAX; y++) {
				if (x == MIN || x == MAX || y == MIN || y == MAX || x == 9 && y>=11 && y<=28 || x == 30 && y>=11 && y<=28) {
					Block tempWall = new Block(getImage("WallBlock.png"),
							normalize((double) x), normalize((double) y));
					walls.add(tempWall);
				}
			}
		}

		// create food
		foodX = 20;
		foodY = 20;
		food = new Block(getImage("FoodBlock.png"), normalize(foodX),
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
		collisionDeath = new SnakeDeath3();
		collisionDeath.setCollisionGroup(PLAYER, ENEMY);
		collisionFood = new SnakeEat3();
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
                
                // Change Levels
                if (keyPressed(KeyEvent.VK_1) )
                {
                    parent.nextGameID = 0;
			finish();
                }
                else if (keyPressed(KeyEvent.VK_2) )
                {
                    parent.nextGameID = 2;
			finish();
                }
                else if (keyPressed(KeyEvent.VK_3) )
                {
                    parent.nextGameID = 3;
			finish();
                }
                else if (keyPressed(KeyEvent.VK_4) )
                {
                    parent.nextGameID = 4;
			finish();
                }
                //
                
		if (delay.action(l) && isAlive) {
			moveSnake();
		} else if (!isAlive){
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

		Block newBlock = new Block(getImage("SnakeBlock.png"), newX, newY);
		snakeBody.add(0, newBlock);
		ENEMY.add(newBlock);

		moveHead();
	}
}
class SnakeEat3 extends BasicCollisionGroup {
	public SnakeEat3() {
		pixelPerfectCollision = false;
	}

	@Override
	public void collided(Sprite snake, Sprite food) {
		resetFood(food);
		GameFrame3.isEat = true;
	}

	public void resetFood(Sprite food) {
		int max = GameFrame3.MAX-1;
		int min = GameFrame3.MIN+1;
		int range = max-min+1;

		int newX = ((int)(Math.random() * range) + min);
		int newY = ((int)(Math.random() * range) + min);
		food.setX(GameFrame3.normalize((double) newX));
		food.setY(GameFrame3.normalize((double) newY));
	}
}

class SnakeDeath3 extends BasicCollisionGroup {
	public SnakeDeath3() {
		pixelPerfectCollision = false;
	}

	@Override
	public void collided(Sprite snake, Sprite causeOfDeath) {
		GameFrame3.isAlive = false;
	}
}

