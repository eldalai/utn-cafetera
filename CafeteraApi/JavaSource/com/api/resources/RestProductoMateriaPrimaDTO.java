package com.api.resources;

/**
 * Representa las materias primas que posee un producto.
 * 
 * @author Nestor
 * 
 */
public class RestProductoMateriaPrimaDTO {
	private Integer id;
	private Integer materiaPrimaId;
	private int cantidad;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getMateriaPrimaId() {
		return materiaPrimaId;
	}

	public void setMateriaPrimaId(Integer materiaPrimaId) {
		this.materiaPrimaId = materiaPrimaId;
	}

}
