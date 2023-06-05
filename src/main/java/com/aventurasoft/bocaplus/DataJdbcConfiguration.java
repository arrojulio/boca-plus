package com.aventurasoft.bocaplus;

import com.aventurasoft.bocaplus.data.entity.calendario.ReglaRecurrente;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglasFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.util.*;

@Configuration
public class DataJdbcConfiguration extends AbstractJdbcConfiguration {
    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        List<Converter> converters = new ArrayList<>();
        converters.add(new Converter<ReglaRecurrente, String> () {
            @Override
            public String convert(ReglaRecurrente reglaRecurrente) {
                return ReglasFactory.toJson(reglaRecurrente);
            }
        });
        converters.add(new Converter<String, ReglaRecurrente> () {
            @Override
            public ReglaRecurrente convert(String json) {
                return ReglasFactory.fromJson(json);
            }
        });
        return new JdbcCustomConversions(converters);

    }
}
