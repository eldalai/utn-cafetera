package com.productos;

import java.util.Collection;

import javax.ejb.Remote;

import com.application.exceptions.BusinessException;
import com.productos.dto.DisponibilidadProductoDTO;
import com.productos.dto.ProductoDTO;
import com.productos.dto.ProductoMateriaPrimaDTO;

@Remote
public interface ProductoServiceRemote {

	/**
	 * Busca un producto por su id.
	 * 
	 * @param productoId
	 * @return
	 */
	ProductoDTO findById(Integer productoId);

	/**
	 * Devuelve un listado de todos los productos.
	 * 
	 * @return
	 */
	Collection<ProductoDTO> listAll();

	/**
	 * Agrega un producto nuevo.
	 * 
	 * @param producto
	 * @return
	 */
	ProductoDTO addProducto(ProductoDTO producto);

	/**
	 * Consume un producto una vez decrementando la cantidad de materias primas que consume..
	 * 
	 * @param productoId
	 * @throws BusinessException
	 */
	void consumirProducto(Integer productoId) throws BusinessException;

	/**
	 * Actualiza el precio del producto.
	 * 
	 * @param productoId
	 * @param nuevoPrecio
	 */
	void actualizarPrecioProducto(Integer productoId, double nuevoPrecio);

	/**
	 * 
	 * Elimina un producto.
	 * @param productoId
	 * @throws BusinessException
	 */
	void eliminarProducto(Integer productoId) throws BusinessException;

	/**
	 * Lista los productos disponibles.
	 * 
	 * @return
	 */
	Collection<DisponibilidadProductoDTO> listDisponibilidadAll();

	
	/***
	 * Recupera un listado de materias primas que posee el producto.
	 * 
	 * @param productoId
	 * @return
	 */
	Collection<ProductoMateriaPrimaDTO> getMateriasPrimasPorProducto(Integer productoId);

	/**
	 * Agrega una materia prima a un producto.
	 * 
	 * @param productoId
	 * @param materiaPrima
	 */
	void addMateriaPrima(int productoId, ProductoMateriaPrimaDTO materiaPrima);

}
