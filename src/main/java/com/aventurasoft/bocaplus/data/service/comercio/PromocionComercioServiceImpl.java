package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.PromocionComercio;
import com.aventurasoft.bocaplus.data.repository.PromocionComercioRepository;
import com.aventurasoft.bocaplus.data.service.CalendarioService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromocionComercioServiceImpl implements PromocionComercioService {
    private PromocionComercioRepository promocionComercioRepository;
    private CalendarioService calendarioService;
    public PromocionComercioServiceImpl(PromocionComercioRepository promocionComercioRepository, CalendarioService calendarioService)
    {
        this.promocionComercioRepository = promocionComercioRepository;
        this.calendarioService = calendarioService;
    }
    @Override
    public CrudRepository<PromocionComercio, Integer> getRepository() {
        return promocionComercioRepository;
    }

    @Override
    public PromocionComercio createNew()
    {
        PromocionComercio promocionComercio = new PromocionComercio();
        promocionComercio.setNew(true);
        return promocionComercio;
    }

    @Override
    public List<PromocionComercio> getPromocionComerciosByComercioId(Integer comercioId) {
        return promocionComercioRepository.getPromocionComerciosByComercioId(comercioId);
    }

    @Override
    public List<PromocionComercio> getPromocionComerciosValidTodayByComercioId(Integer comercioId) {
        List<PromocionComercio> promocionComercios = new ArrayList<>();
        for ( PromocionComercio promocionComercio : this.getPromocionComerciosByComercioId(comercioId))
        {

            if (promocionComercio.getReglaRecurrente() != null && calendarioService.isDatePositive(LocalDate.now(), promocionComercio.getReglaRecurrente().getRrule(), promocionComercio.getStartDate(), promocionComercio.getEndDate()))
                promocionComercios.add(promocionComercio);

        }

        return promocionComercios;
    }
}
