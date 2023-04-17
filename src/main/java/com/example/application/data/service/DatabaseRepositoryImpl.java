package com.example.application.data.service;

import java.io.IOException;

import com.example.application.data.entity.ClientModel;
import com.example.application.data.entity.ClientResponse;
import com.example.application.data.entity.PackageModel;
import com.example.application.data.entity.PaqueteResponse;
import com.example.application.data.entity.PaymentModel;
import com.example.application.data.entity.ProductoCarritoResponse;
import com.example.application.data.entity.ReservaModel;
import com.example.application.data.entity.ReservasResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class DatabaseRepositoryImpl {
	
	private static DatabaseRepositoryImpl INSTANCE = null;
	private DatabaseClient client;
	private DatabaseClient models;

    private DatabaseRepositoryImpl(){
    	client = new DatabaseClient();
    }

    public static DatabaseRepositoryImpl getInstance() {
        if (INSTANCE == null) {
        	synchronized (DatabaseRepositoryImpl.class) {
        		if (INSTANCE == null) { 
                    INSTANCE = new DatabaseRepositoryImpl();
                }
			}
        }
        return INSTANCE;
    }
    
    
    
    //OPERACIONES DE LA VISTA PAQUETES
    public PaqueteResponse listarPaquetes() throws IOException {
    	Call<PaqueteResponse> paquetesCall = client.getDatabaseRepository().listarPaquetes();
    	
    	Response<PaqueteResponse> response = paquetesCall.execute();
    	if(response.isSuccessful()) {
    		return response.body();
    	} else {
    		return null;
    	}
    }
    
    
    public boolean crearPaquetes(PackageModel nuevo) throws IOException {
    	Call<ResponseBody> call = client.getDatabaseRepository().crearPaquetes(nuevo);
    	Response<ResponseBody> response = call.execute();
    	return response.isSuccessful();
    }
    
    
    public boolean actualizarPaquetes(PackageModel actualizar) throws IOException {
    	Call<ResponseBody> call = client.getDatabaseRepository().actualizarPaquetes(actualizar);
    	Response<ResponseBody> response = call.execute();
    	return response.isSuccessful();
    }
    
    
    public boolean eliminarPaquetes(Integer PackageID) throws IOException {
    	Call<ResponseBody> call = client.getDatabaseRepository().eliminarPaquetes(PackageID);
    	Response<ResponseBody> response = call.execute();
    	return response.isSuccessful();
    }
    
    
    
    //OPERACIONES DE LA VISTA CLIENTE
    
    public ClientResponse listarClientes() throws IOException {
    	Call<ClientResponse> paquetesCall = client.getDatabaseRepository().listarClientes();
    	
    	Response<ClientResponse> response = paquetesCall.execute();
    	if(response.isSuccessful()) {
    		return response.body();
    	} else {
    		return null;
    	}
    }
    

    
    public boolean crearClientes(ClientModel nuevo) throws IOException {
    	Call<ResponseBody> call = client.getDatabaseRepository().crearClientes(nuevo);
    	Response<ResponseBody> response = call.execute();
    	return response.isSuccessful();
    }
    
    
    public boolean actualizarClientes(ClientModel actualizar) throws IOException {
    	Call<ResponseBody> call = client.getDatabaseRepository().actualizarClientes(actualizar);
    	Response<ResponseBody> response = call.execute();
    	return response.isSuccessful();
    }

    public boolean crearReserva( ReservaModel nuevaReserva ) throws IOException {
    	Call<ResponseBody> reservaCall = client.getDatabaseRepository().crearReserva(nuevaReserva);
    	Response<ResponseBody> response = reservaCall.execute();
    	
    	return response.isSuccessful();
    	
    }
    
    public boolean actualizarReserva( ReservaModel nuevaReserva ) throws IOException {
    	Call<ResponseBody> reservaCall = client.getDatabaseRepository().actualizarReserva(nuevaReserva);
    	Response<ResponseBody> response = reservaCall.execute();
    	
    	return response.isSuccessful();
    	
    }
    
    
    public ReservasResponse listarReserva() throws IOException {
    	Call<ReservasResponse> reservaCall = client.getDatabaseRepository().listarReservasPendientes();
    	Response<ReservasResponse> response = reservaCall.execute();
    	
    	if(response.isSuccessful()) {
    		return response.body();
    	} else {
    		return null;
    	}
    	
    }
    
    public boolean crearPayment( PaymentModel nuevaPayment ) throws IOException {
    	Call<ResponseBody> paymentCall = client.getDatabaseRepository().crearPayment(nuevaPayment);
    	Response<ResponseBody> response = paymentCall.execute();
    	
    	return response.isSuccessful();
    	
    }
    
    
    //OPERACIONES DE LA PANTALLA CARRITO
    
    public ProductoCarritoResponse consultarProductoCarrito() throws IOException {
    	Call<ProductoCarritoResponse> call = client.getDatabaseRepository().listarProductosCarrito();
    	
    	Response<ProductoCarritoResponse> response = call.execute();
    	if(response.isSuccessful()) {
    		return response.body();
    	} else {
    		return null;
    	}
    }
    
    

}
