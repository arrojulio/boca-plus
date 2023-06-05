package com.aventurasoft.bocaplus.data.entity.calendario;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.dmfs.rfc5545.Weekday;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class ReglasFactoryTest {



    @Test
    public void toJson() {
        ReglaAnual reglaAnual = new ReglaAnual();

        log.info("To json = " + ReglasFactory.toJson(reglaAnual));

        assertTrue(true);
    }

    @Test
    public void fromJson()
    {
        String json = "{\"type\":\"reglaAnual\",\"byMonth\":[11,12],\"tipoAnual\":\"ON\",\"byMonthDay\":[4,5],\"byDay\":[1],\"bySetPos\":[1,2]}";

        ReglaRecurrente result = ReglasFactory.fromJson(json);
        assertNotNull(result);
        log.info(result.toString());

    }

    @Test void toJson2()
    {
        ReglaMensual reglaMensual = new ReglaMensual();
        reglaMensual.setTipoMensual(ReglaMensual.TipoMensual.ON_THE);
        reglaMensual.getByDay().add(Weekday.MO);
        reglaMensual.getByMonthDay().add(2);
        reglaMensual.getBySetPos().add(1);

        log.info(ReglasFactory.toJson(reglaMensual));

        assertTrue(true);
    }
    @Test
    public void fromJson2()
    {
        String json = "{\"type\":\"reglaMensual\",\"tipoMensual\":\"ON_THE\",\"interval\":1,\"byMonthDay\":[2],\"byDay\":[\"MO\"],\"bySetPos\":[1]}";

        ReglaRecurrente result = ReglasFactory.fromJson(json);
        assertNotNull(result);
        log.info(result.toString());

    }

    @Test
    public void toJson3()
    {
        ReglaSemanal regla = new ReglaSemanal();
        regla.getByDay().add(Weekday.MO);
        regla.getByDay().add(Weekday.FR);

        log.info(ReglasFactory.toJson(regla));
        assertTrue(true);

    }

    @Test
    public void fromJson3()
    {
        String json = "{\"type\":\"reglaSemanal\",\"interval\":1,\"byDay\":[\"MO\",\"FR\"]}";
        ReglaRecurrente regla = ReglasFactory.fromJson(json);
        assertNotNull(regla);
        log.info(regla);
    }

    @Test
    public void toJson4()
    {
        ReglaDiaria regla = new ReglaDiaria();
        log.info(ReglasFactory.toJson(regla));
        assertTrue(true);
    }
    @Test
    public void fromJson4()
    {
        String json = "{\"type\":\"reglaDiaria\",\"interval\":1}";
        ReglaRecurrente regla = ReglasFactory.fromJson(json);
        assertNotNull(regla);
        log.info(regla);
    }
}