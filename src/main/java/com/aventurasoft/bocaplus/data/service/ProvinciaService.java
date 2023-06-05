package com.aventurasoft.bocaplus.data.service;

import com.aventurasoft.bocaplus.data.entity.Provincia;

import java.util.List;

public interface ProvinciaService extends CrudService<Provincia, String> {
    List<Provincia> getAllByName();
}
