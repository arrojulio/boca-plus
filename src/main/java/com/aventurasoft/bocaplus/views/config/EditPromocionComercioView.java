package com.aventurasoft.bocaplus.views.config;

import com.aventurasoft.bocaplus.data.entity.Comercio;
import com.aventurasoft.bocaplus.data.entity.Promocion;
import com.aventurasoft.bocaplus.data.entity.PromocionComercio;
import com.aventurasoft.bocaplus.data.entity.calendario.ReglaRecurrente;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.comercio.ComercioService;
import com.aventurasoft.bocaplus.data.service.comercio.PromocionComercioService;
import com.aventurasoft.bocaplus.data.service.PromocionService;
import com.aventurasoft.bocaplus.views.forms.ReglaRecurrenteForm;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "promocion-comercio-editar", layout = MainView.class)
@PageTitle("Editar promociones del comercio")
@CssImport("./styles/views/config/shared-styles.css")
@Secured({Role.ADMIN, Role.COMERCIO})
public class EditPromocionComercioView extends Div implements HasUrlParameter<Integer>, AfterNavigationObserver {
    private Comercio comercioPadre;

    private Grid<PromocionComercio> grid;
    private TextField comercioId = new TextField("Comercio");
    private ComboBox<Promocion> promocionId = new ComboBox<>("Promocion");
    private ReglaRecurrenteForm reglaRecurrente = new ReglaRecurrenteForm();
    private DatePicker startDate = new DatePicker("Fecha Inicio");
    private DatePicker endDate = new DatePicker("Fecha Fin");



    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private Binder<PromocionComercio> binder;

    private PromocionComercio promocionComercio = new PromocionComercio();

    private ComercioService comercioService;
    private PromocionComercioService promocionComercioService;
    private PromocionService promocionService;

    @Autowired
    public EditPromocionComercioView(ComercioService comercioService, PromocionService promocionService, PromocionComercioService promocionComercioService) {
        setId("config-view");
        this.comercioService = comercioService;
        this.promocionComercioService = promocionComercioService;
        this.promocionService = promocionService;


        comercioId.setEnabled(false);

        // Configure Grid
        grid = new Grid<>();

        grid.addColumn(promocionComercio -> comercioService.load(promocionComercio.getComercioId()).getNombre()).setHeader("Comercio");
        grid.addColumn(promocionComercio -> promocionService.load(promocionComercio.getPromocionId()).getNombre()).setHeader("Promocion");


        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        //grid.setDataProvider(new CrudServiceDataProvider<Person, Void>(personServiceImpl));

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {

                    PromocionComercio promocionComercio = promocionComercioService.load(event.getValue().getId());
                    populateForm(promocionComercio);
                } catch (RuntimeException ex)
                {
                    refreshGrid();
                }

            } else {
                clearForm();
            }
        });

        promocionId.setItemLabelGenerator(Promocion::getNombre);
        promocionId.setItems(promocionService.getAllByName());

        // Configure Form
        binder = new Binder<>(PromocionComercio.class);

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
                }).bind(PromocionComercio::getComercioId, PromocionComercio::setComercioId);
        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(promocionId)
                .withConverter(new Converter<Promocion, Integer>() {
                    @Override
                    public Result<Integer> convertToModel(Promocion promocion, ValueContext valueContext) {
                        if (promocion!=null)
                            return Result.ok(promocion.getId());
                        else
                            return Result.ok(0);
                    }

                    @Override
                    public Promocion convertToPresentation(Integer integer, ValueContext valueContext) {
                        try
                        {
                            return promocionService.load(integer);
                        } catch (RuntimeException e)
                        {
                            return null;
                        }
                    }
                }).bind(PromocionComercio::getPromocionId, PromocionComercio::setPromocionId);
        //TODO: finalizar declaraciones error null de formulario
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.promocionComercio == null || this.promocionComercio.getId() == null) {
                    this.promocionComercio = promocionComercioService.createNew();

                }
                binder.writeBean(this.promocionComercio);
                this.promocionComercio.setComercioId(comercioPadre.getId());
                //personServiceImpl.update(this.person);
                promocionComercioService.save(this.promocionComercio);
                clearForm();
                refreshGrid();
                Notification.show("Promocion actualizada");
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
        grid.setItems(promocionComercioService.getPromocionComerciosByComercioId(this.comercioPadre.getId()));

        // Set some data when this view is displayed.
    }
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        AbstractField[] fields = new AbstractField[] {comercioId,promocionId, startDate, endDate };
        for (AbstractField field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        formLayout.add(reglaRecurrente);


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
        grid.setItems(promocionComercioService.getPromocionComerciosByComercioId(comercioPadre.getId()));
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(PromocionComercio value) {
        this.promocionComercio = value;
        binder.readBean(this.promocionComercio);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer comercioId) {
        this.comercioPadre = comercioService.load(comercioId);
    }
}
