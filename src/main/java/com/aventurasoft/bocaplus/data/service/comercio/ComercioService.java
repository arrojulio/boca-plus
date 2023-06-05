package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.Comercio;
import com.aventurasoft.bocaplus.data.service.CrudService;

import java.util.List;

public interface ComercioService extends CrudService<Comercio, Integer> {
    List<Comercio> getAllByName();

}
