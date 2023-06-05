package com.aventurasoft.bocaplus.data.query;

/**
 * Excepcion arrojada por el DataResolverFacotry cuando no encuentra un dataresolver
 * 
 * @author Julio
 *
 */
public class DataResolverNotFoundException extends Exception {
	public DataResolverNotFoundException()
	{
		super("No DataResolver found");
	}
	
	public DataResolverNotFoundException(String name)
	{
		super("DataResolver '" + name + "' not found");
	}
	
}
