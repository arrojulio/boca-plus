package com.aventurasoft.bocaplus.data.service;

import com.aventurasoft.bocaplus.data.entity.Localidad;

import java.util.List;

public interface LocalidadService extends  CrudService<Localidad, String> {
    List<Localidad> getAllByName();
    List<Localidad> getLocalidadesByProvinciaId(String provinciaId);

}
