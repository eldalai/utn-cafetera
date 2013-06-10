package com.admin;

import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.productos.ProductoService;
import com.productos.dto.ProductoDTO;

/**
 * Formulario que muestra la grilla de usuarios.
 * 
 * @author Nestor
 * 
 */
@ManagedBean
@RequestScoped
public class ProductoForm {
	@EJB
	private ProductoService productoService;

	public Collection<ProductoDTO> getProductos() {
		return productoService.listAll();
	}
}
