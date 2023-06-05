/**
 * 
 */
package com.aventurasoft.bocaplus.data.query;

/**
 * Contiene informacion acerca de la columna de resultado
 * @author Julio
 *
 */
public class ResultTableColumn {

	private String name;
	//javaClass string	. ResultSetConverter.getJavaClass
	private DataSetFieldType dataType;	//TODO: revisar si no debe ser DataSetType
	private boolean aggregable;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DataSetFieldType getDataType() {
		return dataType;
	}
	public void setDataType(DataSetFieldType dataType) {
		this.dataType = dataType;
	}
	public boolean isAggregable() {
		return aggregable;
	}
	public void setAggregable(boolean aggregable) {
		this.aggregable = aggregable;
	}
	public boolean isDate()
	{
		return (getDataType() == DataSetFieldType.Date());

	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == null) return false;
		
		if (other instanceof ResultTableColumn)
		{
			return (this.getName().equals(((ResultTableColumn) other).getName()));
		} else
		{
			return false;
		}
	}
}
