package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.Venta;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VentaRepository extends CrudRepository<Venta, Long> {
    @Query("SELECT * FROM venta ORDER BY fecha, hora, sucursal_id")
    List<Venta> findAllByDateAndSucursalId();
}
