package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.Sucursal;
import com.aventurasoft.bocaplus.data.repository.SucursalRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalServiceImpl implements SucursalService {
    private SucursalRepository sucursalRepository;
    public SucursalServiceImpl(SucursalRepository sucursalRepository)
    {
        this.sucursalRepository = sucursalRepository;
    }
    @Override
    public CrudRepository<Sucursal, Integer> getRepository() {
        return sucursalRepository;
    }

    @Override
    public Sucursal createNew() {
        Sucursal sucursal = new Sucursal();
        sucursal.setNew(true);
        return sucursal;
    }

    @Override
    public List<Sucursal> getSucursalByComercioId(Integer comercioId) {
        return sucursalRepository.getSucursalByComercioId(comercioId);
    }
}
