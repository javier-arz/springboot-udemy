/**
 * 
 */
package com.company.books.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.books.backend.model.Libro;
import com.company.books.backend.model.Libro;
import com.company.books.backend.response.LibroResponseRest;
import com.company.books.backend.response.LibroResponseRest;
import com.company.books.backend.service.ILibroService;

/**
 * @author MSI
 *
 */
@RestController
@RequestMapping("/v1")
public class LibroRestController {
	
	@Autowired
	private ILibroService service;
	
	@GetMapping("/libros")
	public ResponseEntity<LibroResponseRest> buscarLibros(){
		return service.buscarLibros();
	}
	
	@GetMapping("/libros/{id}")
	public ResponseEntity<LibroResponseRest> buscarLibrosPorId(@PathVariable Long id){
		
		return service.buscarLibrosPorId(id);
	}
	
	@PostMapping("/libros")
	public ResponseEntity<LibroResponseRest> crearLibro(@RequestBody Libro request)
	{
		ResponseEntity<LibroResponseRest> response = service.crearLibro(request);
		return response;
	}
	
	@PutMapping("/libros/{id}")
	public ResponseEntity<LibroResponseRest> actualizarLibro(@RequestBody Libro request, @PathVariable Long id) {
		ResponseEntity<LibroResponseRest> response = service.actualizarLibro(request, id);
		
		return response;
	}
	
	@DeleteMapping("/libros/{id}")
	public ResponseEntity<LibroResponseRest> eliminarLibro(@PathVariable Long id) {
		
		ResponseEntity<LibroResponseRest>  response = service.eliminarLibro(id);
		
		return response;
	}
	
}