package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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

		holder.textView.setVisibility(View.INVISIBLE);
		//holder.imageView.setVisibility(View.INVISIBLE);

		TextView textField = holder.getTextView(context, message.isYourMessage());
		if (message.getText() != null && !message.getText().equals("")) {
			textField.setVisibility(View.VISIBLE);
			textField.setText(message.getText());
		}


		if (message.isHasAttachment()) {
			for (int i = 0; i < message.getAttachmentList().size(); i++) { //TODO NULL IN ATTACHMENT TYPE
				Attachment attachment = message.getAttachmentList().get(i);
				String attachmentType = String.valueOf(attachment.attachmentType);
				switch (attachmentType) {
					case "PHOTO":
						holder.imageView.setVisibility(View.VISIBLE);
						ImageView imageView = holder.getImageView(context, message.isYourMessage());
						Picasso.get().load(attachment.attachment.toString()).into(imageView);
						break;
				}

			}
		}


	}

	@Override
	public int getItemCount() {
		return messagesList.size();
	}


	static class ViewHolder extends RecyclerView.ViewHolder {
		TextView textView;
		ImageView imageView; //TODO

		ConstraintLayout constraintLayout;
		ConstraintLayout.LayoutParams constraintLayoutParams;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			constraintLayout = itemView.findViewById(R.id.constraint_layout_message);
			constraintLayoutParams = (ConstraintLayout.LayoutParams) constraintLayout.getLayoutParams();

			textView = itemView.findViewById(R.id.textViewMessage);
			imageView = itemView.findViewById(R.id.imageView_full_dialog);
		}

		public TextView getTextView(Context context, boolean isYourMessage) {
			textView.setBackground(isYourMessage ? context.getDrawable(R.drawable.right_dialog) : context.getDrawable(R.drawable.left_dialog));
			setGravity(isYourMessage);
			return textView;
		}

		public ImageView getImageView(Context context, boolean isYouMessage) {
			setGravity(isYouMessage);
			return imageView;
		}

		public void setGravity(boolean isYourMessage) {
			constraintLayoutParams.verticalBias = isYourMessage ? 100 : 0;
			constraintLayout.setLayoutParams(constraintLayoutParams);
		}

	}
}
