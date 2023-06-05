package com.aventurasoft.bocaplus.data.entity;

import com.aventurasoft.bocaplus.data.AbstractEntity;
import lombok.Data;

@Data
public class TagSocio extends AbstractEntity<Integer> {
    private Long socioId;
    private String tag;

}
