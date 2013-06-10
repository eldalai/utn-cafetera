package com.productos.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.productos.entities.MateriaPrima;

/**
 * Abstract Factory para conversion de Etidad a DTO de Materias Primas.
 * 
 * @author Nestor
 * 
 */
public class MateriaPrimaDTOFactory {

	/**
	 * Retorna un ProductoDTO populado con toda la informacion posible.
	 * 
	 * @param producto
	 * @return
	 */
	public static MateriaPrimaDTO getMateriaPrimaDTO(MateriaPrima producto) {
		if(producto == null) {
			return null;
		}
		MateriaPrimaDTO result = new MateriaPrimaDTO();
		result.setCantidadActual(producto.getCantidadActual());
		result.setId(producto.getId());
		result.setNombre(producto.getNombre());
		return result;
	}
	
	public static Collection<MateriaPrimaDTO> getMateriaPrimaDTO(Collection<MateriaPrima> materiasPrimas) {
		if(materiasPrimas == null) {
			return null;
		}

		
		List<MateriaPrimaDTO> retorno = new ArrayList<MateriaPrimaDTO>();
		for(MateriaPrima materiaPrima: materiasPrimas) {
			retorno.add(getMateriaPrimaDTO(materiaPrima));
		}
		return retorno;
	}
}
