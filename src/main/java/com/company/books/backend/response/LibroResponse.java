/**
 * 
 */
package com.company.books.backend.response;

import java.util.List;

import com.company.books.backend.model.Libro;

/**
 * @author MSI
 *
 */
public class LibroResponse {
	
	private List<Libro> libros;

	/**
	 * @return the libros
	 */
	public List<Libro> getLibros() {
		return libros;
	}

	/**
	 * @param libros the libros to set
	 */
	public void setLibros(List<Libro> libros) {
		this.libros = libros;
	}

}
