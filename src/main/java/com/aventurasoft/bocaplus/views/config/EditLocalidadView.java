package com.aventurasoft.bocaplus.views.config;

import com.aventurasoft.bocaplus.data.entity.Localidad;
import com.aventurasoft.bocaplus.data.entity.Provincia;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.LocalidadService;
import com.aventurasoft.bocaplus.data.service.ProvinciaService;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "localidad-editar", layout = MainView.class)
@PageTitle("Editar localidad")
@CssImport("./styles/views/config/shared-styles.css")
@Secured(Role.ADMIN)
public class EditLocalidadView extends Div {

    private Grid<Localidad> grid;
    private TextField id = new TextField("ID");
    private TextField nombre = new TextField("Nombre");
    private ComboBox<Provincia> provinciaId = new ComboBox("Provincia");


    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Localidad> binder;

    private Localidad localidad = new Localidad();

    private LocalidadService localidadService;
    private ProvinciaService provinciaService;

    @Autowired
    public EditLocalidadView(LocalidadService localidadService, ProvinciaService provinciaService) {
        setId("config-view");

        this.provinciaService = provinciaService;
        this.localidadService = localidadService;

        provinciaId.setItemLabelGenerator(Provincia::getNombre);
        provinciaId.setItems(provinciaService.getAllByName());

        // Configure Grid
        grid = new Grid<>(Localidad.class);
        grid.setColumns("id", "nombre"        );
        grid.addColumn(localidad -> provinciaService.load(localidad.getProvinciaId()).getNombre()).setHeader("Provincia");

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        //grid.setDataProvider(new CrudServiceDataProvider<Person, Void>(personServiceImpl));
        grid.setItems(localidadService.getAllByName());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {


                    Localidad localidad  = localidadService.load(event.getValue().getId());
                    populateForm(localidad);
                } catch (RuntimeException ex)
                {
                    refreshGrid();
                }

            } else {
                clearForm();
            }
        });


        // Configure Form

        binder = new Binder<>(Localidad.class);
        binder.forField(provinciaId)
                .withConverter(new Converter<Provincia, String>() {

                    @Override
                    public Result<String> convertToModel(Provincia provincia, ValueContext valueContext) {
                        if (provincia == null)
                            return Result.ok("");
                        else
                            return Result.ok(provincia.getId());

                    }

                    @Override
                    public Provincia convertToPresentation(String provinciaId, ValueContext valueContext) {
                        try
                        {
                            return provinciaService.load(provinciaId);
                        } catch (RuntimeException e)
                        {
                            return null;
                        }
                    }
                }).bind(Localidad::getProvinciaId, Localidad::setProvinciaId);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.localidad == null || localidad.getId() == null) {
                    this.localidad = localidadService.createNew();
                }
                binder.writeBean(this.localidad);
                //personServiceImpl.update(this.person);
                localidadService.save(this.localidad);
                clearForm();
                refreshGrid();
                Notification.show("Localidad actualizada");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the person details.");
            }
        });

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        //splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        splitLayout.setSplitterPosition(50);
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        AbstractField[] fields = new AbstractField[] {id, nombre, provinciaId };
        for (AbstractField field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        //formLayout.setWidthFull();
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);

        //grid.getDataProvider().refreshAll();
        grid.setItems(localidadService.getAllByName());
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Localidad value) {
        this.localidad = value;
        binder.readBean(this.localidad);
    }
}
