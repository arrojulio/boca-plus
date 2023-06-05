package com.aventurasoft.bocaplus.data.service;

import com.aventurasoft.bocaplus.data.entity.Localidad;
import com.aventurasoft.bocaplus.data.repository.LocalidadRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalidadServiceImpl implements LocalidadService {
    private LocalidadRepository localidadRepository;
    public LocalidadServiceImpl(LocalidadRepository localidadRepository)
    {
        this.localidadRepository = localidadRepository;
    }
    @Override
    public CrudRepository<Localidad, String> getRepository() {
        return localidadRepository;
    }

    @Override
    public Localidad createNew() {
        Localidad localidad = new Localidad();
        localidad.setNew(true);
        return localidad;
    }

    @Override
    public List<Localidad> getAllByName() {
        return localidadRepository.findAllByName();
    }

    @Override
    public List<Localidad> getLocalidadesByProvinciaId(String provinciaId) {
        return localidadRepository.getLocalidadesByProvinciaId(provinciaId);
    }
}
