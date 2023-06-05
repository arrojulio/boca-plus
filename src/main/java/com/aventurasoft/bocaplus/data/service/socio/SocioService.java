package com.aventurasoft.bocaplus.data.service.socio;

import com.aventurasoft.bocaplus.data.entity.Socio;
import com.aventurasoft.bocaplus.data.entity.TipoSocio;
import com.aventurasoft.bocaplus.data.service.CrudService;

import java.util.List;

public interface SocioService extends CrudService<Socio, Long> {
    boolean isSocioValido(TipoSocio tipoSocio, String numeroSocio);
    Socio getSocio(TipoSocio tipoSocio, String numeroSocio);
    List<Socio> getSociosByName(String criteria);
}
