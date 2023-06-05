package com.aventurasoft.bocaplus.views.socio;

import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.board.Row;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.aventurasoft.bocaplus.views.socio.SocioView.SocioViewModel;
import com.aventurasoft.bocaplus.views.main.MainView;
import org.springframework.security.access.annotation.Secured;

@Route(value = "socio", layout = MainView.class)
@PageTitle("Socio")
@JsModule("./src/views/socio/socio-view.js")
@Tag("socio-view")
@Secured(Role.SOCIO)
public class SocioView extends PolymerTemplate<SocioViewModel> {


    // This is the Java companion file of a design
    // You can find the design file in /frontend/src/views/src/views/socio/socio-view.js
    // The design can be easily edited by using Vaadin Designer (vaadin.com/designer)

    public static interface SocioViewModel extends TemplateModel {
    }

    public SocioView() {
    }
}
