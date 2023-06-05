/**
 * 
 */
package com.aventurasoft.bocaplus.data.query;


import java.util.ArrayList;
import java.util.List;

/**
 * @author Julio
 *
 */
public class ResultTableDataResolverWriter implements IDataResolverWriter {

	private ResultTable resultTable = new ResultTable();
	private List<Object> dataRow;
	private boolean sessionStarted = false;
	
	/* (non-Javadoc)
	 * @see com.aventurasoft.dataui.dataresolver.IDataResolverWriter#addDestinationColumn(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void addDestinationColumn(String name, DataSetFieldType dataType,
			boolean isAggregable, Integer size) {
		ResultTableColumn column = new ResultTableColumn();
		column.setName(name);
		column.setAggregable(isAggregable);
		column.setDataType(dataType);
		
		resultTable.getColumns().add(column);
	}

	/* (non-Javadoc)
	 * @see com.aventurasoft.dataui.dataresolver.IDataResolverWriter#newLine()
	 */
	@Override
	public void newLine() {
		dataRow = new ArrayList<Object>();

	}

	/* (non-Javadoc)
	 * @see com.aventurasoft.dataui.dataresolver.IDataResolverWriter#addValueToLine(java.lang.Object)
	 */
	@Override
	public void addValueToLine(Object value) {
		if (dataRow==null) this.newLine();
		
		dataRow.add(value);

	}

	/* (non-Javadoc)
	 * @see com.aventurasoft.dataui.dataresolver.IDataResolverWriter#saveLine()
	 */
	@Override
	public void saveLine() throws DataResolverException {
		if (dataRow==null) throw new DataResolverException("No line to add");
		
		resultTable.getElements().add(dataRow);
	}

	public ResultTable getResultTable() {
		return resultTable;
	}

	public void setResultTable(ResultTable resultTable) {
		this.resultTable = resultTable;
	}

	@Override
	public void begingSession() throws DataResolverException {
		this.resultTable = new ResultTable();
		this.sessionStarted = true;
	}

	@Override
	public void endSession() throws DataResolverException {
		this.sessionStarted = false;
		
	}

	@Override
	public boolean isSessionStarted() {

		return this.sessionStarted;
	}


	
}
