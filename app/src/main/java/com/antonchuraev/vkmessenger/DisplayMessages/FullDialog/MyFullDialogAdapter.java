package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.antonchuraev.vkmessenger.R;

import java.util.List;

public class MyFullDialogAdapter extends ArrayAdapter {

	List messagesList;
	List<Long> from_IdList;
	long id;

	TextView RightMessageTextView;
	TextView LeftMessageTextView;

	Context context;
	int resource;

	public MyFullDialogAdapter(@NonNull Context context, int resource, List messages, List from_IdList, long id) {
		super(context, resource, messages);
		this.context = context;
		this.resource = resource;
		this.messagesList = messages;

		this.from_IdList = from_IdList;
		this.id = id;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(resource, null, false);

		initialize(view);


		String text = messagesList.get(position).toString();

		if (from_IdList.get(position) == id) {
			intoLeftTextView(text);
		} else {
			intoRightTextView(text);
		}

		return view;
	}

	private void intoLeftTextView(String text) {
		RightMessageTextView.setVisibility(View.INVISIBLE);
		LeftMessageTextView.setText(text);
	}


	private void intoRightTextView(String text) {
		LeftMessageTextView.setVisibility(View.INVISIBLE);
		RightMessageTextView.setText(text);

	}

	private void initialize(View view) {
		RightMessageTextView = view.findViewById(R.id.textViewMessageRight);
		LeftMessageTextView = view.findViewById(R.id.textViewMessageLeft);
	}


}
