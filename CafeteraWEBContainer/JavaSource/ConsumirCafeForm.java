import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.application.exceptions.BusinessException;
import com.application.exceptions.ValidationError;
import com.productos.ProductoService;
import com.productos.dto.DisponibilidadProductoDTO;
import com.productos.dto.ProductoDTO;

/**
 * Formulario que muestra la grilla de usuarios.
 * 
 * @author Nestor
 * 
 */
@ManagedBean
@SessionScoped
public class ConsumirCafeForm {
	@EJB
	private ProductoService productoService;

	private double saldoActual;

	public Collection<DisponibilidadProductoDTO> getProductosDisponibles() {
		return productoService.listDisponibilidadAll();
	}

	public String insertarMoneda(double importe) {
		saldoActual += importe;
		return null;
	}

	public double getSaldoActual() {
		return saldoActual;
	}
	
	public String consumirProducto(int productoId) {
		try {
			ProductoDTO producto = productoService.findById(productoId);
			if(producto != null && producto.getPrecio() <= saldoActual) {
				productoService.consumirProducto(productoId);
				saldoActual -= producto.getPrecio();
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Inserte monedas para completar el precio del producto.", ""));
			}
			return null;
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
				FacesContext.getCurrentInstance().addMessage("form:" + ve.getProperty(), new FacesMessage(FacesMessage.SEVERITY_ERROR, ve.getError(), ve.getError()));
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

}
