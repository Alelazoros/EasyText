package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyFullDialogAdapter extends ArrayAdapter {
	public static final String TAG = "EASY TEXT";
	float scale;

	List<Message> messagesList;
	Context context;

	ConstraintLayout constraintLayout;
	ConstraintSet constraintSet;

	TextView messageTextView;

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
			messageTextView = addTextViewWithText(constraintLayout, message);

			if (message.getText().startsWith("https")) {
				messageTextView.setClickable(true);
				messageTextView.setTextColor(Color.BLUE);
				messageTextView.setPaintFlags(messageTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

				messageTextView.setOnClickListener(v -> {
					Intent openUrl = new Intent(Intent.ACTION_VIEW);
					openUrl.setData(Uri.parse(messageTextView.getText().toString()));
					openUrl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(openUrl);
				});
			}

		}

		if (message.isHasAttachment()) {

			for (int i = 0; i < message.getAttachmentList().size(); i++) {
				Attachment attachment = message.getAttachmentList().get(i);
				if (attachment.attachment != null) {  //TODO PROCESSING ALL TYPES

					String attachmentType = attachment.attachmentType.toString();
					switch (attachmentType) {
						case "CALL":
						case "AUDIO":
							addAudioPlayer(constraintLayout, attachment);

						case "PHOTO":
							addImageView(constraintLayout, attachment, message.isYourMessage(), messageTextView);
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

	private void addAudioPlayer(ConstraintLayout constraintLayout, Attachment attachment) {
		//TODO ADD AUDIO PLAYER
		TextView textView = new TextView(context);
		textView.setText(attachment.toString());
		textView.setId(View.generateViewId());

		constraintLayout.addView(textView);

	}

	private void addImageView(ConstraintLayout layout, Attachment attachment, boolean isYourMessage, TextView messageTextView) {
		ImageView imageView = getImageView(context, isYourMessage);
		Picasso.get().load(attachment.attachment.toString()).into(imageView, new Callback() {
			@Override
			public void onSuccess() {
				notifyDataSetChanged();
			}

			@Override
			public void onError(Exception e) {

			}
		});
		layout.addView(imageView);


		constraintSet.clone(constraintLayout);
		if (messageTextView != null) {
			constraintSet.connect(imageView.getId(), ConstraintSet.TOP, messageTextView.getId(), ConstraintSet.BOTTOM, getDP(8));
		}
		constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
		constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
		constraintSet.setHorizontalBias(imageView.getId(), isYourMessage ? 1f : 0F);
		constraintSet.applyTo(constraintLayout);

	}

	private ImageView getImageView(Context context, boolean isYourMessage) {
		ImageView imageView = new ImageView(context);
		imageView.setId(View.generateViewId());

		return imageView;
	}

	private TextView addTextViewWithText(ConstraintLayout layout, Message message) {
		TextView messageTextView = getCustomTextView(context, message.isYourMessage(), message.getText());

		layout.addView(messageTextView);

		constraintSet.clone(constraintLayout);
		constraintSet.connect(messageTextView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
		constraintSet.connect(messageTextView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
		constraintSet.setHorizontalBias(messageTextView.getId(), message.isYourMessage() ? 1f : 0F);
		constraintSet.applyTo(constraintLayout);

		return messageTextView;
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
