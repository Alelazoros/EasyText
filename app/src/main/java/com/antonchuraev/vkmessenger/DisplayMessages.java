package com.antonchuraev.vkmessenger;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.vk.api.sdk.*;
import com.vk.api.sdk.requests.VKRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayMessages extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_messagers);
        initialize();
        setOnlineFriendsInList();

    }

    private void setOnlineFriendsInList() {
        //TODO WORK
        VK.execute( new VKRequest("friends.getOnline")  , new VKApiCallback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject) {
                printDebugMessage("success");
                ArrayList arrayList = new ArrayList();
                try {
                    JSONArray jsonArray =  jsonObject.getJSONArray("response");
                    for (int i=0;i<jsonArray.length();i++){
                        arrayList.add(jsonArray.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter<Object>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void fail(@NotNull Exception e) {
                printDebugMessage("fail");
            }
        });
    }

    private void initialize() {
        listView = findViewById(R.id.test);
    }

    void printDebugMessage(String message) {
        if (message != null) {
            Log.d("debug", message);
        }
    }

}
