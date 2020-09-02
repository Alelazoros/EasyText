package com.antonchuraev.vkmessenger.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.antonchuraev.vkmessenger.Dialog.Adapter.MyFullDialogAdapter;
import com.antonchuraev.vkmessenger.MyClasses.Dialog.Dialog;
import com.antonchuraev.vkmessenger.MyClasses.Message.Message;
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


public class FullDialog extends AppCompatActivity {

    private static final String TAG = "EASY TEXT";
    androidx.appcompat.widget.Toolbar toolbar;
    List messages;
    MyFullDialogAdapter myFullDialogAdapter;
    ListView listView;

    Dialog dialog;

    ImageView photo;
    EditText inputMessage;

    private long id;
    private int offset = 0;

    private boolean endReached = false;
    Parcelable state;

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
        listenerAction();

    }

    private void listenerAction() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!view.canScrollVertically(-1) && !endReached) {
                    Log.d(TAG, "END REACHED");
                    endReached = true;

                    RequestMessages(offset);

                    listView.onRestoreInstanceState(state);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputMessage.getWindowToken(), 0);
                return false;
            }
        });

        inputMessage.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getRawX() >= (inputMessage.getRight() - (inputMessage.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width() * 2))) {
                    //pressed drawable
                    if (!inputMessage.getText().toString().equals("")) {
                        Log.d(TAG, "SEND MESSAGE:" + inputMessage.getText());
                        VKSendMessage(inputMessage.getText().toString());
                    }
                    return true;
                }
            }
            return false;
        });

    }

    private void VKSendMessage(String text) {
        VK.execute(new VKRequest("messages.send").addParam("user_id", id).addParam("random_id", new Random().nextLong()).addParam("message", text), new VKApiCallback<JSONObject>() {
            @Override
            public void fail(@NotNull Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Сообщение не отправлено", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(JSONObject jsonObject) {
                Log.d(TAG, "SUCCESS SEND id=" + id);
                //TODO ADD IN LIST
                Message sendedMessage = new Message();
                sendedMessage.setText(inputMessage.getText().toString());
                sendedMessage.setYourMessage(true);

                //TODO
                RequestMessages(offset);
                listView.onRestoreInstanceState(state);

                inputMessage.getText().clear();


            }
        });
    }

    private void RequestMessages(int offsetT) {
        VK.execute(new VKRequest("messages.getHistory").addParam("count", 20).addParam("peer_id", dialog.getReceiverId()).addParam("offset", offsetT).addParam("start_message_id", -1), new VKApiCallback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject) {
                Log.d(TAG, " Success RequestMessages offsetT = " + offsetT);
                try {
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Message message = Message.setMessage(jsonArray.getJSONObject(i));
                        messages.add(message);
                    }

                    myFullDialogAdapter.notifyDataSetChanged(); //TODO BETTER WAY TO UPDATE LISTVIEW

                    offset += 20;
                    endReached = false;

                    state = listView.onSaveInstanceState();


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


        id = dialog.getReceiverId();

        inputMessage = findViewById(R.id.input_text_message);

        messages = new LinkedList();

        myFullDialogAdapter = new MyFullDialogAdapter(getApplicationContext(), messages);
        listView.setAdapter(myFullDialogAdapter);

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
