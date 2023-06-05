package com.aventurasoft.bocaplus.data.service.data;

import com.aventurasoft.bocaplus.data.query.ResultTable;
import com.aventurasoft.bocaplus.views.util.ChartDataUtils;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class EstadisticasServiceImpl implements EstadisticasService {

    DataQueryService dataQueryService;

    @Autowired
    public EstadisticasServiceImpl(DataQueryService dataQueryService)
    {
        this.dataQueryService = dataQueryService;
    }
    @Override
    public DataSeries consumoPorGenero() {
        String query = "SELECT s.genero, SUM(v.importe) importe FROM venta v INNER JOIN socio s ON v.socio_id = s.id GROUP BY s.genero";
        ResultTable resultTable = dataQueryService.getDataFromQuery(query, new MapSqlParameterSource());


        DataSeries dataSeries = new DataSeries(ChartDataUtils.getDataSeriesItemsFromRows(resultTable, 0, 1));

        return dataSeries;
    }

    @Override
    public ResultTable historialVentas()
    {
        String query = "SELECT TO_CHAR(v.fecha, 'YYYY-MM') fecha, SUM(v.importe) importe FROM venta v GROUP BY TO_CHAR(v.fecha, 'YYYY-MM') ORDER BY v.fecha";
        return dataQueryService.getDataFromQuery(query, new MapSqlParameterSource());

    }
}
