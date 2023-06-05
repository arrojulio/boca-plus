package com.aventurasoft.bocaplus.data.service;

import com.aventurasoft.bocaplus.data.entity.Promocion;
import com.aventurasoft.bocaplus.data.entity.PromocionComercio;
import com.aventurasoft.bocaplus.data.repository.PromocionComercioRepository;
import com.aventurasoft.bocaplus.data.repository.PromocionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromocionServiceImpl implements PromocionService {
    private PromocionRepository promocionRepository;
    private PromocionComercioRepository promocionComercioRepository;


    public PromocionServiceImpl(PromocionRepository promocionRepository
        , PromocionComercioRepository promocionComercioRepository)
    {
        this.promocionRepository = promocionRepository;
        this.promocionComercioRepository = promocionComercioRepository;
    }
    @Override
    public CrudRepository<Promocion, Integer> getRepository() {
        return promocionRepository;
    }

    @Override
    public Promocion createNew() {
        Promocion promocion = new Promocion();
        promocion.setNew(true);
        return promocion;
    }

    @Override
    public List<Promocion> getPromocionesByComercioId(Integer comercioId) {
        return promocionRepository.getPromocionesByComercioId(comercioId);
    }

    @Override
    public List<Promocion> getAllByName() {
        return promocionRepository.findAllByName();
    }


}
