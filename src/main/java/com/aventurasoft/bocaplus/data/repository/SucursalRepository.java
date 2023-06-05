package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.Sucursal;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SucursalRepository extends CrudRepository<Sucursal, Integer> {
    @Query("SELECT * FROM sucursal WHERE comercio_id = :comercioId ORDER BY UPPER(nombre)")
    List<Sucursal> getSucursalByComercioId(Integer comercioId);


}
