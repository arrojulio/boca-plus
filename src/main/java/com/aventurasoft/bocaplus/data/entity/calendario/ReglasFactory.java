package com.aventurasoft.bocaplus.data.entity.calendario;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ReglasFactory {

    public static String toJson(ReglaRecurrente reglaRecurrente)
    {
        ObjectMapper mapper = new ObjectMapper();
        if (reglaRecurrente == null) return null;
        try {
            return mapper.writeValueAsString(reglaRecurrente);
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return "";
    }

    public static ReglaRecurrente fromJson(String json)
    {
        ObjectMapper mapper = new ObjectMapper();
        if (json == null) return null;
        try {
            return mapper.readValue(json, ReglaRecurrente.class);
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return null;

    }

    public static List<CalendarItem> getCalendarMonths()
    {
        List<CalendarItem> calendarMonths = new ArrayList<>();
        calendarMonths.add(new CalendarItem(1, "Enero"));
        calendarMonths.add(new CalendarItem(2, "Febrero"));
        calendarMonths.add(new CalendarItem(3, "Marzo"));
        calendarMonths.add(new CalendarItem(4, "Abril"));
        calendarMonths.add(new CalendarItem(5, "Mayo"));
        calendarMonths.add(new CalendarItem(6, "Junio"));
        calendarMonths.add(new CalendarItem(7, "Julio"));
        calendarMonths.add(new CalendarItem(8, "Agosto"));
        calendarMonths.add(new CalendarItem(9, "Septiembre"));
        calendarMonths.add(new CalendarItem(10, "Octubre"));
        calendarMonths.add(new CalendarItem(11, "Noviembre"));
        calendarMonths.add(new CalendarItem(12, "Diciembre"));


        return calendarMonths;
    }

    public static CalendarItem getCalendarMonth(Integer monthId)
    {
        for (CalendarItem calendarItem : getCalendarMonths())
            if (calendarItem.getId().equals(monthId.toString())) return calendarItem;

        throw new RuntimeException("MonthId not found");
    }
    public static List<CalendarItem> getCalendarWeekDays()
    {
        List<CalendarItem> calendarItems = new ArrayList<>();
        calendarItems.add(new CalendarItem("MO", "Lunes"));
        calendarItems.add(new CalendarItem("TU", "Martes"));
        calendarItems.add(new CalendarItem("WE", "Miercoles"));
        calendarItems.add(new CalendarItem("TH", "Jueves"));
        calendarItems.add(new CalendarItem("FR", "Viernes"));
        calendarItems.add(new CalendarItem("SA", "Sabado"));
        calendarItems.add(new CalendarItem("SU", "Domingo"));


        return calendarItems;
    }

    public static CalendarItem getWeekDay(String weekId)
    {
        for (CalendarItem calendarItem : getCalendarWeekDays())
            if (calendarItem.getId().equals(weekId)) return calendarItem;


        throw new RuntimeException("WeekId not found");
    }
}
