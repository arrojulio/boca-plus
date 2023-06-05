package com.aventurasoft.bocaplus.views.forms;

import com.aventurasoft.bocaplus.data.entity.calendario.CalendarItem;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglaAnual;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglaMensual;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglasFactory;
import com.vaadin.componentfactory.Tooltip;
import com.vaadin.componentfactory.TooltipAlignment;
import com.vaadin.componentfactory.TooltipPosition;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import org.dmfs.rfc5545.Weekday;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReglaMensualForm extends AbstractCompositeField<HorizontalLayout, ReglaMensualForm, ReglaMensual>  {
    private RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();

    //ON
    private TextField onInterval = new TextField("repetir cada (meses)");
    private MultiselectComboBox<Integer> onMonthDay = new MultiselectComboBox<>("Dias del mes");    //dias del mes

    //ON_THE
    private TextField onTheBySetPos = new TextField("Relativo");  //relativo a - ENTERO
    private Tooltip onTheBySetPosTooltip = new Tooltip();

    private MultiselectComboBox<CalendarItem> onTheByDay = new MultiselectComboBox<>("Dias de semana"); //dias de la semana
    private TextField onTheInterval = new TextField("cada (meses)");

    private HorizontalLayout onLayout = new HorizontalLayout();
    private VerticalLayout onTheLayout = new VerticalLayout();


    private Binder<ReglaMensual> binder;

    public enum Options
    {
        Fecha, Relativo
    }

    public ReglaMensualForm()
    {
        super(null);

        onTheBySetPosTooltip.attachToComponent(onTheBySetPos);
        onTheBySetPosTooltip.setPosition(TooltipPosition.RIGHT);
        onTheBySetPosTooltip.setAlignment(TooltipAlignment.LEFT);
        onTheBySetPosTooltip.add(new Paragraph("1: primero, 2: segundo,.., -1: ultimo"));
        onTheBySetPosTooltip.add(new Paragraph("lista separada por comas"));
        onTheBySetPosTooltip.add(new Paragraph("1,2,3"));


        radioButtonGroup.setItems(ReglaAnualForm.Options.Fecha.toString(), ReglaAnualForm.Options.Relativo.toString());
        radioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioButtonGroup.addValueChangeListener(e -> {
            if (e.getValue() != null)
            {


                if (e.getValue().equals(ReglaAnualForm.Options.Fecha.toString()))
                {
                    onLayout.setVisible(true); onTheLayout.setVisible(false);

                } else
                {
                    onLayout.setVisible(false); onTheLayout.setVisible(true);

                }
            }

        });

        List<Integer> days = new ArrayList<>();
        for(int d=1 ; d<=31 ; d++)
            days.add(d);
        onMonthDay.setItems(days);

        onLayout.add(onMonthDay, onInterval);

        onTheByDay.setItemLabelGenerator(CalendarItem::getName);
        onTheByDay.setItems(ReglasFactory.getCalendarWeekDays());

        onTheLayout.add(onTheBySetPos, onTheByDay, onTheInterval, onTheBySetPosTooltip);

        VerticalLayout verticalLayout = new VerticalLayout(onLayout, onTheLayout);
        getContent().add(radioButtonGroup, verticalLayout);

        binder = new Binder<>(ReglaMensual.class);
        binder.forField(radioButtonGroup).withConverter(new Converter<String, ReglaMensual.TipoMensual>() {
            @Override
            public Result<ReglaMensual.TipoMensual> convertToModel(String s, ValueContext valueContext) {
                if (s == null) return Result.ok(ReglaMensual.TipoMensual.ON);

                if (s.equals(ReglaAnualForm.Options.Fecha.toString()))
                    return Result.ok(ReglaMensual.TipoMensual.ON);
                else
                    return Result.ok(ReglaMensual.TipoMensual.ON_THE);

            }

            @Override
            public String convertToPresentation(ReglaMensual.TipoMensual tipoMensual, ValueContext valueContext) {
                if (tipoMensual == null) return "";

                if (tipoMensual == ReglaMensual.TipoMensual.ON)
                    return ReglaMensualForm.Options.Fecha.toString();
                else
                    return ReglaMensualForm.Options.Relativo.toString();

            }
        }).bind(ReglaMensual::getTipoMensual, ReglaMensual::setTipoMensual);

        binder.forField(onInterval).withConverter(new StringToIntegerConverter("Debe ser un numero"))
            .bind(ReglaMensual::getInterval, ReglaMensual::setInterval);
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
        }).bind(ReglaMensual::getByMonthDay, ReglaMensual::setByMonthDay);

        binder.forField(onTheInterval).withConverter(new StringToIntegerConverter("Debe ser un numero"))
                .withNullRepresentation(0)
                .bind(ReglaMensual::getInterval, ReglaMensual::setInterval);

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
        }).bind(ReglaMensual::getBySetPos, ReglaMensual::setBySetPos);
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
        }).bind(ReglaMensual::getByDay, ReglaMensual::setByDay);



    }


    @Override
    protected void setPresentationValue(ReglaMensual reglaMensual) {

    }

    @Override
    public void setValue(ReglaMensual reglaMensual)
    {
        binder.setBean(reglaMensual);
    }
    @Override
    public ReglaMensual getValue()
    {
        return binder.getBean();
    }
}
