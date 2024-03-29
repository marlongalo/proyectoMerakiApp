package com.example.application.views.client;

import com.example.application.data.entity.Client;
import com.example.application.data.entity.ClientReport;
import com.example.application.data.entity.ClientResponse;
import com.example.application.data.entity.PackageModel;
import com.example.application.data.entity.ProductosReport;
import com.example.application.data.service.DatabaseRepositoryImpl;
import com.example.application.data.service.ReportGenerator;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@SuppressWarnings("serial")
@PageTitle("Clientes")
@Route(value = "client/:clientModelID?/:action?(edit)", layout = MainLayout.class)
public class ClientView extends Div implements BeforeEnterObserver {

    private final String CLIENTMODEL_ID = "clientModelID";
    private final String CLIENTMODEL_EDIT_ROUTE_TEMPLATE = "client/%s/edit";

    private final Grid<Client> grid = new Grid<>(Client.class, false);

    private TextField name;
    private TextField address;
    private TextField phone;
    private TextField email;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

    private Client client;
    
    private DatabaseRepositoryImpl db;
    private List<Client> clients;//cambio 1

    public ClientView() {
        
        addClassNames("client-view");
        
        db = DatabaseRepositoryImpl.getInstance();

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("clientID").setAutoWidth(true).setHeader("ID");
        grid.addColumn("name").setAutoWidth(true).setHeader("Nombre del cliente") ;
        grid.addColumn("address").setAutoWidth(true).setHeader("Dirección") ;
        grid.addColumn("phone").setAutoWidth(true).setHeader("Teléfono");
        grid.addColumn("email").setAutoWidth(true).setHeader("Correo electrónico");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row select or deselected, populate form, descomento desde linea 84 hasta 89
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CLIENTMODEL_EDIT_ROUTE_TEMPLATE, event.getValue().getClientID()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ClientView.class);
            }
        });
        consultarProductos();


        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.client == null) {
                    this.client = new Client();
                    this.client.setName(name.getValue());
                    this.client.setAddress(address.getValue());
                    this.client.setPhone(phone.getValue());//Creacion Cambio 2
                    this.client.setEmail(email.getValue());//Creacion Cambio 2
                   
                    if(this.client.getName()==null) {
        				Notification.show("Para agregar un registro el campo nombre es requerido, favor digitar un valor valido");
        			}else if(this.client.getPhone()==null || this.client.getPhone().isEmpty()) {
        				Notification.show("Para agregar un registro el campo telefono es requerido, favor digitar un valor valido");
        			}else {
        				try {
        					boolean creado = db.crearClientes(client);
        					if(creado) {
        						Notification.show("Ficha del Cliente creado satisfactoriamente...");
        						clearForm();
        		                refreshGrid();
        		                consultarProductos();
        		                UI.getCurrent().navigate(ClientView.class);
        					}else {
        						Notification.show("Ficha del cliente no pudo ser creada, favor ingresar datos correctos");
        					}
        					
        				}   catch (IOException e1) {
	        					Notification.show("No se pudo crear el cliente favor revisa tu conexion a internet.");
	        					e1.printStackTrace();
        				}
        			
        			}
        		}else {
        			//ACTUALIZACION
        			this.client.setName(name.getValue());
                    this.client.setAddress(address.getValue());
                    this.client.setPhone(phone.getValue());//Creacion Cambio 2
                    this.client.setEmail(email.getValue());//Creacion Cambio 2
                    
                    if(this.client.getName()==null) {
        				Notification.show("El nombre es requerido, favor digitar un valor valido");
        			}else if(this.client.getPhone()==null || this.client.getPhone().isEmpty()) {
        				Notification.show("El nombre es requerido, favor digitar un valor valido");
        			}else {
        			try {
    					boolean actualizado = db.actualizarClientes(client);
	    					if(actualizado) {	    						
	    						clearForm();
	    		                refreshGrid();
	    		                Notification.show("Ficha del cliente fue actualizada satisfactoriamente...");
	    		                UI.getCurrent().navigate(ClientView.class);
	    					}else {
	    						Notification.show("Ficha del cliente no pudo ser actualizada, favor ingresar datos correctos");
	    					}
	    					
	    				}   catch (IOException e1) {
	        					Notification.show("No se pudo actualizar la ficha del cliente favor revisa tu conexion a internet.");
	        					e1.printStackTrace();
    				}
        		}
    		}

	            } catch (ObjectOptimisticLockingFailureException exception) {
	                Notification n = Notification.show(
	                        "Error updating the data. Somebody else has updated the record while you were making changes.");
	                n.setPosition(Position.MIDDLE);
	                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
	            } 
        });
        
        GridContextMenu<Client> menu = grid.addContextMenu();
        GridMenuItem<Client> generarReporte = menu.addItem("Generar Reporte PDF", event -> {
        	Notification.show("Generando reporte PDF...");
    		generarReporte();
        });
        generarReporte.addComponentAsFirst(createIcon(VaadinIcon.PRINT));
        
        
    }//FIN PUBLIC CLIENT VIEW

    
    private void generarReporte() {
		ReportGenerator generador = new ReportGenerator();
		ClientReport datasource = new ClientReport();
		datasource.setclients(clients);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("LOGOCLIENTES_DIR", "reporte2.png");//aca cargamos la imagen
		
		boolean generado = generador.gererarReportePDF("reporteclientes", datasource, parameters);
		if(generado) {
			Anchor url = new Anchor(generador.getReportPath(), "Reporte");
			url.setTarget("_blank");
			Notification.show("Reporte Generado: "+generador.getReportPath(), 5000, Notification.Position.TOP_CENTER);
		}else {
			Notification.show("Ocurrio un problema al generar el reporte");
		}
		
	}
    
    
	private Component createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("color", "var(--lumo-secondary-text-color)")
                .set("margin-inline-end", "var(--lumo-space-s")
                .set("padding", "var(--lumo-space-xs");
        return icon;
    }

	private void consultarProductos() {
		try {
        	ClientResponse clientes = db.listarClientes();	
        	clients = clientes.getItems();//cambio 1
        	Collection<Client> collectionPaquetes = clientes.getItems();
        	grid.setItems(collectionPaquetes);
        	
		} catch (IOException e) {
			// TODO: handle exception
			Notification.show("No se puedieron cargar los clientes.");
			e.printStackTrace();
		}
	}
        

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	//De aqui obtenemos el product ID que se observa en el formulario al dar click en el GRID
        Optional<Long> clientModelId = event.getRouteParameters().get(CLIENTMODEL_ID).map(Long::parseLong);
        if (clientModelId.isPresent()) {//cambio 1
        	Long ID = clientModelId.get();
        	Integer idInteger = ID != null ? ID.intValue() : null;
        	
        	for (Client client : clients ) {//cambio 1
        		if(client.getClientID() == idInteger) {//cambio 1
        			populateForm(client);//cambio 1
        			break;//cambio 1
        		}//cambio 1
        	}//cambio 1
            
        }//cambio 1
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name = new TextField("Nombre");
        address = new TextField("Direccion");
        phone = new TextField("Telefono");
        email = new TextField("Correo Electronico");
       formLayout.add(name, address, phone, email);
       // formLayout.add(clientID, name, address, phone, email);//cambio1 se remueve de la forma clientID

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Client value) {
        this.client = value;
        if(value == null) {//cambio 1
           	name.setValue("");//cambio 1
            address.setValue("");//cambio 1
            phone.setValue("");//cambio 1
            email.setValue("");//cambio 1
        }else {//cambio 1
        	name.setValue(value.getName());//cambio 1
            address.setValue(value.getAddress());//cambio 1
            phone.setValue(value.getPhone());//cambio 1
            email.setValue(value.getEmail());//cambio 1
        }
    }     
    
    
    
}
