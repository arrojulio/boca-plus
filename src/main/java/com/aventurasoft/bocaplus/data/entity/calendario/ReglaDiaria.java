package com.aventurasoft.bocaplus.data.entity.calendario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ReglaDiaria implements ReglaRecurrente {
    @JsonIgnore
    private static final String NAME = "Diariamente";

    private int interval=1;

    @Override
    public String getRrule() {
        String rrule = "FREQ=DAILY;";

        String interval = "INTERVAL=" + getInterval() + ";";

        return rrule+interval;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return NAME;
    }
}
