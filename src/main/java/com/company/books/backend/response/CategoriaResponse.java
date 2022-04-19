package com.company.books.backend.response;

import java.util.List;

import com.company.books.backend.model.Categoria;

public class CategoriaResponse {

	private List<Categoria> categoria;

	/**
	 * @return the categoria
	 */
	public List<Categoria> getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(List<Categoria> categoria) {
		this.categoria = categoria;
	}
	
	
}
