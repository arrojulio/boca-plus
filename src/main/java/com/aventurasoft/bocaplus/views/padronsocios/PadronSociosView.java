package com.aventurasoft.bocaplus.views.padronsocios;

import com.aventurasoft.bocaplus.data.entity.*;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.socio.CategoriaSocioService;
import com.aventurasoft.bocaplus.data.service.LocalidadService;
import com.aventurasoft.bocaplus.data.service.ProvinciaService;
import com.aventurasoft.bocaplus.data.service.socio.SocioService;
import com.aventurasoft.bocaplus.views.config.EditTagSocioView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import com.aventurasoft.bocaplus.views.main.MainView;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;

@Route(value = "padron-socios", layout = MainView.class)
@PageTitle("Padron Socios")
@CssImport("./styles/views/padronsocios/padron-socios-view.css")
@Secured(Role.ADMIN)
public class PadronSociosView extends Div {

    private Grid<Socio> grid;

    private ComboBox<TipoSocio> tipoSocio = new ComboBox<>("Tipo Socio");
    private TextField numeroSocio = new TextField("Numero Socio");
    private TextField nombre = new TextField("Nombre");
    private TextField apellido = new TextField("Apellido");
    private TextField dni = new TextField("DNI");
    private Checkbox activo = new Checkbox("Activo");
    private DatePicker fechaNacimiento = new DatePicker("Fecha Nac");
    private ComboBox<Genero> genero = new ComboBox<>("Genero");
    private TextField direccion = new TextField("Direccion");
    private TextField altura = new TextField("Altura");
    private TextField piso = new TextField("Piso");
    private TextField departamento = new TextField("Departamento");
    private TextField barrioId = new TextField("Barrio");
    private ComboBox<Localidad> localidadId = new ComboBox<>("Localidad");
    private ComboBox<Provincia> provinciaId = new ComboBox<>("Provincia");
     private ComboBox<CategoriaSocio> categoriaSocioId = new ComboBox<>("Categoria");


    private TextField occupation = new TextField("Occupation");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Socio> binder;

    private Socio socio = new Socio();

    private SocioService socioService;
    private CategoriaSocioService categoriaSocioService;
    private ProvinciaService provinciaService;
    private LocalidadService localidadService;
    @Autowired
    public PadronSociosView(SocioService socioService, CategoriaSocioService categoriaSocioService
            , ProvinciaService provinciaService, LocalidadService localidadService) {
        setId("padron-socios-view");
        this.socioService = socioService;
        this.categoriaSocioService = categoriaSocioService;
        this.provinciaService = provinciaService;
        this.localidadService = localidadService;

        this.localidadId.setItemLabelGenerator(Localidad::getNombre);
        this.provinciaId.setItemLabelGenerator(Provincia::getNombre);
        this.provinciaId.addValueChangeListener(e -> {
            if (e.isFromClient())
            {
                if (e.getValue() != null)
                {
                    this.localidadId.setItems(localidadService.getLocalidadesByProvinciaId(e.getValue().getId()));
                } else
                {
                    this.localidadId.setItems(new ArrayList<>());
                }
            }
        });
        this.provinciaId.setItems(provinciaService.getAllByName());

        // Configure Grid
        grid = new Grid<>(Socio.class);
        grid.setColumns("tipoSocio", "numeroSocio", "nombre", "apellido"
            //    , "dni", "activo", "fechaNacimiento", "genero", "direccion"
            //,"altura"
            //, "piso"
            //, "departamento"
            //, "barrioId"
            //, "localidadId"
            //, "provinciaId"
            //, "categoriaSocioId"
        );
        grid.addColumn(socio -> categoriaSocioService.load(socio.getCategoriaSocioId()).getNombre()).setHeader("Categoria");
        grid.addComponentColumn(item -> new Button("Tags", click -> editTags(item))).setHeader("Tags");

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        //grid.setDataProvider(new CrudServiceDataProvider<Person, Void>(personServiceImpl));
        grid.setItems(socioService.getAll());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                 try {
                    Socio socio = socioService.load(event.getValue().getId());
                    populateForm(socio);
                } catch (RuntimeException ex)
                {
                    refreshGrid();
                }

            } else {
                clearForm();
            }
        });

        tipoSocio.setItems(TipoSocio.values());
        genero.setItems(Genero.values());
        categoriaSocioId.setItemLabelGenerator(CategoriaSocio::getNombre);
        categoriaSocioId.setItems(categoriaSocioService.getAll());

        // Configure Form
        binder = new Binder<>(Socio.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(altura).withConverter(new StringToIntegerConverter("Must be a number"))
                .bind(Socio::getAltura, Socio::setAltura);
        binder.forField(categoriaSocioId)
                .withConverter(new Converter<CategoriaSocio, Integer>() {

                    @Override
                    public Result<Integer> convertToModel(CategoriaSocio categoriaSocio, ValueContext valueContext) {
                        if (categoriaSocio==null)
                            return Result.ok(0);
                        else
                            return Result.ok(categoriaSocio.getId());


                    }

                    @Override
                    public CategoriaSocio convertToPresentation(Integer integer, ValueContext valueContext) {
                        try
                        {
                            return categoriaSocioService.load(integer);
                        } catch (RuntimeException ex)
                        {
                            return null;
                        }
                    }
                }).bind(Socio::getCategoriaSocioId, Socio::setCategoriaSocioId);

        binder.forField(localidadId)
                .withConverter(new Converter<Localidad, String>() {
                    @Override
                    public Result<String> convertToModel(Localidad localidad, ValueContext valueContext) {
                        if (localidad != null)
                            return Result.ok(localidad.getId());
                        else
                            return Result.ok("");
                    }

                    @Override
                    public Localidad convertToPresentation(String s, ValueContext valueContext) {
                        try
                        {
                            return localidadService.load(s);
                        } catch (RuntimeException e)
                        {
                            return null;
                        }
                    }
                }).bind(Socio::getLocalidadId, Socio::setLocalidadId);
        binder.forField(provinciaId)
                .withConverter(new Converter<Provincia, String>() {
                    @Override
                    public Result<String> convertToModel(Provincia provincia, ValueContext valueContext) {
                        if (provincia != null)
                            return Result.ok(provincia.getId());
                        else
                            return Result.ok("");
                    }

                    @Override
                    public Provincia convertToPresentation(String s, ValueContext valueContext) {
                        try
                        {
                            return provinciaService.load(s);
                        } catch (RuntimeException e)
                        {
                            return null;
                        }
                    }
                }).bind(Socio::getProvinciaId, Socio::setProvinciaId);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.socio == null || this.socio.getId() == null) {
                    this.socio = socioService.createNew();
                }
                binder.writeBean(this.socio);
                //personServiceImpl.update(this.person);
                socioService.save(this.socio);
                clearForm();
                refreshGrid();
                Notification.show("Socio actualizado");
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

    private void editTags(Socio item) {
        if (item != null)
            UI.getCurrent().navigate(EditTagSocioView.class, item.getId());
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        AbstractField[] fields = new AbstractField[] {tipoSocio, numeroSocio, nombre, apellido, dni
                , activo, fechaNacimiento, genero,provinciaId, localidadId, direccion, altura, piso, departamento
                , barrioId,   categoriaSocioId };
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
        grid.setItems(socioService.getAll());
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Socio value) {
        this.socio = value;
        if (value != null && value.getProvinciaId() != null)
            localidadId.setItems(localidadService.getLocalidadesByProvinciaId(value.getProvinciaId()));
        else
            localidadId.setItems(new ArrayList<>());
        binder.readBean(this.socio);
    }
}
