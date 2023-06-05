package com.aventurasoft.bocaplus.data.entity.calendario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.dmfs.rfc5545.Weekday;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReglaAnual implements ReglaRecurrente {
    private static final String NAME = "Anualmente";

    @JsonProperty
    private List<Integer> byMonth = new ArrayList<>();


    private TipoAnual tipoAnual = TipoAnual.ON;

    //TipoAnual.ON
    private List<Integer> byMonthDay = new ArrayList<>();

    //TipoAnual
    private List<Weekday> byDay = new ArrayList<>();
    private List<Integer> bySetPos = new ArrayList<>();


    public enum TipoAnual
    {
        ON, //BYMONTH=,BYMONTHDAY=
        ON_THE  //BYDAY=,BYSETPOS=,BYMONTH
    }

    @Override
    public String getRrule() {
        String rrule = "FREQ=YEARLY;";

        //ByMonth
        String byMonth = "BYMONTH=" + this.byMonth.stream().map(a->String.valueOf(a)).collect(Collectors.joining(",")) + ";";

        if (tipoAnual == TipoAnual.ON)
        {
            String byMonthDay = "BYMONTHDAY=" + this.byMonthDay.stream().map(a->String.valueOf(a)).collect(Collectors.joining(",")) + ";";
            return rrule+byMonth+byMonthDay;
        } else if (tipoAnual == TipoAnual.ON_THE)
        {
            String byDay = "BYDAY=" + getByDay().stream().map(a->String.valueOf(a)).collect(Collectors.joining(",")) + ";";
            String byStepPos = "BYSETPOS=" + getBySetPos().stream().map(a->String.valueOf(a)).collect(Collectors.joining(",")) + ";";
            return rrule+byDay+byStepPos+byMonth;
        }
        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }
}
