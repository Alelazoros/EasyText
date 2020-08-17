package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import com.antonchuraev.vkmessenger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyFullDialogAdapter extends RecyclerView.Adapter<MyFullDialogAdapter.ViewHolder> {

	List<Message> messagesList;
	private final LayoutInflater inflater;
	Context context;


	public MyFullDialogAdapter(@NonNull Context context, List messages) {
		this.messagesList = messages;
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.activity_my_full_dialog_list_adapter, parent, false);
		return new MyFullDialogAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Message message = messagesList.get(position);

		TextView textField = holder.getTextView(context, message.isYourMessage());

		if (!message.getText().equals("")) {
			textField.setText(message.getText());
		}


		if (message.isHasAttachment()) {
			for (int i = 0; i < message.getAttachmentList().size(); i++) { //TODO NULL IN ATTACHMENT TYPE
				Attachment attachment = message.getAttachmentList().get(i);
				String attachmentType = String.valueOf(attachment.attachmentType);
				switch (attachmentType) {
					case "PHOTO":
						textField.setBackgroundColor(context.getColor(R.color.colorWhite));
						if (!holder.displaysExtraImageView()) {
							holder.addExtraImageView(message.isYourMessage());
						}

						Picasso.get().load(attachment.attachment.toString()).into(holder.extraImageView);

						break;
				}

			}
		} else {
			if (holder.displaysExtraImageView()) {
				holder.removeExtraImageView();
			}

		}


	}

	@Override
	public int getItemCount() {
		return messagesList.size();
	}


	static class ViewHolder extends RecyclerView.ViewHolder {
		TextView textView;

		ConstraintLayout constraintLayout;
		ConstraintSet constraintSet;

		ImageView extraImageView;
		boolean viewAdded = false;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			constraintLayout = itemView.findViewById(R.id.constraint_layout_message);
			textView = itemView.findViewById(R.id.textViewMessage);

			extraImageView = new ImageView(itemView.getContext());

			constraintSet = new ConstraintSet();
			constraintSet.clone(constraintLayout);
		}

		public TextView getTextView(Context context, boolean isYourMessage) {
			textView.setBackground(isYourMessage ? context.getDrawable(R.drawable.right_dialog) : context.getDrawable(R.drawable.left_dialog));
			setBias(textView, isYourMessage);
			return textView;
		}

		public void addExtraImageView(boolean isYourMessage) {
			extraImageView.setId(View.generateViewId());
			((ViewGroup) itemView).addView(extraImageView);
			//setBias(extraImageView , isYourMessage);
			viewAdded = true;
		}

		public void removeExtraImageView() {
			((ViewGroup) itemView).removeView(extraImageView);
			viewAdded = false;
		}

		public boolean displaysExtraImageView() {
			return viewAdded;
		}

		private void setBias(View view, boolean isYourMessage) {
			constraintSet.setHorizontalBias(view.getId(), isYourMessage ? 1F : 0F);
			constraintLayout.setConstraintSet(constraintSet);
		}

	}
}
