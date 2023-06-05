package com.aventurasoft.bocaplus.views.estadisticas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.query.ResultTable;
import com.aventurasoft.bocaplus.data.service.data.EstadisticasService;
import com.aventurasoft.bocaplus.views.util.ChartDataUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "stats", layout = MainView.class)
@PageTitle("Estadisticas")
@CssImport(value = "./styles/views/estadisticas/estadisticas-view.css", include = "lumo-badge")
@CssImport(value = "./styles/custom-chart-styles.css", themeFor = "vaadin-chart", include = "vaadin-chart-default-theme")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
@Secured(Role.ADMIN)
//@RouteAlias(value = "", layout = MainView.class)
public class EstadisticasView extends Div implements AfterNavigationObserver {

    private Grid<HealthGridItem> grid = new Grid<>();

    private Chart consumoPorCiudad = new Chart();
    private Chart consumoPorRubro = new Chart(ChartType.PIE);
    private final H2 comerciosH2 = new H2();
    private final H2 sociosH2 = new H2();
    private final H2 operacionesH2 = new H2();

    private EstadisticasService estadisticasService;

    @Autowired
    public EstadisticasView(EstadisticasService estadisticasService) {

        this.estadisticasService= estadisticasService;


        setId("estadisticas-view");
        Board board = new Board();
        board.addRow(
                createBadge("Comercios", comerciosH2, "primary-text", "Cantidad de comercios adheridos", "badge"),
                createBadge("Socios", sociosH2, "success-text", "Socios activos en sistema", "badge success"),
                createBadge("Actividad", operacionesH2, "error-text","Total operaciones", "badge error")
        );

        consumoPorCiudad.getConfiguration()
                .setTitle("Total ventas por ciudad");
        consumoPorCiudad.getConfiguration().getChart().setType(ChartType.COLUMN);
        WrapperCard monthlyVisitorsWrapper = new WrapperCard("wrapper",
                new Component[] {consumoPorCiudad}, "card");
        board.add(monthlyVisitorsWrapper);

        grid.addColumn(HealthGridItem::getCity).setHeader("Ciudad");
        grid.addColumn(new ComponentRenderer<>(item -> {
            Span span = new Span(item.getStatus());
            span.getElement().getThemeList().add(item.getTheme());
            return span;
        })).setFlexGrow(0).setWidth("100px").setHeader("Estado");
        grid.addColumn(HealthGridItem::getItemDate).setHeader("Fecha")
                .setWidth("140px");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        WrapperCard gridWrapper = new WrapperCard("wrapper",
                new Component[] { new H3("Desempeño por ciudad"), grid }, "card");
        consumoPorRubro.getConfiguration().setTitle("Consumo por rubro");
        WrapperCard responseTimesWrapper = new WrapperCard("wrapper",
                new Component[] {consumoPorRubro}, "card");
        board.addRow(gridWrapper, responseTimesWrapper);

        add(board);
    }

    private WrapperCard createBadge(String title, H2 h2, String h2ClassName,
            String description, String badgeTheme) {
        Span titleSpan = new Span(title);
        titleSpan.getElement().setAttribute("theme", badgeTheme);

        h2.addClassName(h2ClassName);

        Span descriptionSpan = new Span(description);
        descriptionSpan.addClassName("secondary-text");

        return new WrapperCard("wrapper",
                new Component[] { titleSpan, h2, descriptionSpan }, "card",
                "space-m");
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Set some data when this view is displayed.

        // Top row widgets
        comerciosH2.setText("5.234");
        sociosH2.setText("235k");

        operacionesH2.setText("10.235");

        // First chart
        Configuration configuration = consumoPorCiudad.getConfiguration();
        //configuration.addSeries(new ListSeries("CABA", 49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,
        //        194.1, 95.6, 54.4));
        //configuration.addSeries(
        //        new ListSeries("Cordoba", 83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3));
        //configuration.addSeries(
        //        new ListSeries("Rosario", 48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2));
        //configuration.addSeries(
         //       new ListSeries("Mendoza", 42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1));

        XAxis x = new XAxis();
        //x.setCrosshair(new Crosshair());
        //x.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        //configuration.addxAxis(x);
        ResultTable historialVentas = estadisticasService.historialVentas();
        ChartDataUtils.addCategories(historialVentas, x);
        configuration.addxAxis(x);

        configuration.addSeries(ChartDataUtils.getListSeriesFromColumn(historialVentas, 1));

        YAxis y = new YAxis();
        y.setMin(0);
        configuration.addyAxis(y);

        // Grid
        List<HealthGridItem> gridItems = new ArrayList<>();
        gridItems.add(new HealthGridItem(LocalDate.of(2020, 1, 14), "CABA", "CABA", "Bueno", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2020, 1, 14), "Cordoba", "Cordoba", "Medio", "badge error"));
        gridItems.add(new HealthGridItem(LocalDate.of(2020, 1, 14), "Rosario", "Santa Fe", "Bueno", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2020, 1, 14), "Mendoza", "Mendoza", "Excelente", "badge success"));

        grid.setItems(gridItems);

        // Second chart
        //consumoPorRubro = new Chart(ChartType.PIE);
        configuration = consumoPorRubro.getConfiguration();


        PlotOptionsPie options = new PlotOptionsPie();
        options.setInnerSize("0");
        options.setSize("75%");  // Default
        options.setCenter("50%", "50%"); // Default
        //configuration.setPlotOptions(options);



        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Ropa", 4900));
        series.add(new DataSeriesItem("Supermercados", 12100));
        series.add(new DataSeriesItem("Turismo", 3200));
        series.add(new DataSeriesItem("Calzado", 5030));
        series.add(new DataSeriesItem("Electrodomesticos", 23405));


        //configuration.addSeries(series);
        configuration.addSeries(estadisticasService.consumoPorGenero());

        consumoPorRubro.addClassName("first-chart");
        configuration.getChart().setClassName("first-chart");



    }
}
