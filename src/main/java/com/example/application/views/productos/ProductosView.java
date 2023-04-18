package com.example.application.views.productos;
import com.example.application.controller.ProductosInteractor;
import com.example.application.controller.ProductosInteractorImpl;
import com.example.application.data.entity.PackageModel;
import com.example.application.data.entity.PaqueteResponse;
import com.example.application.data.entity.ProductoCarrito;
import com.example.application.data.entity.ProductosReport;
import com.example.application.data.service.DatabaseRepositoryImpl;
import com.example.application.data.service.ReportGenerator;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
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
@PageTitle("Meraki Home, Tu tienda exclusiva de materiales para Escolar y Oficina")
@Route(value = "productos/:packageModelID?/:action?(edit)", layout = MainLayout.class)
public class ProductosView extends Div implements BeforeEnterObserver, ProductosViewModel {//aca se implementa la clase de la interfaz de la vista

    private final String PACKAGEMODEL_ID = "packageModelID";
    private final String PACKAGEMODEL_EDIT_ROUTE_TEMPLATE = "productos/%s/edit";

    private final Grid<PackageModel> grid = new Grid<>(PackageModel.class, false);

    private TextField namePackage;
    private TextField destiny;
    //private TextField duration;
    private TextField activities;
    private TextField price;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

    private PackageModel packageModel;
    
    private ProductosInteractor controlador;//la vista esta hablando directamente a la DB lo cual es incorrecto, debera de usar el controlador
    private List<PackageModel> models;


    public ProductosView() {
    	
        addClassNames("packages-view");
        
        controlador = new ProductosInteractorImpl(this);
        
        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("packageID").setAutoWidth(true).setHeader("ID");
        grid.addColumn("namePackage").setAutoWidth(true).setHeader("Categoria");
        grid.addColumn("destiny").setAutoWidth(true).setHeader("Marca");
        grid.addColumn("activities").setAutoWidth(true).setHeader("Especificaciones");
        grid.addColumn("price").setAutoWidth(true).setHeader("Precio");
        
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
        	
            if (event.getValue() != null) {
              
              UI.getCurrent().navigate(String.format(PACKAGEMODEL_EDIT_ROUTE_TEMPLATE, event.getValue().getPackageID()));
            	
            	//namePackage.setValue(event.getValue().getNamePackage());
            	//packageID.setValue(event.getValue().getPackageID().toString());
            	
            } else {
                clearForm();
                UI.getCurrent().navigate(ProductosView.class);
            }
            
        });
        
        GridContextMenu<PackageModel> menu = grid.addContextMenu();
        
        
        GridMenuItem<PackageModel> comprar = menu.addItem("Agregar al Carrito", event -> {
        	if (event != null && event.getItem() != null) {
        		PackageModel prodAgregar = event.getItem().get();
        		
        		ProductoCarrito productoCarrito = new ProductoCarrito();
        		productoCarrito.setIdproducto(prodAgregar.getPackageID());
        		productoCarrito.setCantidad(1);
            	controlador.agregarProductoCarrito(productoCarrito);
        	}        	
        });
        /*
        GridMenuItem<PackageModel> generarReporte = menu.addItem("Generar Reporte PDF", event -> {
        	Notification.show("Generando reporte PDF...");
    		generarReporte();
        	
        });
        */
        menu.add(new Hr());
        GridMenuItem<PackageModel> delete = menu.addItem("Eliminar", event -> {
        	if (event != null && event.getItem() != null) {
        		PackageModel prodEliminar = event.getItem().get();        	
        		ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader("¿Eliminar \'"+prodEliminar.getNamePackage() + "("+prodEliminar.getDestiny()+")\'?");
                dialog.setText("¿Estás seguro de que deseas eliminar de forma permanente este producto?");

                dialog.setCancelable(true);

                dialog.setConfirmText("Eliminar");
                dialog.setCancelText("Cancelar");
                dialog.setConfirmButtonTheme("error primary");

                dialog.addConfirmListener(eventDialog -> {
                	controlador.eliminarProductos(prodEliminar);
                //
                });
                
                dialog.open();
        	}
        });
        delete.addComponentAsFirst(createIcon(VaadinIcon.TRASH));
        comprar.addComponentAsFirst(createIcon(VaadinIcon.SHOP));
      //  generarReporte.addComponentAsFirst(createIcon(VaadinIcon.SHOP));
        consultarProductos();
        

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.packageModel == null) {
                    this.packageModel = new PackageModel();
                    this.packageModel.setNamePackage(namePackage.getValue());
                    this.packageModel.setDestiny(destiny.getValue());
                    this.packageModel.setActivities(activities.getValue());
                    this.packageModel.setPrice(Integer.parseInt(price.getValue()));
                    
                    if(this.packageModel.getNamePackage()==null) {
                    	Notification.show("Para agregar un registro del paquete el campo nombre es requerido, favor digitar un valor valido");
        			}else if(this.packageModel.getDestiny()==null || this.packageModel.getDestiny().isEmpty()){
        				Notification.show("Para agregar un registro el campo telefono es requerido, favor digitar un valor valido");
        			}else {
        				controlador.crearProductos(packageModel);
        			}
                }else {
        			//ACTUALIZACION
                	this.packageModel.setNamePackage(namePackage.getValue());
                    this.packageModel.setDestiny(destiny.getValue());
                    this.packageModel.setActivities(activities.getValue());
                    this.packageModel.setPrice(Integer.parseInt(price.getValue()));
                    
                    if(this.packageModel.getNamePackage()==null) {
                    	Notification.show("El nombre es requerido, favor digitar un valor valido");
        			}else if(this.packageModel.getDestiny()==null || this.packageModel.getDestiny().isEmpty()) {
        				Notification.show("El nombre es requerido, favor digitar un valor valido");
        			}else {
        				controlador.actualizarProductos(packageModel);
        		}
    		}
                
                
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } 
        });
    }
/*
	private void generarReporte() {
		ReportGenerator generador = new ReportGenerator();
		ProductosReport datasource = new ProductosReport();
		datasource.setProductos(models);
		Map<String, Object> parameters = new HashMap<>();
		generador.gererarReportePDF("reporteproductos", datasource, parameters);
		//generador.generarReportePDF("reporteproductos", datasource, parameters );
	}
*/
	private void consultarProductos() {
		controlador.consultarProductos();
	}
	
	private Component createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("color", "var(--lumo-secondary-text-color)")
                .set("margin-inline-end", "var(--lumo-space-s")
                .set("padding", "var(--lumo-space-xs");
        return icon;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> packageModelId = event.getRouteParameters().get(PACKAGEMODEL_ID).map(Long::parseLong);
        if (packageModelId.isPresent()) {
        	Long ID = packageModelId.get();
        	Integer idInteger = ID != null ? ID.intValue() : null;
        	for(PackageModel packageModel : models) {
        		if(packageModel.getPackageID() == idInteger) {
        			populateForm(packageModel);
        			break;
        		}
        		
        	}

        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        namePackage = new TextField("Categoria");
        destiny = new TextField("Marca");
        activities = new TextField("Especificaciones");
        price = new TextField("Precio");
        formLayout.add(namePackage, destiny, activities, price);

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
    
    
    private void populateForm(PackageModel value) {
    	this.packageModel = value;
    	if(value == null) {
            namePackage.setValue("");
            destiny.setValue("");
            activities.setValue("");
            price.setValue("");
    	}else {	
    		namePackage.setValue(value.getNamePackage());
    		destiny.setValue(value.getDestiny());
    		activities.setValue(value.getActivities());
    		price.setValue(value.getPrice().toString());
    	}

    }
    
    private void actualizarPantalla() {
    	clearForm();
        refreshGrid();
        consultarProductos();
        UI.getCurrent().navigate(ProductosView.class);
    }

	@Override
	public void refrescarGridProductos(List<PackageModel> models) {
		Collection<PackageModel> collectionPaquetes = models;
    	grid.setItems(collectionPaquetes);//TODO: CAMBIO PENDIENTE
    	this.models = models;
	}

	@Override
	public void mostrarMensajeCreacionProducto(PackageModel nuevo, String mensaje) {
		//Notification.show("Ficha del Cliente creado satisfactoriamente...");//se reemplaza por lo siguiente
		Notification.show(String.format(mensaje, nuevo.getNamePackage()));
		actualizarPantalla();
		
	}

	@Override
	public void mostrarMensajeActualizacionProducto(PackageModel nuevo, String mensaje) {
        //Notification.show("Ficha del cliente fue actualizada satisfactoriamente...");
		Notification.show(String.format(mensaje, nuevo.getNamePackage()));
		actualizarPantalla();
	}

	@Override
	public void mostrarMensajeEliminacionProducto(PackageModel nuevo, String mensaje) {
		//Notification.show("Producto eliminado satisfactoriamente...");
		Notification.show(String.format(mensaje, nuevo.getNamePackage()));
		actualizarPantalla();
	}

	@Override
	public void mostrarMensajeProductoAgregadoCarrito(String mensaje) {
		// TODO Auto-generated method stub
		Notification.show(mensaje);
	}
}
