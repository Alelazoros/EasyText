package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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


public class FullDialog extends AppCompatActivity {

    private static final String TAG = "EASY TEXT";
    androidx.appcompat.widget.Toolbar toolbar;
    List messages;
    MyFullDialogAdapter myFullDialogAdapter;
    RecyclerView recyclerView;

    Dialog dialog;

    ImageView photo;
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
        listenerAction();

    }

    private void listenerAction() {

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
                        messages.add(0, message); //TODO
                    }

                    myFullDialogAdapter.notifyDataSetChanged(); //TODO

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
        recyclerView = findViewById(R.id.recycler_view_messages);
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

        messages = new LinkedList();

        myFullDialogAdapter = new MyFullDialogAdapter(getApplicationContext(), messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(myFullDialogAdapter);
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
