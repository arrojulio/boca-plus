package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.CategoriaComercio;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoriaComercioRepository extends CrudRepository<CategoriaComercio, Integer> {
    @Query("SELECT * FROM categoria_comercio ORDER BY UPPER(nombre)")
    List<CategoriaComercio> findAllByName();
}
