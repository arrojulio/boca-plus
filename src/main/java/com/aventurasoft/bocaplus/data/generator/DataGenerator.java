package com.aventurasoft.bocaplus.data.generator;

import com.aventurasoft.bocaplus.data.entity.*;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglaAnual;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.entity.security.User;
import com.aventurasoft.bocaplus.data.repository.UserRepository;
import com.aventurasoft.bocaplus.data.service.*;
import com.aventurasoft.bocaplus.data.service.comercio.*;
import com.aventurasoft.bocaplus.data.service.socio.CategoriaSocioService;
import com.aventurasoft.bocaplus.data.service.socio.SocioService;
import com.aventurasoft.bocaplus.data.service.socio.TagSocioService;
import com.vaadin.flow.spring.annotation.SpringComponent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@SpringComponent
public class DataGenerator {

    private ProvinciaService provinciaService;
    private LocalidadService localidadService;
    private CategoriaComercioService categoriaComercioService;
    private CategoriaSocioService categoriaSocioService;
    private ComercioService comercioService;
    private SucursalService sucursalService;
    private PromocionService promocionService;
    private PromocionComercioService promocionComercioService;
    private SocioService socioService;
    private TagComercioService tagComercioService;
    private TagSocioService tagSocioService;
    private VentaService ventaService;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DataGenerator(ProvinciaService provinciaService
        , LocalidadService localidadService
        , CategoriaComercioService categoriaComercioService
        , CategoriaSocioService categoriaSocioService
        , ComercioService comercioService
        , SucursalService sucursalService
        , PromocionService promocionService
        , PromocionComercioService promocionComercioService
        , SocioService socioService
        , TagComercioService tagComercioService
        , TagSocioService tagSocioService
        , VentaService ventaService
        , UserRepository userRepository
        , PasswordEncoder passwordEncoder)
    {
        this.provinciaService = provinciaService;
        this.localidadService = localidadService;
        this.categoriaComercioService = categoriaComercioService;
        this.categoriaSocioService = categoriaSocioService;
        this.comercioService = comercioService;
        this.sucursalService = sucursalService;
        this.promocionService = promocionService;
        this.promocionComercioService = promocionComercioService;
        this.socioService = socioService;
        this.tagComercioService = tagComercioService;
        this.tagSocioService = tagSocioService;
        this.ventaService = ventaService;

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void loadData() {

            Logger logger = LoggerFactory.getLogger(getClass());
            if (provinciaService.count() != 0L) {
                logger.info("Using existing database");
                return;
            }

            logger.info("Generating demo data");

            createAdmin(userRepository, passwordEncoder);
            createComercio(userRepository, passwordEncoder);

            loadProvincia();
            loadLocalidad();
            loadCategoriaComercio();
            loadCategoriaSocio();
            loadComercio();
            loadPromocion();
            loadPromocionComercio();
            loadSocio();
            loadTagComercio();
            loadTagSocio();
            loadVenta();
//            ExampleDataGenerator<Person> personRepositoryGenerator = new ExampleDataGenerator<>(Person.class);
//            personRepositoryGenerator.setData(Person::setId, DataType.ID);
//            personRepositoryGenerator.setData(Person::setFirstName, DataType.FIRST_NAME);
//            personRepositoryGenerator.setData(Person::setLastName, DataType.LAST_NAME);
//            personRepositoryGenerator.setData(Person::setEmail, DataType.EMAIL);
//            personRepositoryGenerator.setData(Person::setPhone, DataType.PHONE_NUMBER);
//            personRepositoryGenerator.setData(Person::setDateOfBirth, DataType.DATE_OF_BIRTH);
//            personRepositoryGenerator.setData(Person::setOccupation, DataType.OCCUPATION);
//            personRepository.saveAll(personRepositoryGenerator.create(100, seed));
//
//

            logger.info("Generated demo data");

    }
    private void loadProvincia()
    {
        Provincia provincia = provinciaService.createNew();
        provincia.setId("CABA"); provincia.setNombre("CABA");
        provinciaService.save(provincia);
        provincia = provinciaService.createNew();
        provincia.setId("SFE"); provincia.setNombre("Santa Fe");
        provinciaService.save(provincia);
        provincia = provinciaService.createNew();
        provincia.setId("MDZ"); provincia.setNombre("Mendoza");
        provinciaService.save(provincia);
        provincia = provinciaService.createNew();
        provincia.setId("CBA"); provincia.setNombre("Cordoba");
        provinciaService.save(provincia);
        provincia = provinciaService.createNew();
        provincia.setId("BA"); provincia.setNombre("Buenos Aires");
        provinciaService.save(provincia);

    }

    private void loadLocalidad()
    {
        Localidad localidad = localidadService.createNew();
        localidad.setId("CABA"); localidad.setNombre("CABA"); localidad.setProvinciaId("CABA");
        localidadService.save(localidad);
        localidad = localidadService.createNew();
        localidad.setId("La Plata"); localidad.setNombre("La Plata"); localidad.setProvinciaId("BA");
        localidadService.save(localidad);
        localidad = localidadService.createNew();
        localidad.setId("Rosario"); localidad.setNombre("Rosario"); localidad.setProvinciaId("SFE");
        localidadService.save(localidad);
        localidad = localidadService.createNew();
        localidad.setId("Cordoba"); localidad.setNombre("Cordoba"); localidad.setProvinciaId("CBA");
        localidadService.save(localidad);
        localidad = localidadService.createNew();
        localidad.setId("Mendoza"); localidad.setNombre("Mendoza"); localidad.setProvinciaId("MDZ");
        localidadService.save(localidad);

    }

    private void loadCategoriaComercio()
    {
        CategoriaComercio categoriaComercio = categoriaComercioService.createNew();
        categoriaComercio.setNombre("Indumentaria");
        categoriaComercioService.save(categoriaComercio);
        categoriaComercio = categoriaComercioService.createNew();
        categoriaComercio.setNombre("Calzado");
        categoriaComercioService.save(categoriaComercio);
        categoriaComercio = categoriaComercioService.createNew();
        categoriaComercio.setNombre("Electrodomestico");
        categoriaComercioService.save(categoriaComercio);
        categoriaComercio = categoriaComercioService.createNew();
        categoriaComercio.setNombre("Supermercado");
        categoriaComercioService.save(categoriaComercio);

    }

    private void loadCategoriaSocio()
    {
        CategoriaSocio categoriaSocio = categoriaSocioService.createNew();
        categoriaSocio.setNombre("Base");
        categoriaSocioService.save(categoriaSocio);
        categoriaSocio = categoriaSocioService.createNew();
        categoriaSocio.setNombre("Destacado");
        categoriaSocioService.save(categoriaSocio);

    }

    private void loadComercio()
    {
        Comercio comercio = comercioService.createNew();
        comercio.setNombre("Comercio 1");comercio.setRazonSocial("Comercio 1 SA");
        comercio.setLocalidadId("CABA");comercio.setProvinciaId("CABA");comercio.setCategoriaComercioId(1);
        comercioService.save(comercio);
        Sucursal sucursal = sucursalService.createNew();
        sucursal.setNombre("PALERMO");
        sucursal.setComercioId(comercio.getId());
        sucursalService.save(sucursal);
        sucursal = sucursalService.createNew();
        sucursal.setNombre("BELGRANO");
        sucursal.setComercioId(comercio.getId());
        sucursalService.save(sucursal);

        comercio = comercioService.createNew();
        comercio.setNombre("Comercio 2");comercio.setRazonSocial("Comercio 2 SA");
        comercio.setLocalidadId("CABA");comercio.setProvinciaId("CABA");comercio.setCategoriaComercioId(2);
        comercioService.save(comercio);
        sucursal = sucursalService.createNew();
        sucursal.setNombre("CENTRAL");
        sucursal.setComercioId(comercio.getId());
        sucursalService.save(sucursal);


    }

    private void loadPromocion()
    {
        Promocion promocion = promocionService.createNew();
        promocion.setNombre("10% Desc");
        promocionService.save(promocion);
        promocion = promocionService.createNew();
        promocion.setNombre("20% Desc");
        promocionService.save(promocion);
        promocion = promocionService.createNew();
        promocion.setNombre("25% Desc");
        promocionService.save(promocion);
        promocion = promocionService.createNew();
        promocion.setNombre("30% Desc");
        promocionService.save(promocion);
        promocion = promocionService.createNew();
        promocion.setNombre("2x1");
        promocionService.save(promocion);

    }
    private void loadPromocionComercio()
    {
        PromocionComercio promocionComercio = promocionComercioService.createNew();
        promocionComercio.setComercioId(1);promocionComercio.setPromocionId(1);
        promocionComercioService.save(promocionComercio);
        promocionComercio = promocionComercioService.createNew();
        promocionComercio.setComercioId(1);promocionComercio.setPromocionId(2);
        promocionComercioService.save(promocionComercio);
        promocionComercio = promocionComercioService.createNew();
        promocionComercio.setComercioId(1);promocionComercio.setPromocionId(3);
        promocionComercioService.save(promocionComercio);
        promocionComercio = promocionComercioService.createNew();
        promocionComercio.setComercioId(2);promocionComercio.setPromocionId(4);
        promocionComercio.setReglaRecurrente(new ReglaAnual());
        promocionComercioService.save(promocionComercio);


    }

    private void loadSocio()
    {
        Socio socio = socioService.createNew();
        socio.setNumeroSocio("123456"); socio.setNombre("Juan"); socio.setApellido("Perez");socio.setCategoriaSocioId(1);
        socioService.save(socio);
        socio = socioService.createNew();
        socio.setNumeroSocio("2222222"); socio.setNombre("Pedro"); socio.setApellido("Perez");socio.setCategoriaSocioId(1);
        socioService.save(socio);
        socio = socioService.createNew();
        socio.setNumeroSocio("3333333"); socio.setNombre("Pablo"); socio.setApellido("Perez");socio.setCategoriaSocioId(1);
        socioService.save(socio);

    }

    private void loadTagComercio()
    {
        TagComercio tagComercio = tagComercioService.createNew();
        tagComercio.setComercioId(1);tagComercio.setTag("Tag 1");
        tagComercioService.save(tagComercio);
        tagComercio = tagComercioService.createNew();
        tagComercio.setComercioId(1);tagComercio.setTag("Tag 2");
        tagComercioService.save(tagComercio);

    }

    private void loadTagSocio()
    {
        TagSocio tagSocio = tagSocioService.createNew();
        tagSocio.setSocioId(1L);tagSocio.setTag("tag socio 1");
        tagSocioService.save(tagSocio);
        tagSocio = tagSocioService.createNew();
        tagSocio.setSocioId(1L);tagSocio.setTag("tag socio 2");
        tagSocioService.save(tagSocio);
        tagSocio = tagSocioService.createNew();
        tagSocio.setSocioId(2L);tagSocio.setTag("tag socio 1");
        tagSocioService.save(tagSocio);


    }

    private void loadVenta()
    {
        Venta venta = ventaService.createNew();
        venta.setFecha(LocalDate.of(2020,1,1));
        venta.setHora(LocalTime.now());
        venta.setSucursalId(1);
        venta.setSocioId(1l);
        venta.setImporte(BigDecimal.valueOf(1000l));
        venta.setPromocionId(1);
        ventaService.save(venta);
    }

    private User createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return userRepository.save(
                createUser("admin@bocaplus.com.ar", "Admin", "Admin", passwordEncoder.encode("admin"), Role.ADMIN, true, null));
    }
    private void createComercio(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        User user;
        user = userRepository.save(
                createUser("comercio2@bocaplus.com.ar", "Comercio 2", "Comercio 2", passwordEncoder.encode("comercio2"), Role.COMERCIO+ ", " +Role.ADMIN, true, 2));


        user = userRepository.save(
                createUser("comercio1@bocaplus.com.ar", "Comercio 1", "Comercio 1", passwordEncoder.encode("comercio1"), Role.COMERCIO, true, 1));

    }

    private User createUser(String email, String firstName, String lastName, String passwordHash, String role,
                            boolean locked, Integer sucursal) {
        User user = new User();
        user.setNew(true);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPasswordHash(passwordHash);
        user.setRole(role);
        user.setLocked(locked);
        user.setSucursal(sucursal);
        return user;
    }
}