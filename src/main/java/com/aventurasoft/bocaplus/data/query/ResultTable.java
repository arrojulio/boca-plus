/**
 * 
 */
package com.aventurasoft.bocaplus.data.query;


import java.util.ArrayList;
import java.util.List;


/**
 * Contiene una tabla de datos 
 * 
 * @author Julio
 *
 */
public class ResultTable {
	
	public interface FormatElement 
	{
		String formatValue(Object value, int col);
	}
	
	private FormatElement formatter = new FormatElement() {
		
		@Override
		public String formatValue(Object value, int col) {
			
			return value.toString();
		}
	};
	
	//TODO: incorporar informacion de metada data
	
	private List<ResultTableColumn> columns;
	private List<List<Object>> elements;	//rows
	
	public ResultTable()
	{
		columns = new ArrayList<ResultTableColumn>();
		elements = new ArrayList<List<Object>>();
		
	}

	public List<ResultTableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ResultTableColumn> columns) {
		this.columns = columns;
	}

	public List<List<Object>> getElements() {
		return elements;
	}

	private void setElements(List<List<Object>> elements) {
		this.elements = elements;
	}
	
	
	
	@Override
	public String toString()
	{
		String title = "";
		for (int i=0; i<this.columns.size(); i++)
		{
			title += this.columns.get(i).getName();
			if (i+1<this.columns.size())
				title += "\t";
			else
				title += "\n";
		}
		
		String sep = "";
		for (int i=0;i<title.length();i++)
			sep += "-";
		sep += "\n";
		
		String data = "";
		for (int row=0; row<this.elements.size(); row++)
		{
			List<Object> aRow = this.elements.get(row);
			for (int col=0; col<aRow.size(); col++)
			{
				if (aRow.get(col)==null)
					data += "null";
				else
					data += this.formatter.formatValue(aRow.get(col), col);
				
				if(col+1<aRow.size())
					data += "\t";
				else
					data += "\n";
			}
		}
		
		return title + sep + data;
		
	}


	public void setToStringFormatter(FormatElement formatter) {
		this.formatter = formatter;
	}
	
	

}
