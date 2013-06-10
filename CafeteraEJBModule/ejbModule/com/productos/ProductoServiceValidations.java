package com.productos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

import com.application.exceptions.ValidationError;
import com.productos.dto.ProductoDTO;
import com.productos.dto.ProductoMateriaPrimaDTO;
import com.productos.entities.Producto;
import com.productos.entities.ProductoMateriaPrima;
import com.productos.repository.ProductoRepository;

@Stateless
@LocalBean
public class ProductoServiceValidations {
	@EJB
	ProductoRepository productoRepository;

	@EJB
	private MateriaPrimaServiceValidations materiaPrimaValidator;

	/**
	 * Valida agregar un producto nuevo.
	 * 
	 * @param nuevoProducto
	 * @return
	 */
	public List<ValidationError> validarAddProducto(ProductoDTO nuevoProducto) {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		if (nuevoProducto.getId() != null) {
			Producto producto = productoRepository.get(nuevoProducto.getId());
			if (producto != null) {
				errors.add(new ValidationError("id", "El producto ya se encuentra registrado."));
			}
		}

		if (nuevoProducto.getNombre() == null || nuevoProducto.getNombre().length() == 0) {
			errors.add(new ValidationError("nombre", "Debe definir un nombre para el producto."));
		}

		if (nuevoProducto.getPrecio() <= 0) {
			errors.add(new ValidationError("precio", "Debe definir un precio para el producto."));
		}
		return errors;
	}

	/**
	 * Valida consumir un producto.
	 * 
	 * @param productoId
	 * @return
	 */
	public List<ValidationError> validarConsumirProducto(Integer productoId) {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		Producto producto = productoRepository.get(productoId);
		if (producto == null) {
			errors.add(new ValidationError("productoId", "El producto indicado no se encuentra registrado."));
		}

		for (ProductoMateriaPrima pmt : producto.getMateriasPrimas()) {
			List<ValidationError> mpErrors = materiaPrimaValidator.validarConsumirMateriaPrima(pmt.getMateriaPrima().getId(), pmt.getCantidad());
			if (mpErrors.size() > 0) {
				errors.add(new ValidationError(null, "Falta " + pmt.getMateriaPrima().getNombre() + " para " + producto.getNombre() + "."));
			}
		}

		return errors;
	}

	/**
	 * Valida los constraints del entity bean, estas validaciones son internas
	 * del servicio.
	 * 
	 * @param producto
	 * @throws ConstraintViolationException
	 */
	public void validarEntityBean(Producto producto) throws ConstraintViolationException {
		Set<ConstraintViolation<Producto>> errors = Validation.buildDefaultValidatorFactory().getValidator().validate(producto);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException("Errores de validacion", new HashSet<ConstraintViolation<?>>(errors));
		}
	}

	/**
	 * Valida la actualizacion del precio del producto.
	 * 
	 * @param productoId
	 * @param nuevoPrecio
	 * @return
	 */
	public List<ValidationError> validarActualizarPrecioProducto(Integer productoId, double nuevoPrecio) {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		Producto producto = productoRepository.get(productoId);
		if (producto == null) {
			errors.add(new ValidationError("productoId", "El producto indicado no se encuentra registrado."));
		}

		if (nuevoPrecio <= 0) {
			errors.add(new ValidationError("nuevoPrecio", "El precio indicado es invalido."));
		}
		return errors;
	}

	/**
	 * Valida la eliminacion de un producto.
	 * 
	 * @param productoId
	 * @return
	 */
	public List<ValidationError> validarEliminarProducto(Integer productoId) {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		Producto producto = productoRepository.get(productoId);
		if (producto == null) {
			errors.add(new ValidationError("productoId", "El producto indicado no se encuentra registrado."));
		}

		return errors;
	}

	/**
	 * Valida agregar una materia prima a un producto.
	 * 
	 * @param productoId
	 * @param materiaPrima
	 * @return
	 */
	public List<ValidationError> validarAddMateriaPrima(int productoId, ProductoMateriaPrimaDTO materiaPrima) {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		Producto producto = productoRepository.get(productoId);
		if (producto == null) {
			errors.add(new ValidationError("productoId", "El producto indicado no se encuentra registrado."));
		}

		if (materiaPrima.getMateriaPrima() == null) {
			errors.add(new ValidationError("materiaPrima", "Debe definir la materia prima."));
		} else if (materiaPrima.getMateriaPrima().getId() == null) {
			errors.add(new ValidationError("materiaPrima", "Debe definir la materia prima."));
		} else {
			for (ProductoMateriaPrima pmp : producto.getMateriasPrimas()) {
				if (pmp.getMateriaPrima().getId() == materiaPrima.getMateriaPrima().getId()) {
					errors.add(new ValidationError("materiaPrima", "La materia prima ya se encuentra en el producto."));
				}
			}
		}

		if (materiaPrima.getCantidad() <= 0) {
			errors.add(new ValidationError("materiaPrima.cantidad", "La cantidad indicada no es correcta."));
		}

		return errors;
	}

	/**
	 * Valida la actualizacion de los datos de una materia prima asociada a un producto
	 * 
	 * @param productoId
	 * @param materiaPrima
	 * @return
	 */
	public List<ValidationError> validarUpdateMateriaPrima(Integer productoId, ProductoMateriaPrimaDTO materiaPrima) {
		List<ValidationError> errors = new ArrayList<ValidationError>();

		Producto producto = productoRepository.get(productoId);
		if (producto == null) {
			errors.add(new ValidationError("productoId", "El producto indicado no se encuentra registrado."));
		}

		if (materiaPrima.getMateriaPrima() == null) {
			errors.add(new ValidationError("materiaPrima", "Debe definir la materia prima."));
		} else if (materiaPrima.getMateriaPrima().getId() == null) {
			errors.add(new ValidationError("materiaPrima", "Debe definir la materia prima."));
		} else {
			for (ProductoMateriaPrima pmp : producto.getMateriasPrimas()) {
				if (pmp.getMateriaPrima().getId() == materiaPrima.getMateriaPrima().getId()) {
					errors.add(new ValidationError("materiaPrima", "La materia prima ya se encuentra en el producto."));
				}
			}
		}

		if (materiaPrima.getCantidad() <= 0) {
			errors.add(new ValidationError("materiaPrima.cantidad", "La cantidad indicada no es correcta."));
		}

		return errors;
	}
}
