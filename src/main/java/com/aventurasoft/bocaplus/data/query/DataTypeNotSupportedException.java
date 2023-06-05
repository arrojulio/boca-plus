/**
 * 
 */
package com.aventurasoft.bocaplus.data.query;

/**
 * Excepcion relacionada a un tipo de datos no soportado
 * @author Julio
 *
 */
public class DataTypeNotSupportedException extends RuntimeException {
	public DataTypeNotSupportedException(String message)
	{
		super(message);
	}
}
