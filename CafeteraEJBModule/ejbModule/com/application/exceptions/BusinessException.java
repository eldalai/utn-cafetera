package com.application.exceptions;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.ApplicationException;

/**
 * Representa una ecepcion del negocio. Agrega una propedad que identifica el
 * componente en donde se origina el error. Asi mismo define que es una
 * transaccion que debe realizar rollbak en la transaccion.
 * 
 * @author Nestor
 * 
 */
@ApplicationException(rollback = true)
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = -445890439850367761L;
	private List<ValidationError> errores = new ArrayList<ValidationError>();

	public BusinessException(String componente, String message) {
		super(message);

		errores.add(new ValidationError(componente, message));
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String mensaje, List<ValidationError> errores) {
		super(mensaje);
		if (errores != null) {
			this.errores.addAll(errores);
		}
	}

	public List<ValidationError> getErrores() {
		return errores;
	}

	public void setErrores(List<ValidationError> errores) {
		this.errores = errores;
	}
}
