package com.example.application.data.service;

import com.example.application.data.entity.PaymentModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DatabaseServicePayment {
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@POST("turiApp/payment")
	Call<ResponseBody> crearPayment(@Body PaymentModel nuevaPago);

}
