package com.antonchuraev.vkmessenger.Requests;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import java.util.List;

public interface RetrofitInterfaces {

	@GET("user")
	Call<UserDetails> getUserDetails(@Header("Authorization") String credentials);

}
