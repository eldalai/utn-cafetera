package com.api.resources;

import java.net.URI;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.productos.MateriaPrimaService;
import com.productos.dto.MateriaPrimaDTO;

@Stateless
@Path(value="materiasprimas")
@Produces(value={MediaType.APPLICATION_JSON})
@Consumes(value={MediaType.APPLICATION_JSON})
public class RecursoMateriasPrimas {

	@EJB
	private MateriaPrimaService materiaPrimaService;

	@GET
	public Response getMateriasPrimas() {
		Collection<MateriaPrimaDTO> listAll = materiaPrimaService.listAll();
		if( listAll == null || listAll.isEmpty() )
			return Response.noContent().build();
		return Response.ok(listAll).build();
	}
	
	@GET
	@Path(value="/{materiaprima}")
	public Response getMateriaPrima(@DefaultValue("") @PathParam("materiaprima") final Integer id) {
		MateriaPrimaDTO materiaPrima = materiaPrimaService.findById(id);
		if( materiaPrima == null )
			return Response.status(406).entity("materia prima no encontrada").build();
		return Response.ok(materiaPrima).build();
	}
	
	@POST
	public Response crearMateriaPrima(MateriaPrimaDTO materiaPrima) {
		materiaPrima = materiaPrimaService.addMateriaPrima(materiaPrima);
		
		URI location = UriBuilder.fromResource(this.getClass()).path(materiaPrima.getId().toString()).build();
		return Response.created(location).build();
	}
	
	@PUT
	@Path(value="/{materiaprima}")
	public Response actualizarMateriaPrima(@DefaultValue("") @PathParam("materiaprima") final Integer id, MateriaPrimaDTO materiaPrima) {
		try {
			materiaPrimaService.updateMateriaPrima(materiaPrima);
			return Response.ok().build();
		} catch( Exception e ) {
			return Response.status(406).entity(e.getMessage()).build();
		}
	}

}
