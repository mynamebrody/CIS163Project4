package project4;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import java.util.Timer;

/*****************************************************************
 * The Class and panel for MineSweeper to display all the
 * information to the user using a graphic user interface (GUI)
 * using model view control (MVC).
 * 
 * @author Brody Berson
 * @version 1.0
 *****************************************************************/
public class MouseMazeGUI extends JPanel {
	/** AUTO-GENERATED */
	private static final long serialVersionUID = 1L;

	/** GUI frame */
	private JFrame frame;

	/** Creates a new game */
	private MouseMazeGame g;

	/** Creates a new timer to be used to automatically solve */
	private Timer timer;

	/** Label */
	private JLabel label;

	/** Displays Wins */
	private JLabel winCount;

	/** Displays Losses */
	private JLabel loseCount;

	/** Reset Button */
	private JButton resetButton;

	/** Step button */
	private JButton stepButton;

	/** Finish button */
	private JButton finishButton;

	/** Stop Button for the timer */
	private JButton stopButton;

	/** Integer for use of all rows */
	private int rows;

	/** Integer for use of all columns */
	private int columns;

	/** 2D Array of buttons for GUI board */
	private JLabel[][] board;

	/** 2D Array that is ready to call the game board from Game class */
	private Cell[][] gameBoard;

	/** Menu items */
	private JMenuBar menus;
	private JMenu fileMenu;
	private JMenuItem quitItem;
	private JMenuItem newItem;
	private JMenuItem selectItem;

	/*****************************************************************
	 * Main Method to run {@code mainGame}
	 * 
	 * @param args[]
	 ****************************************************************/ 
	public static void main(String args[]){
		mainGame();
	}

	/*****************************************************************
	 * Main Method to run the game and ask for the board size.
	 ****************************************************************/
	public static void mainGame() {
		// size of board
		String board;
		int size = 0;
		// will loop if player makes board size to large or small
		do{
			try {
				board = JOptionPane.showInputDialog(
						"Enter size of board:");
				size = Integer.parseInt(board);
			}

			catch (Exception e)  {
			}
		} while ((size < 4) || (size > 16));
		new MouseMazeGUI(size, false);
	}

	/*****************************************************************
	 * Constructor installs all of the GUI components and sets up
	 * the game board to be called by the main method.
	 * 
	 * @param boardSize - integer to determine the size of the board
	 * @param loadFromText - true if loading from text, or false
	 ****************************************************************/ 
	public MouseMazeGUI(int boardSize, boolean loadFromText){
		// establish the frame
		frame = new JFrame ("Minesweeper");

		rows = boardSize;
		columns = boardSize;

		g = new MouseMazeGame (boardSize, loadFromText);
		board = new JLabel[rows][columns];

		JPanel boardPanel = new JPanel();
		ButtonListener listener = new ButtonListener();
		boardPanel.setLayout(new GridLayout(rows,columns));

		// Creates all JLabels and borders for grid
		for (int row = 0; row < rows; row++) 
			for (int col = 0; col < columns; col++) {
				board[row][col] = new JLabel(null, null,
						SwingConstants.CENTER);
				board[row][col].setOpaque(true);
				board[row][col].setBorder(BorderFactory
						.createLineBorder(Color.black));
				boardPanel.add(board[row][col]);
			}

		// sets up the top panel
		JPanel topPanel = new JPanel();

		winCount = new JLabel ("Wins: " + g.getWins());
		topPanel.add (winCount);

		resetButton = new JButton("Reset");
		resetButton.addActionListener(listener);
		topPanel.add (resetButton);

		stopButton = new JButton("Stop");
		stopButton.addActionListener(listener);
		topPanel.add (stopButton);

		label = new JLabel ("Mouse Maze Game");
		topPanel.add (label);

		stepButton = new JButton("Step");
		stepButton.addActionListener(listener);
		topPanel.add (stepButton);

		finishButton = new JButton("Go");
		finishButton.addActionListener(listener);
		topPanel.add (finishButton);

		loseCount = new JLabel ("Losses: " + g.getLosses());
		topPanel.add (loseCount);

		// set up File menu
		fileMenu = new JMenu("File");
		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(listener);
		newItem = new JMenuItem("New Board");
		newItem.addActionListener(listener);
		selectItem = new JMenuItem("Select Board");
		selectItem.addActionListener(listener);
		fileMenu.add(newItem);
		fileMenu.add(selectItem);
		fileMenu.add(quitItem);
		menus = new JMenuBar();
		frame.setJMenuBar(menus);
		menus.add(fileMenu);

		//grabs the gameBoard and sets up the layout of the frame
		gameBoard = g.getDisplay();
		restartGame();
		frame.add(BorderLayout.CENTER, boardPanel);
		frame.add(BorderLayout.NORTH, topPanel);
		frame.setSize(600,600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	}

	/*****************************************************************
	 * Private helper method to just restart the game class and the
	 * GUI, and updates the win and loss count.
	 ****************************************************************/ 
	private void restartGame() {
		g.reset(false);
		finishButton.setEnabled(true);
		resetButton.setEnabled(true);
		stepButton.setEnabled(true);
		stopButton.setEnabled(false);
		winCount.setText("Wins: " + g.getWins());
		loseCount.setText("Losses: " + g.getLosses());
		update();
	}

	/*****************************************************************
	 * Private helper method to just check to see if there is still
	 * cheese on the board.
	 * 
	 * @return {@code true} if all cheese is found
	 * @return {@code false} if there is still cheese on board
	 ****************************************************************/ 
	private boolean gameOver() {
		for (int row = 0; row < rows; row++) 
			for (int col = 0; col < columns; col++) 
				if (gameBoard[row][col].getType() == CellType.CHEESE)
					return false;
		return true;
	}

	/*****************************************************************
	 * Private helper method to just update all the JLabels with color
	 * and icons to be displayed.
	 ****************************************************************/ 
	private void update() {
		g.update();
		for (int row = 0; row < rows; row++) 
			for (int col = 0; col < columns; col++) {
				board[row][col].setBackground(gameBoard[row][col]
						.getColor());
				board[row][col].setIcon(gameBoard[row][col]
						.getImage());
			}
	}

	/*****************************************************************
	 * Private helper method to auto solve the maze, will continue
	 * until the game is over; you win, or lose.
	 ****************************************************************/
	private void autoRun(){
		timer = new Timer(); 
		solveTask solve = new solveTask();
		timer.schedule(solve,0,130);
	}

	//*****************************************************************
	//  Represents an automatic task to be done using a timer.
	//*****************************************************************
	private class solveTask extends TimerTask{
		public void run() {
			try {
				// range is variable of the boardSize
				g.step(g.smell(rows));
			} catch (EmptyStackException ex) {
				JOptionPane.showMessageDialog(null,
						"Can't Move anymore!");
				g.addLoss();
				restartGame();
				timer.cancel();
			}
			finally {
				update();
				if (gameOver() == true) {
					JOptionPane.showMessageDialog(null,
							"Discovered all the cheese!");
					g.addWin();
					restartGame();
					timer.cancel();
				}
			}
		}
	}

	//*****************************************************************
	//  Represents an action listener for the all the buttons pressed.
	//*****************************************************************
	public class ButtonListener implements ActionListener{
		public void actionPerformed (ActionEvent event){
			// extract the component that was clicked
			JComponent buttonPressed = (JComponent) event.getSource();

			// quits the game
			if (buttonPressed == quitItem)
				System.exit(0);

			// creates a new game with new board size
			if (buttonPressed == newItem) {
				frame.dispose();
				mainGame();
			}

			// creates a new game with new board size
			if (buttonPressed == selectItem) {
				JFileChooser chooser = new JFileChooser(new File("."));
				chooser.setDialogTitle("Import As txt");
				int status = chooser.showOpenDialog(frame);
				if (status == JFileChooser.APPROVE_OPTION) {
					String filename = chooser.getSelectedFile()
							.getAbsolutePath();

					if (filename.contains(".txt")) {
						//only close if right file type
						frame.dispose();
						//determines the boardsize right away
						g.loadFromText(filename);

						//creates new frame game to be displayed
						MouseMazeGUI newG = new MouseMazeGUI(
								g.getColumns(), true);
						newG.g.loadFromText(filename);
						newG.gameBoard = newG.g.getDisplay();
						newG.update();
						JOptionPane.showMessageDialog(null,
								"txt Import Successful..",
								"Import As txt", 
								JOptionPane.INFORMATION_MESSAGE);
					}
					else
						JOptionPane.showMessageDialog(null,
								"txt Import Failed.",
								"Import As txt",
								JOptionPane.ERROR_MESSAGE);
				}
			}

			// creates new game
			if (buttonPressed == resetButton) {
				g.addLoss();
				restartGame();
			}

			// steps the mouse ahead
			if (buttonPressed == stepButton) { 
				try {
					g.step(g.smell(rows));
				} catch (EmptyStackException ex) {
					JOptionPane.showMessageDialog(null,
							"Can't Move anymore");
					g.addLoss();
					restartGame();
				}
				finally {
					update();
					if (gameOver() == true) {
						JOptionPane.showMessageDialog(null,
								"Discovered all the cheese!");
						g.addWin();
						restartGame();
					}
				}
			}

			// will finish the maze
			if (buttonPressed == finishButton) { 
				autoRun();
				finishButton.setEnabled(false);
				resetButton.setEnabled(false);
				stepButton.setEnabled(false);
				stopButton.setEnabled(true);
			}

			// will stop the autoRun
			if (buttonPressed == stopButton) { 
				timer.cancel();
				finishButton.setEnabled(true);
				resetButton.setEnabled(true);
				stepButton.setEnabled(true);
				stopButton.setEnabled(false);
			}
		}
	}
}
