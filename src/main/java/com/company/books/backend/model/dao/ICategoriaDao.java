package com.company.books.backend.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.company.books.backend.model.Categoria;

/**
 * @author MSI
 *
 */
public interface ICategoriaDao extends CrudRepository<Categoria, Long> {

}
