package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.Promocion;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PromocionRepository extends CrudRepository<Promocion, Integer> {
    @Query("SELECT * FROM promocion WHERE id IN (SELECT promocion_id FROM promocion_comercio WHERE comercio_id = :comercioId) ORDER BY UPPER(nombre)")
    List<Promocion> getPromocionesByComercioId(Integer comercioId);

    @Query("SELECT * FROM promocion ORDER BY nombre")
    List<Promocion> findAllByName();
}
