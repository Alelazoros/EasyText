package com.antonchuraev.vkmessenger.DisplayMessages.Adapter;

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

	private final Context context;
	private final int resource;
	private final LayoutInflater inflater;

	public MyListAdapter(@NonNull Context context, int resource, DialogList dialog) {
		super(context, resource, dialog.getDialogList());
		this.context = context;
		this.resource = resource;

		inflater = LayoutInflater.from(context);

		dialogList = dialog.getDialogList();
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(resource, parent, false);
			holder = new ViewHolder();

			holder.photo = convertView.findViewById(R.id.imageViewPhoto);
			holder.name = convertView.findViewById(R.id.textViewUserName);
			holder.lastMessage = convertView.findViewById(R.id.textViewLastMessage);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		Dialog dialog = dialogList.get(position);


		String lastMessageText = dialog.getLastMessage();
		if (lastMessageText != null) {
			lastMessageText = convertStringToMAX_LENGTH(lastMessageText);
		}

		holder.name.setText(dialog.getName());
		if (dialog.isOnline()) {
			holder.name.setTextColor(getContext().getColor(R.color.colorGreen)); //TODO
		} else {
			holder.name.setTextColor(getContext().getColor(R.color.colorDark));
		}


		holder.lastMessage.setText(lastMessageText);

		if (dialog.getMessageColor() != 0) {
			holder.lastMessage.setTextColor(getContext().getColor(dialog.getMessageColor()));
		} else {
			holder.lastMessage.setTextColor(getContext().getColor(R.color.colorDark));
		}


		if (dialog.getPhotoURL() != null) {
			Picasso.get().load(dialog.getPhotoURL()).into(holder.photo);
		} else { //Фото для тех у еого нет
			Picasso.get().load("https://vk.com/images/camera_200.png?ava=1").into(holder.photo);
		}


		return convertView;
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

	static class ViewHolder {
		ImageView photo;
		TextView name;
		TextView lastMessage;
	}

}
