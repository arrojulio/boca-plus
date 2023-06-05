package com.aventurasoft.bocaplus.data.service.socio;

import com.aventurasoft.bocaplus.data.entity.Socio;
import com.aventurasoft.bocaplus.data.entity.TipoSocio;
import com.aventurasoft.bocaplus.data.repository.SocioRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocioServiceImpl implements SocioService {
    private SocioRepository socioRepository;
    public SocioServiceImpl(SocioRepository socioRepository)
    {
        this.socioRepository = socioRepository;
    }
    @Override
    public CrudRepository<Socio, Long> getRepository() {
        return socioRepository;
    }

    @Override
    public Socio createNew()
    {
        Socio socio = new Socio();
        socio.setNew(true);

        return socio;
    }

    @Override
    public boolean isSocioValido(TipoSocio tipoSocio, String numeroSocio) {
        return socioRepository.isSocioValido(tipoSocio, numeroSocio)>0;
    }

    @Override
    public Socio getSocio(TipoSocio tipoSocio, String numeroSocio) {
        return socioRepository.getSocio(tipoSocio, numeroSocio);
    }

    @Override
    public List<Socio> getSociosByName(String criteria) {
        return socioRepository.getSociosByName("%" + criteria + "%");
    }
}
