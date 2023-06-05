package com.aventurasoft.bocaplus.data.service.socio;

import com.aventurasoft.bocaplus.data.entity.TagSocio;
import com.aventurasoft.bocaplus.data.service.CrudService;

import java.util.List;

public interface TagSocioService extends CrudService<TagSocio, Integer> {
    List<TagSocio> getTagSociosBySocioId(Long socioId);

}
