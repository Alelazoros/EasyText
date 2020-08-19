package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.antonchuraev.vkmessenger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyFullDialogAdapter extends ArrayAdapter {
	public static final String TAG = "EASY TEXT";
	float scale;

	List<Message> messagesList;
	Context context;

	ConstraintLayout constraintLayout;
	ConstraintSet constraintSet;

	public MyFullDialogAdapter(@NonNull Context context, List messages) {
		super(context, R.layout.activity_my_full_dialog_list_adapter, messages);
		this.messagesList = messages;
		this.context = context;
		scale = context.getResources().getDisplayMetrics().density;
		constraintSet = new ConstraintSet();
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_my_full_dialog_list_adapter, parent, false);

		constraintLayout = view.findViewById(R.id.constraint_layout_message);


		Message message = messagesList.get(messagesList.size() - 1 - position); //WORK

		if (!message.getText().equals("")) {
			addTextViewWithText(constraintLayout, message);
		}

		if (message.isHasAttachment()) {
			//TODO
			System.out.println(message.toString());

			for (int i = 0; i < message.getAttachmentList().size(); i++) {
				Attachment attachment = message.getAttachmentList().get(i);
				if (attachment.attachment != null) {  //TODO????

					String attachmentType = attachment.attachmentType.toString();
					switch (attachmentType) {
						case "CALL":
						case "LINK":
						case "PHOTO":
							addImageView(constraintLayout, attachment, message.isYourMessage());
							break;

						case "STICKER":
						case "VOICE_MESSAGE":
						case "FORWARDED_MESSAGE":
					}


				}


			}
		}


		return view;
	}

	private void addImageView(ConstraintLayout layout, Attachment attachment, boolean isYourMessage) {
		ImageView imageView = getImageView(context, isYourMessage);
		Picasso.get().load(attachment.attachment.toString()).into(imageView);
		layout.addView(imageView);
	}

	private ImageView getImageView(Context context, boolean isYourMessage) {
		ImageView imageView = new ImageView(context);


		return imageView;
	}

	private void addTextViewWithText(ConstraintLayout layout, Message message) {
		TextView messageTextView = getCustomTextView(context, message.isYourMessage(), message.getText());

		layout.addView(messageTextView);

		constraintSet.clone(constraintLayout);
		constraintSet.connect(messageTextView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
		constraintSet.connect(messageTextView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
		constraintSet.setHorizontalBias(messageTextView.getId(), message.isYourMessage() ? 0f : 1F);
		constraintSet.applyTo(constraintLayout);

	}

	private TextView getCustomTextView(Context context, boolean isYourMessage, String text) {
		TextView textView = new TextView(context);
		textView.setId(View.generateViewId());
		textView.setBackground(context.getDrawable(isYourMessage ? R.drawable.right_dialog : R.drawable.left_dialog));
		textView.setTextSize(16);
		textView.setText(text);
		textView.setTextColor(Color.DKGRAY);
		final int paddingVertical = getDP(13);
		final int paddingHorizontal = getDP(8);
		textView.setPadding(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal);
		textView.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
		textView.setMaxWidth(getDP(350));

		return textView;
	}


	private int getDP(int i) {
		return (int) (i * scale + 0.5f);
	}
}
