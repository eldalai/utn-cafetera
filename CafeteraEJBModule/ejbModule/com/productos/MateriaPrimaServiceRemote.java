package com.productos;

import java.util.Collection;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.application.exceptions.BusinessException;
import com.productos.dto.MateriaPrimaDTO;

@Remote
public interface MateriaPrimaServiceRemote {
	/**
	 * Busca una materia prima por su id.
	 * 
	 * @param materiaPrimaId
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public abstract MateriaPrimaDTO findById(Integer materiaPrimaId);

	/**
	 * Lista todas las materias primas de la base de datos.
	 * 
	 * @return
	 */
	public abstract Collection<MateriaPrimaDTO> listAll();

	/**
	 * Agrega una materia prima a la base de datos.
	 * 
	 * @param usuario
	 * @return
	 */
	MateriaPrimaDTO addMateriaPrima(MateriaPrimaDTO usuario);

	/**
	 * Recarga una materia prima.
	 * 
	 * @param materiaPrimaId
	 * @param cantidad
	 */
	void recargarMateriaPrima(Integer materiaPrimaId, int cantidad);

	/**
	 * Consume una cantidad especifica de materia prima.
	 * 
	 * @param materiaPrimaID
	 * @param cantidad
	 */
	void consumirMateriaPrima(Integer materiaPrimaID, int cantidad);

	void updateMateriaPrima(MateriaPrimaDTO materiaPrima) throws BusinessException;

}