package com.aventurasoft.bocaplus.data.entity;


import com.aventurasoft.bocaplus.data.AbstractEntity;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglaRecurrente;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PromocionComercio extends AbstractEntity<Integer> {
    private Integer comercioId;
    private Integer promocionId;

    private LocalDate startDate;
    private LocalDate endDate;
    private ReglaRecurrente reglaRecurrente;

}
