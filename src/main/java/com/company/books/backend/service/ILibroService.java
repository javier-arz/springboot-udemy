/**
 * 
 */
package com.company.books.backend.service;

import org.springframework.http.ResponseEntity;

import com.company.books.backend.model.Libro;
import com.company.books.backend.response.CategoriaResponseRest;
import com.company.books.backend.response.LibroResponseRest;

/**
 * @author MSI
 *
 */
public interface ILibroService {
	
	public ResponseEntity<LibroResponseRest> buscarLibros() ; 
	public ResponseEntity<LibroResponseRest> buscarLibrosPorId(Long id) ;
	public ResponseEntity<LibroResponseRest> crearLibro(Libro libro);
	public ResponseEntity<LibroResponseRest> actualizarLibro(Libro request, Long id);
	public ResponseEntity<LibroResponseRest> eliminarLibro(Long id); 

}
