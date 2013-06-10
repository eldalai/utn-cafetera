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
import com.productos.dto.DisponibilidadProductoDTO;
import com.productos.dto.ProductoDTO;
import com.productos.dto.ProductoDTOFactory;
import com.productos.dto.ProductoMateriaPrimaDTO;
import com.productos.entities.MateriaPrima;
import com.productos.entities.Producto;
import com.productos.entities.ProductoMateriaPrima;
import com.productos.repository.MateriaPrimaRepository;
import com.productos.repository.ProductoRepository;

/**
 * Son los productos elaborado por la maquina expendedora
 * 
 * @author Nestor
 * 
 */
@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProductoService implements ProductoServiceRemote {
	@EJB
	private ProductoRepository productoRepository;
	@EJB
	private MateriaPrimaRepository materiaPrimaRepository;
	
	@EJB
	private ProductoServiceValidations validator;

	@EJB
	private MateriaPrimaService materiaPrimaService;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ProductoDTO findById(Integer productoId) {
		return ProductoDTOFactory.getProductoDTO(productoRepository.get(productoId));
	}

	@Override
	public Collection<DisponibilidadProductoDTO> listDisponibilidadAll() {
		return ProductoDTOFactory.getDisponibilidadProductoDTO(validator, productoRepository.getAll());
	}

	@Override
	public Collection<ProductoDTO> listAll() {
		return ProductoDTOFactory.getProductoDTO(productoRepository.getAll());
	}

	
	/**
	 * Obtiene un listado de las materias primas que componen a un producto.
	 * 
	 */
	@Override
	public Collection<ProductoMateriaPrimaDTO> getMateriasPrimasPorProducto(Integer productoId) {
		Producto producto = productoRepository.get(productoId);
		
		return ProductoDTOFactory.getProductoMateriaPrimaDTO(producto.getMateriasPrimas());
	}
	
	/**
	 * Agrega un producto a la base de datos.
	 * 
	 */
	@Override
	public ProductoDTO addProducto(ProductoDTO producto) {
		List<ValidationError> errors = validator.validarAddProducto(producto);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al agregar el producto.", errors);
		}

		Producto productoNuevo = new Producto();
		productoNuevo.setNombre(producto.getNombre());
		productoNuevo.setPrecio(producto.getPrecio());

		validator.validarEntityBean(productoNuevo);

		productoRepository.add(productoNuevo);

		return ProductoDTOFactory.getProductoDTO(productoNuevo);
	}

	/**
	 * Agrega una materia prima a un producto.
	 * 
	 */
	@Override
	public void addMateriaPrima(int productoId, ProductoMateriaPrimaDTO materiaPrima) {
		List<ValidationError> errors = validator.validarAddMateriaPrima(productoId, materiaPrima);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al agregar el producto.", errors);
		}

		MateriaPrima mp = materiaPrimaRepository.get(materiaPrima.getMateriaPrima().getId());
		Producto producto = productoRepository.get(productoId);
		producto.addMateriaPrima(mp, materiaPrima.getCantidad());
	}
	
	/**
	 * Actualiza el precio de un prodcuto.
	 * 
	 */
	@Override
	public void actualizarPrecioProducto(Integer productoId, double nuevoPrecio) {
		List<ValidationError> errors = validator.validarActualizarPrecioProducto(productoId, nuevoPrecio);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al actualizar el precio del producto.", errors);
		}

		Producto p = productoRepository.get(productoId);
		p.setPrecio(nuevoPrecio);

		validator.validarEntityBean(p);
	}

	/**
	 * Consume un producto una vez.
	 * 
	 */
	@Override
	public void consumirProducto(Integer productoId) throws BusinessException {
		List<ValidationError> errors = validator.validarConsumirProducto(productoId);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al consumir el producto.", errors);
		}

		Producto p = productoRepository.get(productoId);
		for (ProductoMateriaPrima pmt : p.getMateriasPrimas()) {
			materiaPrimaService.consumirMateriaPrima(pmt.getMateriaPrima().getId(), pmt.getCantidad());
		}
	}

	/**
	 * Elimina un producto.
	 * 
	 */
	@Override
	public void eliminarProducto(Integer productoId) throws BusinessException {
		List<ValidationError> errors = validator.validarEliminarProducto(productoId);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al eliminar el producto.", errors);
		}

		Producto p = productoRepository.get(productoId);
		productoRepository.remove(p);
	}

	/**
	 * Actualiza la informacion de una materia prima asociada a un producto.
	 * 
	 * @param productoId
	 * @param materiaPrimaEditada
	 */
	public void updateMateriaPrima(Integer productoId, ProductoMateriaPrimaDTO materiaPrimaEditada) {
		List<ValidationError> errors = validator.validarUpdateMateriaPrima(productoId, materiaPrimaEditada);
		if (errors.size() > 0) {
			throw new BusinessException("Errores al consumir el producto.", errors);
		}
		
		Producto p = productoRepository.get(productoId);
		for (ProductoMateriaPrima pmt : p.getMateriasPrimas()) {
			if(pmt.getId() == materiaPrimaEditada.getId()) {
				pmt.setMateriaPrima(materiaPrimaRepository.get(materiaPrimaEditada.getMateriaPrima().getId()));
				pmt.setCantidad(materiaPrimaEditada.getCantidad());
				return;
			}
		}
	}
	
	/**
	 * Elimina una materia prima asociada a un producto.
	 * 
	 * @param productoId
	 * @param materiaPrimaId
	 */
	public void eliminarMateriaPrima(Integer productoId, int materiaPrimaId) {
		Producto p = productoRepository.get(productoId);
		p.removeMateriaPrima(materiaPrimaRepository.get(materiaPrimaId));
	}
}
