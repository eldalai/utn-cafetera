package com.admin;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.application.exceptions.BusinessException;
import com.application.exceptions.ValidationError;
import com.productos.MateriaPrimaService;
import com.productos.dto.MateriaPrimaDTO;

/**
 * Bean de control del formulario addUsuario
 * 
 * @author Nestor
 * 
 */
@ManagedBean
@ViewScoped
public class MateriaPrimaNuevaForm {
	@EJB
	private MateriaPrimaService materiaPrimaService;

	private MateriaPrimaDTO materiaPrima;
	private boolean nuevo = true;

	/**
	 * Cuando se incializa el formulario podemos obtener un login del request,
	 * si lo obtenemos es porque se quiere editar un usuario existente, si no se
	 * obtiene el login, es porque se quiere crear un usuario nuevo.
	 * 
	 */
	@PostConstruct
	private void initialize() {
//		int id = NumberUtils.toInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"), 0);
		String stringId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		int id = 0;
		if( stringId == null || stringId.equals("") )
			id = 0;
		else
			id= Integer.parseInt(stringId);
		materiaPrima = materiaPrimaService.findById(id);

		if (materiaPrima == null) {
			materiaPrima = new MateriaPrimaDTO();
		} else {
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
				materiaPrimaService.addMateriaPrima(materiaPrima);
			} else {
				materiaPrimaService.updateMateriaPrima(materiaPrima);
			}
			return "materiaPrima";
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error inesperado del sistema. No se pudo agregar la Materia Prima.", e.getMessage()));
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

	public MateriaPrimaDTO getMateriaPrima() {
		return materiaPrima;
	}

	public void setMateriaPrima(MateriaPrimaDTO materiaPrima) {
		this.materiaPrima = materiaPrima;
	}

}
