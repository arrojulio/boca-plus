package com.aventurasoft.bocaplus.data.service.comercio;

import com.aventurasoft.bocaplus.data.entity.PromocionComercio;
import com.aventurasoft.bocaplus.data.entity.Socio;
import com.aventurasoft.bocaplus.data.entity.Sucursal;
import com.aventurasoft.bocaplus.data.entity.Venta;
import com.aventurasoft.bocaplus.data.repository.PromocionComercioRepository;
import com.aventurasoft.bocaplus.data.repository.SocioRepository;
import com.aventurasoft.bocaplus.data.repository.SucursalRepository;
import com.aventurasoft.bocaplus.data.repository.VentaRepository;
import com.aventurasoft.bocaplus.data.service.UserFriendlyDataException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {
    private VentaRepository ventaRepository;
    private SocioRepository socioRepository;
    private SucursalRepository sucursalRepository;
    private PromocionComercioRepository promocionComercioRepository;

    public VentaServiceImpl(VentaRepository ventaRepository,
                            SocioRepository socioRepository,
                            SucursalRepository sucursalRepository,
                            PromocionComercioRepository promocionComercioRepository) {
        this.ventaRepository = ventaRepository;
        this.socioRepository = socioRepository;
        this.sucursalRepository = sucursalRepository;
        this.promocionComercioRepository = promocionComercioRepository;
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
        Socio socio = socioRepository.findById(socioId)
                .orElseThrow(() -> new UserFriendlyDataException("Socio no encontrado: " + socioId));
        if (!socio.isActivo()) {
            throw new UserFriendlyDataException("Socio inactivo: " + socio.getNumeroSocio());
        }

        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new UserFriendlyDataException("Sucursal no encontrada: " + sucursalId));
        List<PromocionComercio> promociones = promocionComercioRepository
                .getPromocionComerciosByComercioId(sucursal.getComercioId());
        boolean promocionValida = promociones.stream()
                .anyMatch(pc -> pc.getPromocionId().equals(promocionId));
        if (!promocionValida) {
            throw new UserFriendlyDataException("La promocion no pertenece al comercio de la sucursal indicada");
        }

        Venta venta = createNew();
        venta.setSocioId(socioId);
        venta.setSucursalId(sucursalId);
        venta.setPromocionId(promocionId);
        venta.setImporte(importe);

        return ventaRepository.save(venta);
    }
}
