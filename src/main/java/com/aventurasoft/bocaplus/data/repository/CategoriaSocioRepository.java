package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.CategoriaComercio;
import com.aventurasoft.bocaplus.data.entity.CategoriaSocio;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoriaSocioRepository extends CrudRepository<CategoriaSocio, Integer> {
    @Query("SELECT * FROM categoria_socio ORDER BY UPPER(nombre)")
    List<CategoriaSocio> findAllByName();
}
