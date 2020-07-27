package com.antonchuraev.vkmessenger.DisplayMessages.OnlineFriends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.antonchuraev.vkmessenger.DisplayMessages.DialogsList.Dialog;
import com.antonchuraev.vkmessenger.DisplayMessages.DialogsList.DialogList;
import com.antonchuraev.vkmessenger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyListAdapter extends ArrayAdapter {


	List<Dialog> dialogList;

	TextView name;
	TextView lastMessage;

	ImageView photo;

	Context context;
	int resource;

	public MyListAdapter(@NonNull Context context, int resource, DialogList dialog) {
		super(context, resource, dialog.getDialogList());
		this.context = context;
		this.resource = resource;

		dialogList = dialog.getDialogList();
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(resource, null, false);

		name = view.findViewById(R.id.textViewUserName);
		photo = view.findViewById(R.id.imageViewPhoto);
		lastMessage = view.findViewById(R.id.textViewLastMessage);

		//TODO
		name.setText(dialogList.get(position).getName());
		lastMessage.setText(dialogList.get(position).getLastMessage());
		Picasso.get().load(dialogList.get(position).getPhotoURL()).into(photo);

		return view;
	}
}
