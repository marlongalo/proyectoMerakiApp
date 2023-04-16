package com.example.application.views.payment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.application.data.entity.PaymentModel;
import com.example.application.data.entity.ReservaModel;
import com.example.application.data.entity.ReservasResponse;
import com.example.application.data.service.DatabaseServiceImplement;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Pago de Orden")
@Route(value = "payment", layout = MainLayout.class)
public class PaymentView extends Div {
	
	private ComboBox<String> reservasCombo = new ComboBox<>();
	private List<String> itemsReservas = new ArrayList<>();
	private Collection<ReservaModel> collectionReservas;
	private ReservaModel reservaSelected;
	private String price = "";
	
	private DatePicker paymentDate = new DatePicker("Fecha");
	private TextField cardNumber = new TextField("Número de tarjeta");
	private TextField secretNumber = new TextField("Número secreto");
	private TextField dateNumber = new TextField("Fecha de expiración");

    private Button cancel = new Button("Cancelar");
    private Button save = new Button("Pagar");    
    private Span status;
    
    
    private DatabaseServiceImplement db;
    

    public PaymentView() {
    	
    	status = new Span();
        status.setVisible(false);
        db = DatabaseServiceImplement.getInstance();
        
        try {
        	ReservasResponse reservas = db.listarReserva();
        	collectionReservas = reservas.getItems();
        	
        	collectionReservas.forEach( (reserva) -> {
        		itemsReservas.add("Reserva con ID #" + reserva.getReservaid());
        	});

		} catch (Exception e) {
			// TODO: handle exception
			Notification.show("No se pudieron cargar los paquetes.");
		}
        
        addClassName("payment-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());


        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
        	
        	String reservaString = reservasCombo.getValue(); 
        	String reservaID = reservaString.split("#")[1];
        	
        	collectionReservas.forEach( reserva -> {
        		if (reserva.getReservaid() == Integer.parseInt(reservaID)) {
        			price = reserva.getTotalprice().toString();
        			reservaSelected = reserva;
        			ConfirmDialog dialog = new ConfirmDialog();
        	    	
        	    	dialog.setHeader(String.format("Confirmación de pago"));
        	        dialog.setText(new Html("<p>¿Está seguro que desea realizar el pago por la cantidad de "
        	        		+ "<b>Lps. " + price + "</b> por la compra del paquete?"));
        	        
        	        dialog.add("Este cobro se realizará al base a la tarifa de bancos internacionales.");
        	        
        	        dialog.setCancelable(true);
        	        
        	        dialog.addCancelListener(event -> dialogCancel("Canceled"));
        	        
        	        dialog.setConfirmText("Realizar el pago");
        	        dialog.setConfirmButtonTheme("success");
        	        dialog.addConfirmListener(event -> {
        	        	
        	        	PaymentModel pago = new PaymentModel();
        	        	
        	        	pago.setReservaid(reservaSelected.getReservaid());
        	        	pago.setCard(cardNumber.getValue());
        	        	pago.setDatecard(dateNumber.getValue());
        	        	pago.setSecretnumber(secretNumber.getValue());
        	        	pago.setDatepayment(paymentDate.getValue().toString());
        	        	pago.setPrice( Integer.parseInt(price) );
        	        	
        	        	try {
							db.crearPayment(pago);
							
							reservaSelected.setPayed(1);
							db.actualizarReserva(reservaSelected);
							
							
							collectionReservas.remove(reserva);
							
							UI.getCurrent().navigate(String.format("reservar"));
							dialogPagar("Pagado");
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							Notification.show("Algo salió mal guardando.");
							e1.printStackTrace();
						}
        	        	
        	        	
        	        	
        	        });
        	        dialog.open();
				}
        	});

            clearForm();
        });
    }
    
    private void dialogCancel(String value) {
        // Cancelando
        status.setVisible(true);
    }
    
    private void dialogPagar(String value) {
    	// TODO: Guardar en base de datos
        status.setVisible(true);
    }

    private Component createTitle() {
        return new H3("Realizar un pago");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        
        reservasCombo.setLabel("Ordenes pendientes");
        reservasCombo.setWidthFull();
        reservasCombo.setPlaceholder("Orden");
        reservasCombo.setAllowedCharPattern("[\\+\\d]");
        reservasCombo.setItems(itemsReservas);
        reservasCombo.addCustomValueSetListener(e -> reservasCombo.setValue(e.getDetail()));

        formLayout.add(reservasCombo, cardNumber, dateNumber, secretNumber, paymentDate);
        
        formLayout.setColspan(cardNumber, 2);
        formLayout.setColspan(reservasCombo, 2);
        formLayout.setColspan(paymentDate, 2);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private void clearForm() {
        
    }
    

}
