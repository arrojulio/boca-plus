package com.aventurasoft.bocaplus.data.query;

import java.math.BigDecimal;
import java.sql.*;


public class ResultSetConverter {

	
	public static int getcolumnPosition(ResultSetMetaData rsmd, String column) throws SQLException, ColumnNotFound
	{
		for (int i=1; i <= rsmd.getColumnCount(); i++)
		{
			if (rsmd.getColumnLabel(i).toUpperCase().equals(column.toUpperCase()))
				return i;
		}
		for (int i=1; i <= rsmd.getColumnCount(); i++)
		{
			if (rsmd.getColumnName(i).toUpperCase().equals(column.toUpperCase()))
				return i;
		}
		
		throw new ColumnNotFound(column);
		
	}
	
	public static boolean isNumeric(ResultSetMetaData rsmd, int i) throws SQLException
	{
		 if (rsmd.getColumnType(i) == Types.ARRAY) {
	            return false;
	        } else if (rsmd.getColumnType(i) == Types.BIGINT) {
	        	return true;
	        } else if (rsmd.getColumnType(i) == Types.REAL) {
	        	return true;
	        } else if (rsmd.getColumnType(i) == Types.BOOLEAN) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.BLOB) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.DOUBLE) {
	        	return true;
	        } else if (rsmd.getColumnType(i) == Types.FLOAT) {
	        	return true;
	        } else if (rsmd.getColumnType(i) == Types.INTEGER) {
	        	return true;
	        } else if (rsmd.getColumnType(i) == Types.NVARCHAR) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.VARCHAR) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.CHAR) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.NCHAR) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.LONGNVARCHAR) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.LONGVARCHAR) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.TINYINT) {
	        	return true;
	        } else if (rsmd.getColumnType(i) == Types.SMALLINT) {
	        	return true;
	        } else if (rsmd.getColumnType(i) == Types.DATE) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.TIME) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.TIMESTAMP) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.BINARY) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.VARBINARY) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.LONGVARBINARY) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.BIT) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.CLOB) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.NUMERIC) {
	        	return true;
	        } else if (rsmd.getColumnType(i) == Types.DECIMAL) {
	        	return true;
	        } else if (rsmd.getColumnType(i) == Types.DATALINK) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.REF) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.STRUCT) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.DISTINCT) {
	        	return false;
	        } else if (rsmd.getColumnType(i) == Types.JAVA_OBJECT) {
	        	return false;
	        } else {
	        	return false;
	        }
	}
	
	public static Class getJavaClass(ResultSetMetaData rsmd, int i) throws SQLException, DataTypeNotSupportedException
	{
		if (rsmd.getColumnType(i) == Types.ARRAY) {
            return Array.class;
        } else if (rsmd.getColumnType(i) == Types.BIGINT) {
        	return long.class;
        } else if (rsmd.getColumnType(i) == Types.REAL) {
        	return float.class;
        } else if (rsmd.getColumnType(i) == Types.BOOLEAN) {
        	return boolean.class;
        } else if (rsmd.getColumnType(i) == Types.BLOB) {
        	return Blob.class;
        } else if (rsmd.getColumnType(i) == Types.DOUBLE) {
        	return double.class;
        } else if (rsmd.getColumnType(i) == Types.FLOAT) {
        	return float.class;
        } else if (rsmd.getColumnType(i) == Types.INTEGER) {
        	return int.class;
        } else if (rsmd.getColumnType(i) == Types.NVARCHAR) {
        	return String.class;
        } else if (rsmd.getColumnType(i) == Types.VARCHAR) {
        	return String.class;
        } else if (rsmd.getColumnType(i) == Types.CHAR) {
        	return String.class;
        } else if (rsmd.getColumnType(i) == Types.NCHAR) {
        	return String.class;
        } else if (rsmd.getColumnType(i) == Types.LONGNVARCHAR) {
        	return String.class;
        } else if (rsmd.getColumnType(i) == Types.LONGVARCHAR) {
        	return String.class;
        } else if (rsmd.getColumnType(i) == Types.TINYINT) {
        	return int.class;
        } else if (rsmd.getColumnType(i) == Types.SMALLINT) {
        	return int.class;
        } else if (rsmd.getColumnType(i) == Types.DATE) {
        	return Date.class;
        } else if (rsmd.getColumnType(i) == Types.TIME) {
        	return Time.class;
        } else if (rsmd.getColumnType(i) == Types.TIMESTAMP) {
        	return Timestamp.class;
        } else if (rsmd.getColumnType(i) == Types.BINARY) {
        	return byte[].class;
        } else if (rsmd.getColumnType(i) == Types.VARBINARY) {
        	return byte[].class;
        } else if (rsmd.getColumnType(i) == Types.LONGVARBINARY) {
        	return byte[].class;
        } else if (rsmd.getColumnType(i) == Types.BIT) {
        	return boolean.class;
        } else if (rsmd.getColumnType(i) == Types.CLOB) {
        	return Clob.class;
        } else if (rsmd.getColumnType(i) == Types.NUMERIC) {
        	return BigDecimal.class;
        } else if (rsmd.getColumnType(i) == Types.DECIMAL) {
        	return BigDecimal.class;
        } else if (rsmd.getColumnType(i) == Types.DATALINK) {
        	throw new DataTypeNotSupportedException("DATALINK type not supported");
        } else if (rsmd.getColumnType(i) == Types.REF) {
        	return Ref.class;
        } else if (rsmd.getColumnType(i) == Types.STRUCT) {
        	return SQLData.class;
        } else if (rsmd.getColumnType(i) == Types.DISTINCT) {
        	throw new DataTypeNotSupportedException("DISTINCT type not supported");
        } else if (rsmd.getColumnType(i) == Types.JAVA_OBJECT) {
        	throw new DataTypeNotSupportedException("JAVA_OBJECT type not supported");
        } else {
        	throw new DataTypeNotSupportedException("Data type (" + rsmd.getColumnType(i) + ") not supported");
        }
	}

	public static String sqlTypeName(int sourceTypeInt)
	{
		if (sourceTypeInt == Types.ARRAY) {
			return "ARRAY";
		} else if (sourceTypeInt == Types.BIGINT) {
			return "BIGINT";
		} else if (sourceTypeInt == Types.REAL) {
			return "REAL";
		} else if (sourceTypeInt == Types.BOOLEAN) {
			return "BOOLEAN";
		} else if (sourceTypeInt == Types.BLOB) {
			return "BLOB";
		} else if (sourceTypeInt == Types.DOUBLE) {
			return "DOUBLE";
		} else if (sourceTypeInt == Types.FLOAT) {
			return "FLOAT";
		} else if (sourceTypeInt == Types.INTEGER) {
			return "INTEGER";
		} else if (sourceTypeInt == Types.NVARCHAR) {
			return "NVARCHAR";
		} else if (sourceTypeInt == Types.VARCHAR) {
			return "VARCHAR";
		} else if (sourceTypeInt == Types.CHAR) {
			return "CHAR";
		} else if (sourceTypeInt == Types.NCHAR) {
			return "NCHAR";
		} else if (sourceTypeInt == Types.LONGNVARCHAR) {
			return "LONGNVARCHAR";
		} else if (sourceTypeInt == Types.LONGVARCHAR) {
			return "LONGVARCHAR";
		} else if (sourceTypeInt == Types.TINYINT) {
			return "TINYINT";
		} else if (sourceTypeInt == Types.SMALLINT) {
			return "SMALLINT";
		} else if (sourceTypeInt == Types.DATE) {
			return "DATE";
		} else if (sourceTypeInt == Types.TIME) {
			return "TIME";
		} else if (sourceTypeInt == Types.TIMESTAMP) {
			return "TIMESTAMP";
		} else if (sourceTypeInt == Types.BINARY) {
			return "BINARY";
		} else if (sourceTypeInt == Types.VARBINARY) {
			return "VARBINARY";
		} else if (sourceTypeInt == Types.LONGVARBINARY) {
			return "LONGVARBINARY";
		} else if (sourceTypeInt == Types.BIT) {
			return "BIT";
		} else if (sourceTypeInt == Types.CLOB) {
			return "CLOB";
		} else if (sourceTypeInt == Types.NUMERIC) {
			return "NUMERIC";
		} else if (sourceTypeInt == Types.DECIMAL) {
			return "DECIMAL";
		} else if (sourceTypeInt == Types.DATALINK) {
			return "DATALINK";
		} else if (sourceTypeInt == Types.REF) {
			return "REF";
		} else if (sourceTypeInt == Types.STRUCT) {
			return "STRUCT";
		} else if (sourceTypeInt == Types.DISTINCT) {
			return "DISTINCT";
		} else if (sourceTypeInt == Types.JAVA_OBJECT) {
			return "JAVA_OBJECT";
		}

		throw new RuntimeException("SQL Data type not found (" + sourceTypeInt);
	}
	public static DataSetFieldType suggestDataFieldType(int sourceTypeInt) throws DataTypeNotSupportedException {
		
		
		if (sourceTypeInt == Types.ARRAY) {
            return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.BIGINT) {
        	return DataSetFieldType.Numeric();
        } else if (sourceTypeInt == Types.REAL) {
        	return DataSetFieldType.Numeric();
        } else if (sourceTypeInt == Types.BOOLEAN) {
        	return DataSetFieldType.Boolean();
        } else if (sourceTypeInt == Types.BLOB) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.DOUBLE) {
        	return DataSetFieldType.Numeric();
        } else if (sourceTypeInt == Types.FLOAT) {
        	return DataSetFieldType.Numeric();
        } else if (sourceTypeInt == Types.INTEGER) {
        	return DataSetFieldType.Numeric();
        } else if (sourceTypeInt == Types.NVARCHAR) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.VARCHAR) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.CHAR) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.NCHAR) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.LONGNVARCHAR) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.LONGVARCHAR) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.TINYINT) {
        	return DataSetFieldType.Numeric();
        } else if (sourceTypeInt == Types.SMALLINT) {
        	return DataSetFieldType.Numeric();
        } else if (sourceTypeInt == Types.DATE) {
        	return DataSetFieldType.Date();
        } else if (sourceTypeInt == Types.TIME) {
        	return DataSetFieldType.Date();
        } else if (sourceTypeInt == Types.TIMESTAMP) {
        	return DataSetFieldType.Date();
        } else if (sourceTypeInt == Types.BINARY) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.VARBINARY) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.LONGVARBINARY) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.BIT) {
        	return DataSetFieldType.Boolean();
        } else if (sourceTypeInt == Types.CLOB) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.NUMERIC) {
        	return DataSetFieldType.Numeric();
        } else if (sourceTypeInt == Types.DECIMAL) {
        	return DataSetFieldType.Numeric();
        } else if (sourceTypeInt == Types.DATALINK) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.REF) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.STRUCT) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.DISTINCT) {
        	return DataSetFieldType.String();
        } else if (sourceTypeInt == Types.JAVA_OBJECT) {
        	return DataSetFieldType.String();
        } 
        	
		throw new DataTypeNotSupportedException("Type not supported");
        
		
	}


	public static DataSetFieldType getDataSetFieldType(ResultSetMetaData rsmd, int i) throws SQLException, DataTypeNotSupportedException
	{
		return suggestDataFieldType(rsmd.getColumnType(i));
        
	}
	public static Object getDataTyped(ResultSet rs, ResultSetMetaData rsmd, int i) throws SQLException
	{
		String column_name = rsmd.getColumnName(i);
		
        if (rsmd.getColumnType(i) == Types.ARRAY) {
            return rs.getArray(column_name);
        } else if (rsmd.getColumnType(i) == Types.BIGINT) {
        	return rs.getLong(column_name);
        } else if (rsmd.getColumnType(i) == Types.REAL) {
        	return rs.getFloat(column_name);
        } else if (rsmd.getColumnType(i) == Types.BOOLEAN) {
        	return rs.getBoolean(column_name);
        } else if (rsmd.getColumnType(i) == Types.BLOB) {
        	return rs.getBlob(column_name);
        } else if (rsmd.getColumnType(i) == Types.DOUBLE) {
        	return rs.getDouble(column_name);
        } else if (rsmd.getColumnType(i) == Types.FLOAT) {
        	return rs.getDouble(column_name);
        } else if (rsmd.getColumnType(i) == Types.INTEGER) {
        	return rs.getInt(column_name);
        } else if (rsmd.getColumnType(i) == Types.NVARCHAR) {
        	return rs.getNString(column_name);
        } else if (rsmd.getColumnType(i) == Types.VARCHAR) {
        	return rs.getString(column_name);
        } else if (rsmd.getColumnType(i) == Types.CHAR) {
        	return rs.getString(column_name);
        } else if (rsmd.getColumnType(i) == Types.NCHAR) {
        	return rs.getNString(column_name);
        } else if (rsmd.getColumnType(i) == Types.LONGNVARCHAR) {
        	return rs.getNString(column_name);
        } else if (rsmd.getColumnType(i) == Types.LONGVARCHAR) {
        	return rs.getString(column_name);
        } else if (rsmd.getColumnType(i) == Types.TINYINT) {
        	return rs.getByte(column_name);
        } else if (rsmd.getColumnType(i) == Types.SMALLINT) {
        	return rs.getShort(column_name);
        } else if (rsmd.getColumnType(i) == Types.DATE) {
        	return rs.getDate(column_name);
        } else if (rsmd.getColumnType(i) == Types.TIME) {
        	return rs.getTime(column_name);
        } else if (rsmd.getColumnType(i) == Types.TIMESTAMP) {
        	return rs.getTimestamp(column_name);
        } else if (rsmd.getColumnType(i) == Types.BINARY) {
        	return rs.getBytes(column_name);
        } else if (rsmd.getColumnType(i) == Types.VARBINARY) {
        	return rs.getBytes(column_name);
        } else if (rsmd.getColumnType(i) == Types.LONGVARBINARY) {
        	return rs.getBinaryStream(column_name);
        } else if (rsmd.getColumnType(i) == Types.BIT) {
        	return rs.getBoolean(column_name);
        } else if (rsmd.getColumnType(i) == Types.CLOB) {
        	return rs.getClob(column_name);
        } else if (rsmd.getColumnType(i) == Types.NUMERIC) {
        	return rs.getBigDecimal(column_name);
        } else if (rsmd.getColumnType(i) == Types.DECIMAL) {
        	return rs.getBigDecimal(column_name);
        } else if (rsmd.getColumnType(i) == Types.DATALINK) {
        	return rs.getURL(column_name);
        } else if (rsmd.getColumnType(i) == Types.REF) {
        	return rs.getRef(column_name);
        } else if (rsmd.getColumnType(i) == Types.STRUCT) {
        	return rs.getObject(column_name); // must be a custom mapping consists of a class that implements the interface SQLData and an entry in a java.util.Map object.
        } else if (rsmd.getColumnType(i) == Types.DISTINCT) {
        	return rs.getObject(column_name); // must be a custom mapping consists of a class that implements the interface SQLData and an entry in a java.util.Map object.
        } else if (rsmd.getColumnType(i) == Types.JAVA_OBJECT) {
        	return rs.getObject(column_name);
        } else {
        	return rs.getString(i);
        }
	}
}