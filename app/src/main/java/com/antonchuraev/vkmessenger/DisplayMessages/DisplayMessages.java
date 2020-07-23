package com.antonchuraev.vkmessenger.DisplayMessages;

import android.os.Bundle;
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

public class DisplayMessages extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

	ListView listView;

	SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_messagers);
		initialize();
		VKOnlineFriendsRequest();

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
		swipeRefreshLayout.setOnRefreshListener(this);
		// делаем повеселее
		swipeRefreshLayout.setColorScheme(R.color.colorPrimary);

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
	}

	@Override
	public void onRefresh() {
		swipeRefreshLayout.setRefreshing(true);
		VKOnlineFriendsRequest();
		swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1500);
	}
}
