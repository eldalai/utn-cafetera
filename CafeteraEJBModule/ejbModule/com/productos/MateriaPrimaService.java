package com.productos;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.application.exceptions.BusinessException;
import com.application.exceptions.ValidationError;
import com.productos.dto.MateriaPrimaDTO;
import com.productos.dto.MateriaPrimaDTOFactory;
import com.productos.entities.MateriaPrima;
import com.productos.repository.MateriaPrimaRepository;

/**
 * Son las materias primas que forman al producto.
 * 
 * @author Nestor
 * 
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MateriaPrimaService implements MateriaPrimaServiceRemote {
	@EJB
	private MateriaPrimaRepository materiaPrimaRepository;

	@EJB
	private MateriaPrimaServiceValidations validator;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public MateriaPrimaDTO findById(Integer id) {
		return MateriaPrimaDTOFactory.getMateriaPrimaDTO(materiaPrimaRepository.get(id));
	}

	@Override
	public Collection<MateriaPrimaDTO> listAll() {
		return MateriaPrimaDTOFactory.getMateriaPrimaDTO(materiaPrimaRepository.getAll());
	}

	@Override
	public MateriaPrimaDTO addMateriaPrima(MateriaPrimaDTO materiaPrima) throws BusinessException {
		List<ValidationError> errors = validator.validarAddMateriaPrima(materiaPrima);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al agregar la materia prima.", errors);
		}

		MateriaPrima materiaPrimaNueva = new MateriaPrima();
		materiaPrimaNueva.setCantidadActual(materiaPrima.getCantidadActual());
		materiaPrimaNueva.setNombre(materiaPrima.getNombre());

		validator.validarEntityBean(materiaPrimaNueva);

		materiaPrimaRepository.add(materiaPrimaNueva);

		return MateriaPrimaDTOFactory.getMateriaPrimaDTO(materiaPrimaNueva);
	}

	@Override
	public void updateMateriaPrima(MateriaPrimaDTO materiaPrima) throws BusinessException {
		List<ValidationError> errors = validator.validarUpdateMateriaPrima(materiaPrima);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al agregar la materia prima.", errors);
		}

		MateriaPrima materiaPrimaNueva = materiaPrimaRepository.get(materiaPrima.getId());
		materiaPrimaNueva.setCantidadActual(materiaPrima.getCantidadActual());
		materiaPrimaNueva.setNombre(materiaPrima.getNombre());
		validator.validarEntityBean(materiaPrimaNueva);
	}
	
	@Override
	public void recargarMateriaPrima(Integer materiaPrimaId, int cantidad) throws BusinessException {
		List<ValidationError> errors = validator.validarRecargarMateriaPrima(materiaPrimaId, cantidad);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al recargar la materia prima.", errors);
		}

		MateriaPrima materiaPrima = materiaPrimaRepository.get(materiaPrimaId);
		materiaPrima.setCantidadActual(materiaPrima.getCantidadActual() + cantidad);
	}

	@Override
	public void consumirMateriaPrima(Integer materiaPrimaId, int cantidad) {
		List<ValidationError> errors = validator.validarConsumirMateriaPrima(materiaPrimaId, cantidad);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al consumir la materia prima.", errors);
		}

		MateriaPrima materiaPrima = materiaPrimaRepository.get(materiaPrimaId);
		materiaPrima.setCantidadActual(materiaPrima.getCantidadActual() - cantidad);
	}
}
