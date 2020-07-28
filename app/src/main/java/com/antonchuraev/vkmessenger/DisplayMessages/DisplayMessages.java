package com.antonchuraev.vkmessenger.DisplayMessages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.antonchuraev.vkmessenger.Authorization.Authorization;
import com.antonchuraev.vkmessenger.DisplayMessages.DialogsList.Dialog;
import com.antonchuraev.vkmessenger.DisplayMessages.DialogsList.DialogList;
import com.antonchuraev.vkmessenger.DisplayMessages.OnlineFriends.MyListAdapter;
import com.antonchuraev.vkmessenger.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayMessages extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener , AdapterView.OnItemClickListener {

	ListView listView;

	SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_messagers);
		initialize();
		//VKOnlineFriendsRequest();
		VKLastDialogsRequest();

		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(R.color.colorPrimary);

		listView.setOnItemClickListener(this);
	}

	private void VKLastDialogsRequest() {
		VK.execute(new VKRequest("messages.getConversations").addParam("extended", 1).addParam("count", 200).addParam("fields", "photo_200"), new VKApiCallback<JSONObject>() {
			@Override
			public void fail(@NotNull Exception e) {
				e.printStackTrace();
				Intent authorization = new Intent(getApplicationContext(), Authorization.class);
				startActivity(authorization);

			}

			@Override
			public void success(JSONObject jsonObject) {
				try {
					JSONObject response = jsonObject.getJSONObject("response");

					DialogList lastConversations = new DialogList(response);

					MyListAdapter myListAdapter = new MyListAdapter(getApplicationContext(), R.layout.activity_my_list_adapter, lastConversations);
					listView.setAdapter(myListAdapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		});
	}


	private void initialize() {
		listView = findViewById(R.id.list_view);
		swipeRefreshLayout = findViewById(R.id.refresh);
	}

	@Override
	public void onRefresh() {
		swipeRefreshLayout.setRefreshing(true);
		VKLastDialogsRequest();
		swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1500);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//TODO NEW ACTIVITY
		Intent fullDialog = new Intent(this, FullDialog.class);
		Dialog dialog = (Dialog) listView.getItemAtPosition(position);

		fullDialog.putExtra("NAME", dialog.getName());
		fullDialog.putExtra("PHOTO_URL", dialog.getPhotoURL());
		startActivity(fullDialog);

	}
}
