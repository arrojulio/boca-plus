package com.aventurasoft.bocaplus.data.entity;

import com.aventurasoft.bocaplus.data.AbstractEntity;
import lombok.Data;



@Data
public class Comercio extends AbstractEntity<Integer> {
    private String nombre = "";
    private String razonSocial = "";
    private String cuit = "";
    private String direccion = "";
    private int altura = 0;
    private String piso = "";
    private String departamento = "";
    private String barrioId = "";
    private String localidadId = "";
    private String provinciaId = "";

    private Integer categoriaComercioId = 0;


}
