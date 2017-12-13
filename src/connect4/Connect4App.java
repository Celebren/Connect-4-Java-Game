package connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Connect4App extends Application{
	// variables declarations
	private String redPlayerName = "Red";
	private String yellowPlayerName = "Yellow";

	private final String WINDOW_TITLE = "Java Connect 4 Game";	
	private final String RED_PIECE = "/images/redGamePiece.png";
	private final String YELLOW_PIECE = "/images/yellowGamePiece.png";
	private final String GAME_PIC = "/images/connect4pic.png";
	private final String CHEER = "/sound/cheer.mp3";
	private final String TOKEN_FALL = "/sound/tokenFall.mp3";
	private final String DRAW = "/sound/draw.mp3";
	
	// end state strings
	private String dialogTitle;
	private String dialogHeader;
	private String winningPiece;
	private String winner;

	// GUI constants
	private final int TILE_SIZE = 80; // size of tiles that will be the basis of making the board, tokens and overlay
	private final int COLUMNS = 7;
	private final int ROWS = 6;
	private final int BOARD_TRANSY = 25;
	private final int OVERLAY_TRANSY = 29;

	// booleans for tracking turns and game states
	private boolean redsTurn = true; // red starts first
	private boolean gameExited = false; // helper boolean to prevent displaying first player declaration dialog if the game was exited in the welcome dialog
	private boolean winnerRed = true; // helper boolean to store the current player's turn before it's changed. 
	private boolean gameIsOver = false;

	// create two dimensional array of GameTokens
	private GameToken[][] gameGrid = new GameToken[COLUMNS][ROWS];
	
	/*
	 *  index guide for gameGrid:
	 *  
	 *  	columns
	 * 		  0   1   2   3   4   5   6
	 * rows	 ___________________________   
	 *    0 |___|___|___|___|___|___|___|
	 *    1 |___|___|___|___|___|___|___|
	 *    2 |___|___|___|___|___|___|___|
	 *    3 |___|___|___|___|___|___|___|
	 *    4 |___|___|___|___|___|___|___|
	 *    5 |___|___|___|___|___|___|___|
	 *    
	 */
	
	// create new root pane to display placed tokens
	// this is not in the token class as it is used in both
	// createContent() and the class itself
	private Pane tokenDisplayRoot = new Pane();


	public static void main(String[] args) {		
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// create and show new application stage with scene made in createContent()		
		Scene gameScene = new Scene(createContent(stage),630, 575); // make new scene.
		stage.setTitle(WINDOW_TITLE);		
		stage.getIcons().add(new Image(RED_PIECE));
		stage.setScene(gameScene); // add the scene to the stage
		stage.setResizable(false); // prevent window for being resizable
		stage.show();		
		introDialog(); // show intro dialog.		
	} // end start()

	// method randomizing who goes first and display message informing the players
	private void firstTurnRandomizer() {
		redsTurn = Math.random() < 0.5;		
	} // end of firstTurnRandomizer()

	// make dialog displaying which player goes first
	private void displayWhoGoesNextDialog() {
		String activePlayerName;
		String activePiece;

		// Create the custom dialog.
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(WINDOW_TITLE);

		// Get the Stage in order to set window title bar icon
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		// add the icon
		stage.getIcons().add(new Image(this.getClass().getResource(RED_PIECE).toString()));

		if (redsTurn) {
			activePlayerName = redPlayerName;
			activePiece = RED_PIECE;
		} else {
			activePlayerName = yellowPlayerName;
			activePiece = YELLOW_PIECE;
		}

		alert.setHeaderText(activePlayerName + " goes first");

		// Set the icon 
		alert.setGraphic(new ImageView(this.getClass().getResource(activePiece).toString()));

		alert.showAndWait();
	} // end of displayWhoGoesNext()

	//intro dialog creation method
	private void introDialog() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(WINDOW_TITLE + " - Welcome");
		dialog.setHeaderText("Welcome to Java Connect 4 game!"
				+ "\nPlease enter players names.");

		//Set the window title bar icon by setting the main stage as the owner of the dialog
		// Get the Stage in order to set window title bar icon
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		
		// add the icon
		stage.getIcons().add(new Image(this.getClass().getResource(RED_PIECE).toString()));			

		// create header graphic
		dialog.setGraphic(new ImageView(this.getClass().getResource(GAME_PIC).toString()));

		// Create custom buttons
		ButtonType btnStartGame = new ButtonType("Start Game", ButtonData.OK_DONE);
		ButtonType btnLoadGame = new ButtonType("Load Game" , ButtonData.OK_DONE);
		ButtonType btnExitApp = new ButtonType("Exit", ButtonData.CANCEL_CLOSE);		

		//display buttons
		dialog.getDialogPane().getButtonTypes().addAll(btnStartGame, btnLoadGame, btnExitApp);

		// Create player name labels and fields into a new grid
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 120));

		TextField redPlayer = new TextField();
		redPlayer.setPromptText(redPlayerName);		
		TextField yellowPlayer = new TextField();
		yellowPlayer.setPromptText(yellowPlayerName);

		grid.add(new Label("Red player's name:"), 0, 0);
		grid.add(redPlayer, 1, 0);
		grid.add(new Label("Yellow player's name:"), 0, 1);
		grid.add(yellowPlayer, 1, 1);

		// add grid to dialog
		dialog.getDialogPane().setContent(grid);

		// Convert the results to a redPlayerName-yellowPlayerName-pair when the start game button is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == btnStartGame) {				
				firstTurnRandomizer();
				return new Pair<>(redPlayer.getText(), yellowPlayer.getText());
			} else if(dialogButton == btnLoadGame) {				
				loadGame();				
				return new Pair<>(redPlayer.getText(), yellowPlayer.getText());
			} else if (dialogButton == btnExitApp) { // if the exit button is clicked, qiot the application
				//System.out.println("This should not run");
				gameExited = true;
				Platform.exit();
				return new Pair<>(redPlayer.getText(), yellowPlayer.getText());		    	
			}
			return null;
		});

		// make new optional pair (can return null) named result that displays the dialog and takes in the text input
		Optional<Pair<String, String>> result = dialog.showAndWait();

		// add text input from pair t0 player names variables
		result.ifPresent(playerNames -> {
			// if there are name inputs by the user change the
			// player names to that input
			if (!playerNames.getKey().isEmpty()) {
				redPlayerName = playerNames.getKey();
			}

			if (!playerNames.getValue().isEmpty()) {
				yellowPlayerName = playerNames.getValue();
			}
		});

		//playerNamesChecker(); // check if player names are empty
		
		// only dusplay who goes next if the game was not exited
		if (!gameExited) {
			displayWhoGoesNextDialog();
		}

	} // end introDialog()

	// load game method
	private void loadGame() {
		// reset the game before loading
		reset();
		
		// instantiate LoadGame 
		LoadGame newLoad = new LoadGame();
		
		// call readFile() method to get saved values from text file
		newLoad.readFile();
		
		// assign those values to the program's variables
		redPlayerName = newLoad.getRedPlayerName();
		if (redPlayerName.isEmpty()) {
			redPlayerName = "Red";
		}
		//System.out.println("Loaded red name is " + redPlayerName);
		yellowPlayerName = newLoad.getYellowPlayerName();
		if (yellowPlayerName.isEmpty()) {
			yellowPlayerName = "Yellow";
		}
		//System.out.println("Loaded yellow name is " + yellowPlayerName);
		redsTurn = newLoad.getRedsTurn();
		//System.out.println("redsTurn is " + newLoad.getRedsTurn());
		gameGrid = newLoad.getGameGrid();
		
		// iterate through the loaded grid
		for (int r = 0; r < ROWS; r++) {				
			for (int c = 0; c < COLUMNS; c++) {
				// make a new token with the token in that position in that grid
				GameToken newToken = gameGrid[c][r];
				// if newToken is not null add it to the GUI
				if (newToken !=null) { 					
					//System.out.println("Array " + newToken.stringOutput());
					// then create token to be displayed in the UI
					tokenDisplayRoot.getChildren().add(newToken);

					// set token position
					newToken.setTranslateX(c * (TILE_SIZE + 7) + TILE_SIZE / 4);
					newToken.setTranslateY(r * (TILE_SIZE + 7) + TILE_SIZE / 4);
				}			
			} // end of c loop 
		} // end of r loop
	} // end of loadGame()

	// Method that creates the display pane and adds elements to it
	// (grid,columns overlay and menu bar).	 
	private Parent createContent(Stage stage) {

		// instantiate new border pane
		BorderPane root = new BorderPane();

		// order or adding things to root determines display position
		// shapes written first appear in the background and last appear in
		// foreground

		// display tokens when created, behind the game board
		root.getChildren().add(tokenDisplayRoot);

		Shape GameBoardGridShape = makeBoardGrid();
		root.getChildren().add(GameBoardGridShape);

		// add transparent overlay indicating user selection
		// the addall() method adds all the overlays in the list one after the other
		root.getChildren().addAll(makeColumnsOverlay());

		// add menubar to the pane                
		root.setTop(createMenuBars(stage));
		
		//return the content to the start method for display
		return root;
	} // end createContent()


	// returns the game board grid
	private Shape makeBoardGrid() {
		/* 
		 * make a new rectangle and set its width as the number of
		 * columns (+1 to make it a bit bigger) times the size of a single game
		 * tile and its height is the number of rows (+1) times the size of a
		 * single tile
		 * 
		 * syntax Rectangle(width, height)
		 * 
		 */
		Rectangle gameBoardRectangle = new Rectangle((COLUMNS + 1) * TILE_SIZE, (ROWS + 1) * TILE_SIZE);
		// move the new rectangle down 25 pixes to accomodate for the menubar
		gameBoardRectangle.setY(BOARD_TRANSY); 

		// convert rectangle into javafx.scene.shape to get access to substract function
		Shape gameBoardAsShape = gameBoardRectangle;

		// On this rectangle, subtract 42 circle shapes
		// Iterate through the rows and columns using nested loops:
		for (int row = 0; row < ROWS; row++) {
			for (int column = 0; column < COLUMNS; column++) {

				// Create new circle with a radius of half the tile size 
				Circle circle = new Circle(TILE_SIZE / 2);

				// set the centre of the circle to the centre of the tile
				circle.setCenterX(TILE_SIZE / 2);
				circle.setCenterY(TILE_SIZE / 1.2);

				 
				// display the circles in the centre of each tiles
				// row/column variable: determines position of each new circle
				// "(TILE_SIZE + n)": +n determines spacing
				// "+ TILE_SIZE / n": adjusts position on the board 
				circle.setTranslateX(column * (TILE_SIZE + 7) + TILE_SIZE / 4); // keep consistent with token translations
				circle.setTranslateY(row * (TILE_SIZE + 7) + TILE_SIZE / 4); // keep consistent with token translations

				// finally sabstract the circles from the grid:
				gameBoardAsShape = Shape.subtract(gameBoardAsShape, circle);
			} // end columns loop
		} // end rows loop

		// add lighting to the board using Light.Distant library manual example
		// without any changes
		Light.Distant light = new Light.Distant();
		light.setAzimuth(45.0);
		light.setElevation(40.0);

		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(5.0);		

		// set board colour
		gameBoardAsShape.setFill(Color.rgb(66, 185, 244));

		// add lighting effect
		gameBoardAsShape.setEffect(lighting);

		return gameBoardAsShape;
	} // and makeBoardGrid() 


	// this method creates all menubars and their functions
	private MenuBar createMenuBars(Stage stage) {

		// Create MenuBar
		MenuBar menuBar = new MenuBar();

		// Create menus
		Menu fileMenu = new Menu("File");        
		Menu helpMenu = new Menu("Help");

		// Create MenuItems
		MenuItem newGameItem = new MenuItem("New Game");
		newGameItem.setOnAction(actionEvent -> {			
			reset();
			firstTurnRandomizer();
			introDialog();			
		});

		MenuItem resetItem = new MenuItem("Reset Board");
		resetItem.setOnAction(actionEvent -> {
			reset();
			firstTurnRandomizer();
			displayWhoGoesNextDialog();
		});

		MenuItem saveGameItem = new MenuItem("Save Game");
		saveGameItem.setOnAction(actionEvent -> {
			SaveGame newSave = new SaveGame(redPlayerName, yellowPlayerName, redsTurn, gameGrid);
			newSave.writeToFile();
		});

		MenuItem loadGameItem = new MenuItem("Load Game");
		loadGameItem.setOnAction(actionEvent -> {
			loadGame();
			displayWhoGoesNextDialog();
		});

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(actionEvent -> Platform.exit());

		MenuItem rulesItem = new MenuItem("Rules");
		rulesItem.setOnAction(actionEvent -> {
			makeNewDialog(stage, WINDOW_TITLE + " - Connect 4 Rules", TextVariables.getRulesTxt());        	       	
		});

		MenuItem aboutItem = new MenuItem("About");
		aboutItem.setOnAction(actionEvent -> {
			makeNewDialog(stage, WINDOW_TITLE + " - About", TextVariables.getAboutTxt());
		});

		// Add menuItems to the Menus
		fileMenu.getItems().addAll(newGameItem, resetItem, saveGameItem, loadGameItem, exitItem);        
		helpMenu.getItems().addAll(rulesItem, aboutItem);

		// Add Menus to the MenuBar
		menuBar.getMenus().addAll(fileMenu, helpMenu);

		return menuBar;
	} // end create MenuBars

	// method for making menu bar dialogs
	private void makeNewDialog(Stage stage, String dialogTitle, String dialogText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(dialogTitle);
		alert.initOwner(stage);
		alert.setHeaderText(null);
		alert.setContentText(dialogText);        	
		alert.showAndWait(); 
	} // end makeNewDialog()

	// make an overlay indicating which column the user is targeting
	// this is a list of transparent rectangles
	private List<Rectangle> makeColumnsOverlay() {
		List<Rectangle> listOfOverlays = new ArrayList<>();

		for (int column = 0; column < COLUMNS; column++) {
			// the overlay's width is equal to the width of a single tile and
			// height is equal to the heigh of the board
			Rectangle tileOverlay = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);

			// position the overlay over a column
			tileOverlay.setTranslateX(column * (TILE_SIZE + 7) + TILE_SIZE / 4);

			// move overlay down to align with the circles
			tileOverlay.setTranslateY(OVERLAY_TRANSY);

			// make the overlay transparent
			tileOverlay.setFill(Color.TRANSPARENT);
			
			// make column number final in order to pass it to the placeToken method
			// otherwise the compiler gives an error
			final int COLUMN = column; 

			/* 
			 * when the user clicks, add a game token.
			 * call the function placeToken passing to it a new game token
			 * (passing who's turn it is into the constructor)
			 * and the column number
			 * 
			 */
			tileOverlay.setOnMouseClicked(e -> placeNewToken(new GameToken(redsTurn), COLUMN));

			// add overlay to the list
			listOfOverlays.add(tileOverlay);
		} // end columns for loop

		return listOfOverlays;
	} // end makeColumnsOverlay()	


	// make method to add tokens. Once token is placed, run method checking if game has ended
	private void placeNewToken(GameToken token, int column) {

		// will start checking each row in the selected column.
		// as rows are in an array, they range from 0 to 5 instead of 1 to 6
		// so subtract one from ROWS
		int row = ROWS -1; // this will start at row 5 witch is the bottom row and go upwards to row 0

		// check if token can be placed by going thorugh each tile in the row
		do {
			// check if there is a token present in this position using getToken() 
			if (!getToken(column, row).isPresent()) { 
				// if the tile is empty, break out of the loop and proceed to place
				// a new token
				break;
			}
			// reduce the  row to check the next position in the column
			row--;
		} while (row >= 0); // if no empty tiles are found, finish checking once all rows are checked (i.e. row = 0)

		// if the column is empty (i.e. the do-while loop has finished
		// checking each row and the row is now -1) return to break out of the method
		if (row < 0) {
			return;
		}

		// first add new token to the array in the current row in the logic
		gameGrid[column][row] = token;	

		// then create token to be displayed in the GUI
		tokenDisplayRoot.getChildren().add(token);

		// set token column position
		token.setTranslateX(column * (TILE_SIZE + 7) + TILE_SIZE / 4);

		// make row final in order to pass it to gameEnded()
		final int ROW = row;
		// set the animation speed in seconds
		double animationDuration = 0.5;
		// check if the game has ended
		if (endStateChecker(column, ROW)) {
			// the current player's turn must be saved in a different variable now
			// in order for it to be passed to the winner declaration dialog
			// which is displayed later, after the token animation has
			// stopped playing and the turn has changed
			winnerRed = redsTurn; // save the curent player turn			
			gameIsOver = true; // change game is over to true
			animationDuration = 0.1; // make animation play faster so that the last payed tile appears before the winner declaration dialog
		}

		// change turn
		redsTurn = !redsTurn;		
		// create TranslateTransition animation for the tokens fallining into place
		TranslateTransition tokensFallingAnimation = new TranslateTransition(Duration.seconds(animationDuration), token);
		// set the final position of the token (where the animation will end)
		tokensFallingAnimation.setToY(row * (TILE_SIZE + 7) + TILE_SIZE / 4);

		tokensFallingAnimation.play(); // play animation		

		tokensFallingAnimation.setOnFinished(e -> playSoundEffect(TOKEN_FALL)); // play sound effect when animation dinishes

		/* debug loop
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				GameToken newToken = gameGrid[c][r];
				System.out.println("Array " + newToken);
			}
		}
		 */

		// check if there are no more empty cells left
		if (noMoreCells()) {
			noOneWon(); // display no one won dialog
		}

		// display game over dialog declaring winner if the game has ended
		if (gameIsOver) {
			declareWinner(winnerRed); // display winner dialog
		}	

	} // end placeToken()

	// method that checks if there are no more empty cells
	private boolean noMoreCells() {
		// initialise a counter of cells
		int notNullCells = 0;

		// iterrate through rows and collumns increasing the counter if no empty cells are found
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				if ( gameGrid[c][r] != null ) {
					notNullCells++;
				}
			}
		}		

		// if all 42 of the game board's cells are not empty return true and the app will display a dialog
		if ( notNullCells == 42 ) {
			return true;
		}
		return false;
	} // end of noMoreCells()

	// method checking if the game has ended (4 tokens in a row)
	// pass in column and row of added token
	private boolean endStateChecker(int column, int row) {
		// create 4 array lists of tokens for each check:
		// vertical, horizontal, diagonall from top left to bottom right
		// and diagonal from bottom left to top right
		
		ArrayList<GameToken> vertical = new ArrayList<>();
		// add tokens to the list from a row position  of 3 cells above (row - 3) and
		// 3 cells below (row + 3). if there are no tokens to add in any of those
		// positions, add a token of the other colour.
		/*
		 *  for example if column = 3 and row = 3 the tokens will be added from cells
		 *  3,0 3,1 3,2 3,3 3,4 3,5 (column, row)
		 *  
		 *  	columns
		 * 		  0   1   2   3   4   5   6
		 * rows	 ___________________________   
		 *    0 |___|___|___|_x_|___|___|___|
		 *    1 |___|___|___|_x_|___|___|___|
		 *    2 |___|___|___|_x_|___|___|___|
		 *    3 |___|___|___|_o_|___|___|___|
		 *    4 |___|___|___|_x_|___|___|___|
		 *    5 |___|___|___|_x_|___|___|___|
		 *
		 *    o : starting point
		 *    x : added points
		 */
		for (int r = (row - 3); r <= (row + 3);  r++) {
			GameToken newToken= getToken(column, r).orElse(new GameToken(!redsTurn)); // return token if present. Otherwise return  a token for the other colour
			vertical.add(newToken);
		}

		ArrayList<GameToken> horizontal = new ArrayList<>();
		// add tokens to the list from a column position  of 3 cells to the left (column - 3) and
		// 3 cells to the right (column + 3). if there are no tokens to add in any of those
		// positions, add a token of the other colour.
		/*
		 *  for example if column = 3 and row = 3 the tokens will be added from cells
		 *  0,3 1,3 2,3 3,3 4,3 5,3 6,3
		 *  
		 *  	columns
		 * 		  0   1   2   3   4   5   6
		 * rows	 ___________________________   
		 *    0 |___|___|___|___|___|___|___|
		 *    1 |___|___|___|___|___|___|___|
		 *    2 |___|___|___|___|___|___|___|
		 *    3 |_x_|_x_|_x_|_o_|_x_|_x_|_x_|
		 *    4 |___|___|___|___|___|___|___|
		 *    5 |___|___|___|___|___|___|___|
		 *    
		 */
		for (int c = (column - 3); c <= (column + 3);  c++) {
			GameToken newToken= getToken(c, row).orElse(new GameToken(!redsTurn));
			vertical.add(newToken);
		}

		ArrayList<GameToken> diagonal1 = new ArrayList<>();
		// initialise a counter that will increase by 1 on each loop
		// this is also used as starting row coordinate
		int rowsIncreasing = row - 3;
		// go through each position from 3 cells to the left to 3 cells to the right
		// and add to the list a token from the position with c for column and rowsIncreasing
		// for the row. each time the column increases so does the row (with rowsIncreasing++)
		/*
		 *  for example if column = 3 and row = 3 the tokens will be added from the cells
		 *  0,0 1,1 2,2 3,3 4,4 5,5 6,6
		 *  
		 *  	columns
		 * 		  0   1   2   3   4   5   6
		 * rows	 ___________________________   
		 *    0 |_x_|___|___|___|___|___|___|
		 *    1 |___|_x_|___|___|___|___|___|
		 *    2 |___|___|_x_|___|___|___|___|
		 *    3 |___|___|___|_o_|___|___|___|
		 *    4 |___|___|___|___|_x_|___|___|
		 *    5 |___|___|___|___|___|_x_|___|
		 *		 
		 */
		for (int c = (column - 3); c <= (column + 3); c++) {				
			GameToken newToken= getToken(c, rowsIncreasing).orElse(new GameToken(!redsTurn));				
			diagonal1.add(newToken);
			rowsIncreasing++;
		}
		
		ArrayList<GameToken> diagonal2 = new ArrayList<>();
		// initialise a counter that will decrease by 1 on each loop
		// this is also used as starting row coordinate
		int rowsDecreasing = row + 3;
		// go through each position from 3 cells to the left to 3 cells to the right
		// and add to the list a token from the position with c for column and rowsDecreasing
		// for the row. each time the column increases the row decreases (with rowsDecreasing--)
		/*
		 *  for example if column = 3 and row = 3 the tokens will be added from the cells
		 *  1,5 2,4 3,3 4,2 5,1 6,0 
		 *  
		 *  	columns
		 * 		  0   1   2   3   4   5   6
		 * rows	 ___________________________   
		 *    0 |___|___|___|___|___|___|_x_|
		 *    1 |___|___|___|___|___|_x_|___|
		 *    2 |___|___|___|___|_x_|___|___|
		 *    3 |___|___|___|_o_|___|___|___|
		 *    4 |___|___|_x_|___|___|___|___|
		 *    5 |___|_x_|___|___|___|___|___|
		 *		 
		 */
		for (int c = (column - 3); c <= (column + 3); c++) {				
			GameToken newToken= getToken(c, rowsDecreasing).orElse(new GameToken(!redsTurn));				
			diagonal2.add(newToken);
			rowsDecreasing--;
		}
		
		// call fourInARoWChecker passwing each of the lists and if return the result. If any returns true, the game will end 
		return fourInARowChecker(vertical) || fourInARowChecker(horizontal) || fourInARowChecker(diagonal1) || fourInARowChecker(diagonal2);
	} // end of endStateChecker()

	private boolean fourInARowChecker(ArrayList<GameToken> rowList) {
		// initialise a counter that will be increasing for every time
		// the token in the position being checked matches the user's turn
		// otherwise it resets to 0
		int fourInARowCounter = 0;
		
		// iterate through the tokens in the passed list
		for (GameToken token : rowList) {
			//System.out.println(fourInARowCounter + " in a row");

			// check is the token is the same colour as the active players turn
			if (token.getIsRed() == redsTurn) {
				// increment result by one
				fourInARowCounter++;
				// if result == 4 then winning combination is found
				if (fourInARowCounter == 4) {
					return true;
				}
			} else { // if the token doesn't match the actuve player colour, reset the counter
				// System.out.println("counter reset");
				fourInARowCounter = 0;
			}
		} // end of points loop
		// if counter never reaches 4 return false
		return false;
	} // end fourInARowChecker()

	// method displaying dialog when no one won
	private void noOneWon() {
		dialogTitle = WINDOW_TITLE + " - It's a draw!";
		dialogHeader = "No more empty cells! \nIt's a draw!";
		winningPiece = "";

		endGameDialog(DRAW, dialogTitle, dialogHeader, winningPiece);			
	} // end of noOneWon()

	// method for game over state. 
	private void declareWinner(boolean winnerRed) {
		if (winnerRed) {
			winner = redPlayerName;
			winningPiece = RED_PIECE;
		} else {
			winner = yellowPlayerName;
			winningPiece = YELLOW_PIECE;
		}		

		dialogTitle = WINDOW_TITLE + " - " + winner + " wins!";
		dialogHeader = winner + " wins!";

		endGameDialog(CHEER, dialogTitle, dialogHeader, winningPiece);
	} // end declareWinner()

	// method for displaying end game dialog
	private void endGameDialog(String soundEffect, String dialogTitle, String dialogHeader, String pieceIcon) {
		// play sound effect
		playSoundEffect(soundEffect);

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(dialogTitle);
		alert.setHeaderText(dialogHeader);
		alert.setContentText("Play again?");

		// Get the Stage in order to set window title bar icon
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		// add the icon
		stage.getIcons().add(new Image(this.getClass().getResource(RED_PIECE).toString()));

		// set dialog graphic
		alert.setGraphic(new ImageView(this.getClass().getResource(pieceIcon).toString()));

		// make buttons
		ButtonType btnNewGame = new ButtonType("Start New Game");
		ButtonType btnLoadGame = new ButtonType("Load Game");
		ButtonType btnExitGame = new ButtonType("Exit", ButtonData.CANCEL_CLOSE);

		// display buttons
		alert.getButtonTypes().setAll(btnNewGame, btnLoadGame, btnExitGame);
		
		Optional<ButtonType> result = alert.showAndWait();
		// set button behaviour
		if (result.get() == btnNewGame){
			reset();
			firstTurnRandomizer();
			introDialog();
		} else if (result.get() == btnLoadGame){
			loadGame();
			displayWhoGoesNextDialog();
		} else {
			Platform.exit();
		}
	} // end of endGameDialog

	// make method that checks if token is present.
	// the return type of this is Optional as it can return null when no tokens exist in the position
	private Optional<GameToken> getToken(int column, int row) {
		// check if column and row are within the grid boundries. Can get out of boundries in game end condition method 
		// where the cells checked are 3 positions before and after the position being checked. If the position is
		// out of bounds the 4 in a rouw checker will behave as if there was a token of a different colour
		if (column < 0 || column >= COLUMNS || row < 0 || row >= ROWS) {
			return Optional.empty(); // return null 
		}
		return Optional.ofNullable(gameGrid[column][row]); // otherwise return tile content. If no token found returns null
	} // end of getToken()	

	// method that resets the game
	private void reset() {
		//redPlayerName = null; // reset players names
		//yellowPlayerName = null;
		gameIsOver = false; // reset game over state
		winnerRed = false; // reset the winner
		redsTurn = true; // reset who goes first
		gameGrid = new GameToken[COLUMNS][ROWS]; // reset the gid
		tokenDisplayRoot.getChildren().clear(); // reset the images of placed tokens		
	} // end of reset()

	// method for playing sound effects
	private void playSoundEffect(String soundEffect) {
		Media sound = new Media(this.getClass().getResource(soundEffect).toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	} // end of playSoundEffect()
	
	// method checking if player names are empty
	/* not useful anymore
	private void playerNamesChecker() {
		if (redPlayerName.isEmpty()) {
			redPlayerName = "Red";
		}

		if (yellowPlayerName.isEmpty()) {
			yellowPlayerName = "Yellow";
		}
	} // end playerNamesChecker()
	*/
} // end Application class
