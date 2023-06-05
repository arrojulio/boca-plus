package com.aventurasoft.bocaplus.data.entity.calendario;

import lombok.extern.log4j.Log4j2;
import org.dmfs.rfc5545.Weekday;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
class ReglaAnualTest {

    @Test
    void getRrule1() {
        ReglaAnual reglaAnual1 = new ReglaAnual();
        reglaAnual1.setTipoAnual(ReglaAnual.TipoAnual.ON);
        reglaAnual1.getByMonth().add(11);
        reglaAnual1.getByMonth().add(12);
        reglaAnual1.getByMonthDay().add(11);

        log.info("getRrule1 : " + reglaAnual1.getRrule());

        assertTrue(true);
    }

    @Test
    void getRrule2() {
        ReglaAnual reglaAnual = new ReglaAnual();
        reglaAnual.setTipoAnual(ReglaAnual.TipoAnual.ON_THE);
        reglaAnual.getByMonth().add(12);
        reglaAnual.getByDay().add(Weekday.MO);
        reglaAnual.getByDay().add(Weekday.TU);
        reglaAnual.getBySetPos().add(1);

        log.info("getRrule : " + reglaAnual.getRrule());

        assertTrue(true);
    }
}