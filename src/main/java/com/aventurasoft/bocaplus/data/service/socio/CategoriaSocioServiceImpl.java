package com.aventurasoft.bocaplus.data.service.socio;

import com.aventurasoft.bocaplus.data.entity.CategoriaSocio;
import com.aventurasoft.bocaplus.data.repository.CategoriaSocioRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaSocioServiceImpl implements CategoriaSocioService {
    private CategoriaSocioRepository categoriaSocioRepository;
    public CategoriaSocioServiceImpl(CategoriaSocioRepository categoriaSocioRepository)
    {
        this.categoriaSocioRepository = categoriaSocioRepository;
    }
    @Override
    public CrudRepository<CategoriaSocio, Integer> getRepository() {
        return categoriaSocioRepository;
    }

    @Override
    public CategoriaSocio createNew() {
        CategoriaSocio categoriaSocio = new CategoriaSocio();
        categoriaSocio.setNew(true);
        return categoriaSocio;
    }

    @Override
    public List<CategoriaSocio> getAllByName() {
        return categoriaSocioRepository.findAllByName();
    }
}
