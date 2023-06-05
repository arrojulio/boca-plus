package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.Venta;
import com.aventurasoft.bocaplus.data.service.CrudService;

import java.math.BigDecimal;
import java.util.List;

public interface VentaService extends CrudService<Venta, Long> {
    List<Venta> findAllByDateAndSucursalId();

    Venta registrarVenta(Integer sucursalId, Integer promocionId, Long socioId, BigDecimal importe);
}
