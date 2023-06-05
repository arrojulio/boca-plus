package com.aventurasoft.bocaplus.data.service.data;

import com.aventurasoft.bocaplus.data.query.ResultTable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public interface DataQueryService {
    ResultTable getDataFromQuery(String query, MapSqlParameterSource args);
}
