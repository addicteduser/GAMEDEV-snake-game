package snakegame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.golden.gamedev.Game;

public class GameFrame extends Game {
	private final int DIMENSION = 16; // 16x16 sized blocks
	private final int MIN = 0;
	private final int MAX = 39;
	private final double speed = 0.1;

	private static boolean isAlive; // flag to check if snake is still alive

	private Block snake;
	private Block food;
	private ArrayList<Block> walls;

	private double snakeX, snakeY;
	private double foodX, foodY;
	private int snakeDirection; // 1 up, 2 right, 3 down, 4 left

	@Override
	public void initResources() {
		// create snake
		snakeX = 1;
		snakeY = 1;
		snakeDirection = 2;
		snake = new Block(getImage("./img/snakeblock.png"), normalize(snakeX),
				normalize(snakeY));

		// create food
		foodX = 20;
		foodY = 20;
		food = new Block(getImage("./img/foodblock.png"), normalize(foodX),
				normalize(foodY));

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
	}

	@Override
	public void render(Graphics2D gd) {
		// TODO Auto-generated method stub
		gd.setColor(Color.GRAY);
		gd.fillRect(0, 0, getWidth(), getHeight());

		snake.render(gd);
		food.render(gd);

		for (Block wall : walls) {
			wall.render(gd);
		}
	}

	@Override
	public void update(long l) {
		while (isAlive) {
			if (Math.abs(snake.getX() - food.getX()) < DIMENSION && Math.abs(snake.getY() - food.getY()) < DIMENSION)
				resetFood();

			readInput();
			moveSnake();

			snake.update(l);
			food.update(l);
		}
	}

	/* USER DEFINED FUNCTIONS */
	public double normalize(double val) {
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
		if (snakeDirection == 1)
			snakeY -= speed;
		else if (snakeDirection == 2)
			snakeX += speed;
		else if (snakeDirection == 3)
			snakeY += speed;
		else if (snakeDirection == 4)
			snakeX -= speed;

		snake.setX(normalize(snakeX));
		snake.setY(normalize(snakeY));
		
		checkIfDead();
	}

	public void resetFood() {
		int newX, newY;
		newX = ((int) ((Math.random() * 100) % 40));
		newY = ((int) ((Math.random() * 100) % 40));
		food.setX(normalize((double) newX));
		food.setY(normalize((double) newY));
	}
	
	public void checkIfDead() {
		for (Block wall : walls) {
			if (Math.abs(snake.getX() - wall.getX()) < DIMENSION && Math.abs(snake.getY() - wall.getY()) < DIMENSION)
				System.out.println("DEAD!");
		}
	}

}
