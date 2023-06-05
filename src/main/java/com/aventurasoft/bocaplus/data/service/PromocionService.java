package com.aventurasoft.bocaplus.data.service;

import com.aventurasoft.bocaplus.data.entity.Promocion;

import java.util.List;

public interface PromocionService extends CrudService<Promocion, Integer> {
    List<Promocion> getPromocionesByComercioId(Integer comercioId);

    List<Promocion> getAllByName();

}
