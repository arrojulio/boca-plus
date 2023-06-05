package com.aventurasoft.bocaplus.views.forms;

import com.aventurasoft.bocaplus.data.entity.calendario.ReglaDiaria;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglaSemanal;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;

public class ReglaDiariaForm extends AbstractCompositeField<VerticalLayout, ReglaDiariaForm, ReglaDiaria> {
    private TextField onInterval = new TextField("repetir cada (dias)");

    private Binder<ReglaDiaria> binder;

    public ReglaDiariaForm()
    {
        super(null);

        getContent().add(onInterval);

        binder = new Binder<>(ReglaDiaria.class);

        binder.forField(onInterval).withConverter(new StringToIntegerConverter("Debe ser un numero"))
                .withNullRepresentation(0)
                .bind(ReglaDiaria::getInterval, ReglaDiaria::setInterval);

    }

    @Override
    protected void setPresentationValue(ReglaDiaria reglaDiaria) {

    }

    @Override
    public void setValue(ReglaDiaria reglaDiaria)
    {
        binder.setBean(reglaDiaria);
    }
    @Override
    public ReglaDiaria getValue()
    {
        return binder.getBean();
    }
}
