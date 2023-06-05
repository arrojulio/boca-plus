package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.CategoriaComercio;
import com.aventurasoft.bocaplus.data.repository.CategoriaComercioRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaComercioServiceImpl implements CategoriaComercioService {
    private CategoriaComercioRepository categoriaComercioRepository;

    public CategoriaComercioServiceImpl(CategoriaComercioRepository categoriaComercioRepository)
    {
        this.categoriaComercioRepository = categoriaComercioRepository;
    }
    @Override
    public CrudRepository<CategoriaComercio, Integer> getRepository() {
        return categoriaComercioRepository;
    }

    @Override
    public CategoriaComercio createNew() {
        CategoriaComercio categoriaComercio = new CategoriaComercio();
        categoriaComercio.setNew(true);
        return categoriaComercio;
    }


    @Override
    public List<CategoriaComercio> getAllByName() {
        return categoriaComercioRepository.findAllByName();
    }
}
