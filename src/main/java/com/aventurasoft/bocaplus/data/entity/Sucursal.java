package com.aventurasoft.bocaplus.data.entity;

import com.aventurasoft.bocaplus.data.AbstractEntity;
import lombok.Data;

@Data
public class Sucursal extends AbstractEntity<Integer> {
    private Integer comercioId = 0;
    private String nombre = "";
    private boolean abierta = true;

    private String direccion = "";
    private int altura = 0;
    private String piso = "";
    private String departamento = "";
    private String barrioId = "";  //Barrio
    private String localidadId = "";    //Localidad
    private String provinciaId = "";    //Provincia

}
