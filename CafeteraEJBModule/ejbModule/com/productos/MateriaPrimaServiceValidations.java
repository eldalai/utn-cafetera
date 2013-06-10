package com.productos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

import com.application.exceptions.BusinessException;
import com.application.exceptions.ValidationError;
import com.productos.dto.MateriaPrimaDTO;
import com.productos.entities.MateriaPrima;
import com.productos.repository.MateriaPrimaRepository;

@Stateless
@LocalBean
public class MateriaPrimaServiceValidations {
	@EJB
	MateriaPrimaRepository materiaPrimaRepository;

	/**
	 * Valida la recarga de materias primas en la maquina
	 * 
	 * @param materiaPrimaId
	 * @param cantidad
	 * @return
	 * @throws BusinessException
	 */
	public List<ValidationError> validarRecargarMateriaPrima(Integer materiaPrimaId, int cantidad) throws BusinessException {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		MateriaPrima materiaPrima = materiaPrimaRepository.get(materiaPrimaId);
		if (materiaPrima == null) {
			errors.add(new ValidationError("materiaPrimaId", "La materia prima indicada no existe en la base de datos."));
		}

		if (cantidad < 0) {
			errors.add(new ValidationError("cantidad", "No se puede cargar la cantidad indicada."));
		}

		return errors;
	}

	/**
	 * Valida consumir materia prima.
	 * 
	 * @param materiaPrimaId
	 * @param cantidad
	 * @return
	 */
	public List<ValidationError> validarConsumirMateriaPrima(Integer materiaPrimaId, int cantidad) {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		MateriaPrima materiaPrima = materiaPrimaRepository.get(materiaPrimaId);
		if (materiaPrima == null) {
			errors.add(new ValidationError("id", "La materia prima indicada no existe en la base de datos."));
			return errors;
		}

		if (cantidad < 0 || cantidad > materiaPrima.getCantidadActual()) {
			errors.add(new ValidationError("cantidad", "No se puede consumir la cantidad indicada."));
		}

		return errors;

	}

	/**
	 * Valida agregar materia primas nuevas.
	 * 
	 * @param mp
	 */
	public List<ValidationError> validarAddMateriaPrima(MateriaPrimaDTO mp) {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		if (mp.getId() != null) {
			MateriaPrima materiaPrima = materiaPrimaRepository.get(mp.getId());
			if (materiaPrima != null) {
				errors.add(new ValidationError("id", "Ya existe la materia prima indicada."));
			}
		}

		if (mp.getNombre() == null || mp.getNombre().length() == 0) {
			errors.add(new ValidationError("nombre", "Debe especificar un nombre para la materia prima."));
		}

		if (mp.getCantidadActual() <= 0) {
			errors.add(new ValidationError("CantidadActual", "La cantidad actual no puede ser negativa."));
		}

		return errors;
	}

	/**
	 * Valida los constraints del entity bean, estas validaciones son internas
	 * del servicio.
	 * 
	 * @param materiaPrima
	 * @throws ConstraintViolationException
	 */
	public void validarEntityBean(MateriaPrima materiaPrima) throws ConstraintViolationException {
		Set<ConstraintViolation<MateriaPrima>> errors = Validation.buildDefaultValidatorFactory().getValidator().validate(materiaPrima);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException("Errores de validacion", new HashSet<ConstraintViolation<?>>(errors));
		}
	}

	public List<ValidationError> validarUpdateMateriaPrima(MateriaPrimaDTO mp) {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		if (mp == null || mp.getId() == null) {
			errors.add(new ValidationError("id", "No existe la materia prima indicada."));
			return errors;
		}
		
		MateriaPrima materiaPrima = materiaPrimaRepository.get(mp.getId());
		if (materiaPrima == null) {
			errors.add(new ValidationError("id", "No existe la materia prima indicada."));
		}

		if (mp.getNombre() == null || mp.getNombre().length() == 0) {
			errors.add(new ValidationError("nombre", "Debe especificar un nombre para la materia prima."));
		}

		if (mp.getCantidadActual() <= 0) {
			errors.add(new ValidationError("CantidadActual", "La cantidad actual no puede ser negativa."));
		}

		return errors;
	}
}
