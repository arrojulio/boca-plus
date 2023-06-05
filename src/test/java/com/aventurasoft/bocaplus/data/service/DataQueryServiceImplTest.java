package com.aventurasoft.bocaplus.data.service;

import com.aventurasoft.bocaplus.data.query.ResultTable;
import com.aventurasoft.bocaplus.data.service.data.DataQueryService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Log4j2
public class DataQueryServiceImplTest {

    @Autowired
    DataQueryService dataQueryService;
    @Test
    void getDataFromQuery() {
        MapSqlParameterSource args = new MapSqlParameterSource();
        List<Integer> ids = new ArrayList<>();
        //ids.add(1);
        ids.add(2);

        args.addValue("ids", ids);

        ResultTable resultTable = dataQueryService.getDataFromQuery("SELECT * FROM comercio WHERE id IN (:ids)", args);

        assertNotNull(resultTable);
        log.info(resultTable.toString());
    }
}