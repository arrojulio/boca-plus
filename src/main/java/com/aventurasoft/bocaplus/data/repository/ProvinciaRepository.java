package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.Provincia;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProvinciaRepository extends CrudRepository<Provincia, String> {
    @Query("SELECT * FROM provincia ORDER BY UPPER(nombre)")
    List<Provincia> findAllByName();

}
