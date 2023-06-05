package com.aventurasoft.bocaplus.data.entity;

import com.aventurasoft.bocaplus.data.AbstractEntity;
import lombok.Data;




@Data
public class Localidad extends AbstractEntity<String> {
    private String nombre = "";
    private String provinciaId;


}
