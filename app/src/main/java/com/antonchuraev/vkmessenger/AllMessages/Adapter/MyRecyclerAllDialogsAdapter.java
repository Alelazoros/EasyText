package com.antonchuraev.vkmessenger.AllMessages.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antonchuraev.vkmessenger.MyClasses.Dialog.Dialog;
import com.antonchuraev.vkmessenger.MyClasses.Dialog.DialogList;
import com.antonchuraev.vkmessenger.R;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyRecyclerAllDialogsAdapter extends RecyclerView.Adapter<MyRecyclerAllDialogsAdapter.ViewHolder> {

	private final int LAST_MESSAGE_MAX_LENGTH = 35;

	private static ClickListener clickListener;
	private final LayoutInflater inflater;
	Context context;
	private List<Dialog> dialogList;

	public MyRecyclerAllDialogsAdapter(@NonNull Context context, DialogList dialog) {
		inflater = LayoutInflater.from(context);
		dialogList = dialog.getDialogList();
		this.context = context;
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

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.activity_my_list_adapter, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Dialog dialog = dialogList.get(position);

		String lastMessageText = dialog.getLastMessage();
		if (lastMessageText != null) {
			lastMessageText = convertStringToMAX_LENGTH(lastMessageText);
		}

		holder.name.setText(dialog.getName());
		if (dialog.isOnline()) {
			holder.name.setTextColor(context.getColor(R.color.colorGreen));
		} else {
			holder.name.setTextColor(context.getColor(R.color.colorDark));
		}


		holder.lastMessage.setText(lastMessageText);

		if (dialog.getMessageColor() != 0) {
			holder.lastMessage.setTextColor(context.getColor(dialog.getMessageColor()));
		} else {
			holder.lastMessage.setTextColor(context.getColor(R.color.colorDark));
		}


		if (dialog.getPhotoURL() != null) {
			Picasso.get().load(dialog.getPhotoURL()).into(holder.photo);
		} else { //Фото для тех у еого нет
			Picasso.get().load("https://vk.com/images/camera_200.png?ava=1").into(holder.photo);
		}


	}

	@Override
	public int getItemCount() {
		return dialogList.size();
	}

	public void setOnItemClickListener(ClickListener clickListener) {
		MyRecyclerAllDialogsAdapter.clickListener = clickListener;
	}

	public interface ClickListener {
		void onItemClick(int position, View v);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		ImageView photo;
		TextView name;
		TextView lastMessage;


		public ViewHolder(@NonNull View itemView) {
			super(itemView);

			itemView.setOnClickListener(this);
			photo = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
			name = (TextView) itemView.findViewById(R.id.textViewUserName);
			lastMessage = (TextView) itemView.findViewById(R.id.textViewLastMessage);
		}

		@Override
		public void onClick(View v) {
			clickListener.onItemClick(getAdapterPosition(), v);
		}

	}


}
