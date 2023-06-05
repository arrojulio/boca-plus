package com.aventurasoft.bocaplus.data.entity.calendario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.dmfs.rfc5545.Weekday;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReglaSemanal implements ReglaRecurrente {
    @JsonIgnore
    private static final String NAME = "Semanalmente";


    private Integer interval=1;
    private List<Weekday> byDay = new ArrayList<>();

    @Override
    @JsonIgnore
    public String getRrule() {
        String rrule = "FREQ=MONTHLY;";

        String interval = "INTERVAL=" + getInterval() + ";";

        String byDay = "BYDAY=" + getByDay().stream().map(a->String.valueOf(a)).collect(Collectors.joining(",")) + ";";

        return rrule+byDay+interval;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
