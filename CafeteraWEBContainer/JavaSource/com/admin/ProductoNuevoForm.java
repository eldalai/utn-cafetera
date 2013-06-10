package com.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringUtils;

import com.application.exceptions.BusinessException;
import com.application.exceptions.ValidationError;
import com.productos.MateriaPrimaService;
import com.productos.ProductoService;
import com.productos.dto.MateriaPrimaDTO;
import com.productos.dto.ProductoDTO;
import com.productos.dto.ProductoMateriaPrimaDTO;

/**
 * Bean de control del formulario addUsuario
 * 
 * @author Nestor
 * 
 */
@ManagedBean
@ViewScoped
public class ProductoNuevoForm {
	@EJB
	private ProductoService productoService;
	@EJB 
	private MateriaPrimaService materiaPrimaService;

	private ProductoDTO producto;
	private Collection<ProductoMateriaPrimaDTO> prodcutosMateriasPrimas;
	private ProductoMateriaPrimaDTO productoMateriaPrimaEditada;
	private boolean nuevo = true;

	/**
	 * Cuando se incializa el formulario podemos obtener un login del request,
	 * si lo obtenemos es porque se quiere editar un usuario existente, si no se
	 * obtiene el login, es porque se quiere crear un usuario nuevo.
	 * 
	 */
	@PostConstruct
	private void initialize() {
		String stringId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		if( stringId == null || stringId.equals("") ) {
			producto = new ProductoDTO();
			prodcutosMateriasPrimas = new ArrayList<ProductoMateriaPrimaDTO>();
			nuevo = true;
		}	else {
			int id= Integer.parseInt(stringId);
			producto = productoService.findById(id);
			prodcutosMateriasPrimas = productoService.getMateriasPrimasPorProducto(id);
			nuevo = false;
		}	
	}

	/**
	 * Se procesa guardar el login. Si se producen errores se vuelve a la vista
	 * original, mostrando los errores.
	 * 
	 * @throws IOException
	 */
	public String save() throws IOException {
		try {
			if (nuevo) {
				productoService.addProducto(producto);
			} else {
				productoService.actualizarPrecioProducto(producto.getId(), producto.getPrecio());
			}
			return "producto";
		} catch (BusinessException be) {
			processBusinessException(be);
		} catch (ConstraintViolationException cve) {
			processConstraintViolationException(cve);
		} catch (EJBException e) {
			if (e.getCause().getClass().isAssignableFrom(ConstraintViolationException.class)) {
				processConstraintViolationException((ConstraintViolationException) e.getCause());
			} else if (e.getCause().getClass().isAssignableFrom(BusinessException.class)) {
				processBusinessException((BusinessException) e.getCause());
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error inesperado del sistema. No se pudo agregar el Producto.", e.getMessage()));
		}
		return null;
	}

	/**
	 * Cuando se pulsa agregar materia prima, se crea el objeto
	 * ProductoMateriaPrimaDTO. Y se vuelve al formulario, el formulario va a
	 * mostrar el elemento de edicion.
	 * 
	 */
	public String agregarMateriaPrima() {
		if (!nuevo) {
			productoMateriaPrimaEditada = new ProductoMateriaPrimaDTO();
		}
		return null;
	}

	/**
	 * Cuando se pulsa agregar materia prima, se crea el objeto
	 * ProductoMateriaPrimaDTO. Y se vuelve al formulario, el formulario va a
	 * mostrar el elemento de edicion.
	 * 
	 */
	public String editarMateriaPrima(int materiaPrimaId) {
		if (!nuevo) {
			for (ProductoMateriaPrimaDTO pmp : prodcutosMateriasPrimas) {
				if (pmp.getId() == materiaPrimaId) {
					productoMateriaPrimaEditada = pmp;
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Elimina una materia prima del producto. 
	 * 
	 * @param materiaPrimaId
	 * @return
	 */
	public String borrarMateriaPrima(int materiaPrimaId) {
		if (!nuevo) {
			productoService.eliminarMateriaPrima(producto.getId(), materiaPrimaId);
			prodcutosMateriasPrimas = productoService.getMateriasPrimasPorProducto(producto.getId());
			
		}
		return null;
	}
	
	/**
	 * Agrega la materia prima que se esta editando.
	 * 
	 * @return
	 */
	public String saveMateriaPrimaEditada() {
		try {
			if (!nuevo) {
				if(productoMateriaPrimaEditada.getId() != null && productoMateriaPrimaEditada.getId() > 0) {
					productoService.updateMateriaPrima(producto.getId(), productoMateriaPrimaEditada);
				} else {
					productoService.addMateriaPrima(producto.getId(), productoMateriaPrimaEditada);
				}

				// Actualizo el formulario
				producto = productoService.findById(producto.getId());
				prodcutosMateriasPrimas = productoService.getMateriasPrimasPorProducto(producto.getId());
				productoMateriaPrimaEditada = null;
			}
		} catch (BusinessException be) {
			processBusinessException(be);
		} catch (ConstraintViolationException cve) {
			processConstraintViolationException(cve);
		} catch (EJBException e) {
			if (e.getCause().getClass().isAssignableFrom(ConstraintViolationException.class)) {
				processConstraintViolationException((ConstraintViolationException) e.getCause());
			} else if (e.getCause().getClass().isAssignableFrom(BusinessException.class)) {
				processBusinessException((BusinessException) e.getCause());
			}
		} catch (Exception e) {
			e.printStackTrace();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error inesperado del sistema. No se pudo agregar el Producto.", e.getMessage()));
		}

		return null;
	}

	/**
	 * Procesa y revisa las ecepciones de negocio obtenidas.
	 * 
	 * @param e
	 */
	private void processBusinessException(BusinessException e) {
		BusinessException be = (BusinessException) e;
		if (be.getErrores().size() > 0) {
			for (ValidationError ve : be.getErrores()) {
				FacesContext.getCurrentInstance().addMessage("form:" + StringUtils.replace(ve.getProperty(), ".", "_"), new FacesMessage(FacesMessage.SEVERITY_ERROR, ve.getError(), ve.getError()));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, be.getMessage(), be.getMessage()));
		}
	}

	/**
	 * Procesa y revisa las ecepciones de negocio obtenidas.
	 * 
	 * @param cve
	 */
	private void processConstraintViolationException(ConstraintViolationException cve) {
		for (ConstraintViolation<?> v : cve.getConstraintViolations()) {
			FacesContext.getCurrentInstance().addMessage("form:" + v.getPropertyPath().toString(), new FacesMessage(FacesMessage.SEVERITY_ERROR, v.getMessage(), v.getMessage()));
		}
	}

	public ProductoDTO getProducto() {
		return producto;
	}
	public void setProducto(ProductoDTO producto) {
		this.producto = producto;
	}

	public Collection<ProductoMateriaPrimaDTO> getProductoMateriasPrimas() {
		return prodcutosMateriasPrimas;
	}
	public void setProductoMateriasPrimas(Collection<ProductoMateriaPrimaDTO> materiasPrimas) {
		this.prodcutosMateriasPrimas = materiasPrimas;
	}

	public ProductoMateriaPrimaDTO getProductoMateriaPrimaEditada() {
		return productoMateriaPrimaEditada;
	}
	public void setProductoMateriaPrimaEditada(ProductoMateriaPrimaDTO materiaPrimaEditada) {
		this.productoMateriaPrimaEditada = materiaPrimaEditada;
	}

	public Collection<MateriaPrimaDTO> getMateriasPrimas() {
		return materiaPrimaService.listAll();
	}
	
	public boolean isNuevo() {
		return nuevo;
	}
}
