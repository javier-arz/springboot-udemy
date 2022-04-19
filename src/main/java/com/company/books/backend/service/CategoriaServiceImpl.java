package com.company.books.backend.service;

import java.util.ArrayList;
import java.util.Date;
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
import com.company.books.backend.model.dao.ICategoriaDao;
import com.company.books.backend.response.CategoriaResponseRest;

@Service
public class CategoriaServiceImpl implements ICategoriaService{

	private static final Logger log = LoggerFactory.getLogger(CategoriaServiceImpl.class);
	
	@Autowired
	private ICategoriaDao categoriaDao;
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoriaResponseRest> buscarCategorias() {
		
		log.info("Inicio método buscarCategorias()");
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		
		try {
			List<Categoria> categoria = (List<Categoria>) categoriaDao.findAll();
			response.getCategoriaResponse().setCategoria(categoria);
			response.setMetadata("Respuesta Ok", "00", "Respuesta Exitosa");
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK) ; // OK  es 200
		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al consultar categorías");
			log.error("Error al consultar categoría", e.getMessage());
			
			e.getStackTrace();
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR) ; // este es el 500
		}

		//response.setMetadata("Respuesta Ok", "00", "Respuesta Exitosa");
		//return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK) ; // OK  es 200
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoriaResponseRest> buscarPorId(Long id) {

		log.info("Inicio método buscarPorId") ;
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		List<Categoria> list = new ArrayList<>();
		
		try {
			Optional<Categoria> categoria = categoriaDao.findById(id);
			
			if (categoria.isPresent()) {
				list.add(categoria.get());
				response.getCategoriaResponse().setCategoria(list);
			}
			else {
				log.error("Error al consultar categoria");
				response.setMetadata("Respuesta Fail", "-1", "Categoría no encontrada");
				
				return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al consultar categorías");
			log.error("Error al consultar categoría", e.getMessage());
			
			e.getStackTrace();
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR) ; 
		}

		response.setMetadata("Respuesta Ok", "00", ""+new Date());
		return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK) ; // OK  es 200
	}

	@Override
	@Transactional
	public ResponseEntity<CategoriaResponseRest> crearCat(Categoria categoria) {

		log.info("Inicio método crear categoria") ;
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		List<Categoria> list = new ArrayList<>();
		try {
			
			Categoria categoriaGuardada = categoriaDao.save(categoria);
			
			if (categoriaGuardada != null) {
				list.add(categoriaGuardada);
				response.getCategoriaResponse().setCategoria(list);
			}
			else {
				log.error("Error al guardada categoria");
				response.setMetadata("Respuesta Fail", "-1", "Categoría no guardada");
				
				return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			response.setMetadata("Respuesta Fail", "-1", "Error al crear categorías");
			log.error("Error al crear categoría", e.getMessage());
			
			e.getStackTrace();
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR) ; 
		}

		response.setMetadata("Respuesta Ok", "00", "Categoria Creada");
		return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK) ; // OK  es 200
	}

	@Override
	@Transactional
	public ResponseEntity<CategoriaResponseRest> actualizarCat(Categoria categoria, Long id) {

		log.info("Inicio método actualizar");
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		List<Categoria> list = new ArrayList<>();
		
		try {
			Optional<Categoria> categoriaBuscada = categoriaDao.findById(id);
			
			if (categoriaBuscada.isPresent()) {
				categoriaBuscada.get().setNombre(categoria.getNombre());
				categoriaBuscada.get().setDescripcion(categoria.getDescripcion());
				
				Categoria categoriaActualizar = categoriaDao.save(categoriaBuscada.get());
				
				if (categoriaActualizar != null) {
					response.setMetadata("Respuesta OK", "00", "CategoriaActualizada");
					list.add(categoriaActualizar);
					response.getCategoriaResponse().setCategoria(list);
				}
				else {
					log.error("Error Actualizando la categoria");
					response.setMetadata("Respuesta Fail", "-1", "Categoria NO Actualizada");
					
					return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.BAD_REQUEST) ; 
				}
			}
			else {

				log.error("Error Actualizando la categoria");
				response.setMetadata("Respuesta Fail", "-1", "Categoria NO Actualizada");
				
				return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.NOT_FOUND) ; 
			}
		} catch (Exception e) {

			log.error("Error Actualizando la categoria", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta Fail", "-1", "Categoria NO Actualizada");
			
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR) ; 
		}

		return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK) ;
	}

	@Override
	@Transactional
	public ResponseEntity<CategoriaResponseRest> eliminarCat(Long id) {

		log.info("Inicio método eliminar");
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		List<Categoria> list = new ArrayList<>();
		
		try {
			// Primero buscar si la categoría existe, para controlar la excepción
			Optional<Categoria> categoriaAEliminar = categoriaDao.findById(id);
			
			if (categoriaAEliminar.isEmpty())
			{
				log.error("La categoría con ID " + id +  " no fue encontrada");
				response.setMetadata("Respuesta Fail", "-1", "Categoria NO Eliminada. No fue encontrada");
				
				return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.NOT_FOUND) ; 
			}
			else {
				//eliminamos el registro
				categoriaDao.deleteById(id);
				response.setMetadata("Respuesta Ok", "00", "Categoria Eliminada");
				list.add(categoriaAEliminar.get());
				response.getCategoriaResponse().setCategoria(list);
				
			}
					
			
		} catch (Exception e) {

			log.error("Error Eliminando la categoria", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta Fail", "-1", "Categoria NO Eliminada");
			
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR) ; 
		}
		
		return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK);
	}
	
	

}
