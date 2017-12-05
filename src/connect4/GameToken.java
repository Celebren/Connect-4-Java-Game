package connect4;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

//create game tokens class
public class GameToken extends Circle {
	// set boolean constant that determines the colour of the token based on who's turn it is
	private final boolean isRed;
	
	private static int TILE_SIZE = 80;

	// load images
	Image redToken = new Image("/images/redGamePiece.png");
	Image yellowToken = new Image("/images/yellowGamePiece.png");

	// Constructor
	public GameToken(boolean isRed) {
		// super constructor for the circle
		super(TILE_SIZE / 2);

		// call circle's setFill and instead of a colour add an ImagePattern with the game piece image
		if (isRed) { 
			setFill(new ImagePattern(redToken));
		} else {
			setFill(new ImagePattern(yellowToken));
		}
		
		// set color
		this.isRed = isRed;

		// set the circle centre
		setCenterX(TILE_SIZE / 2);
		setCenterY(TILE_SIZE / 1.2);

	} // end constructor

	// turn getter method
	public boolean getIsRed() {
		return this.isRed;
	}
	
	// method for displaying a string that will indicate the colour of the token
	// used to save the tokens in the grid into a save game text file. Also useful
	// for debugging
	public String stringOutput() {
		String output = "";		
		if (isRed) { 
			output = "red";
		} else {
			output = "yellow";
		}
		return output;
	}
} // end game token class