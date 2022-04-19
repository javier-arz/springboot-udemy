/**
 * 
 */
package com.company.books.backend.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.company.books.backend.model.Libro;

/**
 * @author MSI
 *
 */
public interface LibroRepository extends CrudRepository<Libro, Long> {

}
