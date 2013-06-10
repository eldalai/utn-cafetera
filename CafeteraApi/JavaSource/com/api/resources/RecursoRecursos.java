package com.api.resources;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyProviderFactory;


@Stateless
@Path(value="recursos")
@Produces(value={MediaType.APPLICATION_JSON})
@Consumes(value={MediaType.APPLICATION_JSON})
public class RecursoRecursos {

	@GET
	public Response getRecursos() {
		Collection<RecursoDTO> listAll = new LinkedList<RecursoDTO>();
		listAll.add( new RecursoDTO( "usuarios", resolveResourceUrl( RecursoUsuarios.class ) ) );
		listAll.add( new RecursoDTO( "materiasprimas", resolveResourceUrl( RecursoMateriasPrimas.class ) ) );
		listAll.add( new RecursoDTO( "productos", resolveResourceUrl( RecursoProductos.class ) ) );
		listAll.add(new RecursoDTO("productosMateriasPrimas", resolveResourceUrl(RecursoProductos.class) + "/{productoId}/materiasprimas"));
		listAll.add( new RecursoDTO( "cafes", resolveResourceUrl( RecursoCafes.class ) ) );
		return Response.ok(listAll).build();
	}

	private String resolveResourceUrl(Class<?> resourceClass) {
		UriBuilder path = UriBuilder.fromResource(resourceClass);
		return resolvePathUrl(path.build());
	}
	private String resolvePathUrl(URI path) {
		URI baseUri = ResteasyProviderFactory.getContextData(HttpRequest.class).getUri().getBaseUri();
		URI resolve = baseUri.resolve(path);
		return resolve.toString();
	}
	
}
