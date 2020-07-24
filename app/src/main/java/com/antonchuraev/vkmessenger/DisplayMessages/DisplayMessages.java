package com.antonchuraev.vkmessenger.DisplayMessages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.antonchuraev.vkmessenger.DisplayMessages.OnlineFriends.MyListAdapter;
import com.antonchuraev.vkmessenger.MyClasses.VKUser.VKUsersList;
import com.antonchuraev.vkmessenger.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.antonchuraev.vkmessenger.MyClasses.MyHelper.printDebugMessage;

public class DisplayMessages extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener , AdapterView.OnItemClickListener {

	ListView listView;

	SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_messagers);
		initialize();
		VKOnlineFriendsRequest();

		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(R.color.colorPrimary);

		listView.setOnItemClickListener(this);
	}

	private void VKOnlineFriendsRequest() {
		VK.execute(new VKRequest<>("friends.getOnline"), new VKApiCallback<JSONObject>() {
			@Override
			public void fail(@NotNull Exception e) {

			}

			@Override
			public void success(JSONObject jsonObject) {
				printDebugMessage("Success");

				try {
					VK.execute(new VKRequest("users.get").addParam("user_ids", jsonObject.getJSONArray("response").toString()).addParam("fields" , "photo_200"), new VKApiCallback<JSONObject>() {
						@Override
						public void fail(@NotNull Exception e) {

						}

						@Override
						public void success(JSONObject jsonObject) {

							try {
								JSONArray onlineFriendsJSONArray = jsonObject.getJSONArray("response");
								VKUsersList vkUsersList = new VKUsersList(onlineFriendsJSONArray);

								MyListAdapter myListAdapter = new MyListAdapter(getApplicationContext(),R.layout.activity_my_list_adapter,vkUsersList.getVkUsers());
								listView.setAdapter(myListAdapter);
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					});
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
		VKOnlineFriendsRequest();
		swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1500);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//TODO NEW ACTIVITY
		Intent fullDialog = new Intent(this,FullDialog.class);

		startActivity(fullDialog);

	}
}
