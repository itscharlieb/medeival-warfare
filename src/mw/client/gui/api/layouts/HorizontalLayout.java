package mw.client.gui.api.layouts;

import mw.client.gui.api.basics.ObservableWindowComponent;

/**
 * The HorizontalLayout class provides an easier way to deal with GridLayouts that have only 1 row.
 * @author Hugo Kapp
 *
 */
public class HorizontalLayout extends GridLayout {

	private int currentColumn = 0;
	
	/* ========================
	 * 		Constructors
	 * ========================
	 */

	public HorizontalLayout(int x, int y, int height, int columns)
	{
		super(x,y,0,height,1,columns);
	}
	
	/** 
	 * Creates a new HorizontalLayout with a position and a number of columns.
	 * @param x the x coordinate of the new HorizontalLayout
	 * @param y the y coordinate of the new HorizontalLayout
	 * @param columns the number of columns of the new HorizontalLayout
	 */
	public HorizontalLayout(int x, int y, int columns)
	{
		super(x,y,1,columns);
	}

	/**
	 * Creates a new HorizontalLayout with a number of columns. This constructor should only be used if the new
	 * HorizontalLayout is going to be used in another Layout.
	 * @param columns the number of columns of the new HorizontalLayout
	 */
	public HorizontalLayout(int columns)
	{
		this(0,0,columns);
	}
	
	/* ==========================
	 * 		Public methods
	 * ==========================
	 */

	/**
	 * Appends the given ObservableWindowComponent to this HorizontalLayout. This is an easy way to fill in a HorizontalLayout,
	 * but it cannot be used in conjunction with addComponent(ObservableWindowComponent, column).
	 * @param comp the new ObservableWindowComponent to append to this HorizontalLayout
	 * @throws IllegalStateException if the method addComponent(ObservableWindowComponent, column) was used before on this HorizontalLayout
	 */
	public void addComponent(ObservableWindowComponent comp)
	{
		if (currentColumn==-1)
			throw new IllegalStateException("You cannot use addComponent(comp) after having specified a column for a component");
		addComponent(comp, 0, currentColumn);
		currentColumn++;
	}
	
	/**
	 * Adds the given ObservableWindowComponent to this HorizontalLayout, in the given column.
	 * @param comp the new ObservableWindowComponent to append to this HorizontalLayout
	 * @param column the column to add the new ObservableWindowComponent into
	 */
	public void addComponent(ObservableWindowComponent comp, int column)
	{
		addComponent(comp, 0, column);
		currentColumn=-1;
	}

	public void removeComponent(int column)
	{
		super.removeComponent(0, column);
	}
	
	/* ==========================
	 * 		Private methods
	 * ==========================
	 */


	/* ==========================
	 * 		Inherited methods
	 * ==========================
	 */



	/* ========================
	 * 		Static methods
	 * ========================
	 */

}