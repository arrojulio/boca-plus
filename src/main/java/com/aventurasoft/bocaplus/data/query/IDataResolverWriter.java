/**
 * 
 */
package com.aventurasoft.bocaplus.data.query;


/**
 * Interfaz a implementar por los mulktiples destinos de los data resolvers
 * @author Julio
 *
 */
public interface IDataResolverWriter {
	

	
	void begingSession() throws DataResolverException; //Iniciar la sesion antes de comenzar
	
	void addDestinationColumn(String name, DataSetFieldType dataType, boolean isAggregable, Integer size) throws DataTypeNotSupportedException, DataResolverException;
	void newLine() throws DataResolverException;
	void addValueToLine(Object value) throws DataResolverException;
	void saveLine() throws DataResolverException;

	void endSession() throws DataResolverException;	//Siempre se debe finalizar la sesion
	boolean isSessionStarted(); 
	
}
