package com.aventurasoft.bocaplus.views.util;

import com.aventurasoft.bocaplus.data.query.ResultTable;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.html.ListItem;

import java.util.ArrayList;
import java.util.List;

public class ChartDataUtils {

    //Lee la primer columna del resultado y la agrega como categoria
    public static void addCategories(ResultTable resultTable, XAxis xAxis)
    {
        for ( List<Object> row : resultTable.getElements())
        {
            xAxis.addCategory(row.get(0).toString());
        }
    }

    public static ListSeries getListSeriesFromColumn(ResultTable resultTable, int column)
    {
        ListSeries listSeries = new ListSeries(resultTable.getColumns().get(column).getName());
        for (List<Object> row : resultTable.getElements())
            listSeries.addData((Number) row.get(column));
        return listSeries;
    }

    public static List<ListSeries> getListSeriesFromColumnRange(ResultTable resultTable, int fromColumn, int toColumn)
    {
        List<ListSeries> listSeries = new ArrayList<>();
        for (int column = fromColumn; column <= toColumn; column++)
            listSeries.add(getListSeriesFromColumn(resultTable, column));
        return listSeries;
    }

    public static DataSeriesItem getDataSeriesItemFromColumn(ResultTable resultTable, int column)
    {
        return new DataSeriesItem(
                resultTable.getColumns().get(column).getName()
                , (Number) resultTable.getElements().get(0).get(column)
            );

    }
    public static List<DataSeriesItem> getDataSeriesItemFromColumnRange(ResultTable resultTable, int fromColumn, int toColumn)
    {
        List<DataSeriesItem> dataSeriesItems = new ArrayList<>();
        for (int column=fromColumn; column<= toColumn; column++)
            dataSeriesItems.add(getDataSeriesItemFromColumn(resultTable, column));
        return dataSeriesItems;
    }

    public static List<DataSeriesItem> getDataSeriesItemsFromRows(ResultTable resultTable, int nameColumn, int yColumn)
    {
        List<DataSeriesItem> dataSeriesItems = new ArrayList<>();
        for (List<Object> row : resultTable.getElements())
            dataSeriesItems.add(new DataSeriesItem(row.get(nameColumn).toString(), (Number) row.get(yColumn)));
        return dataSeriesItems;
    }



}
