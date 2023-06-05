package com.aventurasoft.bocaplus.views.config;

import com.aventurasoft.bocaplus.data.entity.Socio;
import com.aventurasoft.bocaplus.data.entity.TagSocio;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.socio.SocioService;
import com.aventurasoft.bocaplus.data.service.socio.TagSocioService;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.aventurasoft.bocaplus.views.padronsocios.PadronSociosView;
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

@Route(value = "tag-csocio-editar", layout = MainView.class)
@PageTitle("Editar tags del socio")
@CssImport("./styles/views/config/shared-styles.css")
@Secured(Role.ADMIN)
public class EditTagSocioView extends Div implements HasUrlParameter<Long>, AfterNavigationObserver {
    private Socio socioPadre;

    private Grid<TagSocio> grid;
    private TextField socioId = new TextField("Socio");
    private TextField tag = new TextField("Tag");



    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private Binder<TagSocio> binder;

    private TagSocio tagSocio = new TagSocio();

    private SocioService socioService;
    private TagSocioService tagSocioService;

    @Autowired
    public EditTagSocioView(SocioService socioService, TagSocioService tagSocioService) {
        setId("config-view");
        this.socioService = socioService;
        this.tagSocioService = tagSocioService;


        socioId.setEnabled(false);

        // Configure Grid
        grid = new Grid<>();

        grid.addColumn(item -> socioService.load(item.getSocioId()).getApellidoNombre()).setHeader("Socio");
        grid.addColumn(TagSocio::getTag).setHeader("Tag");


        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        //grid.setDataProvider(new CrudServiceDataProvider<Person, Void>(personServiceImpl));

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {

                    TagSocio tagSocio = tagSocioService.load(event.getValue().getId());
                    populateForm(tagSocio);
                } catch (RuntimeException ex)
                {
                    refreshGrid();
                }

            } else {
                clearForm();
            }
        });



        // Configure Form
        binder = new Binder<>(TagSocio.class);

        binder.forField(socioId)
                .withConverter(new Converter<String, Long>() {

                    @Override
                    public Result<Long> convertToModel(String s, ValueContext valueContext) {
                        return Result.ok(0l);
                    }

                    @Override
                    public String convertToPresentation(Long id, ValueContext valueContext) {
                        try
                        {
                            return socioService.load(id).getApellidoNombre();
                        } catch (RuntimeException e)
                        {
                            return "";
                        }
                    }
                }).bind(TagSocio::getSocioId, TagSocio::setSocioId);
        // Bind fields. This where you'd define e.g. validation rules
        binder.forField(tag).bind(TagSocio::getTag, TagSocio::setTag);
        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.tagSocio == null || this.tagSocio.getId() == null) {
                    this.tagSocio = tagSocioService.createNew();

                }
                binder.writeBean(this.tagSocio);
                this.tagSocio.setSocioId(socioPadre.getId());
                //personServiceImpl.update(this.person);
                tagSocioService.save(this.tagSocio);
                clearForm();
                refreshGrid();
                Notification.show("Tag actualizado");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the person details.");
            }
        });

        close.addClickListener( e -> {
            UI.getCurrent().navigate(PadronSociosView.class);
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
        grid.setItems(tagSocioService.getTagSociosBySocioId(this.socioPadre.getId()));

        // Set some data when this view is displayed.
    }
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        AbstractField[] fields = new AbstractField[] {socioId,tag };
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
        grid.setItems(tagSocioService.getTagSociosBySocioId(socioPadre.getId()));
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(TagSocio value) {
        this.tagSocio = value;
        binder.readBean(this.tagSocio);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long socioId) {
        this.socioPadre = socioService.load(socioId);
    }

}
