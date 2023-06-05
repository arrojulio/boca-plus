package com.aventurasoft.bocaplus.views.forms;

import com.aventurasoft.bocaplus.data.entity.calendario.CalendarItem;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglaAnual;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglasFactory;
import com.vaadin.componentfactory.Tooltip;
import com.vaadin.componentfactory.TooltipAlignment;
import com.vaadin.componentfactory.TooltipPosition;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.ReusableMessage;
import org.dmfs.rfc5545.Weekday;
import org.springframework.data.relational.core.sql.In;
import org.vaadin.gatanaso.MultiselectComboBox;


import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReglaAnualForm  extends AbstractCompositeField<HorizontalLayout, ReglaAnualForm, ReglaAnual> {
    private RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();

    private MultiselectComboBox<CalendarItem> onByMonth = new MultiselectComboBox<>("Mes");    //meses
    private MultiselectComboBox<Integer> onMonthDay = new MultiselectComboBox<>("Dias del mes");    //dias del mes

    private TextField onTheBySetPos = new TextField("Relativo a");  //relativo a - ENTERO
    private Tooltip onTheBySetPosTooltip = new Tooltip();
    private MultiselectComboBox<CalendarItem> onTheByDay = new MultiselectComboBox<>("Dias de semana"); //dias de la semana
    private MultiselectComboBox<CalendarItem> onTheByMonth = new MultiselectComboBox<>("Meses"); //meses

    private HorizontalLayout onLayout = new HorizontalLayout();
    private VerticalLayout onTheLayout = new VerticalLayout();


    private Binder<ReglaAnual> binder;

    public enum Options
    {
        Fecha, Relativo
    }
    public ReglaAnualForm()
    {
        super(null);

        onTheBySetPosTooltip.attachToComponent(onTheBySetPos);
        onTheBySetPosTooltip.setPosition(TooltipPosition.RIGHT);
        onTheBySetPosTooltip.setAlignment(TooltipAlignment.LEFT);
        onTheBySetPosTooltip.add(new Paragraph("1: primero, 2: segundo,.., -1: ultimo"));
        onTheBySetPosTooltip.add(new Paragraph("lista separada por comas"));
        onTheBySetPosTooltip.add(new Paragraph("1,2,3"));


        radioButtonGroup.setItems(Options.Fecha.toString(), Options.Relativo.toString());
        radioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioButtonGroup.addValueChangeListener(e -> {
            if (e.getValue() != null)
            {


                if (e.getValue().equals(Options.Fecha.toString()))
                {
                    onLayout.setVisible(true); onTheLayout.setVisible(false);

                } else
                {
                    onLayout.setVisible(false); onTheLayout.setVisible(true);


                }
            }

        });


        onByMonth.setItems(ReglasFactory.getCalendarMonths());
        onByMonth.setItemLabelGenerator(CalendarItem::getName);

        List<Integer> days = new ArrayList<>();
        for(int d=1 ; d<=31 ; d++)
            days.add(d);
        onMonthDay.setItems(days);
        onLayout.add(onByMonth, onMonthDay);


        onTheByDay.setItemLabelGenerator(CalendarItem::getName);
        onTheByDay.setItems(ReglasFactory.getCalendarWeekDays());
        onTheByMonth.setItems(ReglasFactory.getCalendarMonths());
        onTheByMonth.setItemLabelGenerator(CalendarItem::getName);


        onTheLayout.add(onTheByDay, onTheByMonth, onTheBySetPos, onTheBySetPosTooltip);

        VerticalLayout verticalLayout = new VerticalLayout(onLayout, onTheLayout);
        getContent().add(radioButtonGroup, verticalLayout);

        binder = new Binder<>(ReglaAnual.class);
        binder.forField(radioButtonGroup).withConverter(new Converter<String, ReglaAnual.TipoAnual>() {
            @Override
            public Result<ReglaAnual.TipoAnual> convertToModel(String s, ValueContext valueContext) {
                if (s == null) return Result.ok(ReglaAnual.TipoAnual.ON);

                if (s.equals(Options.Fecha.toString()))
                    return Result.ok(ReglaAnual.TipoAnual.ON);
                else
                    return Result.ok(ReglaAnual.TipoAnual.ON_THE);

            }

            @Override
            public String convertToPresentation(ReglaAnual.TipoAnual tipoAnual, ValueContext valueContext) {
                if (tipoAnual == null) return "";

                if (tipoAnual == ReglaAnual.TipoAnual.ON)
                    return Options.Fecha.toString();
                else
                    return Options.Relativo.toString();

            }
        }).bind(ReglaAnual::getTipoAnual, ReglaAnual::setTipoAnual);
        binder.forField(onByMonth).withConverter(new Converter<Set<CalendarItem>, List<Integer>>() {
            @Override
            public Result<List<Integer>> convertToModel(Set<CalendarItem> calendarItems, ValueContext valueContext) {
                List<Integer> list = new ArrayList<>();

                if (calendarItems == null) Result.ok(list);

                for (CalendarItem calendarItem : calendarItems)
                    list.add(Integer.valueOf(calendarItem.getId()));

                return Result.ok(list);
            }

            @Override
            public Set<CalendarItem> convertToPresentation(List<Integer> integers, ValueContext valueContext) {
                if (integers == null) return new HashSet<>();

                Set<CalendarItem> set = new HashSet<>();
                for (Integer id : integers)
                    set.add(ReglasFactory.getCalendarMonth(id));

                return set;
            }
        }).bind(ReglaAnual::getByMonth, ReglaAnual::setByMonth);

        binder.forField(onMonthDay).withConverter(new Converter<Set<Integer>, List<Integer>>() {
            @Override
            public Result<List<Integer>> convertToModel(Set<Integer> integers, ValueContext valueContext) {
                List<Integer> list = new ArrayList<>();

                if (integers == null) return Result.ok(list);

                for (Integer id : integers)
                    list.add(id);

                return Result.ok(list);
            }

            @Override
            public Set<Integer> convertToPresentation(List<Integer> integers, ValueContext valueContext) {
                Set<Integer> set = new HashSet<>();
                if (integers == null) return set;

                for (Integer id : integers) set.add(id);

                return  set;
            }
        }).bind(ReglaAnual::getByMonthDay, ReglaAnual::setByMonthDay);

        binder.forField(onTheByDay).withConverter(new Converter<Set<CalendarItem>, List<Weekday>>() {
            @Override
            public Result<List<Weekday>> convertToModel(Set<CalendarItem> calendarItems, ValueContext valueContext) {
                List<Weekday> list = new ArrayList<>();
                if (calendarItems == null) return Result.ok(list);

                for (CalendarItem calendarItem : calendarItems) list.add(Weekday.valueOf(calendarItem.getId()));

                return Result.ok(list);
            }

            @Override
            public Set<CalendarItem> convertToPresentation(List<Weekday> weekdays, ValueContext valueContext) {
                Set<CalendarItem> set = new HashSet<>();
                if (weekdays == null) return set;
                for (Weekday weekday : weekdays) set.add(ReglasFactory.getWeekDay(weekday.toString()));

                return set;
            }
        }).bind(ReglaAnual::getByDay, ReglaAnual::setByDay);

        binder.forField(onTheByMonth).withConverter(new Converter<Set<CalendarItem>, List<Integer>>() {
            @Override
            public Result<List<Integer>> convertToModel(Set<CalendarItem> calendarItems, ValueContext valueContext) {
                List<Integer> list = new ArrayList<>();
                if (calendarItems == null) return Result.ok(list);

                for (CalendarItem calendarItem : calendarItems) list.add(Integer.valueOf(calendarItem.getId()));

                return  Result.ok(list);
            }

            @Override
            public Set<CalendarItem> convertToPresentation(List<Integer> integers, ValueContext valueContext) {
                Set<CalendarItem> set = new HashSet<>();
                if (integers == null) return set;

                for (Integer integer : integers) set.add(ReglasFactory.getCalendarMonth(integer));

                return set;
            }
        }).bind(ReglaAnual::getByMonth, ReglaAnual::setByMonth);

        binder.forField(onTheBySetPos).withConverter(new Converter<String, List<Integer>>() {
            @Override
            public Result<List<Integer>> convertToModel(String s, ValueContext valueContext) {
                if (s == null || s.equals("")) return Result.ok(new ArrayList<>());

                try {
                    List<Integer> list = Stream.of(s.split(",")).map(String::trim).map(Integer::valueOf).collect(Collectors.toList());
                    return Result.ok(list);
                } catch (Exception e)
                {
                    return Result.ok(new ArrayList<>());
                }
            }

            @Override
            public String convertToPresentation(List<Integer> integers, ValueContext valueContext) {
                if (integers == null) return "";

                List<String> list = integers.stream().map(e -> e.toString()).collect(Collectors.toList());

                return String.join(", ", list);

            }
        }).bind(ReglaAnual::getBySetPos, ReglaAnual::setBySetPos);




    }

    @Override
    protected void setPresentationValue(ReglaAnual reglaAnual) {



    }

    @Override
    public void setValue(ReglaAnual reglaAnual)
    {
        binder.setBean(reglaAnual);

    }

    @Override
    public ReglaAnual getValue()
    {
        return binder.getBean();
    }

}
