package com.aventurasoft.bocaplus.views.registrarventa;

import com.aventurasoft.bocaplus.app.CurrentUser;
import com.aventurasoft.bocaplus.data.entity.*;
import com.aventurasoft.bocaplus.data.entity.security.Role;
import com.aventurasoft.bocaplus.data.service.PromocionService;
import com.aventurasoft.bocaplus.data.service.comercio.ComercioService;
import com.aventurasoft.bocaplus.data.service.comercio.PromocionComercioService;
import com.aventurasoft.bocaplus.data.service.comercio.SucursalService;
import com.aventurasoft.bocaplus.data.service.comercio.VentaService;
import com.aventurasoft.bocaplus.data.service.socio.SocioService;
import com.aventurasoft.bocaplus.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.component.notification.Notification;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Route(value = "registrar-venta")
@PageTitle("Registrar venta")
@CssImport("./styles/views/registrarventa/registrarventa-view.css")
@Secured({Role.COMERCIO, Role.ADMIN})
@Log4j2
public class RegistrarVentaComercioView extends VerticalLayout {

    private H1 comercioSucursalLabel = new H1();
    private Label usuarioLabel = new Label();
    private Image okImage = new Image();
    private Image errorImage = new Image();

    private ComboBox<TipoSocio> tipoSocioComboBox = new ComboBox<>("Tipo socio");
    private TextField socioTextField = new TextField("Nro Socio");
    private ComboBox<Promocion> promocionComercioComboBox = new ComboBox<>("Promocion");
    private TextField importe = new TextField("Importe");


    private FormLayout datosForm = new FormLayout();
    private Button validarButton = new Button("Validar");
    private VerticalLayout datosLayout = new VerticalLayout();

    private H1 resultado = new H1();
    private Label resultadoInfo = new Label();
    private Label resultadoImporte = new Label();
    private Button volverButton = new Button("Otra validacion");
    private VerticalLayout resultadoLayout = new VerticalLayout();

    private SocioService socioService;
    private CurrentUser currentUser;
    private SucursalService sucursalService;
    private ComercioService comercioService;
    private VentaService ventaService;
    private PromocionComercioService promocionComercioService;
    private PromocionService promocionService;
    private Comercio comercio;
    private Sucursal sucursal;

    @Autowired
    public RegistrarVentaComercioView(SocioService socioService, CurrentUser currentUser
        , SucursalService sucursalService, ComercioService comercioService
        , VentaService ventaService, PromocionComercioService promocionComercioService
                                      , PromocionService promocionService
        )
    {
        setId("registrarventa-view");
        this.socioService = socioService;
        this.currentUser = currentUser;
        this.sucursalService = sucursalService;
        this.comercioService = comercioService;
        this.ventaService = ventaService;
        this.promocionComercioService = promocionComercioService;
        this.promocionService = promocionService;


        okImage.setSrc("images/ok.png");
        okImage.setVisible(false);
        errorImage.setSrc("images/error.png");
        errorImage.setVisible(false);

        tipoSocioComboBox.setItems(TipoSocio.values());


        validarButton.addClickListener(e -> validar());
        volverButton.addClickListener(e -> {

            socioTextField.setValue("");
            importe.setValue("");
            tipoSocioComboBox.setValue(null);
            promocionComercioComboBox.setValue(null);
            okImage.setVisible(false);
            errorImage.setVisible(false);
            removeAll();
            add(datosLayout);
        });


        if (this.currentUser == null || this.currentUser.getUser() == null) {
            comercioSucursalLabel.setText("Error - usuario no enontrado");
            datosLayout.add(comercioSucursalLabel);
        } else if (this.currentUser.getUser().getSucursal() == null)
        {
            comercioSucursalLabel.setText("Error - sucursal no asignada a usuario");
            datosLayout.add(comercioSucursalLabel);
        } else
        {

            sucursal = this.sucursalService.load(currentUser.getUser().getSucursal());
            comercio = this.comercioService.load(sucursal.getComercioId());

            comercioSucursalLabel.setText(getComercio());
            usuarioLabel.setText("Usuario: " + currentUser.getUser().getEmail());

            promocionComercioComboBox.setItemLabelGenerator(Promocion::getNombre);
            promocionComercioComboBox.setItems(getPromocionesActivas());

            datosForm.add(tipoSocioComboBox, socioTextField, promocionComercioComboBox, importe);
            datosLayout.add(comercioSucursalLabel, usuarioLabel, datosForm,validarButton);
            resultadoLayout.add(okImage, errorImage,  resultado, resultadoInfo, resultadoImporte, volverButton);
        }


        add(datosLayout);
    }

    private void validar() {
        if (socioTextField.getValue().isEmpty())
        {
            Notification.show("Socio faltante");
            return;

        }
        if (promocionComercioComboBox.getValue() == null)
        {
            Notification.show("Promocion faltante");
            return;

        }
        if (importe.getValue().isEmpty())
        {
            Notification.show("Importe faltante");
            return;

        }

        removeAll();

        try {
            Socio socio = socioService.getSocio(tipoSocioComboBox.getValue(), socioTextField.getValue());
            if (socio != null && socio.isActivo()) {


                Venta venta = ventaService.registrarVenta(sucursal.getId(), promocionComercioComboBox.getValue().getId()
                    , socio.getId(), new BigDecimal(importe.getValue()));


                resultado.setText("Socio Valido");
                resultado.getStyle().set("color", "green");

                resultadoInfo.setText("Compra registrada: id " + venta.getId() + " Socio:" + socio.getApellido() + ", " + socio.getNombre());
                resultadoImporte.setText("Importe: " + NumberFormat.getCurrencyInstance().format(venta.getImporte()));
                okImage.setVisible(true);
                add(resultadoLayout);
                return;

            } else {
                errorImage.setVisible(true);
                resultado.setText("Socio Invalido");
                resultado.getStyle().set("color", "red");
                resultadoInfo.setText("");
            }


        } catch (RuntimeException e)
        {
            errorImage.setVisible(true);
            log.error(e.getMessage());
            resultado.setText("Error Registrando operacion");
            resultado.getStyle().set("color", "red");
            resultadoInfo.setText("Error: " + e.getMessage());
        }
        add(resultadoLayout);

        resultadoImporte.setText("");


    }

    private String getComercio()
    {
        if (this.currentUser.getUser() != null && currentUser.getUser().getSucursal() != null)
        {
            try {


                return comercio.getNombre() + " - " + sucursal.getNombre();
            } catch (RuntimeException e)
            {
                log.error(e.getMessage());
            }
        }
        return "";
    }

    private List<Promocion> getPromocionesActivas()
    {
        List<Promocion> promocions = new ArrayList<>();
        for (PromocionComercio promocionComercio : promocionComercioService.getPromocionComerciosValidTodayByComercioId(comercio.getId()))
            promocions.add(promocionService.load(promocionComercio.getId()));

        return promocions;
    }
}
