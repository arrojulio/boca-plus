package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.PromocionComercio;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PromocionComercioRepository extends CrudRepository<PromocionComercio, Integer> {
    @Query("SELECT pc.* FROM promocion_comercio pc LEFT OUTER JOIN promocion p ON pc.promocion_id = p.id WHERE pc.comercio_id = :comercioId ORDER BY UPPER(p.nombre)")
    List<PromocionComercio> getPromocionComerciosByComercioId(Integer comercioId);

}
