package com.aventurasoft.bocaplus.views.config;

import com.aventurasoft.bocaplus.data.entity.Comercio;
import com.aventurasoft.bocaplus.data.entity.TagComercio;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.comercio.ComercioService;
import com.aventurasoft.bocaplus.data.service.comercio.TagComercioService;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

@Route(value = "tag-comercio-editar", layout = MainView.class)
@PageTitle("Editar tags del comercio")
@CssImport("./styles/views/config/shared-styles.css")
@Secured({Role.ADMIN, Role.COMERCIO})
public class EditTagComercioView extends Div implements HasUrlParameter<Integer>, AfterNavigationObserver {
    private Comercio comercioPadre;

    private Grid<TagComercio> grid;
    private TextField comercioId = new TextField("Comercio");
    private TextField tag = new TextField("Tag");



    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private Binder<TagComercio> binder;

    private TagComercio tagComercio = new TagComercio();

    private ComercioService comercioService;
    private TagComercioService tagComercioService;

    @Autowired
    public EditTagComercioView(ComercioService comercioService, TagComercioService tagComercioService) {
        setId("config-view");
        this.comercioService = comercioService;
        this.tagComercioService = tagComercioService;


        comercioId.setEnabled(false);

        // Configure Grid
        grid = new Grid<>();

        grid.addColumn(item -> comercioService.load(item.getComercioId()).getNombre()).setHeader("Comercio");
        grid.addColumn(TagComercio::getTag).setHeader("Tag");


        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        //grid.setDataProvider(new CrudServiceDataProvider<Person, Void>(personServiceImpl));

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {

                    TagComercio tagComercio = tagComercioService.load(event.getValue().getId());
                    populateForm(tagComercio);
                } catch (RuntimeException ex)
                {
                    refreshGrid();
                }

            } else {
                clearForm();
            }
        });



        // Configure Form
        binder = new Binder<>(TagComercio.class);

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
                }).bind(TagComercio::getComercioId, TagComercio::setComercioId);
        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(tag).bind(TagComercio::getTag, TagComercio::setTag);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.tagComercio == null || this.tagComercio.getId() == null) {
                    this.tagComercio = tagComercioService.createNew();

                }
                binder.writeBean(this.tagComercio);
                this.tagComercio.setComercioId(comercioPadre.getId());
                //personServiceImpl.update(this.person);
                tagComercioService.save(this.tagComercio);
                clearForm();
                refreshGrid();
                Notification.show("Tag actualizado");
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
        grid.setItems(tagComercioService.getTagComerciosByComercioId(this.comercioPadre.getId()));

        // Set some data when this view is displayed.
    }
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        AbstractField[] fields = new AbstractField[] {comercioId,tag };
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
        grid.setItems(tagComercioService.getTagComerciosByComercioId(comercioPadre.getId()));
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(TagComercio value) {
        this.tagComercio = value;
        binder.readBean(this.tagComercio);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer comercioId) {
        this.comercioPadre = comercioService.load(comercioId);
    }
}
