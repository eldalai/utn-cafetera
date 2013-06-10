package com.admin;

import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.productos.MateriaPrimaService;
import com.productos.dto.MateriaPrimaDTO;

/**
 * Formulario que muestra la grilla de usuarios.
 * 
 * @author Nestor
 *
 */
@ManagedBean
@RequestScoped
public class MateriaPrimaForm {
	@EJB
	private MateriaPrimaService materiaPrimaService;

	public Collection<MateriaPrimaDTO> getMateriasPrimas() {
		return materiaPrimaService.listAll();
	}
}
