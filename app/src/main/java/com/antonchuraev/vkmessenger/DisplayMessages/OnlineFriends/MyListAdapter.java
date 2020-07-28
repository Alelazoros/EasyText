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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyListAdapter extends ArrayAdapter {

	private final int LAST_MESSAGE_MAX_LENGTH = 35;

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

		String lastMessageText = dialogList.get(position).getLastMessage();
		lastMessageText = convertStringToMAX_LENGTH(lastMessageText);

		name.setText(dialogList.get(position).getName());
		lastMessage.setText(lastMessageText);

		if (dialogList.get(position).getMessageColor() != 0) {
			lastMessage.setTextColor(getContext().getColor(dialogList.get(position).getMessageColor()));
		}

		Picasso.get().load(dialogList.get(position).getPhotoURL()).into(photo);

		return view;
	}

	@NotNull
	private String convertStringToMAX_LENGTH(String lastMessageText) {
		if (lastMessageText.length() > LAST_MESSAGE_MAX_LENGTH) {
			lastMessageText = lastMessageText.substring(0, LAST_MESSAGE_MAX_LENGTH);
			if (lastMessageText.contains(" ")) {
				lastMessageText = lastMessageText.substring(0, lastMessageText.lastIndexOf(" ")) + "...";
			}
		}
		return lastMessageText;
	}
}
