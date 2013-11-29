package project4;

import java.awt.Color;
import java.io.File;
import java.util.*;
import javax.swing.*;

/*****************************************************************
 * The base game, that the GUI always checks to see what will be
 * displayed by using MVC.
 * 
 * @author Brody Berson
 * @version 1.0
 *****************************************************************/
public class MouseMazeGame {
	/** 2D Array of Cell type to set up the game */
	private Cell[][] mazeBoard;

	/** Stack of Points to be pushed and poped */
	private Stack<Point> stack;

	/** Integer for rows throughout */
	private int rows;

	/** Integer for columns throughout */
	private int columns;

	/** Keeps track of wins */
	private int wins;

	/** Keeps track of losses */
	private int losses;

	/*****************************************************************
	 * Method that sets up a new game according to its parameter by
	 * the size of the board for the GUI to display.
	 * 
	 * @param boardSize - integer to determine the size of the board
	 * @param loadFromText - true if loading from text, or false
	 *****************************************************************/
	public MouseMazeGame(int boardSize, boolean loadFromText) {
		rows = boardSize;
		columns = boardSize;
		mazeBoard = new Cell[rows][columns];
		// has a different start up for loading from text
		if (!loadFromText)
			reset(loadFromText);
	}

	/*****************************************************************
	 * Method that checks to see if the given cell is within the
	 * game board and is valid to be selected.
	 * 
	 * @param row - integer to determine correct row
	 * @param col - integer to determine correct column
	 * @return true - when the given cell is available
	 * @return false - when the given cell is not available
	 *****************************************************************/
	public boolean isAvailable(int row, int col) {
		if ((row>=0 && col >=0 && row <rows && col <columns) &&
				(mazeBoard[row][col].getType() != CellType.WALL) &&
				(mazeBoard[row][col].getType() != CellType.VISITED) &&
				(mazeBoard[row][col].getType() != CellType.PATH))
			return true;
		return false;
	}

	/*****************************************************************
	 * Method that smells the mouse within 3 block radius to help move
	 * the mouse in the "right" direction.
	 * 
	 * @param range - can change how wide the smell range is
	 * @return 1 default or cheese is in in the top right quadrant
	 * @return 2 cheese is in the bottom right quadrant
	 * @return 3 cheese is in the bottom left quadrant
	 * @return 4 cheese is in the top left quadrant
	 *****************************************************************/
	public int smell (int range) {
		int mouseRow = 0;
		int mouseCol = 0;
		// retrieves and sets the row and col to where the Mouse is
		for (int r = 0; r < rows; r++) 
			for (int c = 0; c < columns; c++) 
				if (mazeBoard[r][c].getType() == CellType.MOUSE) {
					mouseRow = r;
					mouseCol = c;
				}

		// will look in the top right quadrant
		for (int r = mouseRow; r < mouseRow+range; r++) 
			for (int c = mouseCol; c < mouseCol+range; c++)
				if (isAvailable(r,c))
					if (mazeBoard[r][c].getType() == CellType.CHEESE)
						return 1;
		// will look in the bottom right quadrant
		for (int r = mouseRow-range; r < mouseRow; r++) 
			for (int c = mouseCol; c < mouseCol+range; c++)
				if (isAvailable(r,c))
					if (mazeBoard[r][c].getType() == CellType.CHEESE)
						return 2;
		// will look in the bottom left quadrant
		for (int r = mouseRow-range; r < mouseRow; r++) 
			for (int c = mouseCol-range; c < mouseCol; c++)
				if (isAvailable(r,c))
					if (mazeBoard[r][c].getType() == CellType.CHEESE)
						return 3;
		// will look in the top left quadrant
		for (int r = mouseRow; r < mouseRow+range; r++) 
			for (int c = mouseCol-range; c < mouseCol; c++)
				if (isAvailable(r,c))
					if (mazeBoard[r][c].getType() == CellType.CHEESE)
						return 4;

		// if nothing is found; default
		return 1;
	}

	/*****************************************************************
	 * Method that steps the mouse along the maze according to if the
	 * mouse can smell it or not.
	 * 
	 * @param smell - tells what set of directions to look
	 *****************************************************************/
	public void step (int smell) {
		int row = 0;
		int col = 0;
		// retrieves and sets the row and col to where the Mouse is
		for (int r = 0; r < rows; r++) 
			for (int c = 0; c < columns; c++) 
				if (mazeBoard[r][c].getType() == CellType.MOUSE) {
					row = r;
					col = c;
				}

		// if mouse can not smell it, default, or in the top right
		if (smell == 1) {
			if (isAvailable(row+1,col)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				row++;
			}
			else if (isAvailable(row,col+1)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				col++;
			}
			else if (isAvailable(row,col-1)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				col--;
			}
			else if (isAvailable(row-1,col)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				row--;
			}
			//will pop backwards if you can not move forward
			else {
				mazeBoard[row][col].setType(CellType.VISITED);
				row = stack.peek().getRow();
				col = stack.pop().getCol();
			}
			mazeBoard[row][col].setType(CellType.MOUSE);
		}
		// smelt in the bottom right quadrant
		else if (smell == 2) {
			if (isAvailable(row-1,col)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				row--;
			}
			else if (isAvailable(row,col+1)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				col++;
			}
			else if (isAvailable(row,col-1)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				col--;
			}
			else if (isAvailable(row+1,col)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				row++;
			}
			//will pop backwards if you can not move forward
			else {
				mazeBoard[row][col].setType(CellType.VISITED);
				row = stack.peek().getRow();
				col = stack.pop().getCol();
			}
			mazeBoard[row][col].setType(CellType.MOUSE);
		}
		// smelt in the bottom left quadrant
		else if (smell == 3) {
			if (isAvailable(row-1,col)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				row--;
			}
			else if (isAvailable(row,col-1)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				col--;
			}
			else if (isAvailable(row,col+1)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				col++;
			}
			else if (isAvailable(row+1,col)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				row++;
			}
			//will pop backwards if you can not move forward
			else {
				mazeBoard[row][col].setType(CellType.VISITED);
				row = stack.peek().getRow();
				col = stack.pop().getCol();
			}
			mazeBoard[row][col].setType(CellType.MOUSE);
		}
		// smelt in the top left quadrant
		else if (smell == 4) {
			if (isAvailable(row+1,col)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				row++;
			}
			else if (isAvailable(row,col-1)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				col--;
			}
			else if (isAvailable(row,col+1)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				col++;
			}
			else if (isAvailable(row-1,col)) {
				stack.push(new Point(row,col));
				mazeBoard[row][col].setType(CellType.PATH);
				row--;
			}
			//will pop backwards if you can not move forward
			else {
				mazeBoard[row][col].setType(CellType.VISITED);
				row = stack.peek().getRow();
				col = stack.pop().getCol();
			}
			mazeBoard[row][col].setType(CellType.MOUSE);
		}
	}

	/*****************************************************************
	 * A helper method that will prepare the game to be wiped clean,
	 * randomly place the walls, cheese, and mouse.
	 * 
	 * @param loadFromText - determines being loaded from a text file
	 *****************************************************************/
	public void reset(boolean loadFromText) {
		stack = new Stack<Point>();
		// Wipes the board clean
		for (int row = 0; row < rows; row++) 
			for (int col = 0; col < columns; col++) 
				mazeBoard[row][col] = new Cell (CellType.OPEN);

		// Only called if you are not loading from a txt file
		if (!loadFromText) {
			// Creates random Walls, Cheese, and Mouse
			randomPlacer(CellType.WALL, rows*3);
			randomPlacer(CellType.CHEESE, 1);
			randomPlacer(CellType.MOUSE, 1);
			update();
		}
	}

	/*****************************************************************
	 * A helper method that will randomly place a CellType,
	 * {@code number} of times
	 * 
	 * @param type - will place whatever CellType you'd like
	 * @param number - number of that CellType to be placed
	 *****************************************************************/
	public void randomPlacer(CellType type, int number) {
		Random r = new Random();
		for (int i = 0; i < number; i++) {
			int row = r.nextInt(rows);
			int col = r.nextInt(columns);
			//if there already something there, retries
			if (mazeBoard[row][col].getType() != CellType.OPEN)
				i--;
			else
				mazeBoard[row][col].setType(type);
		}
	}

	/*****************************************************************
	 * Private Helper method that will set all the colors and icons
	 * which is dependent on the CellType.
	 *****************************************************************/
	public void update() {
		for (int row = 0; row < rows; row++) 
			for (int col = 0; col < columns; col++) {
				if (mazeBoard[row][col].getType() == CellType.CHEESE)
					mazeBoard[row][col].setImage(new ImageIcon(
							"/users/brodyberson/cheese.png"));

				if (mazeBoard[row][col].getType() == CellType.MOUSE)
					mazeBoard[row][col].setImage(new ImageIcon(
							"/users/brodyberson/mouse.png"));

				if (mazeBoard[row][col].getType() == CellType.WALL)
					mazeBoard[row][col].setImage(new ImageIcon(
							"/users/brodyberson/wall.png"));

				if (mazeBoard[row][col].getType() == CellType.VISITED){
					mazeBoard[row][col].setImage(null);
					mazeBoard[row][col].setColor(Color.RED);
				}
				if (mazeBoard[row][col].getType() == CellType.PATH){
					mazeBoard[row][col].setImage(null);
					mazeBoard[row][col].setColor(Color.GREEN);
				}
			}
	}

	/*****************************************************************
	 * Loads the list from the proper formatted text file to populate
	 * a list of accounts.
	 *****************************************************************/
	public void loadFromText(String filename) {
		try {
			// open the data file
			Scanner fileReader = new Scanner (new File(filename));
			Scanner lineReader;
			int boardSize = fileReader.nextInt();
			rows = boardSize;
			columns = boardSize;
			mazeBoard = new Cell[rows][columns];
			reset(true);
			fileReader.nextLine();
			// continue while there is more data to read
			while (fileReader.hasNext()) {
				// read one line of data
				String line = fileReader.nextLine().trim();
				lineReader = new Scanner(line).useDelimiter(",");

				// read the items one at a time
				int row = lineReader.nextInt();
				int col = lineReader.nextInt();
				String type = lineReader.next().trim();
				if (type.equals("c"))
					mazeBoard[row][col].setType(CellType.CHEESE);
				if (type.equals("m"))
					mazeBoard[row][col].setType(CellType.MOUSE);
				if (type.equals("w"))
					mazeBoard[row][col].setType(CellType.WALL);
			}
			fileReader.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "txt Import Failed.",
					"Import As txt", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*****************************************************************
	 * Returns the game board to be displayed in the GUI.
	 * 
	 * @return {@code gameBoard} - 2D Array of all Cell type.
	 *****************************************************************/
	public Cell[][] getDisplay() {
		return mazeBoard;
	}

	/*****************************************************************
	 * Returns the game Stack of Points for the game.
	 * 
	 * @return {@code stack} - ordered pairs called Points
	 *****************************************************************/
	public Stack<Point> getStack(){
		return stack;
	}

	/*****************************************************************
	 * Returns the rows of the game board.
	 * 
	 * @return {@code rows} - rows of game board
	 *****************************************************************/
	public int getRows() {
		return rows;
	}

	/*****************************************************************
	 * Returns the columns of the game board.
	 * 
	 * @return {@code columns} - columns of game board
	 *****************************************************************/
	public int getColumns() {
		return columns;
	}

	/*****************************************************************
	 * Returns the number of consecutive wins in the given session.
	 * 
	 * @return {@code wins} - number of consecutive wins
	 *****************************************************************/
	public int getWins() {
		return wins;
	}

	/*****************************************************************
	 * Returns the number of consecutive losses in the given session.
	 * 
	 * @return {@code losses} - number of consecutive losses
	 *****************************************************************/
	public int getLosses() {
		return losses;
	}

	/*****************************************************************
	 * Adds one win to the win count.
	 *****************************************************************/
	public void addWin() {
		wins++;
	}

	/*****************************************************************
	 * Adds one loss to the loss count.
	 *****************************************************************/
	public void addLoss() {
		losses++;
	}
}
