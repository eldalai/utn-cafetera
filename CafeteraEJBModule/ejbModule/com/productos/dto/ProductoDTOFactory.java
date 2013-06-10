package com.productos.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.productos.ProductoServiceValidations;
import com.productos.entities.Producto;
import com.productos.entities.ProductoMateriaPrima;

/**
 * Abstract Factory para conversion de Etidad a DTO de Producto.
 * 
 * @author Nestor
 * 
 */
public class ProductoDTOFactory {
	
	/**
	 * Retorna un ProductoDTO populado con toda la informacion posible.
	 * 
	 * @param producto
	 * @return
	 */
	public static ProductoDTO getProductoDTO(Producto producto) {
		if(producto == null) {
			return null;
		}

		ProductoDTO result = new ProductoDTO();
		result.setPrecio(producto.getPrecio());
		result.setId(producto.getId());
		result.setNombre(producto.getNombre());
		return result;
	}
	public static Collection<ProductoDTO> getProductoDTO(Collection<Producto> productos) {
		if(productos == null) {
			return null;
		}

		
		List<ProductoDTO> retorno = new ArrayList<ProductoDTO>();
		for(Producto producto: productos) {
			retorno.add(getProductoDTO(producto));
		}
		return retorno;
	}
	/**
	 * Retorna un ProductoDTO populado con la informacion de disponiblidad para conusmir.
	 * 
	 * @param producto
	 * @return
	 */
	public static DisponibilidadProductoDTO getDisponibilidadProductoDTO(ProductoServiceValidations validator, Producto producto) {
		DisponibilidadProductoDTO result = new DisponibilidadProductoDTO();
		result.setPrecio(producto.getPrecio());
		result.setId(producto.getId());
		result.setNombre(producto.getNombre());
		result.setDisponible(validator.validarConsumirProducto(producto.getId()).size() == 0);
		return result;
	}
	public static Collection<DisponibilidadProductoDTO> getDisponibilidadProductoDTO(ProductoServiceValidations validator, Collection<Producto> productos) {
		List<DisponibilidadProductoDTO> retorno = new ArrayList<DisponibilidadProductoDTO>();
		for(Producto producto: productos) {
			retorno.add(getDisponibilidadProductoDTO(validator, producto));
		}
		return retorno;
	}
	
	/**
	 * Retorna un productoMateriaPrima con toda la informacion relacionada.
	 * 
	 * @param pmp
	 * @return
	 */
	public static ProductoMateriaPrimaDTO getProductoMateriaPrimaDTO(ProductoMateriaPrima pmp) {
		if(pmp == null) {
			return null;
		}
		
		ProductoMateriaPrimaDTO result = new ProductoMateriaPrimaDTO();
		result.setCantidad(pmp.getCantidad());
		result.setId(pmp.getId());
		result.setMateriaPrima(MateriaPrimaDTOFactory.getMateriaPrimaDTO(pmp.getMateriaPrima()));
		return result;
	}
	
	public static Collection<ProductoMateriaPrimaDTO> getProductoMateriaPrimaDTO(Collection<ProductoMateriaPrima> materiasPrimas) {
		if(materiasPrimas == null) {
			return null;
		}
		
		List<ProductoMateriaPrimaDTO> retorno = new ArrayList<ProductoMateriaPrimaDTO>();
		for(ProductoMateriaPrima materiaPrima: materiasPrimas) {
			retorno.add(getProductoMateriaPrimaDTO(materiaPrima));
		}
		return retorno;
	}
}
