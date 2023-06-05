package com.aventurasoft.bocaplus.views.config;


import com.aventurasoft.bocaplus.data.entity.CategoriaSocio;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.socio.CategoriaSocioService;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasStyle;
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
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "categoria-socio-editar", layout = MainView.class)
@PageTitle("Editar categoria socio")
@CssImport("./styles/views/config/shared-styles.css")
@Secured(Role.ADMIN)
public class EditCategoriaSocioView extends Div {

    private Grid<CategoriaSocio> grid;

    private TextField nombre = new TextField("Nombre");


    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Binder<CategoriaSocio> binder;

    private CategoriaSocio categoriaSocio = new CategoriaSocio();


    private CategoriaSocioService categoriaSocioService;

    @Autowired
    public EditCategoriaSocioView(CategoriaSocioService categoriaSocioService) {
        setId("config-view");

        this.categoriaSocioService = categoriaSocioService;


        // Configure Grid
        grid = new Grid<>(CategoriaSocio.class);
        grid.setColumns("nombre"        );

        grid.getColumns().forEach(column -> column.setAutoWidth(true));

        //grid.setDataProvider(new CrudServiceDataProvider<Person, Void>(personServiceImpl));
        grid.setItems(categoriaSocioService.getAllByName());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                try {


                    CategoriaSocio categoriaSocio = categoriaSocioService.load(event.getValue().getId());
                    populateForm(categoriaSocio);
                } catch (RuntimeException ex)
                {
                    refreshGrid();
                }

            } else {
                clearForm();
            }
        });


        // Configure Form
        binder = new Binder<>(CategoriaSocio.class);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.categoriaSocio == null || categoriaSocio.getId() == null) {
                    this.categoriaSocio = categoriaSocioService.createNew();
                }
                binder.writeBean(this.categoriaSocio);
                //personServiceImpl.update(this.person);
                categoriaSocioService.save(this.categoriaSocio);
                clearForm();
                refreshGrid();
                Notification.show("Categoria Socio actualizado");
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
        AbstractField[] fields = new AbstractField[] { nombre };
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
        grid.setItems(categoriaSocioService.getAllByName());
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(CategoriaSocio value) {
        this.categoriaSocio = value;
        binder.readBean(this.categoriaSocio);
    }

}
