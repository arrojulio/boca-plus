package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.PromocionComercio;
import com.aventurasoft.bocaplus.data.service.CrudService;

import java.util.List;

public interface PromocionComercioService extends CrudService<PromocionComercio, Integer> {
    List<PromocionComercio> getPromocionComerciosByComercioId(Integer comercioId);

    List<PromocionComercio> getPromocionComerciosValidTodayByComercioId(Integer comercioId);

}
