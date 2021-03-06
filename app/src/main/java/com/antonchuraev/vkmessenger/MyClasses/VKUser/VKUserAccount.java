package com.antonchuraev.vkmessenger.MyClasses.VKUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;


public class VKUserAccount {

	public String access_token;
	public long user_id;

	private SharedPreferences sPref;

	public VKUserAccount(String access_token, long user_id) {
		this.access_token = access_token;
		this.user_id = user_id;
	}

	public VKUserAccount() {

	}

	public void save(Context context) {
		sPref = context.getSharedPreferences("VKUserAccount", MODE_PRIVATE);
		SharedPreferences.Editor ed = sPref.edit();
		ed.putString("access_token", access_token);
		ed.putLong("user_id", user_id);
		ed.apply();
		Log.d("Easy Text", "SAVE VKUserAccount access_token:" + access_token + "\nuser_id:" + user_id);
	}

	public void restore(Context context) {
		sPref = context.getSharedPreferences("VKUserAccount", MODE_PRIVATE);
		access_token = sPref.getString("access_token", "");
		user_id = sPref.getLong("user_id", 0);
		Log.d("Easy Text", "LOAD VKUserAccount access_token:" + access_token + "\nuser_id:" + user_id);
	}


}
