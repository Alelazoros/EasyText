package com.antonchuraev.vkmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	Button buttonEnterAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

		setContentView(R.layout.activity_main);
		initialize();
		buttonEnterAccount.setOnClickListener(v->{
			authentication();
		});

		authentication();

	}



	private void initialize() {
		buttonEnterAccount = findViewById(R.id.button_enter_account);
	}

	void authentication(){
		List permissionList= new ArrayList<>();
		permissionList.add(VKScope.NOTIFY);
		permissionList.add(VKScope.FRIENDS);
		permissionList.add(VKScope.PAGES);
		permissionList.add(VKScope.STATUS);
		//permissionList.add(VKScope.MESSAGES); //TODO GET ROOTS
		permissionList.add(VKScope.OFFLINE);
		permissionList.add(VKScope.NOTIFICATIONS);

		VK.login(this,permissionList);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		VKAuthCallback callback = new VKAuthCallback (){
			@Override
			public void onLoginFailed(int i) {
				// User didn't pass authorization
				printDebugMessage("// User didn't pass authorization");
				authentication();
			}

			@Override
			public void onLogin(@NotNull VKAccessToken vkAccessToken) {
				// User passed authorization
				printDebugMessage(" // User passed authorization =="+vkAccessToken.getAccessToken());
				
				Intent displayMessages = new Intent(getBaseContext(), DisplayMessages.class);
				startActivity(displayMessages);
			}
		};

		if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}



	void printDebugMessage(String message) {
		if (message != null) {
			Log.d("debug", message);
		}
	}


}
