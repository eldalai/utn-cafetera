package com.application.exceptions;

/**
 * Esta clase permite representar un error de validacion.
 * 
 * @author Nestor
 *
 */
public class ValidationError {
	private String property;
	private String error;

	public ValidationError(String property, String error) {
		this.property = property;
		this.error = error;
	}
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
