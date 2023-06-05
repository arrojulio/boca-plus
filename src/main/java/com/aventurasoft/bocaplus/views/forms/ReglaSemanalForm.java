package com.aventurasoft.bocaplus.views.forms;

import com.aventurasoft.bocaplus.data.entity.calendario.CalendarItem;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglaMensual;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglaSemanal;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglasFactory;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

public class ReglaSemanalForm extends AbstractCompositeField<HorizontalLayout, ReglaSemanalForm, ReglaSemanal> {

    private TextField onInterval = new TextField("repetir cada (semana)");
    private MultiselectComboBox<CalendarItem> onTheByDay = new MultiselectComboBox<>("Dias de semana"); //dias de la semana

    private Binder<ReglaSemanal> binder;

    public ReglaSemanalForm()
    {
        super(null);

        onTheByDay.setItemLabelGenerator(CalendarItem::getName);
        onTheByDay.setItems(ReglasFactory.getCalendarWeekDays());

        getContent().add(onInterval, onTheByDay);

        binder = new Binder<>(ReglaSemanal.class);
        binder.forField(onInterval).withConverter(new StringToIntegerConverter("Debe ser un numero"))
                .withNullRepresentation(0)
                .bind(ReglaSemanal::getInterval, ReglaSemanal::setInterval);
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
        }).bind(ReglaSemanal::getByDay, ReglaSemanal::setByDay);


    }

    @Override
    protected void setPresentationValue(ReglaSemanal reglaSemanal) {


    }

    @Override
    public void setValue(ReglaSemanal reglaSemanal)
    {
        binder.setBean(reglaSemanal);
    }
    @Override
    public ReglaSemanal getValue()
    {
        return binder.getBean();
    }
}
