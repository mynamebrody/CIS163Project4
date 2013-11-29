package project4;

import java.awt.Color;
import javax.swing.ImageIcon;

/*****************************************************************
 * Class that creates a type cell that will be invoked into a 2D
 * array list to set up the MineSweeper Game.
 *
 * @author Brody Berson
 * @version 1.0
 *****************************************************************/
public class Cell {
	/** Color of Cell */
	private Color color;

	/** Image that can be displayed */
	private ImageIcon image;

	/** The Type of Cell from eNum CellType */
	private CellType type;

	/*****************************************************************
	 * Constructor that will set the color, image and the type of cell.
	 * 
	 * @param type - The type of the cell that will be kept track of
	 *****************************************************************/
	public Cell(CellType type) {
		this.type = type;
	}

	/*****************************************************************\
	 * Returns the current color.
	 * 
	 * @return {@code color} - color for the cell
	 *****************************************************************/
	public Color getColor() {
		return color;
	}
	
	/*****************************************************************
	 * Sets the current color.
	 * 
	 * @param {@code color} to be set for the cell
	 *****************************************************************/
	public void setColor(Color color) {
		this.color = color;
	}
	
	/*****************************************************************
	 * Returns the current image.
	 * 
	 * @return {@code image} - image for the cell
	 *****************************************************************/
	public ImageIcon getImage() {
		return image;
	}
	
	/*****************************************************************
	 * Sets the current image.
	 * 
	 * @param {@code image} to be set for the cell
	 *****************************************************************/
	public void setImage(ImageIcon image) {
		this.image = image;
	}
	
	/*****************************************************************
	 * Returns the current CellType.
	 * 
	 * @return {@code type} - current CellType for the cell
	 *****************************************************************/
	public CellType getType() {
		return type;
	}
	
	/*****************************************************************
	 * Can change the CellType if needed.
	 * 
	 * @param {@code type} to be set or changed for the cell
	 *****************************************************************/
	public void setType(CellType type) {
		this.type = type;
	}
}
