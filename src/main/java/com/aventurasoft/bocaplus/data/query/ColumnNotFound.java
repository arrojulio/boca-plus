/**
 * 
 */
package com.aventurasoft.bocaplus.data.query;

/**
 * @author Julio
 *
 */
public class ColumnNotFound extends Exception {
	private String column;
	
	
	public String getColumn() {
		return column;
	}


	public void setColumn(String column) {
		this.column = column;
	}


	public ColumnNotFound(String column)
	{
		super("Column " + column + " not found");
	}
}
