package com.aventurasoft.bocaplus.data.service.data;

import com.aventurasoft.bocaplus.data.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Service
public class DataQueryServiceImpl implements DataQueryService {

    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    public DataQueryServiceImpl(NamedParameterJdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResultTable getDataFromQuery(String query, MapSqlParameterSource args) {

        return jdbcTemplate.query(query, args,  new ResultSetExtractor<ResultTable>() {
            @Override
            public ResultTable extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                ResultSetMetaData rsmd = resultSet.getMetaData();
                ResultTableDataResolverWriter writer = new ResultTableDataResolverWriter();

                writer.begingSession();

                for (int i=1 ; i<=rsmd.getColumnCount(); i++)
                {

                    String sourceColumnName = null;
                    sourceColumnName = rsmd.getColumnName(i);
                    if (sourceColumnName.isEmpty()) sourceColumnName = rsmd.getColumnLabel(i);

                    String columnName = sourceColumnName;

                    if (columnName == null)
                    {
                        throw new DataResolverException("No mapping found for sourceColumn " + sourceColumnName);

                    }
                    DataSetFieldType dataType = ResultSetConverter.getDataSetFieldType(rsmd, i);
                    boolean isAggregable = ResultSetConverter.isNumeric(rsmd, i);
                    writer.addDestinationColumn(columnName, dataType, isAggregable, rsmd.getPrecision(i));

                }


                while (resultSet.next())
                {
                    writer.newLine();
                    for (int i=1; i<=rsmd.getColumnCount(); i++)
                    {
                        writer.addValueToLine(ResultSetConverter.getDataTyped(resultSet, rsmd, i));

                    }
                    writer.saveLine();
                }

                writer.endSession();

                return writer.getResultTable();
            }
        });
    }
}
