package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.antonchuraev.vkmessenger.R;
import com.squareup.picasso.Picasso;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.antonchuraev.vkmessenger.MyClasses.MyHelper.printDebugMessage;

public class FullDialog extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;

    private static int randomId = new Random().nextInt(); //TODO
    MyFullDialogAdapter myFullDialogAdapter;

    ImageView photo;
    ListView listView;
    EditText inputMessage;
    ImageButton sendMessage;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_dialog);
        initialize();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("NAME"));

        Picasso.get().load(getIntent().getStringExtra("PHOTO_URL")).into(photo);

        RequestAllMessages();

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
                printDebugMessage("success send");
                inputMessage.setText(" ");

                showKeyboard(inputMessage);

                RequestAllMessages();
            }
        });
    }

    private void RequestAllMessages() {
        VK.execute(new VKRequest("messages.getHistory").addParam("count", 40).addParam("peer_id", id), new VKApiCallback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject) {
                try {

                    JSONObject response = jsonObject.getJSONObject("response");
                    JSONArray items = response.getJSONArray("items");

                    List messages = new LinkedList();
                    List from_id = new LinkedList();

                    for (int i = 0; i < items.length(); i++) {
                        String text = items.getJSONObject(i).getString("text");

                        if (!text.equals("")) {
                            messages.add(text);
                            from_id.add(items.getJSONObject(i).getLong("from_id"));
                        }
                    }

                    Collections.reverse(messages);
                    Collections.reverse(from_id);

                    myFullDialogAdapter = new MyFullDialogAdapter(getApplicationContext(), R.layout.activity_my_full_dialog_list_adapter, messages, from_id, id);

                    listView.setAdapter(myFullDialogAdapter);

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
        listView = findViewById(R.id.list_view_messages);
        photo = findViewById(R.id.full_dialog_photo);
        id = getIntent().getLongExtra("ID", 0);
        inputMessage = findViewById(R.id.input_text_message);
        sendMessage = findViewById(R.id.button_send_message);
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
