package com.aventurasoft.bocaplus.data.entity;

import com.aventurasoft.bocaplus.data.AbstractEntity;
import lombok.Data;

import java.time.LocalDate;


@Data
public class Socio extends AbstractEntity<Long> {
    private TipoSocio tipoSocio = TipoSocio.Regular;
    private String numeroSocio = "";
    private String nombre = "";
    private String apellido = "";
    private String dni = "";
    private boolean activo = true;
    private LocalDate fechaNacimiento = LocalDate.of(1980, 1, 1);
    private Genero genero = Genero.SIN_DEFINIR;
    private String direccion = "";
    private int altura = 0;
    private String piso = "";
    private String departamento = "";
    private String barrioId = "";  //Barrio
    private String localidadId = "";    //Localidad
    private String provinciaId = "";    //Provincia
    private Integer categoriaSocioId = 0;

    public String getApellidoNombre()
    {
        return getApellido() + ", " + getNombre();
    }
}
