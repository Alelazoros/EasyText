package com.antonchuraev.vkmessenger.DispalayMessages;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.antonchuraev.vkmessenger.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.antonchuraev.vkmessenger.MyClasses.MyHelper.printDebugMessage;

public class DisplayMessages extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_messagers);
        initialize();
        VKOnlineFriendsRequest();
    }

	private void VKOnlineFriendsRequest() {
		VK.execute(new VKRequest<>("friends.getOnline"), new VKApiCallback<JSONObject>() {
			@Override
			public void fail(@NotNull Exception e) {

			}

			@Override
			public void success(JSONObject jsonObject) {
				printDebugMessage("Success");

				ArrayList arrayList = new ArrayList();
				arrayList.add(jsonObject.toString());

				ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
				listView.setAdapter(arrayAdapter);

			}
		});
	}


	private void initialize() {
        listView = findViewById(R.id.test);
    }



}
