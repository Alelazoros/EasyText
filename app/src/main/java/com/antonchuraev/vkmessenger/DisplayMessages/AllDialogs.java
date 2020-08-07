package com.antonchuraev.vkmessenger.DisplayMessages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.antonchuraev.vkmessenger.Authorization.Authorization;
import com.antonchuraev.vkmessenger.DisplayMessages.Adapter.MyRecyclerAllDialogsAdapter;
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

public class AllDialogs extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
	public final String TAG = "Easy Text";

	RecyclerView recyclerView;

	SwipeRefreshLayout swipeRefreshLayout;

	DialogList lastConversations;
	MyRecyclerAllDialogsAdapter myRecyclerAllDialogsAdapter;

	private int offset = 0;

	boolean endReach = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_messagers);
		initialize();
		listenersAction();
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
				} catch (JSONException e) {
					e.printStackTrace();
				}
				myRecyclerAllDialogsAdapter.notifyItemRangeInserted(offset, 12);
				offset += 12;
				endReach = false;
			}
		});
	}


	private void initialize() {
		recyclerView = findViewById(R.id.recycler_view);
		swipeRefreshLayout = findViewById(R.id.refresh);
		swipeRefreshLayout.setColorScheme(R.color.colorPrimary);
		lastConversations = new DialogList();
		myRecyclerAllDialogsAdapter = new MyRecyclerAllDialogsAdapter(getApplicationContext(), lastConversations);
		recyclerView.setAdapter(myRecyclerAllDialogsAdapter);
	}

	private void listenersAction() {
		swipeRefreshLayout.setOnRefreshListener(this);
		myRecyclerAllDialogsAdapter.setOnItemClickListener(new MyRecyclerAllDialogsAdapter.ClickListener() {
			@Override
			public void onItemClick(int position, View v) {
				Log.d(TAG, " Item on position " + position + " clicked");
				Intent fullDialog = new Intent(getApplicationContext(), FullDialog.class);
				Dialog dialog = (Dialog) lastConversations.getDialogList().get(position);
				fullDialog.putExtra("DIALOG", dialog);
				startActivity(fullDialog);

			}
		});
		recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
			@Override
			public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
				if (!recyclerView.canScrollVertically(1) && !endReach) {
					Log.d(TAG, "END REACHED");
					endReach = true;
					VKLastDialogsRequest(offset);
				}

			}
		});

	}

	@Override
	public void onRefresh() {
		Log.d(TAG, "onRefresh");
		swipeRefreshLayout.setRefreshing(true);
		lastConversations.dialogList.removeIf(x -> lastConversations.dialogList.indexOf(x) > 12);
		myRecyclerAllDialogsAdapter.notifyDataSetChanged();
		offset = 13;
		VKLastDialogsRequest(offset);

		swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 1500);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		lastConversations.dialogList.removeIf(x -> lastConversations.dialogList.indexOf(x) > 12);
		myRecyclerAllDialogsAdapter.notifyDataSetChanged();
	}
}
