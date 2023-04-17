package com.example.application.data.service;
import java.util.concurrent.TimeUnit;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseClient {
	
	private String baseUrl = "https://apex.oracle.com/pls/apex/grupo_uno_pav2/";
	private Long timeOut = (long) 30000;
	private Retrofit retrofit;
	private HttpLoggingInterceptor interceptor = null;
	
	public DatabaseClient() {
		
		interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		
		OkHttpClient client = new OkHttpClient.Builder()
				.addInterceptor(interceptor)
				.connectTimeout(this.timeOut, TimeUnit.MILLISECONDS)
				.writeTimeout(this.timeOut, TimeUnit.MILLISECONDS)
				.readTimeout(this.timeOut, TimeUnit.MILLISECONDS)
				.build();
		
		retrofit = new Retrofit.Builder()
				.client(client)
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create( new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create() ))
				.build();
		
	}
	
	public DatabaseRepository getDatabaseRepository() {
		return retrofit.create(DatabaseRepository.class);
	}
	

}
