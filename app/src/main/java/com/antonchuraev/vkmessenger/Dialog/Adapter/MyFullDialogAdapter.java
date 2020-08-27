package com.antonchuraev.vkmessenger.Dialog.Adapter;

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
import com.antonchuraev.vkmessenger.MyClasses.Message.Attachment;
import com.antonchuraev.vkmessenger.MyClasses.Message.Message;
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

		TextView messageTextView = null;
		if (!message.getText().equals("")) {
			messageTextView = addTextViewWithText(constraintLayout, message.isYourMessage(), message.getText());

			if (message.getText().startsWith("https")) {

				TextView finalMessageTextView = messageTextView;
				messageTextView.setOnClickListener(v -> {
					Intent openUrl = new Intent(Intent.ACTION_VIEW);
					openUrl.setData(Uri.parse(finalMessageTextView.getText().toString()));
					openUrl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(openUrl);
				});

			}

		}

		if (message.isHasAttachment()) {

			for (int i = 0; i < message.getAttachmentList().size(); i++) {
				Attachment attachment = message.getAttachmentList().get(i);
				if (attachment.attachment != null) {  //TODO PROCESSING ALL TYPES
					String attachmentType = String.valueOf(attachment.attachmentType);
					System.out.println(attachmentType);
					switch (attachmentType) {
						case "CALL":
						case "AUDIO":
							addAudioPlayer(constraintLayout, attachment);
							break;

						case "PHOTO":
							addImageView(constraintLayout, attachment, message.isYourMessage(), messageTextView);
							break;

						case "LINK":
							TextView link = addTextViewWithText(constraintLayout, message.isYourMessage(), attachment.attachment.toString());
							link.setOnClickListener(v -> {
								Intent openUrl = new Intent(Intent.ACTION_VIEW);
								openUrl.setData(Uri.parse(link.getText().toString()));
								openUrl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(openUrl);
							});
							break;

						case "STICKER":
						case "VOICE_MESSAGE":
						case "FORWARDED_MESSAGE":
						case "WALL":
							System.out.println("ADASD " + attachment);
							addAudioPlayer(constraintLayout, attachment);
							break;
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

	private ImageView addImageView(ConstraintLayout layout, Attachment attachment, boolean isYourMessage, TextView messageTextView) {
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

		setBias(isYourMessage, imageView);
		if (messageTextView != null) {
			constraintSet.connect(imageView.getId(), ConstraintSet.TOP, messageTextView.getId(), ConstraintSet.BOTTOM, getDP(8));
		}

		return imageView;
	}

	private ImageView getImageView(Context context, boolean isYourMessage) {
		ImageView imageView = new ImageView(context);
		imageView.setId(View.generateViewId());

		return imageView;
	}

	private TextView addTextViewWithText(ConstraintLayout layout, boolean isYourMessage, String text) {
		TextView messageTextView = getCustomTextView(context, isYourMessage, text);
		layout.addView(messageTextView);
		setBias(isYourMessage, messageTextView);
		return messageTextView;
	}

	private void setBias(Boolean isYourMessage, View view) {
		constraintSet.clone(constraintLayout);
		constraintSet.connect(view.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
		constraintSet.connect(view.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
		constraintSet.setHorizontalBias(view.getId(), isYourMessage ? 1f : 0F);
		constraintSet.applyTo(constraintLayout);
	}

	private TextView getCustomTextView(Context context, boolean isYourMessage, String text) {
		TextView textView = new TextView(context);
		textView.setId(View.generateViewId());
		textView.setBackground(context.getDrawable(isYourMessage ? R.drawable.right_dialog : R.drawable.left_dialog));
		textView.setTextSize(16);
		textView.setText(text);
		final int paddingVertical = getDP(13);
		final int paddingHorizontal = getDP(8);
		textView.setPadding(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal);
		textView.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
		textView.setMaxWidth(getDP(350));

		if (text.startsWith("https")) {
			textView.setClickable(true);
			textView.setTextColor(Color.BLUE);
			textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		} else {
			textView.setTextColor(Color.DKGRAY);
		}

		return textView;
	}


	private int getDP(int i) {
		return (int) (i * scale + 0.5f);
	}
}
