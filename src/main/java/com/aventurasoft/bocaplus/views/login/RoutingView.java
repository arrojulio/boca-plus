package com.aventurasoft.bocaplus.views.login;

import com.aventurasoft.bocaplus.app.CurrentUser;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.views.comercio.ComercioView;
import com.aventurasoft.bocaplus.views.estadisticas.EstadisticasView;
import com.aventurasoft.bocaplus.views.socio.SocioView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route(value = "routing")
@RouteAlias(value = "")
@PageTitle("")
public class RoutingView extends VerticalLayout implements AfterNavigationObserver {

    private CurrentUser currentUser;

    public RoutingView(CurrentUser currentUser)
    {
        this.currentUser = currentUser;

    }
    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (currentUser != null && currentUser.getUser() != null)
        {
            if (currentUser.getUser().getRole().equals(Role.ADMIN.toString()) || currentUser.getUser().getRole().equals(Role.CLUB.toString()))
                UI.getCurrent().navigate(EstadisticasView.class);
            else if (currentUser.getUser().getRole().equals(Role.COMERCIO.toString()))
                UI.getCurrent().navigate(ComercioView.class);
            else if (currentUser.getUser().getRole().equals(Role.SOCIO.toString()))
                UI.getCurrent().navigate(SocioView.class);
            else
                UI.getCurrent().navigate(EstadisticasView.class);
        }
    }
}
