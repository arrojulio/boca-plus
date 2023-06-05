package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.CategoriaComercio;
import com.aventurasoft.bocaplus.data.service.CrudService;

import java.util.List;

public interface CategoriaComercioService extends CrudService<CategoriaComercio, Integer> {
    List<CategoriaComercio> getAllByName();
}
