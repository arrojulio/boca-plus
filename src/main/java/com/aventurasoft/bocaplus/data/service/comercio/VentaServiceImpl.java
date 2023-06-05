package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.Venta;
import com.aventurasoft.bocaplus.data.repository.VentaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {
    private VentaRepository ventaRepository;
    public VentaServiceImpl(VentaRepository ventaRepository)
    {
        this.ventaRepository = ventaRepository;
    }
    @Override
    public CrudRepository<Venta, Long> getRepository() {
        return ventaRepository;
    }

    @Override
    public Venta createNew() {
        Venta venta = new Venta();
        venta.setNew(true);
        venta.setFecha(LocalDate.now());
        venta.setHora(LocalTime.now());
        return venta;
    }

    @Override
    public List<Venta> findAllByDateAndSucursalId() {
        return ventaRepository.findAllByDateAndSucursalId();
    }

    @Override
    public Venta registrarVenta(Integer sucursalId, Integer promocionId, Long socioId, BigDecimal importe) {
        //TODO: validar usuario
        //TODO: validar promocion

        Venta venta = createNew();
        venta.setSocioId(socioId);
        venta.setSucursalId(sucursalId);
        venta.setPromocionId(promocionId);
        venta.setImporte(importe);

        return ventaRepository.save(venta);
    }
}
