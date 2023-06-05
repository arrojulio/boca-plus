package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.Comercio;
import com.aventurasoft.bocaplus.data.repository.ComercioRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComercioServiceImpl implements ComercioService {
    private ComercioRepository comercioRepository;
    public ComercioServiceImpl(ComercioRepository comercioRepository)
    {
        this.comercioRepository = comercioRepository;
    }
    @Override
    public CrudRepository<Comercio, Integer> getRepository() {
        return comercioRepository;
    }

    @Override
    public Comercio createNew() {
        Comercio comercio = new Comercio();
        comercio.setNew(true);
        return comercio;
    }

    @Override
    public List<Comercio> getAllByName() {
        return comercioRepository.findAllByNombre();
    }
}
