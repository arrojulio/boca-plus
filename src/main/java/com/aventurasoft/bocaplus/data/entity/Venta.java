package com.aventurasoft.bocaplus.data.entity;

import com.aventurasoft.bocaplus.data.AbstractEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class Venta extends AbstractEntity<Long> {
    private LocalDate fecha;
    private LocalTime hora;
    private Integer sucursalId;
    private Long socioId;
    private Integer promocionId;
    private BigDecimal importe;


}
