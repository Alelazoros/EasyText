package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.antonchuraev.vkmessenger.DisplayMessages.DialogsList.Dialog;
import com.antonchuraev.vkmessenger.R;
import com.squareup.picasso.Picasso;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class FullDialog extends AppCompatActivity implements AbsListView.OnScrollListener {

    private static final String TAG = "EASY TEXT";
    androidx.appcompat.widget.Toolbar toolbar;
    List messages;
    MyFullDialogAdapter myFullDialogAdapter;
    Dialog dialog;
    private int messageId; //TODO

    ImageView photo;
    ListView listView;
    EditText inputMessage;
    ImageButton sendMessage;
    private long id;
    private int offset = 0;
    private boolean endReached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_dialog);

        initialize();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(dialog.getName());
        Picasso.get().load(dialog.getPhotoURL()).into(photo);

        RequestMessages(offset);

        sendMessage.setOnClickListener(v -> {
            if (inputMessage.getText() != null) {
                VKSendMessage(inputMessage.getText().toString());
            }
        });


    }

    private void VKSendMessage(String text) {
        VK.execute(new VKRequest("messages.send").addParam("user_id", id).addParam("random_id", new Random().nextLong()).addParam("message", text), new VKApiCallback<JSONObject>() {
            @Override
            public void fail(@NotNull Exception e) {
                e.printStackTrace();
            }

            @Override
            public void success(JSONObject jsonObject) {

            }
        });
    }

    private void RequestMessages(int offsetT) {
        VK.execute(new VKRequest("messages.getHistory").addParam("count", 20).addParam("peer_id", dialog.getReceiverId()).addParam("offset", offsetT).addParam("start_message_id", -1), new VKApiCallback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Message message = Message.getMessage(jsonArray.getJSONObject(i));
                        messages.add(message);
                    }

                    myFullDialogAdapter.notifyDataSetChanged();

                    offset += 20;
                    endReached = false;

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fail(@NotNull Exception e) {
                e.printStackTrace();
            }
        });


    }

    private void initialize() {
        toolbar = findViewById(R.id.full_message_tool_bar);
        dialog = (Dialog) getIntent().getSerializableExtra("DIALOG");
        listView = findViewById(R.id.list_view_messages);
        photo = findViewById(R.id.full_dialog_photo);

        String type = String.valueOf(dialog.getType());
        switch (type) {
            case "CHAT":
                id = Long.parseLong("2000000000" + dialog.getReceiverId());
                break;
            case "USER":
                id = dialog.getReceiverId();
                break;
            case "GROUP":
                id = dialog.getReceiverId() * (-1);
                break;
        }

        inputMessage = findViewById(R.id.input_text_message);
        sendMessage = findViewById(R.id.button_send_message);

        messages = new LinkedList();

        myFullDialogAdapter = new MyFullDialogAdapter(getApplicationContext(), messages);
        listView.setAdapter(myFullDialogAdapter);
        listView.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                listView.getFooterViewsCount()) >= (myFullDialogAdapter.getCount() - 1)) {

            Log.d(TAG, " list end reached");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "scroll: firstVisibleItem:" + firstVisibleItem
                + ", visibleItemCount:" + visibleItemCount
                + ", totalItemCount:" + totalItemCount
                + ", offset:" + offset);

        if (firstVisibleItem == 0 && visibleItemCount != 0 && !endReached) {
            Log.d(TAG, " list start reached");
            endReached = true;
            RequestMessages(offset);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void showKeyboard(final EditText ettext) {
        ettext.requestFocus();
        ettext.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                   keyboard.showSoftInput(ettext, 0);
                               }
                           }
                , 1);
    }

}
