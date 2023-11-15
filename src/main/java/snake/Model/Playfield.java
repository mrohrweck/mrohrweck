package snake.Model;

import java.util.ArrayList;
import java.util.Random;

import snake.Controller.Controller;
import snake.View.MainView;
import snake.View.ScoreView;
import javafx.animation.*;




public class Playfield {

	/**
	 * Number of GameObjects to store in X-axis
	 */
	private static final int BWIDTH = MainView.WIDTH/ Object.SIZE;
	/**
	 * Number of GameObjects to store in Y-axis
	 */
	private static final int BHEIGHT = (MainView.HEIGHT - 20)/ Object.SIZE;
	/**
	 * List of fruits
	 */
	private ArrayList<Fruit> fruits; 

	/**

	 * Score value
	 */
	private int score;
	/**
	 * Snake object
	 */
	private Snake snake; 
	/**
	 * Snake's head
	 */
	private BodyPart head;
	/**
	 * Random number for generating points to place objects on them
	 */
	Random rand; 
	/**
	 * State of the game
	 */
	private GameState state;
	/**
	 *  Object of ScoreView to exchange informations about actual score
	 */
	private ScoreView scoreView; 

	/**
	 * Boolean value that tells if a new obstacle can be added
	 */
	private boolean addObstacle; 
	/**
	 * Number of possible obstacles on the field
	 */
	private int obstaclesNumber;


	
	/**
	 * Timers for super fruit and it's effect
	 */
	private Timeline timeSuper, timeSFruit;
	
	/**
	 * Default constructor of board class to initialize starting variables
	 */
	public Playfield() {
		
		scoreView = new ScoreView();
		fruits = new ArrayList<>();
		score  = 0;
		snake = new Snake();
		rand = new Random();
		head = snake.getHead();
		state = GameState.Started;
		addObstacle = true;
		
	}
	
	/**
	 * Method to check if an collision occurred, either of the snake head with it's body or with an obstacle on the board
	 * @return Returns the finished state of game
	 */
	public GameState checkCollision() {
		
		int headX, headY, helpX, helpY;
		
		headX = head.getX();
		headY = head.getY();
		
		// checks if snake hit itself
		for(int i = 1; i < snake.getSize(); ++i) {

			helpX = snake.getBodyPart(i).getX();
			helpY = snake.getBodyPart(i).getY();

			if (helpX == headX && helpY == headY) {
				reset();
				return GameState.Finished;
			}
		}

		//check collision head with borders
		helpX = snake.getHead().getX();
		helpY = snake.getHead().getY();

		if (headX  <= 0 || headX >= MainView.WIDTH || headY <= 0 || headY >= MainView.HEIGHT - 20 ) {
			reset();
			return GameState.Finished;
		}

		
		return Controller.getState();
	}



	
	/**
	 * Method to check if snake ate a fruit
	 */
	public void checkEaten() {
		
		int headX, headY, foodX, foodY;
		headX = head.getX();
		headY = head.getY();

		// check for a fruit on board 	
		for(int i = 0; i < fruits.size(); ++i){
			
			foodX = fruits.get(i).getX();
			foodY = fruits.get(i).getY();
			
			if(foodX == headX && foodY == headY) {
				
				removeFruit(i);
				addLength(); // adds body part to snake
				score++;
		
				addObstacle = false;
			}		
		}
	}
	
	/**
	 * Method for updating fruits on board
	 */
	public void updateFruit() {
		
		int foodX = 0, foodY = 0, sFoodX = -1, sFoodY = -1; // foodX, foodY - coordinates for normal fruit, with s for super 
		int []place; // place on board, will hold X and Y
		
		if(fruits.size() <= 0) { // if there's no fruit


			
			do {
				place = placeFruit();
				foodX = place[0];
				foodY = place[1]; 	
			}while(foodX == sFoodX && foodY == sFoodY);
		
			addFruit(foodX, foodY, sFoodX, sFoodY);
		}
	}
	
	/**
	 * Method to place a fruit on the board
	 * @return Returns point(X,Y) on board
	 */
	private int[] placeFruit() {
		
		int []point = new int[2];
		
		int helpX, helpY, foodX = 0, foodY = 0;
		boolean helpS, helpO;	// for Snake and Obstacles
		boolean collision = true;

		while(collision) {
				
			helpS = helpO = false;
			foodX = (rand.nextInt(BWIDTH)* Object.SIZE)+ Object.SIZE/2;
			foodY = (rand.nextInt(BHEIGHT)* Object.SIZE)+ Object.SIZE/2;
				
			for(int i = 0; i < snake.getSize(); ++i) {
					
				helpX = snake.getBodyPart(i).getX();
				helpY = snake.getBodyPart(i).getY();
	
				if(helpX == foodX && helpY == foodY) {
					break;
				}
						
				if(i == snake.getSize() - 1) {
					helpS = true;
				}
			}

			if(helpS) {
					collision = false;
				}
		}
		point[0] = foodX;
		point[1] = foodY;
		return point;	
	}
	
	/**
	 * Method to generate a new fruit in the game(2 if it's time for the super-fruit)
	 * @param foodX X coordinate of normal fruit
	 * @param foodY	Y coordinate of normal fruit
	 * @param sFoodX X coordinate of super fruit(-1 by default)
	 * @param sFoodY Y coordinate of super fruit(-1 by default)
	 */
	public void addFruit(int foodX, int foodY, int sFoodX, int sFoodY) {

		fruits.add(new Fruit(foodX, foodY)); // add new fruit to fruit array
	}
	
	/**
	 * Method to remove a normal fruit from the board, after being eaten by snake
	 * @param i Position of the fruit in array list
	 */
	public void removeFruit(int i) {
		fruits.remove(i);
	}
	

	/**
	 * Method for calling another method from ScoreView to add a point to the score
	 */
	public void updateScore() {
		scoreView.addScore(score);
	}

	public void resetScore() {
		score = 0;
		updateScore();
	}
	
	/**
	 * Add new part to snake's body after eating a fruit
	 */
	public void addLength() {
		BodyPart b1 = snake.getBodyPart(snake.getSize()-1), b2 = snake.getBodyPart(snake.getSize()-2);
		if(b1.getX() > b2.getX())
			snake.addBodyPart(b1.getX()+ Object.SIZE, b1.getY());
		else if(b1.getX() < b2.getX())
			snake.addBodyPart(b1.getX()- Object.SIZE, b1.getY());
		else if(b1.getY() >= b2.getY())
			snake.addBodyPart(b1.getX(), b1.getY()+ Object.SIZE);
		else if(b1.getY() >= b2.getY())
			snake.addBodyPart(b1.getX(), b1.getY()- Object.SIZE);
	}
	
	/**
	 * Resets basic values of the game after lose
	 */
	private void reset() {
		snake.setStart();
		fruits.clear();
		//score = 0;
		addObstacle = true;
	}
	
	/**
	 * Returns fruits
	 * @return Array with fruits
	 */
	public ArrayList<Fruit> getFruits(){
		return fruits;
	}
	


	
	/**
	 * Returns the snake
	 * @return Snake object
	 */
	public Snake getSnake() {
		return snake;
	}
	
	/**
	 * Returns the object representing ScoreView in Board class
	 * @return The ScoreView object
	 */
	public ScoreView getScoreView() {
		return scoreView;
	}
	
	/**
	 * Returns the actual score
	 * @return Value of score
	 */
	public int getScore() {
		return score;
	}

	
	/**
	 * Returns the actual state of the game
	 * @return Value of GameState
	 */
	public GameState getState() {
		return state;
	}
	


}