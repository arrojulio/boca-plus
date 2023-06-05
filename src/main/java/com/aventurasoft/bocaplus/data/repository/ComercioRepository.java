package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.Comercio;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ComercioRepository extends CrudRepository<Comercio, Integer> {
    @Query("SELECT * FROM comercio ORDER BY UPPER(nombre)")
    List<Comercio> findAllByNombre();
}
