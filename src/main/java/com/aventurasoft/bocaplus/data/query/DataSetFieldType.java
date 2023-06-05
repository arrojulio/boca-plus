/**
 * 
 */
package com.aventurasoft.bocaplus.data.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * DataSetField Types 
 * 
 * @author Julio
 *
 */
public class DataSetFieldType {
	private static DataSetFieldType _Boolean;
	private static DataSetFieldType _String;
	private static DataSetFieldType _Numeric;
	private static DataSetFieldType _Date;
	private static DataSetFieldType _Money;

	private String type = null;
	
	
	public static List<DataSetFieldType> dataSetFieldTypes()
	{
		List<DataSetFieldType> ret = new ArrayList<DataSetFieldType>();
		ret.add(Boolean());
		ret.add(String());
		ret.add(Numeric());
		ret.add(Date());
		ret.add(Money());
		
		return ret;
	}

	public static boolean isAggregable(DataSetFieldType type)
	{
		if (type == null) return false;
		if (type.equals(Numeric())) return true;
		
		return false;
	}
	
	public static List<DataSetFieldType> getAggregableDataSetFieldTypes()
	{
		List<DataSetFieldType> ret = new ArrayList<DataSetFieldType>();
		ret.add(Numeric());
		
		return ret;
	}
	
	public static DataSetFieldType Boolean()
	{
		if (_Boolean == null)
			try {
				_Boolean = new DataSetFieldType("Boolean");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return _Boolean;
		
	}
	public static DataSetFieldType String()
	{
		if (_String == null)
			try {
				_String = new DataSetFieldType("String");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return _String;
	}
	public static DataSetFieldType Numeric()
	{
		if (_Numeric == null)
			try {
				_Numeric = new DataSetFieldType("Numeric");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return _Numeric;
	}
	public static DataSetFieldType Date()
	{
		if (_Date == null)
			try {
				_Date = new DataSetFieldType("Date");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return _Date;
	}
	public static DataSetFieldType Money()
	{
		if (_Money == null)
			try {
				_Money = new DataSetFieldType("Money");
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		return _Money;
	}
	
	@JsonCreator
	public DataSetFieldType(@JsonProperty("type") String type) throws Exception
	{
		this.setType(type);
	}
	
	public static DataSetFieldType getDataSetFieldType(String type) throws DataSetFieldTypeException
	{
		for (DataSetFieldType fieldType : dataSetFieldTypes())
			if (fieldType.getType().toUpperCase().equals(type.toUpperCase()))
				return fieldType;
		throw new DataSetFieldTypeException("Cannot find related DataFieldType of " + type);
	}

	public String getType() {
		return type;
	}

	private void setType(String type) throws Exception {
		
		if (type == null) throw new Exception("Null type");
		
		this.type = type;
	}
	
	public DataSetFieldType getDataSetType(String type)
	{
		for (DataSetFieldType fieldType : dataSetFieldTypes())
			if (fieldType.getType().toUpperCase().equals(type))
				return fieldType;
		return null;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (obj instanceof DataSetFieldType)
		{
			if (((DataSetFieldType) obj).getType().toUpperCase().equals(this.getType().toUpperCase())) return true;
			
		} else if (obj instanceof String)
			if (((String) obj).toUpperCase().equals(this.getType().toUpperCase())) return true;
		
		return false;
	}
	
	@Override
	public String toString()
	{
		return this.getType();
	}
}
