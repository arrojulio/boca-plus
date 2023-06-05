package com.aventurasoft.bocaplus.data.service.data;

import com.aventurasoft.bocaplus.data.query.ResultTable;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.DataSeries;

public interface EstadisticasService {
    DataSeries consumoPorGenero();
    ResultTable historialVentas();
}
