package com.aventurasoft.bocaplus.views.main;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.aventurasoft.bocaplus.app.CurrentUser;
import com.aventurasoft.bocaplus.app.SecurityUtils;
import com.aventurasoft.bocaplus.data.entity.Comercio;
import com.aventurasoft.bocaplus.data.entity.Sucursal;
import com.aventurasoft.bocaplus.data.service.comercio.ComercioService;
import com.aventurasoft.bocaplus.data.service.comercio.SucursalService;
import com.aventurasoft.bocaplus.views.config.*;
import com.aventurasoft.bocaplus.views.export.ExportDataView;
import com.aventurasoft.bocaplus.views.registrarventa.RegistrarVentaComercioView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.aventurasoft.bocaplus.views.estadisticas.EstadisticasView;
import com.aventurasoft.bocaplus.views.padronsocios.PadronSociosView;
import com.aventurasoft.bocaplus.views.comercio.ComercioView;
import com.aventurasoft.bocaplus.views.registrarventa.RegistrarventaView;
import com.aventurasoft.bocaplus.views.socio.SocioView;
import com.aventurasoft.bocaplus.views.about.AboutView;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/main/main-view.css")
@PWA(name = "BocaPlus", shortName = "BocaPlus", enableInstallPrompt = false)
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Log4j2
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    private CurrentUser currentUser;
    private final ComercioService comercioService;
    private final SucursalService sucursalService;

    @Autowired
    public MainView(CurrentUser currentUser, SucursalService sucursalService, ComercioService comercioService) {
        this.currentUser = currentUser;
        this.comercioService = comercioService;
        this.sucursalService = sucursalService;

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));



    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        //viewTitle.setWidth("100%");
        layout.add(viewTitle);
        layout.setFlexGrow(1, viewTitle);

        HorizontalLayout userLayout = new HorizontalLayout();

        if (currentUser !=  null && currentUser.getUser() != null) {
            if (currentUser.getUser().getSucursal() != null)
            {
                try {
                    Sucursal sucursal = this.sucursalService.load(currentUser.getUser().getSucursal());
                    Comercio comercio = this.comercioService.load(sucursal.getComercioId());

                    userLayout.add(new Label(comercio.getNombre() + " - " + sucursal.getNombre()));
                } catch (RuntimeException e)
                {
                    log.error(e.getMessage());
                }

            }
            userLayout.add(new Label(currentUser.getUser().getEmail()));
        }

        userLayout.add(new Label("  "));

        userLayout.add(new Image("images/user.svg", "Avatar"));
        ContextMenu contextMenu = new ContextMenu(userLayout);
        contextMenu.setOpenOnClick(true);
        contextMenu.addItem("Settings",
                e -> Notification.show("Not implemented yet.", 3000,
                        Notification.Position.BOTTOM_CENTER));
        contextMenu.addItem("Log Out",
                e -> logout()
        );

        layout.add(userLayout);
        return layout;
    }

    public void logout()
    {
        UI.getCurrent().close();
        UI.getCurrent().getSession().close();

        UI.getCurrent().getPage().executeJs("window.location.href='/logout'");
    }
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "BocaPlus logo"));
        logoLayout.add(new H1("BocaPlus"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        List<Component> tabs = new ArrayList<>();
        if (SecurityUtils.isAccessGranted(EstadisticasView.class))
            tabs.add(createTab("Estadisticas", EstadisticasView.class));
        if (SecurityUtils.isAccessGranted(PadronSociosView.class))
            tabs.add(createTab("Padron Socios", PadronSociosView.class));
        if (SecurityUtils.isAccessGranted(ComercioView.class))
            tabs.add(createTab("Comercio", ComercioView.class));


        if (SecurityUtils.isAccessGranted(RegistrarventaView.class))
            tabs.add(createTab("Registrar venta", RegistrarventaView.class));
        if (SecurityUtils.isAccessGranted(RegistrarventaView.class))
            tabs.add(createTab("Registrar venta Operaciones", RegistrarVentaComercioView.class));

        if (SecurityUtils.isAccessGranted(SocioView.class))
            tabs.add(createTab("Socio", SocioView.class));
        if (SecurityUtils.isAccessGranted(AboutView.class))
            tabs.add(createTab("About", AboutView.class));
        if (SecurityUtils.isAccessGranted(EditComercioView.class))
            tabs.add(createTab("Editar Comercios", EditComercioView.class));

        if (SecurityUtils.isAccessGranted(EditCategoriaComercio.class))
            tabs.add(createTab("Editar Categoria Comercio", EditCategoriaComercio.class));
        if (SecurityUtils.isAccessGranted(EditCategoriaSocioView.class))
            tabs.add(createTab("Editar Categoria Socio", EditCategoriaSocioView.class));
        if (SecurityUtils.isAccessGranted(EditProvinciaView.class))
            tabs.add(createTab("Editar Provincia", EditProvinciaView.class));
        if (SecurityUtils.isAccessGranted(EditLocalidadView.class))
            tabs.add(createTab("Editar Localidad", EditLocalidadView.class));
        if (SecurityUtils.isAccessGranted(EditPromocionView.class))
            tabs.add(createTab("Editar Promociones", EditPromocionView.class));
        if (SecurityUtils.isAccessGranted(ExportDataView.class))
            tabs.add(createTab("Exportar datos", ExportDataView.class));

        Component[] components = new Component[tabs.size()];
        int i = 0;
        for (Component component : tabs) {
            components[i] = component;
            i++;
        }

        return components;

    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class)
                        .equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
