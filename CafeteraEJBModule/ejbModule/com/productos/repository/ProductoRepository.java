package com.productos.repository;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.application.repository.Repository;
import com.productos.entities.Producto;

/**
 * Repositorio de Almacenamiento de Productos del Sistema.
 * 
 * @author Nestor
 * 
 */
@Stateless
@LocalBean
public class ProductoRepository implements Repository<Integer, Producto> {
	@PersistenceContext(unitName = "cafeteraDS")
	private EntityManager entityManager;

	@Override
	public void add(Producto newOne) {
		entityManager.persist(newOne);
	}

	@Override
	public void remove(Producto toDelete) {
		entityManager.remove(toDelete);
	}

	@Override
	public Producto get(Integer id) {
		return entityManager.find(Producto.class, id);
	}

	@Override
	public List<Producto> getAll() {
		String q = "SELECT p from " + Producto.class.getName() + " p ";
		TypedQuery<Producto> query = entityManager.createQuery(q, Producto.class);

		List<Producto> result = query.getResultList();
		if (result == null) {
			result = new ArrayList<Producto>();
		}
		return result;
	}

	@Override
	public long size() {
		String q = "SELECT count(p) from " + Producto.class.getName() + " p";
		return (Long) entityManager.createQuery(q).getSingleResult();
	}
}
