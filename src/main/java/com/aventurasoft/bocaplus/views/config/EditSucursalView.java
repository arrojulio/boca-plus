package com.aventurasoft.bocaplus.views.config;

import com.aventurasoft.bocaplus.data.entity.*;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.*;
import com.aventurasoft.bocaplus.data.service.comercio.ComercioService;
import com.aventurasoft.bocaplus.data.service.comercio.SucursalService;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
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
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;

@Route(value = "sucursal-editar", layout = MainView.class)
@PageTitle("Editar sucursales")
@CssImport("./styles/views/config/shared-styles.css")
@Secured({Role.ADMIN, Role.COMERCIO})
public class EditSucursalView extends Div implements HasUrlParameter<Integer>, AfterNavigationObserver {
    private Comercio comercioPadre;

    private Grid<Sucursal> grid;
    private TextField comercioId = new TextField("Comercio");
    private TextField nombre = new TextField("Nombre");
    private Checkbox abierta = new Checkbox("Abierta");

    private TextField direccion = new TextField("Direccion");
    private TextField altura = new TextField("Altura");
    private TextField piso = new TextField("Piso");
    private TextField departamento = new TextField("Departamento");
    private TextField barrioId = new TextField("Barrio");
    private ComboBox<Localidad> localidadId = new ComboBox<>("Localidad");
    private ComboBox<Provincia> provinciaId = new ComboBox<>("Provincia");


    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private Binder<Sucursal> binder;

    private Sucursal sucursal = new Sucursal();

    private ComercioService comercioService;
    private SucursalService sucursalService;
    private ProvinciaService provinciaService;
    private LocalidadService localidadService;

    @Autowired
    public EditSucursalView(ComercioService comercioService, SucursalService sucursalService
            , ProvinciaService provinciaService, LocalidadService localidadService) {
        setId("config-view");
        this.comercioService = comercioService;
        this.sucursalService = sucursalService;
        this.provinciaService = provinciaService;
        this.localidadService = localidadService;

        comercioId.setEnabled(false);
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
        grid = new Grid<>();

        grid.addColumn(sucursal -> comercioService.load(sucursal.getComercioId()).getNombre()).setHeader("Comercio");
        grid.addColumn(Sucursal::getNombre).setHeader("Sucursal");

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        //grid.setDataProvider(new CrudServiceDataProvider<Person, Void>(personServiceImpl));

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {

                    Sucursal sucursal = sucursalService.load(event.getValue().getId());
                    populateForm(sucursal);
                } catch (RuntimeException ex)
                {
                    refreshGrid();
                }

            } else {
                clearForm();
            }
        });


        // Configure Form
        binder = new Binder<>(Sucursal.class);

        binder.forField(comercioId)
                .withConverter(new Converter<String, Integer>() {

                    @Override
                    public Result<Integer> convertToModel(String s, ValueContext valueContext) {
                        return Result.ok(0);
                    }

                    @Override
                    public String convertToPresentation(Integer integer, ValueContext valueContext) {
                        try
                        {
                            return comercioService.load(integer).getNombre();
                        } catch (RuntimeException e)
                        {
                            return "";
                        }
                    }
                }).bind(Sucursal::getComercioId, Sucursal::setComercioId);
        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(altura).withConverter(new StringToIntegerConverter("Must be a number"))
                .bind(Sucursal::getAltura, Sucursal::setAltura);

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
                }).bind(Sucursal::getLocalidadId, Sucursal::setLocalidadId);
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
                }).bind(Sucursal::getProvinciaId, Sucursal::setProvinciaId);
        //TODO: finalizar declaraciones error null de formulario
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.sucursal == null || this.sucursal.getId() == null) {
                    this.sucursal = sucursalService.createNew();

                }
                binder.writeBean(this.sucursal);
                this.sucursal.setComercioId(comercioPadre.getId());
                //personServiceImpl.update(this.person);
                sucursalService.save(this.sucursal);
                clearForm();
                refreshGrid();
                Notification.show("Sucursal actualizada");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the person details.");
            }
        });

        close.addClickListener( e -> {
            UI.getCurrent().navigate(EditComercioView.class);
        });

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        //splitLayout.setOrientation(SplitLayout.Orientation.VERTICAL);
        splitLayout.setSplitterPosition(50);
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        grid.setItems(sucursalService.getSucursalByComercioId(this.comercioPadre.getId()));

        // Set some data when this view is displayed.
    }
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        AbstractField[] fields = new AbstractField[] {comercioId, nombre, abierta
                , provinciaId, localidadId, direccion, altura, piso, departamento
                , barrioId  };
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
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonLayout.add(save, cancel, close);
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
        grid.setItems(sucursalService.getSucursalByComercioId(comercioPadre.getId()));
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Sucursal value) {
        this.sucursal = value;
        if (value != null && value.getProvinciaId() != null)
            localidadId.setItems(localidadService.getLocalidadesByProvinciaId(value.getProvinciaId()));
        else
            localidadId.setItems(new ArrayList<>());
        binder.readBean(this.sucursal);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer comercioId) {
        this.comercioPadre = comercioService.load(comercioId);

    }
}
