package com.aventurasoft.bocaplus.views.export;

import com.aventurasoft.bocaplus.data.entity.Socio;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.exporters.SocioExcelExporter;
import com.aventurasoft.bocaplus.data.service.socio.SocioService;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.StreamResourceWriter;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.frontend.installer.DefaultFileDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "export-data", layout = MainView.class)
@PageTitle("Exportar informacion")
@CssImport("./styles/views/export/export-data-view.css")
@Secured(Role.ADMIN)
public class ExportDataView extends VerticalLayout {

    TextField searchTextField = new TextField();
    Button exportSociosButton = new Button("Exportar socios a excel");

    private SocioService socioService;
    @Autowired
    public ExportDataView(SocioService socioService)
    {
        this.socioService = socioService;
        StreamResource streamResource = new StreamResource("socios.xlsx", new StreamResourceWriter() {
            @Override
            public void accept(OutputStream outputStream, VaadinSession vaadinSession) throws IOException {
                List<Socio> socios = socioService.getSociosByName(searchTextField.getValue()).stream().collect(Collectors.toList());
                SocioExcelExporter exporter = new SocioExcelExporter(socios);
                exporter.write(outputStream);
            }
        });
        streamResource.setCacheTime(-1);
        streamResource.setContentType("application/excel");


        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(streamResource);
        buttonWrapper.wrapComponent(exportSociosButton);
        searchTextField.setLabel("Filtrar por: ");
        add(searchTextField);
        add(buttonWrapper);

    }


}
