package com.api.resources;

import java.net.URI;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.productos.ProductoService;

@Stateless
@Path(value="cafes")
@Produces(value={MediaType.APPLICATION_JSON})
@Consumes(value={MediaType.APPLICATION_JSON})
public class RecursoCafes {

	@EJB
	private ProductoService productoService;

	@POST
	public Response crearCafe(CafeDTO cafe) {
		productoService.consumirProducto(cafe.getProducto());
		
		URI location = UriBuilder.fromResource(this.getClass()).build();
		return Response.created(location).build();
	}
	
}