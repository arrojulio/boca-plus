package com.aventurasoft.bocaplus.data.repository;

import com.aventurasoft.bocaplus.data.entity.Socio;
import com.aventurasoft.bocaplus.data.entity.TipoSocio;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SocioRepository extends CrudRepository<Socio, Long> {
    @Query("SELECT COUNT(*) FROM socio WHERE tipo_socio = :tipoSocio AND numero_socio = :numeroSocio")
    int isSocioValido(TipoSocio tipoSocio, String numeroSocio);

    @Query("SELECT TOP 1 * FROM socio WHERE tipo_socio = :tipoSocio AND numero_socio = :numeroSocio")
    Socio getSocio(TipoSocio tipoSocio, String numeroSocio);

    @Query("SELECT * FROM socio WHERE nombre LIKE :criteria OR apellido LIKE :criteria")
    List<Socio> getSociosByName(String criteria);
}
