package com.aventurasoft.bocaplus.views.about;

import com.aventurasoft.bocaplus.data.entity.calendario.*;
import com.aventurasoft.bocaplus.views.forms.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.aventurasoft.bocaplus.views.main.MainView;

@Route(value = "about", layout = MainView.class)
@PageTitle("About")
@CssImport("./styles/views/about/about-view.css")
public class AboutView extends HorizontalLayout {


    public AboutView() {
        setId("about-view");
        add(new Label("BocaPlus desarrollado por AventuraSoft S.A."));


    }

}
