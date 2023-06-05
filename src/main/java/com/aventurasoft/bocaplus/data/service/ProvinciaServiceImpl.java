package com.aventurasoft.bocaplus.data.service;

import com.aventurasoft.bocaplus.data.entity.Provincia;
import com.aventurasoft.bocaplus.data.repository.ProvinciaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinciaServiceImpl implements ProvinciaService {
    private ProvinciaRepository provinciaRepository;
    public ProvinciaServiceImpl(ProvinciaRepository provinciaRepository)
    {
        this.provinciaRepository = provinciaRepository;
    }
    @Override
    public CrudRepository<Provincia, String> getRepository() {
        return provinciaRepository;
    }

    @Override
    public Provincia createNew() {
        Provincia provincia = new Provincia();
        provincia.setNew(true);
        return provincia;
    }

    @Override
    public List<Provincia> getAllByName() {
        return provinciaRepository.findAllByName();
    }
}
