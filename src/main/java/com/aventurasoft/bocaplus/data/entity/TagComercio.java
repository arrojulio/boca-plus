package com.aventurasoft.bocaplus.data.entity;

import com.aventurasoft.bocaplus.data.AbstractEntity;
import lombok.Data;

@Data
public class TagComercio extends AbstractEntity<Integer> {
    private Integer comercioId;
    private String tag = "";

}
