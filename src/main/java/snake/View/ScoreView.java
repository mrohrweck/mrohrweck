package snake.View;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class ScoreView {
	
	private StackPane stack; 
	private Label lScoreText, lScore, lInfoPause, lInfoESC;
	/**
	 * Height of score panel
	 */
	public static final int SCORE_HEIGHT = 25;
	/**
	 * Color of score label
	 */
	private final Color scoreColor = Color.WHITE;
	/**
	 * Color of the score panel
	 */
	private final Color scoreFieldColor = Color.DARKBLUE;;
	
	public ScoreView() {
		
		stack = new StackPane();
		stack.setStyle("-fx-background-color: "+MainView.SCENE_COLOR);
		Rectangle rScoreBackground = new Rectangle(MainView.WIDTH , SCORE_HEIGHT);
		rScoreBackground.setFill(scoreFieldColor);
	    

        
	    // setting the labels
		lScoreText = new Label("SCORE: ");
		lScoreText.setFont(new Font(20));
		lScoreText.setTextFill(scoreColor);
	
		lScore = new Label("0");
		lScore.setFont(new Font(20));
		lScore.setTextFill(scoreColor);

		lInfoPause = new Label("Press SPACE to Pause");
		lInfoPause.setFont(new Font(13));
		lInfoPause.setTextFill(scoreColor);

		lInfoESC = new Label("Press ESC to Exit");
		lInfoESC.setFont(new Font(13));
		lInfoESC.setTextFill(scoreColor);
		
		stack.getChildren().addAll(rScoreBackground, lScoreText, lScore, lInfoPause, lInfoESC);
		stack.getChildren().get(1).setTranslateX(-30);
		stack.getChildren().get(2).setTranslateX(40);
		stack.getChildren().get(3).setTranslateX(150);
		stack.getChildren().get(4).setTranslateX(-170);
	}
	
	/**
	 * Gets the actual score and displays it on the scene
	 * @param score in game
	 */
	public void addScore(int score) {

		lScore.setText(Integer.toString(score));
	}
	
	/**
	 * Returns the stack that holds all elements to be displayed on the score panel
	 * @return the stack
	 */
	public StackPane getStack() {

		return stack;
	}	
}