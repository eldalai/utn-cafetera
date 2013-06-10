package com.api.resources;


public class RecursoDTO {

	private String name;
	private String uri;

	public RecursoDTO(String name, String uri) {
		this.setName(name);
		this.setUri(uri);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
