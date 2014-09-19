package snakegame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.golden.gamedev.Game;


public class GameFrame extends Game {
	Block snake;
	Block food;
	ArrayList<Block> wall;
	
	double speed;
	double snakeX, snakeY;
	double foodX, foodY;
	//ArrayList<Double> wallX, wallY;
	int snakeDirection; // 1 up, 2 right, 3 down, 4 left
	
	final double dimension = 16; // 16x16
	
	@Override
	public void initResources() {
		speed = 0.1;
		snakeX = 1;
		snakeY = 1;
		snakeDirection = 2;
		snake = new Block(getImage("snakeblock.png"), normalize(snakeX), normalize(snakeY));
		
		foodX = 20;
		foodY = 20;
		food = new Block(getImage("foodblock.png"), normalize(foodX), normalize(foodY));
		
		//wallX = 0;
		//wallY = 0;
		
	}

	@Override
	public void render(Graphics2D gd) {
		// TODO Auto-generated method stub
		gd.setColor(Color.GRAY);
		gd.fillRect(0, 0, getWidth(), getHeight());
		
		snake.render(gd);
		food.render(gd);
	}

	@Override
	public void update(long l) {
		if(Math.abs(snake.getX()-food.getX()) < dimension && Math.abs(snake.getY()-food.getY()) < dimension)
			resetFood();
		
		readInput();
		moveSnake();
		
		snake.update(l);
		food.update(l);
	}
	
	/* USER DEFINED FUNCTIONS */
	public double normalize(double val) {
		return val*dimension;
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
		if(snakeDirection == 1)
			snakeY -= speed;
		else if (snakeDirection == 2)
			snakeX += speed;
		else if (snakeDirection == 3)
			snakeY += speed;
		else if (snakeDirection == 4)
			snakeX -= speed;
		
		snake.setX(normalize(snakeX));
		snake.setY(normalize(snakeY));
	}
	
	public void resetFood() {
		int newX, newY;
		newX = ((int)((Math.random() * 100) % 40));
		newY = ((int)((Math.random() * 100) % 40));
		food.setX(normalize((double)newX));
		food.setY(normalize((double)newY));
	}

}
