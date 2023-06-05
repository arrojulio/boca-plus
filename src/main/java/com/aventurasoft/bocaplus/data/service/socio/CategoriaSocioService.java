package com.aventurasoft.bocaplus.data.service.socio;

import com.aventurasoft.bocaplus.data.entity.CategoriaSocio;
import com.aventurasoft.bocaplus.data.service.CrudService;

import java.util.List;

public interface CategoriaSocioService extends CrudService<CategoriaSocio, Integer> {
    List<CategoriaSocio> getAllByName();

}
