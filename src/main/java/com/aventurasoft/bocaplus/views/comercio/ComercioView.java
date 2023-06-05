package com.aventurasoft.bocaplus.views.comercio;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.aventurasoft.bocaplus.data.entity.security.Role;
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
import org.springframework.security.access.annotation.Secured;


@Route(value = "comercio", layout = MainView.class)
@PageTitle("Comercio")
@CssImport(value = "./styles/views/comercio/comercio-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
@Secured(Role.COMERCIO)
public class ComercioView extends Div implements AfterNavigationObserver {

    private Grid<HealthGridItem> grid = new Grid<>();

    private Chart totalVentas = new Chart();
    private Chart chartPieEdades = new Chart();
    private final H2 totalVentasValor = new H2();
    private final H2 totalVentasMesValor = new H2();
    private final H2 nuevosClientesValor = new H2();

    public ComercioView() {
        setId("comercio-view");
        Board board = new Board();
        board.addRow(
                createBadge("Total ventas", totalVentasValor, "primary-text", "Ventas vinculadas a BocaPlus", "badge"),
                createBadge("Mensual", totalVentasMesValor, "success-text", "Ventas mes en curso", "badge success"),
                createBadge("Nuevos clientes", nuevosClientesValor, "success-text","Nuevos clientes bajo BocaPlus", "badge success")
        );

        totalVentas.getConfiguration()
                .setTitle("Historico Ventas");
        totalVentas.getConfiguration().getChart().setType(ChartType.SPLINE);
        totalVentas.setTimeline(true);
        WrapperCard monthlyVisitorsWrapper = new WrapperCard("wrapper",
                new Component[] {totalVentas}, "card");
        board.add(monthlyVisitorsWrapper);

        grid.addColumn(HealthGridItem::getCity).setHeader("Sucursal");
        grid.addColumn(new ComponentRenderer<>(item -> {
            Span span = new Span(item.getStatus());
            span.getElement().getThemeList().add(item.getTheme());
            return span;
        })).setFlexGrow(0).setWidth("100px").setHeader("Estado");
        //grid.addColumn(HealthGridItem::getItemDate).setHeader("Date")
        //        .setWidth("140px");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        WrapperCard gridWrapper = new WrapperCard("wrapper",
                new Component[] { new H3("Desempeño sucursales"), grid }, "card");

        chartPieEdades.getConfiguration().setTitle("Clientes por edades");
        chartPieEdades.getConfiguration().getChart().setType(ChartType.PIE);
        WrapperCard responseTimesWrapper = new WrapperCard("wrapper",
                new Component[] {chartPieEdades}, "card");
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
        totalVentasValor.setText("$1,234,984");
        totalVentasMesValor.setText("$345,503");
        nuevosClientesValor.setText("1,304");

        // First chart
        Configuration configuration = totalVentas.getConfiguration();
        configuration.getTooltip().setEnabled(true);
        Tooltip tooltip = new Tooltip();
        tooltip.setPointFormat("<span>{series.name}</span>: <b>{point.y}</b> <br/>");
        tooltip.setValueDecimals(2);
        configuration.setTooltip(tooltip);
        //configuration.addSeries(new ListSeries("Tokyo", 49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4,
        //        194.1, 95.6, 54.4));
        //configuration.addSeries(
        //       new ListSeries("New York", 83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3));
        //configuration.addSeries(
        //        new ListSeries("London", 48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2));
        //configuration.addSeries(
        //        new ListSeries("Berlin", 42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1));

        //XAxis x = new XAxis();
        //x.setCrosshair(new Crosshair());
        //x.setCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        //configuration.addxAxis(x);



        YAxis yAxis = new YAxis();
        Labels label = new Labels();
        label.setFormatter("function() { return (this.value.toString().replace(/(\\d)(?=(\\d{3})+(?!\\d))/g, '$1,')); }");
        yAxis.setLabels(label);
        configuration.addyAxis(yAxis);

        DataSeries ventas = new DataSeries("Ventas");
        DataSeriesItem item;
        item = new DataSeriesItem();
        item.setX(Date.valueOf(LocalDate.of(2020,1,1)));
        item.setY(34056);
        ventas.add(item);
        item = new DataSeriesItem();
        item.setX(Date.valueOf(LocalDate.of(2020,2,1)));
        item.setY(54604);
        ventas.add(item);
        item = new DataSeriesItem();
        item.setX(Date.valueOf(LocalDate.of(2020,3,1)));
        item.setY(45322);
        ventas.add(item);
        item = new DataSeriesItem();
        item.setX(Date.valueOf(LocalDate.of(2020,4,1)));
        item.setY(32456);
        ventas.add(item);
        configuration.setSeries(ventas);



        // Grid
        List<HealthGridItem> gridItems = new ArrayList<>();
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Palermo", "Germany", "Good", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Belgrano", "Romania", "Excellent", "badge success"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "Caballito", "Mexico", "Good", "badge"));
        gridItems.add(new HealthGridItem(LocalDate.of(2019, 1, 14), "La Boca", "Japan", "Excellent", "badge success"));


        grid.setItems(gridItems);

        // Second chart
        configuration = chartPieEdades.getConfiguration();
        PlotOptionsPie options = new PlotOptionsPie();
        options.setInnerSize("0");
        options.setSize("75%");  // Default
        options.setCenter("50%", "50%"); // Default
        configuration.setPlotOptions(options);
        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("0-20", 15));
        series.add(new DataSeriesItem("20-30", 30));
        series.add(new DataSeriesItem("30-40", 25));
        series.add(new DataSeriesItem("40-50", 10));
        series.add(new DataSeriesItem("50-60", 15));
        series.add(new DataSeriesItem("> 60", 15));
        configuration.addSeries(series);
    }
}
