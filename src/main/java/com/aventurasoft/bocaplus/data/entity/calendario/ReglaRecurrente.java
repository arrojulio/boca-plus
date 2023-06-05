package com.aventurasoft.bocaplus.data.entity.calendario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReglaAnual.class, name = "reglaAnual"),
        @JsonSubTypes.Type(value = ReglaMensual.class, name = "reglaMensual"),
        @JsonSubTypes.Type(value = ReglaSemanal.class, name = "reglaSemanal"),
        @JsonSubTypes.Type(value = ReglaDiaria.class, name = "reglaDiaria")
})
public interface ReglaRecurrente {
    @JsonIgnore
    String getName();
    @JsonIgnore
    String getRrule();

}
