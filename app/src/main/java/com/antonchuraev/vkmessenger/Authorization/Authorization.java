package com.antonchuraev.vkmessenger.Authorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.antonchuraev.vkmessenger.AllMessages.AllDialogs;
import com.antonchuraev.vkmessenger.MyClasses.VKUser.VKUserAccount;
import com.antonchuraev.vkmessenger.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class Authorization extends AppCompatActivity {

    Button buttonEnterAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        initialize();
        authentication();
        buttonEnterAccount.setOnClickListener(v -> {
            authentication();
        });

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
        permissionList.add(VKScope.MESSAGES); //TODO GET ROOTS
        permissionList.add(VKScope.OFFLINE);
        permissionList.add(VKScope.NOTIFICATIONS);

        VK.login(this,permissionList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        VKAuthCallback callback = new VKAuthCallback (){
            @Override
            public void onLoginFailed(int i) {
                Log.d(getResources().getString(R.string.app_name), "// User didn't pass authorization repeat authentication");
                authentication();
            }

            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                // User passed authorization
                Log.d(getResources().getString(R.string.app_name), " // User passed authorization ");

                VKUserAccount vkUserAccount = new VKUserAccount(vkAccessToken.getAccessToken(),vkAccessToken.getUserId());
                vkUserAccount.save(getApplicationContext());

	            Intent displayMessages = new Intent(getBaseContext(), AllDialogs.class);
                startActivity(displayMessages);
            }
        };

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
