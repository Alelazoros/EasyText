package com.antonchuraev.vkmessenger.Authorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.antonchuraev.vkmessenger.Authorization.Retrofit.RetrofitApplication;
import com.antonchuraev.vkmessenger.DisplayMessages.AllDialogs;
import com.antonchuraev.vkmessenger.MyClasses.VKUser.VKUserAccount;
import com.antonchuraev.vkmessenger.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.antonchuraev.vkmessenger.MyClasses.MyHelper.printDebugMessage;

public class MainActivity extends AppCompatActivity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authorithasion);

		getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

		VKUserAccount vkUserAccount = new VKUserAccount();
		vkUserAccount.restore(this);

		RetrofitApplication.getApi().getUserDetails(vkUserAccount.access_token).enqueue(new Callback<VKUserAccount>() {
			@Override
			public void onResponse(Call<VKUserAccount> call, Response<VKUserAccount> response) {
				printDebugMessage(" // User passed authorization access_token == " + vkUserAccount.access_token);
				Intent displayMessages = new Intent(getBaseContext(), AllDialogs.class);
				startActivity(displayMessages);
			}

			@Override
			public void onFailure(Call<VKUserAccount> call, Throwable t) {
				printDebugMessage("start authorization Intent");
				Intent authorizationIntent = new Intent(getApplicationContext(), Authorization.class);
				startActivity(authorizationIntent);
			}
		});
	}

}
