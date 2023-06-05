package com.aventurasoft.bocaplus.views.registrarventa;

import com.aventurasoft.bocaplus.data.entity.*;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.*;
import com.aventurasoft.bocaplus.data.service.comercio.ComercioService;
import com.aventurasoft.bocaplus.data.service.comercio.PromocionComercioService;
import com.aventurasoft.bocaplus.data.service.comercio.SucursalService;
import com.aventurasoft.bocaplus.data.service.comercio.VentaService;
import com.aventurasoft.bocaplus.data.service.socio.SocioService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

import com.aventurasoft.bocaplus.views.main.MainView;
import org.springframework.security.access.annotation.Secured;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Route(value = "ventas", layout = MainView.class)
@PageTitle("Registrar venta")
@CssImport("./styles/views/registrarventa/registrarventa-view.css")
@Secured({Role.COMERCIO, Role.ADMIN})
public class RegistrarventaView extends Div {

    private Grid<Venta> grid;
    private DatePicker fecha = new DatePicker("Fecha");
    private TimePicker hora = new TimePicker("Hora");
    private ComboBox<Comercio> comercioComboBox = new ComboBox<>("Comercio");
    private ComboBox<Sucursal> sucursalId = new ComboBox<>("Sucursal");
    private ComboBox<Socio> socioId = new ComboBox<>("Socio");
    private ComboBox<Promocion> promocionId = new ComboBox<>("Promocion");
    private TextField importe = new TextField("Importe");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<Venta> binder;

    private Venta venta = new Venta();

    private VentaService ventaService;
    private ComercioService comercioService;
    private SucursalService sucursalService;
    private PromocionComercioService promocionComercioService;
    private PromocionService promocionService;
    private SocioService socioService;
    @Autowired
    public RegistrarventaView(VentaService ventaService
            , ComercioService comercioService
            , SucursalService sucursalService
            , PromocionComercioService promocionComercioService
            , PromocionService promocionService
            , SocioService socioService) {
        setId("registrarventa-view");

        this.ventaService = ventaService;
        this.comercioService = comercioService;
        this.sucursalService = sucursalService;
        this.promocionComercioService = promocionComercioService;
        this.promocionService = promocionService;
        this.socioService = socioService;


        comercioComboBox.setItemLabelGenerator(Comercio::getNombre);
        comercioComboBox.setItems(comercioService.getAllByName());
        comercioComboBox.addValueChangeListener(e -> {
            //if (e.isFromClient()) {
                if (e.getValue() == null) {
                    sucursalId.setItems(new ArrayList<>());
                    promocionId.setItems(new ArrayList<>());

                } else {
                    sucursalId.setItems(sucursalService.getSucursalByComercioId(e.getValue().getId()));
                    promocionId.setItems(promocionService.getPromocionesByComercioId(e.getValue().getId()));
                }
                sucursalId.setValue(null);
                promocionId.setValue(null);
            //}
        });
        sucursalId.setItemLabelGenerator(Sucursal::getNombre);
        promocionId.setItemLabelGenerator(Promocion::getNombre);
        socioId.setItemLabelGenerator((ItemLabelGenerator<Socio>) socio -> socio.getNumeroSocio() + " - " + socio.getApellidoNombre());
        socioId.setItems(socioService.getAll());

        fecha.setEnabled(false);
        hora.setEnabled(false);
        // Configure Grid
        grid = new Grid<>();
        //grid.setColumns("fecha", "sucursalId", "socioId", "promocionComercioId", "importe");
        grid.addColumn(new LocalDateRenderer<>(
                Venta::getFecha, "yyyy-MM-dd"
        )).setHeader("Fecha");
        grid.addColumn(venta -> comercioService.load(sucursalService.load(venta.getSucursalId()).getComercioId()).getNombre()).setHeader("Comercio");
        grid.addColumn(venta -> sucursalService.load(venta.getSucursalId()).getNombre()).setHeader("Sucursal");
        grid.addColumn(venta -> socioService.load(venta.getSocioId()).getApellidoNombre()).setHeader("Socio");
        grid.addColumn(venta -> NumberFormat.getCurrencyInstance().format(venta.getImporte())).setHeader("Importe");

        grid.getColumns().forEach(column -> column.setAutoWidth(true));
        //grid.setDataProvider(new CrudServiceDataProviderLong<Venta, Void>(ventaService));
        grid.setItems(ventaService.findAllByDateAndSucursalId());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                //Optional<Person> personFromBackend = personServiceImpl.get(event.getValue().getId());
                try {
                    Venta ventaFromBackend = ventaService.load(event.getValue().getId());
                    populateForm(ventaFromBackend);
                } catch (RuntimeException ex)
                {
                    refreshGrid();
                }

            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new Binder<>(Venta.class);

        // Bind fields. This where you'd define e.g. validation rules
//        binder.forField(fecha)
//                .withConverter(new Converter<String, LocalDate>() {
//
//                    @Override
//                    public Result<LocalDate> convertToModel(String s, ValueContext valueContext) {
//                        return null;
//                    }
//
//                    @Override
//                    public String convertToPresentation(LocalDate localDate, ValueContext valueContext) {
//                        if (localDate != null)
//                            return localDate.toString();
//                        else
//                            return "";
//                    }
//                }).bind(Venta::getFecha,null);
//        binder.forField(hora)
//                .withConverter(new Converter<String, LocalTime>() {
//
//                    @Override
//                    public Result<LocalTime> convertToModel(String s, ValueContext valueContext) {
//                        return null;
//                    }
//
//                    @Override
//                    public String convertToPresentation(LocalTime localTime, ValueContext valueContext) {
//                        if (localTime != null)
//                            return localTime.toString();
//                        else
//                            return "";
//                    }
//                }).bind(Venta::getHora, null);
//        binder.forField(comercioComboBox)
//                .withConverter(new Converter<Comercio, Integer>() {
//                    @Override
//                    public Result<Integer> convertToModel(Comercio comercio, ValueContext valueContext) {
//                        return null;
//                    }
//
//                    @Override
//                    public Comercio convertToPresentation(Integer sucursalId, ValueContext valueContext) {
//                        try {
//                            return comercioService.load(sucursalService.load(sucursalId).getComercioId());
//                        } catch (RuntimeException e) {
//                            return null;
//                        }
//                    }
//                }).bind(Venta::getSucursalId, null);
        binder.forField(sucursalId)
                .withConverter(new Converter<Sucursal, Integer>() {

                    @Override
                    public Result<Integer> convertToModel(Sucursal sucursal, ValueContext valueContext) {
                        if (sucursal != null)
                            return Result.ok(sucursal.getId());
                        else
                            return Result.ok(0);
                    }

                    @Override
                    public Sucursal convertToPresentation(Integer integer, ValueContext valueContext) {
                        try
                        {
                            return sucursalService.load(integer);
                        } catch (RuntimeException exception) {
                            return null;
                        }
                    }
                }).bind(Venta::getSucursalId, Venta::setSucursalId);
        binder.forField(socioId)
                .withConverter(new Converter<Socio, Long>() {

                    @Override
                    public Result<Long> convertToModel(Socio socio, ValueContext valueContext) {
                        if (socio != null)
                            return Result.ok(socio.getId());
                        else
                            return Result.ok(0l);

                    }

                    @Override
                    public Socio convertToPresentation(Long aLong, ValueContext valueContext) {
                        try {
                            return socioService.load(aLong);
                        } catch (RuntimeException e)
                        {
                            return null;
                        }
                    }
                }).bind(Venta::getSocioId, Venta::setSocioId);
        binder.forField(promocionId)
                .withConverter(new Converter<Promocion, Integer>() {
                    @Override
                    public Result<Integer> convertToModel(Promocion promocion, ValueContext valueContext) {
                        if (promocion != null)
                            return Result.ok(promocion.getId());
                        else
                            return Result.ok(0);
                    }

                    @Override
                    public Promocion convertToPresentation(Integer integer, ValueContext valueContext) {
                        try {
                            return promocionService.load(integer);
                        } catch (RuntimeException e)
                        {
                            return null;
                        }
                    }
                }).bind(Venta::getPromocionId, Venta::setPromocionId);
        binder.forField(importe).withConverter(new StringToBigDecimalConverter("Must be a number"))
                .bind(Venta::getImporte, Venta::setImporte);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.venta == null || this.venta.getId() == null) {
                    this.venta = ventaService.createNew();
                    binder.writeBean(this.venta);
                    this.venta.setFecha(LocalDate.now());
                    this.venta.setHora(LocalTime.now());

                } else
                {
                    binder.writeBean(this.venta);
                }

                //personServiceImpl.update(this.person);
                ventaService.save(this.venta);

                clearForm();
                refreshGrid();
                Notification.show("Venta actualizada");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the person details.");
            }
        });

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(60);

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
        AbstractField[] fields = new AbstractField[] { fecha, hora, comercioComboBox, sucursalId, promocionId, socioId, importe};
        for (AbstractField field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
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
        grid.setItems(ventaService.findAllByDateAndSucursalId());
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Venta value) {
        this.venta = value;
        if (value != null && value.getSucursalId() != null)
        {
            try {
                Sucursal sucursal = sucursalService.load(value.getSucursalId());
                Comercio comercio = comercioService.load(sucursal.getComercioId());
                comercioComboBox.setValue(comercio);
                sucursalId.setItems(sucursalService.getSucursalByComercioId(comercio.getId()));
                sucursalId.setValue(sucursal);
            } catch (RuntimeException e)
            {
                comercioComboBox.setValue(null);
                sucursalId.setItems(new ArrayList<>());
                promocionId.setItems(new ArrayList<>());
            }
        } else
        {
            comercioComboBox.setValue(null);
            sucursalId.setItems(new ArrayList<>());
            promocionId.setItems(new ArrayList<>());
        }

        binder.readBean(this.venta);
    }
}
