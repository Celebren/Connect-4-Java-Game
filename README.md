# Connect-4-Java-Game
A Connect 4 game developed in Java using JavaFX.  
Was developed as an eclipse project.
  
  
When the game starts, on the intro dialog window, users can enter name for each player (colour). If no names are given, defaults are used (red and yellow). A saved game can be loaded. Selecting new game starts the game. First turn is randomised each time the game starts or restarts.  
Menu Items: 
 - File/New Game: restarts the game and displays the intro dialog, player names are stored but can be changed
 - File/Reset Board: resets the game board. Player names are kept and can't be changed.
 - File/Save Game: Saves the current game, player names and who's turn it is. Creates save file named 'savedGame.txt'
 - File/Load game: Loads the saved game and displays who's turn it is.
 - File/Exit: Exits the application
 - Help/Rules: Displays the rules of the game
 - Help/About: Info about the application

### To run
Import and run from any java IDE. 

### Bugs
* When building into standalone executable performance during gameplay is severely slow
* In Ubuntu, the menu bar disappears as soon as the user releases the mouse button. Only way to navigate menu bars in linux is by using the Alt and arrow keys and enter key to select an item.

### To do
- Move sound files to the src folder
- Make Jar file
