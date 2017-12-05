package connect4;


// class that stores long text variables to avoid cluttering
// the main class. variables and methods are static so that
// they can be called directly from the class without needing
// to create a new object
public class TextVariables {
	
	private static String aboutTxt = "Java Connect 4 Game\n"
			+ "A javaFX application by Rena Silver\n"
			+ "Matriculation Number 40321603\n"
			+ "Edinburgh Napier University 2017\n"
			+ "SET11102 Software Development 1, Coursework";
	
	public static String getAboutTxt() {
		return aboutTxt;
	}
	
	private static String rulesTxt = "Connect Four\n" + 
			"From Wikipedia\n" + 
			"Designed by Howard Wexler and " + 
			"Ned Strongin\n" + 
			"Published by Milton Bradley / Hasbro\n" + 
			"Publication date 	1974\n" + 
			"Genre  	Abstract strategy\n" + 
			"Players 	2\n" + 
			"Playing time 	1 - 10 minutes\n" + 
			"\n" + 
			"Connect Four (also known as Captain's Mistress, Four Up, Plot Four, Find Four, Four in a Row, Four in a Line and Gravitrips (in Soviet Union)) is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs.";
	
	public static String getRulesTxt() {
		return rulesTxt;
	}
}
