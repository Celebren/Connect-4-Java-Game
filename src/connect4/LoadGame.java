package connect4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoadGame {
	// Declare variables to be saved
	private String redPlayerName;
	private String yellowPlayerName;

	private final int COLUMNS = 7;
	private final int ROWS = 6;	

	private boolean redsTurn;
	private String redsTurnStr;

	private GameToken[][] gameGrid = new GameToken[COLUMNS][ROWS];
	private String tokenColour;

	// constructor 
	LoadGame() {}

	// method for reading save game file
	public void readFile() {
		try { // buffered writer must be in an try-catch exception
			// instantiate new BufferedReader that reads from the savedGame.txt file
			BufferedReader loadGameBufferedReader = new BufferedReader(new FileReader("savedGame.txt"));
			
			// add to the variables the value of each line read
			this.redPlayerName = loadGameBufferedReader.readLine();
			this.yellowPlayerName = loadGameBufferedReader.readLine();
			this.redsTurnStr = loadGameBufferedReader.readLine();
			//System.out.println("in class redsTurnStr = " + this.redsTurnStr);

			// convert redsTurn from string to boolean
			if (this.redsTurnStr.equals("false")) {
				this.redsTurn = false;				
			} else {
				this.redsTurn = true;
			}

			//System.out.println("in class redsTurn =  " + this.redsTurn);
			// iterate through the 2D array
			for (int r = 0; r < ROWS; r++) {
				for (int c = 0; c < COLUMNS; c++) {
					// for each line read, assign the colour read to tokenColour
					tokenColour = loadGameBufferedReader.readLine();
					//System.out.println("tokenColour = " + tokenColour);
					
					// if tokenColour is red or yellow
					// create a new token and add it to the grid position [c][r]
					// if the tokenColour is "empty" then no tokens will be added
					if (tokenColour.equals("red")) {
						GameToken newToken = new GameToken(true);
						gameGrid[c][r] = newToken;
						//System.out.println("newToken " + newToken.stringOutput());
					} else if (tokenColour.equals("yellow")) {
						GameToken newToken = new GameToken(false);
						gameGrid[c][r] = newToken;
						//.out.println("newToken " + newToken.stringOutput());
					} 
				}
			} // end of array iteration
			
			// close buffered reader
			loadGameBufferedReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}


	} // end of readFile()

	// getter methods
	public String getRedPlayerName() {
		return this.redPlayerName;
	}	

	public String getYellowPlayerName() {
		return this.yellowPlayerName;
	}

	public boolean getRedsTurn() {
		return this.redsTurn;
	}

	public GameToken[][] getGameGrid() {
		return this.gameGrid;
	}
} // end of LoadGame class
