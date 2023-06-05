package com.aventurasoft.bocaplus.views.forms;

import com.aventurasoft.bocaplus.data.entity.calendario.*;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;

public class ReglaRecurrenteForm extends AbstractCompositeField<HorizontalLayout, ReglaRecurrenteForm, ReglaRecurrente> {
    private RadioButtonGroup<Reglas> radioButtonGroup = new RadioButtonGroup<>();

    private ReglaAnualForm reglaAnualForm = new ReglaAnualForm();
    private ReglaMensualForm reglaMensualForm = new ReglaMensualForm();
    private ReglaSemanalForm reglaSemanalForm = new ReglaSemanalForm();
    private ReglaDiariaForm reglaDiariaForm = new ReglaDiariaForm();

    private VerticalLayout content = new VerticalLayout();

    private ReglaRecurrente reglaRecurrente= null;

    public enum Reglas
    {
        Diario,
        Semanal,
        Mensual,
        Anual
    }
    public ReglaRecurrenteForm()
    {
        super(null);

        radioButtonGroup.setItems(Reglas.values());
        radioButtonGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioButtonGroup.addValueChangeListener(e -> {

                setContent(e.getValue());
        });

        reglaAnualForm.setValue(new ReglaAnual());
        reglaMensualForm.setValue(new ReglaMensual());
        reglaSemanalForm.setValue(new ReglaSemanal());
        reglaDiariaForm.setValue(new ReglaDiaria());

        getContent().add(radioButtonGroup, content);

    }

    private void setContent(Reglas regla)
    {
        content.removeAll();
        if (regla == Reglas.Diario) content.add(reglaDiariaForm);
        else if (regla == Reglas.Semanal) content.add(reglaSemanalForm);
        else if (regla == Reglas.Mensual) content.add(reglaMensualForm);
        else if (regla == Reglas.Anual) content.add(reglaAnualForm);
    }

    @Override
    protected void setPresentationValue(ReglaRecurrente reglaRecurrente) {

    }

    @Override
    public void setValue(ReglaRecurrente reglaRecurrente)
    {
        this.reglaRecurrente = reglaRecurrente;
        if (reglaRecurrente == null)
            radioButtonGroup.setValue(null);

        if (reglaRecurrente instanceof ReglaAnual)
        {
            reglaAnualForm.setValue((ReglaAnual) reglaRecurrente);
            radioButtonGroup.setValue(Reglas.Anual);
        } else if (reglaRecurrente instanceof ReglaMensual)
        {
            reglaMensualForm.setValue((ReglaMensual) reglaRecurrente);
            radioButtonGroup.setValue(Reglas.Mensual);
        } else if (reglaRecurrente instanceof ReglaSemanal)
        {
            reglaSemanalForm.setValue((ReglaSemanal) reglaRecurrente);
            radioButtonGroup.setValue(Reglas.Semanal);
        } else if (reglaRecurrente instanceof ReglaDiaria)
        {
            reglaDiariaForm.setValue((ReglaDiaria) reglaRecurrente);
            radioButtonGroup.setValue(Reglas.Diario);
        }

    }

    @Override
    public ReglaRecurrente getValue()
    {
        if (radioButtonGroup.getValue() == Reglas.Anual)
            return reglaAnualForm.getValue();
        else if (radioButtonGroup.getValue() == Reglas.Mensual)
            return reglaMensualForm.getValue();
        else if (radioButtonGroup.getValue() == Reglas.Semanal)
            return reglaSemanalForm.getValue();
        else if (radioButtonGroup.getValue() == Reglas.Diario)
            return reglaDiariaForm.getValue();
        else
            return null;
    }
}
