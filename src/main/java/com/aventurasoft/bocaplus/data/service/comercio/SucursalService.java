package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.Sucursal;
import com.aventurasoft.bocaplus.data.service.CrudService;

import java.util.List;

public interface SucursalService extends CrudService<Sucursal, Integer> {
    List<Sucursal> getSucursalByComercioId(Integer comercioId);
}
