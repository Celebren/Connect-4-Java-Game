package connect4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveGame {	
	// Declare variables to be saved
	private String redPlayerName;
	private String yellowPlayerName;
	
	private final int COLUMNS = 7;
	private final int ROWS = 6;	
	
	private boolean redsTurn;
	private String redsTurnStr;
	
	private GameToken[][] gameGrid = new GameToken[COLUMNS][ROWS];
	
	
	// Constructor. Takes in all variables that need to be saved
	SaveGame(String redPlayerName, String yellowPlayerName, boolean redsTurn, GameToken[][] gameGrid) {
		this.redPlayerName = redPlayerName;
		this.yellowPlayerName = yellowPlayerName;
		this.redsTurn = redsTurn;
		this.gameGrid = gameGrid;		
	}
	
	// Method to save the variables into file using buffered writer
	public void writeToFile() {
		// convert redsTurn to string as the writer only writes strings
		if (this.redsTurn) {
			this.redsTurnStr = "true";
		} else {
			this.redsTurnStr = "false";
		}
		
		try { // buffered writer must be in an try-catch exception
			// instantiate a new BufferedWriter that creates and writes on the "savedGame.txt" file
			BufferedWriter saveGameBufferedWriter = new BufferedWriter(new FileWriter("savedGame.txt"));
			// write into the file
			saveGameBufferedWriter.write(this.redPlayerName);
			saveGameBufferedWriter.newLine();
			saveGameBufferedWriter.write(this.yellowPlayerName);
			saveGameBufferedWriter.newLine();
			saveGameBufferedWriter.write(this.redsTurnStr);
			saveGameBufferedWriter.newLine();
			
			// iterate through the 2D array
			for (int r = 0; r < ROWS; r++) {
				for (int c = 0; c < COLUMNS; c++) {
					// if the position is not empty, write the colour of the token there
					if (this.gameGrid[c][r] != null) {
						GameToken newToken = this.gameGrid[c][r];
						
						saveGameBufferedWriter.write(newToken.stringOutput());
						saveGameBufferedWriter.newLine();	
					} else { //otherwise write "empty"
						saveGameBufferedWriter.write("empty");
						saveGameBufferedWriter.newLine();
					}
					
				}
			} // end of array iteration
			
			// flush and close buffered writer
			saveGameBufferedWriter.flush();
			saveGameBufferedWriter.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	} // end of writeToFile()
} // end of SaveGame class
