/**
 * 
 */
package com.company.books.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.books.backend.model.Categoria;
import com.company.books.backend.model.Libro;
import com.company.books.backend.model.dao.ICategoriaDao;
import com.company.books.backend.model.dao.LibroRepository;
import com.company.books.backend.response.LibroResponseRest;

/**
 * @author MSI
 *
 */

@Service
public class LibroServiceImpl implements ILibroService {
	
	private static final Logger log = LoggerFactory.getLogger(LibroServiceImpl.class);
	
	@Autowired
	private LibroRepository libroRepository;
	
	@Autowired
	private ICategoriaDao categoriaDao;
	

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> buscarLibros() {
		
		log.info("Inicio método buscarLibros()");
		
		LibroResponseRest response = new LibroResponseRest();
		
		try {
			List<Libro> libros = (List<Libro>) libroRepository.findAll();
			response.getLibroResponse().setLibros(libros);
			response.setMetadata("Respuesta Ok", "00", "Respuesta Exitosa");
		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al consultar libros");
			log.error("Error al consultar libro", e.getMessage());
			
			e.getStackTrace();
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR) ; // este es el 500
		}

		//response.setMetadata("Respuesta Ok", "00", "Respuesta Exitosa");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK) ; // OK  es 200
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> buscarLibrosPorId(Long id) {
		log.info("Inicio método buscarLibrosPorId") ;
		
		
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			Optional<Libro> Libro = libroRepository.findById(id);
			
			if (Libro.isPresent()) {
				list.add(Libro.get());
				response.getLibroResponse().setLibros(list);				
			}
			else {
				log.error("Error al consultar Libro");
				response.setMetadata("Respuesta Fail", "-1", "Libro no encontrado");
				
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al consultar Libros");
			log.error("Error al consultar Libro", e.getMessage());
			
			e.getStackTrace();
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR) ; 
		}

		response.setMetadata("Respuesta Ok", "00", "Respuesta Exitosa");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK) ; // OK  es 200
	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> crearLibro(Libro libro) {
		log.info("Inicio método crearLibro");
		
		LibroResponseRest responseRest = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			// Buscar la categoría
			Optional<Categoria> categoriaAsignada = categoriaDao.findById(libro.getCategoria().getId());
			
			if (categoriaAsignada.isEmpty()) {
				log.error("Error al guardar el libro cat");
				responseRest.setMetadata("Respuesta Fail", "-1", "Error al guardar el libro");
				
				return new ResponseEntity<LibroResponseRest>(responseRest, HttpStatus.BAD_REQUEST);
				
			} else {
				log.warn("Categoria encontrada " + categoriaAsignada.get().getNombre());
					
				
				libro.setCategoria(categoriaAsignada.get());
								
				Libro libroGuardado = libroRepository.save(libro);
				
				log.warn("Libro guardado " + libroGuardado.getNombre());
				
				if (libroGuardado != null) {
					list.add(libroGuardado);
					responseRest.getLibroResponse().setLibros(list);
				}

			}
			
		} catch (Exception e) {
			log.error("Error al guardar el libro");
			responseRest.setMetadata("Respuesta Fail", "-1", "Error al guardar el libro");
			
			return new ResponseEntity<LibroResponseRest>(responseRest, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		responseRest.setMetadata("Respuesta OK", "00", "Libro guardado");
		
		return new ResponseEntity<LibroResponseRest>(responseRest, HttpStatus.OK); 

	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> actualizarLibro(Libro libro, Long id) {

		log.info("Inicio método actualizar Libro");
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			
			Optional<Categoria> categoriaValidada = categoriaDao.findById(libro.getCategoria().getId());
			
			// Se está asignando una categoría que no existe
			if (categoriaValidada.isEmpty()) {
				log.error("Error Actualizando el libro (categoria no existe)");
				response.setMetadata("Respuesta Fail", "-1", "Libro NO Actualizada - no cat");
				
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.BAD_REQUEST) ; 
				
			} else {
				Optional<Libro> libroBuscado = libroRepository.findById(id);
				
				if (libroBuscado.isPresent()) {
					libroBuscado.get().setNombre(libro.getNombre());
					libroBuscado.get().setDescripcion(libro.getDescripcion());
					libroBuscado.get().setCategoria(categoriaValidada.get());
					
					Libro libroActualizar = libroRepository.save(libroBuscado.get());
					
					if (libroActualizar != null) {
						response.setMetadata("Respuesta OK", "00", "CategoriaActualizada");
						list.add(libroActualizar);
						response.getLibroResponse().setLibros(list);
					}
					else {
						log.error("Error Actualizando la categoria");
						response.setMetadata("Respuesta Fail", "-1", "Categoria NO Actualizada");
						
						return new ResponseEntity<LibroResponseRest>(response, HttpStatus.BAD_REQUEST) ; 
					}
					
				}
				else {

					log.error("Error Actualizando la categoria");
					response.setMetadata("Respuesta Fail", "-1", "Categoria NO Actualizada");
					
					return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND) ; 
				}

			}
		} catch (Exception e) {

			log.error("Error Actualizando el libro", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta Fail", "-1", "Libro NO Actualizado");
			
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR) ; 
		}

		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK) ;
	
	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> eliminarLibro(Long id) {

		log.info("Inicio método eliminar Libro");
		
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			// Primero buscar si el libro existe, para controlar la excepción
			Optional<Libro> libroAEliminar = libroRepository.findById(id);
			
			if (libroAEliminar.isEmpty())
			{
				log.error("El libro con ID " + id +  " no fue encontrado");
				response.setMetadata("Respuesta Fail", "-1", "Libro NO Eliminada. No fue encontradp");
				
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND) ; 
			}
			else {
				//eliminamos el registro
				libroRepository.deleteById(id);
				response.setMetadata("Respuesta Ok", "00", "Libro Eliminada");
				list.add(libroAEliminar.get());
				response.getLibroResponse().setLibros(list);
				
			}
					
			
		} catch (Exception e) {

			log.error("Error Eliminando el libro", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta Fail", "-1", "Libro NO Eliminado");
			
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR) ; 
		}
		
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}

}
