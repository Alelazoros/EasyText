package com.antonchuraev.vkmessenger.Authorization.Retrofit;

import com.antonchuraev.vkmessenger.MyClasses.VKUser.VKUserAccount;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface RetrofitInterfaces {

	@GET("user")
	Call<VKUserAccount> getUserDetails(@Header("Authorization") String credentials);

}
