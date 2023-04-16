package com.example.application.data.service;
import com.example.application.data.entity.ClientModel;
import com.example.application.data.entity.ClientResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface DatabaseServiceClient {
	
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
	Call<ResponseBody> crearClientes(@Body ClientModel nuevo);
	
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@PUT("turiApp/clientes")
	Call<ResponseBody> actualizarClientes(@Body ClientModel actualizar);

}



