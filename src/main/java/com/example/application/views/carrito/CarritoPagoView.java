package com.example.application.views.carrito;
import com.example.application.data.entity.ProductoCarrito;
import com.example.application.data.entity.ProductoCarritoResponse;
import java.util.ArrayList;
import java.util.Collection;
import com.example.application.data.service.DatabaseRepositoryImpl;
import com.example.application.views.MainLayout;
import com.example.application.controller.CarritoPagoInteractor;
import com.example.application.controller.CarritoPagoInteractorImpl;
import com.example.application.data.entity.ClientModel;
import com.example.application.data.entity.ClientResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Flex;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexWrap;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Position;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import java.io.IOException;


@PageTitle("Carrito")
@Route(value = "carrito", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class CarritoPagoView extends Div implements CarritoPagoViewModel{
	//private TextField phone;
    private static final Set<String> states = new LinkedHashSet<>();
    private static final Set<String> countries = new LinkedHashSet<>();
	private static final long serialVersionUID = 1L;
	private ComboBox<String> clientCombo = new ComboBox<>();
	//private ComboBox<String> clientCombo1 = new ComboBox<>();
	Collection<ClientModel> collectionClientes;
	private ClientModel clienteSelected;
   // private Hr dividerHr = new Hr();
  //  private Button calculate = new Button("Calcular");
	private List<String> itemsClientes = new ArrayList<>();
	//private List<String> itemsClientes1 = new ArrayList<>();
    private CarritoPagoInteractor controlador;
    private List<ProductoCarrito> items;
    //private DatabaseServiceImplement db;
    //private String value;
    private Main content;
    
    static {
        states.addAll(Arrays.asList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
                "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
                "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi",
                "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
                "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
                "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
                "West Virginia", "Wisconsin", "Wyoming"));

        countries.addAll(Arrays.asList("Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola",
                "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia",
                "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize",
                "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Bouvet Island",
                "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei Darussalam", "Bulgaria",
                "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
                "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands",
                "Colombia", "Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus",
                "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador",
                "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands",
                "Faroe Islands", "Federated States of Micronesia", "Fiji", "Finland", "France", "French Guiana",
                "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana",
                "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea",
                "Guinea-Bissau", "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong",
                "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Ivory Coast",
                "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
                "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau",
                "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
                "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Moldova", "Monaco", "Mongolia",
                "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands",
                "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue",
                "Norfolk Island", "North Korea", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau",
                "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal",
                "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis",
                "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe",
                "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
                "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands",
                "South Korea", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname",
                "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic",
                "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago",
                "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
                "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands",
                "United States Virgin Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City State", "Venezuela",
                "Vietnam", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zaire", "Zambia",
                "Zimbabwe"));
    }
	
	
    public CarritoPagoView() {
        addClassNames("carrito-view");
        addClassNames(Display.FLEX, FlexDirection.COLUMN, Height.FULL);

        controlador = new CarritoPagoInteractorImpl(this);

        content = new Main();
        content.addClassNames(Display.GRID, Gap.XLARGE, AlignItems.START, JustifyContent.CENTER, MaxWidth.SCREEN_MEDIUM,
                Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);
        content.add(createCheckoutForm());
        controlador.consultarProductoCarrito(); 
        controlador.listarClientes();
    }

    
    private Component createCheckoutForm() {
        Section checkoutForm = new Section();
        checkoutForm.addClassNames(Display.FLEX, FlexDirection.COLUMN, Flex.GROW);

        H2 header = new H2("Realizar Pago");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        Paragraph note = new Paragraph("Todos los campos son obligatorios");
        note.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        checkoutForm.add(header, note);

        checkoutForm.add(createPersonalDetailsSection());
        checkoutForm.add(createShippingAddressSection());
        checkoutForm.add(createPaymentInformationSection());
        checkoutForm.add(new Hr());
        checkoutForm.add(createFooter());

        return checkoutForm;
    }

    private Section createPersonalDetailsSection() {
        Section personalDetails = new Section();
        personalDetails.addClassNames(Display.FLEX, FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM);

        Paragraph stepOne = new Paragraph("Paso de Verificacion 1/3");
        stepOne.addClassNames(Margin.NONE, FontSize.SMALL, TextColor.SECONDARY);

        H3 header = new H3("Detalles Personales");
        header.addClassNames(Margin.Bottom.MEDIUM, Margin.Top.SMALL, FontSize.XXLARGE);
        
/*
        try {
        	ClientResponse paquetes = db.listarClientes();
        	collectionClientes = paquetes.getItems();
        	
        	collectionClientes.forEach( (cliente) -> {
           		itemsClientes.add(cliente.getName());
        		itemsClientes.add(cliente.getAddress());
        		
          	});

		} catch (Exception e) {
			// TODO: handle exception
			Notification.show("No se pudieron cargar los clientes.");
		}
  */      
        /*
        add(createFormLayout());       
        calculate.addClickListener(e -> {
        	String clienteString = clientCombo.getValue();
        	
        	collectionClientes.forEach(cliente -> {
        		if( clienteString == cliente.getName() ) {
        			clienteSelected = cliente;
        			
        		}
        	});
        	
        });
        */
       
        TextField name = new TextField("Nombre");
        name.setRequiredIndicatorVisible(true);
        name.setPattern("[\\p{L} \\-]+");
        name.addClassNames(Margin.Bottom.SMALL);
		

        EmailField email = new EmailField("Dirección de Correo"); 
        email.setRequiredIndicatorVisible(true);
        email.addClassNames(Margin.Bottom.SMALL);

        /*
        TextField phone = new TextField("Número de teléfono");
        phone.setRequiredIndicatorVisible(true);
        phone.setPattern("[\\d \\-\\+]+");
        phone.addClassNames(Margin.Bottom.SMALL);
        phone.setValue("");
*/
        
        clientCombo.addValueChangeListener(event -> {
            String selectedValue = event.getValue();
            TextField phone = new TextField("Número de teléfono");
            phone.setRequiredIndicatorVisible(true);
            phone.setPattern("[\\d \\-\\+]+");
            phone.addClassNames(Margin.Bottom.SMALL);
            phone.setValue(selectedValue);
            personalDetails.add(phone);
        });

        
/*
        clientCombo.addAttachListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		try
        		{
        			clientCombo.add(itemsClientes);
        		}catch(Exception ex)
        		{
        			JOptionPane.showMessageDialog(null, ex);
        		}
        	}
        });
        
      */  
        Checkbox rememberDetails = new Checkbox("Recordar datos personales para la próxima vez");
        rememberDetails.addClassNames(Margin.Top.SMALL);

        personalDetails.add(stepOne, header, createFormLayout());
        return personalDetails;
    }

    private Section createShippingAddressSection() {
        Section shippingDetails = new Section();
        shippingDetails.addClassNames(Display.FLEX, FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM);

        Paragraph stepTwo = new Paragraph("Paso de Verificacion 2/3");
        stepTwo.addClassNames(Margin.NONE, FontSize.SMALL, TextColor.SECONDARY);

        H3 header = new H3("Detalles de envio");
        header.addClassNames(Margin.Bottom.MEDIUM, Margin.Top.SMALL, FontSize.XXLARGE);

        ComboBox<String> countrySelect = new ComboBox<>("Pais");
        countrySelect.setRequiredIndicatorVisible(true);
        countrySelect.addClassNames(Margin.Bottom.SMALL);

        
        TextArea address = new TextArea("Dirección de casa");
        address.setMaxLength(200);
        address.setRequiredIndicatorVisible(true);
        address.addClassNames(Margin.Bottom.SMALL);

        Div subSection = new Div();
        subSection.addClassNames(Display.FLEX, FlexWrap.WRAP, Gap.MEDIUM);

        TextField postalCode = new TextField("Codigo Postal");
        postalCode.setRequiredIndicatorVisible(true);
        postalCode.setPattern("[\\d \\p{L}]*");
        postalCode.addClassNames(Margin.Bottom.SMALL);

        TextField city = new TextField("Ciudad");
        city.setRequiredIndicatorVisible(true);
        city.addClassNames(Flex.GROW, Margin.Bottom.SMALL);

        subSection.add(postalCode, city);

        ComboBox<String> stateSelect = new ComboBox<>("Estado");
        stateSelect.setRequiredIndicatorVisible(true);

        stateSelect.setItems(states);
        stateSelect.setVisible(false);
        countrySelect.setItems(countries);
        countrySelect
                .addValueChangeListener(e -> stateSelect.setVisible(countrySelect.getValue().equals("United States")));

        Checkbox sameAddress = new Checkbox("La dirección de facturación es la misma que la dirección de envío");
        sameAddress.addClassNames(Margin.Top.SMALL);

        Checkbox rememberAddress = new Checkbox("Recordar dirección para la próxima vez");

        shippingDetails.add(stepTwo, header,  address, subSection, stateSelect, sameAddress,
                rememberAddress);
        return shippingDetails;
    }

    private Component createPaymentInformationSection() {
        Section paymentInfo = new Section();
        paymentInfo.addClassNames(Display.FLEX, FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM);

        Paragraph stepThree = new Paragraph("Paso de Verificacion 3/3");
        stepThree.addClassNames(Margin.NONE, FontSize.SMALL, TextColor.SECONDARY);

        H3 header = new H3("Detalles Personales");
        header.addClassNames(Margin.Bottom.MEDIUM, Margin.Top.SMALL, FontSize.XXLARGE);

        TextField cardHolder = new TextField("Nombre del titular de la tarjeta");
        cardHolder.setRequiredIndicatorVisible(true);
        cardHolder.setPattern("[\\p{L} \\-]+");
        cardHolder.addClassNames(Margin.Bottom.SMALL);

        Div subSectionOne = new Div();
        subSectionOne.addClassNames(Display.FLEX, FlexWrap.WRAP, Gap.MEDIUM);

        TextField cardNumber = new TextField("Numero de la tarjeta");
        cardNumber.setRequiredIndicatorVisible(true);
        cardNumber.setPattern("[\\d ]{12,23}");
        cardNumber.addClassNames(Margin.Bottom.SMALL);

        TextField securityCode = new TextField("Código de Seguridad");
        securityCode.setRequiredIndicatorVisible(true);
        securityCode.setPattern("[0-9]{3,4}");
        securityCode.addClassNames(Flex.GROW, Margin.Bottom.SMALL);
        securityCode.setHelperText("Que es esto?");

        subSectionOne.add(cardNumber, securityCode);

        Div subSectionTwo = new Div();
        subSectionTwo.addClassNames(Display.FLEX, FlexWrap.WRAP, Gap.MEDIUM);

        Select<String> expirationMonth = new Select<>();
        expirationMonth.setLabel("Mes de expiración");
        expirationMonth.setRequiredIndicatorVisible(true);
        expirationMonth.setItems("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");

        Select<String> expirationYear = new Select<>();
        expirationYear.setLabel("Año de expiración");
        expirationYear.setRequiredIndicatorVisible(true);
        expirationYear.setItems("23", "24", "25", "26","27","28","29","30");

        subSectionTwo.add(expirationMonth, expirationYear);

        paymentInfo.add(stepThree, header, cardHolder, subSectionTwo);
        return paymentInfo;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.BETWEEN, Margin.Vertical.MEDIUM);

        Button cancel = new Button("Cancelar orden");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button pay = new Button("Pago Seguro", new Icon(VaadinIcon.LOCK));
        pay.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        footer.add(cancel, pay);
        return footer;
    }
	
	/*
    private Aside createAside() {
        Aside aside = new Aside();
        aside.addClassNames(Background.CONTRAST_5, BoxSizing.BORDER, Padding.LARGE, BorderRadius.LARGE,
                Position.STICKY);
        Header headerSection = new Header();
        headerSection.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.BETWEEN, Margin.Bottom.MEDIUM);
        H3 header = new H3(""
        		+ "Lista de Productos");
        header.addClassNames(Margin.NONE);
        Button edit = new Button("Editar");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        headerSection.add(header, edit);

        try {
        	ProductoCarritoResponse respuesta = db.consultarProductoCarrito();
        	
        	
        }	catch (IOException e1) {
			Notification.show("No se pudo consultar el carrito, favor revisa tu conexion a internet.");
			e1.printStackTrace();
	    }


        aside.add(headerSection, ul);
        return aside;
    }
    */

    private ListItem createListItem(String producto, String descripcion, String cantidad, String total ) {
        ListItem item = new ListItem();
        item.addClassNames(Display.FLEX, JustifyContent.BETWEEN);

        Div subSection = new Div();
        subSection.addClassNames(Display.GRID, FlexDirection.COLUMN);

        subSection.add(new Span(producto));
        Span descripcionSpan = new Span(descripcion);
        descripcionSpan.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        subSection.add(descripcionSpan);
        
        Span cantidadSpan = new Span(cantidad);
        cantidadSpan.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        subSection.add(cantidadSpan);

        Span totalSpan = new Span(total);
        item.add(subSection, totalSpan);
        
        
        return item;
    }
    
	
	private Component createFormLayout() { 	
    	FormLayout formLayout = new FormLayout();
    	
    	formLayout.addClassNames(Display.FLEX, FlexDirection.COLUMN, Margin.Bottom.XLARGE, Margin.Top.MEDIUM);
    	clientCombo.setLabel("Clientes");
    	clientCombo.addClassNames(FontSize.SMALL, TextColor.SECONDARY);;
    	clientCombo.setPlaceholder("Clientes");
    	clientCombo.setAllowedCharPattern("[\\+\\d]");
    	clientCombo.setItems(itemsClientes);

    	//PRUEBAS
    	
    	
    	
    	
    	//FIN PRUEBAS
    	
    	
    	
        formLayout.add(clientCombo); // Include the field you will need.
       // formLayout.setResponsiveSteps(
               // new ResponsiveStep("1", 1),
             //   new ResponsiveStep("500px", 0));
        formLayout.setColspan(clientCombo, 2);
        formLayout.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
    	return formLayout;
    }


	@Override
	public void mostrarProductosEnCarrito(List<ProductoCarrito> producto) {
		Aside aside = new Aside();
        aside.addClassNames(Background.CONTRAST_5, BoxSizing.BORDER, Padding.LARGE, BorderRadius.LARGE,
                Position.STICKY);
        Header headerSection = new Header();
        headerSection.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.BETWEEN, Margin.Bottom.MEDIUM);
        H3 header = new H3(""
        		+ "Lista de Productos");
        header.addClassNames(Margin.NONE);
        Button edit = new Button("Editar");
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        headerSection.add(header, edit);
		UnorderedList ul = new UnorderedList();
        ul.addClassNames(ListStyleType.NONE, Margin.NONE, Padding.NONE, Display.FLEX, FlexDirection.COLUMN, Gap.MEDIUM);
		for (ProductoCarrito productoCarrito : producto) {
    		ul.add(createListItem(productoCarrito.getProducto(), productoCarrito.getDescripcion(), 
    				"Cantidad: "+productoCarrito.getCantidad() + " ("+productoCarrito.getPrice()+"c/u)" ,
    				"L "+productoCarrito.getTotal()));
    	}
		aside.add(headerSection, ul);
		content.add(aside);
		add(content);
	}


	@Override
	public void mostrarClientesEnCarrito(List<ClientModel> list) {
		try {
        	//ClientResponse paquetes = listarClientes();
        	//collectionClientes = paquetes.getItems();
        	
        	collectionClientes.forEach( (cliente) -> {
           		itemsClientes.add(cliente.getName());
        		itemsClientes.add(cliente.getAddress());
        		
          	});

		} catch (Exception e) {
			// TODO: handle exception
			Notification.show("No se pudieron cargar los clientes.");
		}
		
	}









		
   
}
