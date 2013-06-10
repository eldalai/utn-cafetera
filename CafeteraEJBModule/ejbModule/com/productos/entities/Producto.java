package com.productos.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * Representa los distintos productos elaborados por la cafetera.
 * 
 * @author Nestor
 * 
 */
@Entity
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private double precio;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "productoId")
	private List<ProductoMateriaPrima> materiasPrimas;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	/**
	 * Recupero todas las materias primas del producto.
	 * 
	 * @return
	 */
	public List<ProductoMateriaPrima> getMateriasPrimas() {
		return materiasPrimas;
	}

	/**
	 * Agrego una materia prima al producto.
	 * 
	 * @param materiaPrima
	 * @param cantidad
	 */
	public void addMateriaPrima(MateriaPrima materiaPrima, int cantidad) {
		if (materiasPrimas == null) {
			materiasPrimas = new ArrayList<ProductoMateriaPrima>();
		}
		for (ProductoMateriaPrima productoMateriaPrima : materiasPrimas) {
			if (productoMateriaPrima.getMateriaPrima().getId() == materiaPrima.getId()) {
				productoMateriaPrima.setCantidad(cantidad);
				return;
			}
		}
		ProductoMateriaPrima newOne = new ProductoMateriaPrima();
		newOne.setCantidad(cantidad);
		newOne.setMateriaPrima(materiaPrima);
		newOne.setProducto(this);
		materiasPrimas.add(newOne);
	}

	/**
	 * Elimino una materia prima del producto.
	 * 
	 * @param materiaPrima
	 */
	public void removeMateriaPrima(MateriaPrima materiaPrima) {
		if (materiasPrimas == null) {
			materiasPrimas = new ArrayList<ProductoMateriaPrima>();
		}
		for (ProductoMateriaPrima productoMateriaPrima : materiasPrimas) {
			if (productoMateriaPrima.getMateriaPrima().getId() == materiaPrima.getId()) {
				materiasPrimas.remove(productoMateriaPrima);
				return;
			}
		}
	}

}
