package com.antonchuraev.vkmessenger.DisplayMessages;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.antonchuraev.vkmessenger.R;
import com.squareup.picasso.Picasso;

public class FullDialog extends AppCompatActivity {

    androidx.appcompat.widget.Toolbar toolbar;

    ImageView photo;

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


    }

    private void initialize() {
        toolbar = findViewById(R.id.full_message_tool_bar);
        photo = findViewById(R.id.full_dialog_photo);
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

}
