package com.example.application.data.service;
import com.example.application.data.entity.Client;
import com.example.application.data.entity.ClientResponse;
import com.example.application.data.entity.PackageModel;
import com.example.application.data.entity.PaqueteResponse;
import com.example.application.data.entity.PaymentModel;
import com.example.application.data.entity.ProductoCarrito;
import com.example.application.data.entity.ProductoCarritoResponse;
import com.example.application.data.entity.ReservaModel;
import com.example.application.data.entity.ReservasResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface DatabaseRepository {
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@GET("turiApp/clientes")
	Call<ClientResponse> listarClientes();
	
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@POST("turiApp/clientes")
	Call<ResponseBody> crearClientes(@Body Client nuevo);
	
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@PUT("turiApp/clientes")
	Call<ResponseBody> actualizarClientes(@Body Client actualizar);
	
	
	//*********************SERVICIOS DE PAQUETES******************************************
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@GET("turiApp/paquetes")
	Call<PaqueteResponse> listarPaquetes();
	


	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@POST("turiApp/paquetes")
	Call<ResponseBody> crearPaquetes(@Body PackageModel nuevo);
	
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@PUT("turiApp/paquetes")
	Call<ResponseBody> actualizarPaquetes(@Body PackageModel actualizar);
	
	
	@Headers({ 
		"Accept: application/json", 
		"User-Agent: Retrofit-Sample-App"
	})
	@DELETE("turiApp/paquetes")
	Call<ResponseBody> eliminarPaquetes(@Query("ID") Integer ID);
	
	
	
	//*********************SERVICIOS DE PAGO DE LA VISTA PAYMENT******************************************
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@POST("turiApp/payment")
	Call<ResponseBody> crearPayment(@Body PaymentModel nuevaPago);
	
	
	
	
	//*********************SERVICIOS DE RESERVAS******************************************
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	
	@GET("turiApp/reservas")
	Call<ReservasResponse> listarReservasPendientes();
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@POST("turiApp/reservas")
	Call<ResponseBody> crearReserva(@Body ReservaModel nuevaReserva);
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@PUT("turiApp/reservas")
	Call<ResponseBody> actualizarReserva(@Body ReservaModel nuevaReserva);
	

	//*********************SERVICIOS DE CARRITO******************************************
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@GET("turiApp/carrito")
	Call<ProductoCarritoResponse> listarProductosCarrito();
	
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@POST("turiApp/carrito")
	Call<ResponseBody> agregarProductoCarrito(@Body ProductoCarrito nuevo);
}



