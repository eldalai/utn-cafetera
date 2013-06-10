package com.productos.dto;

/**
 * Representa las materias primas que posee un producto.
 * 
 * @author Nestor
 * 
 */
public class ProductoMateriaPrimaDTO {
	private Integer id;
	private MateriaPrimaDTO materiaPrima = new MateriaPrimaDTO();
	private int cantidad;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MateriaPrimaDTO getMateriaPrima() {
		return materiaPrima;
	}

	public void setMateriaPrima(MateriaPrimaDTO materiaPrima) {
		this.materiaPrima = materiaPrima;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
