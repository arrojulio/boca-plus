package com.aventurasoft.bocaplus.views.config;

import com.aventurasoft.bocaplus.app.CurrentUser;
import com.aventurasoft.bocaplus.app.SecurityUtils;
import com.aventurasoft.bocaplus.data.entity.*;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.comercio.CategoriaComercioService;
import com.aventurasoft.bocaplus.data.service.comercio.ComercioService;
import com.aventurasoft.bocaplus.data.service.LocalidadService;
import com.aventurasoft.bocaplus.data.service.ProvinciaService;
import com.aventurasoft.bocaplus.data.service.comercio.SucursalService;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;

@Route(value = "comercio-editar", layout = MainView.class)
@PageTitle("Editar comercios")
@CssImport("./styles/views/config/shared-styles.css")
@Secured({Role.ADMIN, Role.COMERCIO})
public class EditComercioView extends Div {

    private Grid<Comercio> grid;

    private TextField nombre = new TextField("Nombre");
    private TextField razonSocial = new TextField("Razon Social");
    private TextField cuit = new TextField("CUIT");

    private TextField direccion = new TextField("Direccion");
    private TextField altura = new TextField("Altura");
    private TextField piso = new TextField("Piso");
    private TextField departamento = new TextField("Departamento");
    private TextField barrioId = new TextField("Barrio");
    private ComboBox<Localidad> localidadId = new ComboBox<>("Localidad");
    private ComboBox<Provincia> provinciaId = new ComboBox<>("Provincia");
    private ComboBox<CategoriaComercio> categoriaComercioId = new ComboBox<>("Categoria");


    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Comercio> binder;

    private Comercio comercio = new Comercio();

    private ComercioService comercioService;
    private CategoriaComercioService categoriaComercioService;
    private ProvinciaService provinciaService;
    private LocalidadService localidadService;
    private CurrentUser currentUser;
    private SucursalService sucursalService;

    @Autowired
    public EditComercioView(ComercioService comercioService, CategoriaComercioService categoriaComercioService
        , ProvinciaService provinciaService, LocalidadService localidadService, CurrentUser currentUser, SucursalService sucursalService) {
        setId("config-view");
       this.comercioService = comercioService;
       this.categoriaComercioService = categoriaComercioService;
       this.provinciaService = provinciaService;
       this.localidadService = localidadService;
       this.currentUser = currentUser;
       this.sucursalService = sucursalService;

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
        grid = new Grid<>(Comercio.class);
        grid.setColumns("nombre", "cuit"
        );
        grid.addColumn(comercio -> categoriaComercioService.load(comercio.getCategoriaComercioId()).getNombre()).setHeader("Categoria");
        grid.addComponentColumn(item -> new Button("Sucursales", click -> editSucursales(item))).setHeader("Sucursales");
        grid.addComponentColumn(item -> new Button("Promociones", click -> editPromociones(item))).setHeader("Promociones");
        grid.addComponentColumn(item -> new Button("Tags", click -> editTags(item))).setHeader("Tags");

        if (currentUser.getUser() != null) {
            if (SecurityUtils.isRole(Role.ADMIN))
                grid.setItems(comercioService.getAllByName());
            else if (SecurityUtils.isRole(Role.COMERCIO))
            {
                if (currentUser.getUser().getSucursal() != null)
                {
                    Sucursal sucursal = sucursalService.load(currentUser.getUser().getSucursal());
                    grid.setItems(Collections.singletonList(comercioService.load(sucursal.getComercioId())));
                }

            }


        }
        //grid.setDataProvider(new CrudServiceDataProvider<Person, Void>(personServiceImpl));


        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {

                    Comercio comercio = comercioService.load(event.getValue().getId());
                    populateForm(comercio);
                } catch (RuntimeException ex)
                {
                    refreshGrid();
                }

            } else {
                clearForm();
            }
        });

        categoriaComercioId.setItemLabelGenerator(CategoriaComercio::getNombre);
        categoriaComercioId.setItems(categoriaComercioService.getAllByName());

        // Configure Form
        binder = new Binder<>(Comercio.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(altura).withConverter(new StringToIntegerConverter("Must be a number"))
                .bind(Comercio::getAltura, Comercio::setAltura);
        binder.forField(categoriaComercioId)
                .withConverter(new Converter<CategoriaComercio, Integer>() {

                    @Override
                    public Result<Integer> convertToModel(CategoriaComercio categoriaComercio, ValueContext valueContext) {
                        if (categoriaComercio==null)
                            return Result.ok(0);
                        else
                            return Result.ok(categoriaComercio.getId());


                    }

                    @Override
                    public CategoriaComercio convertToPresentation(Integer integer, ValueContext valueContext) {
                        try
                        {
                            return categoriaComercioService.load(integer);
                        } catch (RuntimeException ex)
                        {
                            return null;
                        }
                    }
                }).bind(Comercio::getCategoriaComercioId, Comercio::setCategoriaComercioId);
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
                }).bind(Comercio::getLocalidadId, Comercio::setLocalidadId);
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
                }).bind(Comercio::getProvinciaId, Comercio::setProvinciaId);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.comercio == null || this.comercio.getId() == null) {
                    this.comercio = comercioService.createNew();
                }
                binder.writeBean(this.comercio);
                //personServiceImpl.update(this.person);
                comercioService.save(this.comercio);
                clearForm();
                refreshGrid();
                Notification.show("Comercio actualizado");
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

    private void editSucursales(Comercio item) {
        if (item != null)
            UI.getCurrent().navigate(EditSucursalView.class, item.getId());
    }

    private void editPromociones(Comercio item)
    {
        if (item != null)
            UI.getCurrent().navigate(EditPromocionComercioView.class, item.getId());
    }
    private void editTags(Comercio item)
    {
        if (item != null)
            UI.getCurrent().navigate(EditTagComercioView.class, item.getId());
    }
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        AbstractField[] fields = new AbstractField[] { nombre, razonSocial, cuit
                , provinciaId, localidadId, direccion, altura, piso, departamento
                , barrioId,   categoriaComercioId };
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
        grid.setItems(comercioService.getAllByName());
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Comercio value) {
        this.comercio = value;
        if (value != null && value.getProvinciaId() != null)
            localidadId.setItems(localidadService.getLocalidadesByProvinciaId(value.getProvinciaId()));
        else
            localidadId.setItems(new ArrayList<>());
        binder.readBean(this.comercio);
    }
}
