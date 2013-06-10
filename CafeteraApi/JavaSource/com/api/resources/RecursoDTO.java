package com.api.resources;

import java.net.URI;

public class RecursoDTO {

	private String name;
	private URI uri;

	public RecursoDTO(String name, URI uri) {
		this.setName(name);
		this.setUri(uri);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}
	
}
