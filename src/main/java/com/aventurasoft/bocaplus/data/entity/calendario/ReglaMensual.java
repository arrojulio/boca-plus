package com.aventurasoft.bocaplus.data.entity.calendario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.dmfs.rfc5545.Weekday;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReglaMensual implements ReglaRecurrente {
    private static final String NAME = "Mensualmente";

    private TipoMensual tipoMensual = TipoMensual.ON;
    private Integer interval = 1;

    //ON
    private List<Integer> byMonthDay = new ArrayList<>();

    //ON_THE
    private List<Weekday> byDay = new ArrayList<>();
    private List<Integer> bySetPos = new ArrayList<>();



    public enum TipoMensual
    {
        ON, ON_THE
    }


    @Override
    public String getRrule() {
        String rrule = "FREQ=MONTHLY;";

        String interval = "INTERVAL=" + getInterval() + ";";

        if (tipoMensual == TipoMensual.ON)
        {
            String byMonthDay = "BYMONTHDAY=" + this.byMonthDay.stream().map(a->String.valueOf(a)).collect(Collectors.joining(",")) + ";";
            return rrule+byMonthDay+interval;
        } else if (tipoMensual == TipoMensual.ON_THE)
        {
            String byDay = "BYDAY=" + getByDay().stream().map(a->String.valueOf(a)).collect(Collectors.joining(",")) + ";";
            String byStepPos = "BYSETPOS=" + getBySetPos().stream().map(a->String.valueOf(a)).collect(Collectors.joining(",")) + ";";
            return rrule+byDay+byStepPos+interval;
        }
        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }
}
