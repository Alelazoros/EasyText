package com.antonchuraev.vkmessenger;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;
import com.vk.api.sdk.auth.VKAccessToken;

public class SampleApplication extends android.app.Application {


	VKTokenExpiredHandler tokenTracker = () -> {
		// token expired
		Log.d("debug","// token expired");
	};

	@Override
	public void onCreate() {
		super.onCreate();
		VK.addTokenExpiredHandler(tokenTracker);
		VK.initialize(this);
	}
}

