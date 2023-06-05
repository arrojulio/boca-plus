package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.Localidad;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocalidadRepository extends CrudRepository<Localidad, String> {
    @Query("SELECT * FROM localidad ORDER BY UPPER(nombre)")
    List<Localidad> findAllByName();

    @Query("SELECT * FROM localidad WHERE provincia_id = :provinciaId ORDER BY UPPER(nombre)")
    List<Localidad> getLocalidadesByProvinciaId(String provinciaId);
}
