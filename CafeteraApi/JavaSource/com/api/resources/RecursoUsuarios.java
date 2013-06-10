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

import com.seguridad.UsuariosService;
import com.seguridad.dto.ActualizarUsuarioDTO;
import com.seguridad.dto.RegistrarUsuarioDTO;
import com.seguridad.dto.UsuarioDTO;

@Stateless
@Path(value="usuarios")
@Produces(value={MediaType.APPLICATION_JSON})
@Consumes(value={MediaType.APPLICATION_JSON})
public class RecursoUsuarios {

	@EJB
	private UsuariosService usuarioService;

	@GET
	public Response getUsuarios() {
		Collection<UsuarioDTO> listAll = usuarioService.listAll();
		if( listAll == null || listAll.isEmpty() )
			return Response.noContent().build();
		return Response.ok(listAll).build();
	}
	
	@GET
	@Path(value="/{usuario}")
	public Response getUsuario(@DefaultValue("") @PathParam("usuario") final String loginUsuario) {
		UsuarioDTO usuarioDTO = usuarioService.findByLogin(loginUsuario);
		if( usuarioDTO == null )
			return Response.status(406).entity("usuario no encontrado").build();
		return Response.ok(usuarioDTO).build();
	}
	
	@POST
	public Response registrarUsuario(RegistrarUsuarioDTO registrarUsuario) {
		usuarioService.registrarUsuario(registrarUsuario);
		
		URI location = UriBuilder.fromResource(this.getClass()).path(registrarUsuario.getLogin()).build();;
		return Response.created(location).build();
	}
	
	@PUT
	@Path(value="/{usuario}")
	public Response actualizarUsuario(@DefaultValue("") @PathParam("usuario") final String loginUsuario, ActualizarUsuarioDTO actualizarUsuario) {
		try {
			usuarioService.actualizarUsuario( actualizarUsuario );
			return Response.ok().build();
		} catch( Exception e ) {
			return Response.status(406).entity(e.getMessage()).build();
		}
	}
	
	@DELETE
	@Path(value="/{usuario}")
	public Response borrarUsuario(@DefaultValue("") @PathParam("usuario") final String loginUsuario) {
		usuarioService.desactivarUsuario(loginUsuario);
		return Response.ok().build();
	}
	

	public String desactivarUsuario(String login) {
		usuarioService.desactivarUsuario(login);
		return null;
	}
	
	public String activarUsuario(String login) {
		usuarioService.activarUsuario(login);
		return null;
	}
}
