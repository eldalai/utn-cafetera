package com.api.resources;

import java.net.URI;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.productos.ProductoService;
import com.productos.dto.MateriaPrimaDTO;
import com.productos.dto.ProductoDTO;
import com.productos.dto.ProductoMateriaPrimaDTO;

@Stateless
@Path(value="productos")
@Produces(value={MediaType.APPLICATION_JSON})
@Consumes(value={MediaType.APPLICATION_JSON})
public class RecursoProductos {

	@EJB
	private ProductoService productoService;

	@EJB
	private MateriaPrimaService materiaPrimaService;

	@GET
	public Response getProductos() {
		Collection<ProductoDTO> listAll = productoService.listAll();
		if( listAll == null || listAll.isEmpty() )
			return Response.noContent().build();
		return Response.ok(listAll).build();
	}
	
	@GET
	@Path(value="/{producto}")
	public Response getProducto(@DefaultValue("") @PathParam("producto") final Integer id) {
		ProductoDTO producto = productoService.findById(id);
		if( producto == null )
			return Response.status(406).entity("producto no encontrada").build();
		return Response.ok(producto).build();
	}
	
	@POST
	public Response crearProducto(ProductoDTO producto) {
		producto = productoService.addProducto(producto);
		
		URI location = UriBuilder.fromResource(this.getClass()).path(producto.getId().toString()).build();
		return Response.created(location).build();
	}
	
	@PUT
	@Path(value="/{producto}")
	public Response actualizarProducto(@DefaultValue("") @PathParam("producto") final Integer id, ProductoDTO producto) {
		try {
			// todo
//			productoService. updateProducto(producto);
			return Response.ok().build();
		} catch( Exception e ) {
			return Response.status(406).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(value="/{producto}/materiasprimas")
	public Response getMateriasPrimasProducto(@DefaultValue("") @PathParam("producto") final Integer productoId) {
		Collection<ProductoMateriaPrimaDTO> listAll = productoService.getMateriasPrimasPorProducto(productoId);
		if( listAll == null || listAll.isEmpty() )
			return Response.noContent().build();
		return Response.ok(listAll).build();
	}
	
	@POST
	@Path(value="/{producto}/materiasprimas")
	public Response asigProducto(@DefaultValue("") @PathParam("producto") final Integer productoId, 
			RestProductoMateriaPrimaDTO productoMateriaPrima) {
		MateriaPrimaDTO materiaPrima = materiaPrimaService.findById(productoMateriaPrima.getMateriaPrimaId());
		
		ProductoMateriaPrimaDTO productoMateriaPrimaDTO = new ProductoMateriaPrimaDTO();
		
		productoMateriaPrimaDTO.setCantidad(productoMateriaPrima.getCantidad());
		productoMateriaPrimaDTO.setMateriaPrima(materiaPrima);
		
		productoService.addMateriaPrima(productoId, productoMateriaPrimaDTO);
		
		URI location = UriBuilder.fromResource(this.getClass()).build();
		return Response.created(location).build();
	}
	
	@DELETE
	@Path(value="/{producto}/materiasprimas/{materiaprima}")
	public Response asigProducto(@DefaultValue("") @PathParam("producto") final Integer productoId, 
			@DefaultValue("") @PathParam("materiaprima") final Integer materiaPrimaId) {
		
		productoService.eliminarMateriaPrima(productoId, materiaPrimaId);
		
		return Response.ok().build();
	}

	
	
}
