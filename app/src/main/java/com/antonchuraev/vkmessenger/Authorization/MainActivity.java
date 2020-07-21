package com.antonchuraev.vkmessenger.Authorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.antonchuraev.vkmessenger.DispalayMessages.DisplayMessages;
import com.antonchuraev.vkmessenger.MyClasses.VKAccount;
import com.antonchuraev.vkmessenger.R;
import com.antonchuraev.vkmessenger.Requests.UserDetails;
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

		VKAccount vkAccount = new VKAccount();
		vkAccount.restore(this);

		RetrofitApplication.getApi().getUserDetails(vkAccount.access_token).enqueue(new Callback<UserDetails>() {
			@Override
			public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
				printDebugMessage(" // User passed authorization access_token == "+vkAccount.access_token);
				Intent displayMessages = new Intent(getBaseContext(), DisplayMessages.class);
				startActivity(displayMessages);
			}

			@Override
			public void onFailure(Call<UserDetails> call, Throwable t) {
				printDebugMessage("start authorization Intent");
				Intent authorizationIntent = new Intent(getApplicationContext(), Authorization.class);
				startActivity(authorizationIntent);
			}
		});
	}

}
