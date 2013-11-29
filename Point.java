package project4;

/*****************************************************************
 * A helper Class that contains an ordered pair to be pushed
 * and popped in a stack.
 *
 * @author Brody Berson
 * @version 1.0
 *****************************************************************/
public class Point {
	/** Integer for the row */
	private int row;

	/** Integer for the column */
	private int col;

	/*****************************************************************
	 * Constructor to create a point that can be pushed or popped on
	 * a stack.
	 * 
	 * @param row - int to determine the row
	 * @param col - int to determine the column
	 *****************************************************************/
	public Point(int row, int col) {
		this.row = row;
		this.col = col;
	}

	/*****************************************************************
	 * Can return the current row.
	 * 
	 * @return {@code row} - current row of the point
	 *****************************************************************/
	public int getRow() {
		return row;
	}

	/*****************************************************************
	 * Can return the current column.
	 *  
	 * @return {@code col} - current column of the point
	 *****************************************************************/
	public int getCol() {
		return col;
	}
}
