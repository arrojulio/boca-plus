/**
 * 
 */
package com.aventurasoft.bocaplus.data.query;

/**
 * Excepcion generica para los data resolvers
 * @author Julio
 *
 */
public class DataResolverException extends RuntimeException {

	public DataResolverException()
	{
		
	}
	public DataResolverException(String message)
	{
		super(message);
	}
}
