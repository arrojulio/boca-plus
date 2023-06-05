package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.TagComercio;
import com.aventurasoft.bocaplus.data.service.CrudService;

import java.util.List;

public interface TagComercioService extends CrudService<TagComercio, Integer> {
    List<TagComercio> getTagComerciosByComercioId(Integer comercioId);
}
