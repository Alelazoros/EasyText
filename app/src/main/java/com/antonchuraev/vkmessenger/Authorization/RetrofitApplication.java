package com.antonchuraev.vkmessenger.Authorization;

import com.antonchuraev.vkmessenger.Requests.RetrofitInterfaces;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApplication extends android.app.Application {
	private static RetrofitInterfaces authorization;
	private Retrofit retrofit;

	@Override
	public void onCreate() {
		super.onCreate();
		retrofit = new Retrofit.Builder()
				.baseUrl("https://api.vk.com") //Базовая часть адреса
				.addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
				.build();
		authorization = retrofit.create(RetrofitInterfaces.class); //Создаем объект, при помощи которого будем выполнять запросы
	}

	public static RetrofitInterfaces getApi() {
		return authorization;
	}

}

