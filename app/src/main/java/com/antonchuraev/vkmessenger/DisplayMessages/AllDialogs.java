package com.antonchuraev.vkmessenger.DisplayMessages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.antonchuraev.vkmessenger.Authorization.Authorization;
import com.antonchuraev.vkmessenger.DisplayMessages.Adapter.MyListAdapter;
import com.antonchuraev.vkmessenger.DisplayMessages.DialogsList.Dialog;
import com.antonchuraev.vkmessenger.DisplayMessages.DialogsList.DialogList;
import com.antonchuraev.vkmessenger.DisplayMessages.FullDialog.FullDialog;
import com.antonchuraev.vkmessenger.R;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class AllDialogs extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
	public final String TAG = "Easy Text";

	ListView listView;

	SwipeRefreshLayout swipeRefreshLayout;

	DialogList lastConversations;
	MyListAdapter myListAdapter;

	private int offset = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_messagers);
		initialize();
		setListeners();


	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		VKLastDialogsRequest(offset);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart");
		offset = 13;
		VKLastDialogsRequest(offset);
	}

	private void VKLastDialogsRequest(int offsetInCall) {
		VK.execute(new VKRequest("messages.getConversations").addParam("offset", offsetInCall).addParam("extended", 1).addParam("count", 12).addParam("fields", "photo_200,online"), new VKApiCallback<JSONObject>() {
			@Override
			public void fail(@NotNull Exception e) {
				e.printStackTrace();
				Intent authorization = new Intent(getApplicationContext(), Authorization.class);
				startActivity(authorization);
			}

			@Override
			public void success(JSONObject jsonObject) {
				Log.d(TAG, "success VKLastDialogsRequest offset:" + offset);
				try {
					lastConversations.addToListFromJSONObject(jsonObject.getJSONObject("response"));
					myListAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				offset += 12; //TODO STOP INCREMENT AFTER REACHING THE END
			}
		});
	}


	private void initialize() {
		listView = findViewById(R.id.list_view);
		swipeRefreshLayout = findViewById(R.id.refresh);
		swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
		lastConversations = new DialogList();
		myListAdapter = new MyListAdapter(getApplicationContext(), R.layout.activity_my_list_adapter, lastConversations);
		listView.setAdapter(myListAdapter);
	}

	private void setListeners() {
		swipeRefreshLayout.setOnRefreshListener(this);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
	}

	@Override
	public void onRefresh() {
		Log.d(TAG, "onRefresh");
		swipeRefreshLayout.setRefreshing(true);
		//TODO
		swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1500);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent fullDialog = new Intent(this, FullDialog.class);
		Dialog dialog = (Dialog) listView.getItemAtPosition(position);

		fullDialog.putExtra("NAME", dialog.getName());
		fullDialog.putExtra("PHOTO_URL", dialog.getPhotoURL());
		fullDialog.putExtra("ID", dialog.getReceiverId());

		startActivity(fullDialog);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
				&& (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
				listView.getFooterViewsCount()) >= (myListAdapter.getCount() - 1)) {

			Log.d(TAG, " list end reached");
			VKLastDialogsRequest(offset);
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		Log.d(TAG, "scroll: firstVisibleItem:" + firstVisibleItem
				+ ", visibleItemCount:" + visibleItemCount
				+ ", totalItemCount:" + totalItemCount
				+ ", offset:" + offset);


	}


	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		//TODO обрезание списка
		lastConversations.dialogList.removeIf(x -> lastConversations.dialogList.indexOf(x) > 12);
		myListAdapter.notifyDataSetChanged();

	}
}
