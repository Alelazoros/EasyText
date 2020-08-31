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
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
					switch (attachmentType) {
						case "CALL":
						case "AUDIO":
							addAudioPlayer(constraintLayout, attachment);
							break;

						case "PHOTO":
							addImageView(constraintLayout, attachment.attachment.toString(), message.isYourMessage(), messageTextView);
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

							addForwardedMessage(constraintLayout, message.isYourMessage(), (List) attachment.attachment);

							break;

						case "WALL":

							parseWall((List) attachment.attachment, constraintLayout, message.isYourMessage());
							break;
					}


				}


			}
		}


		return view;
	}

	private void addForwardedMessage(ConstraintLayout constraintLayout, boolean yourMessage, List attachment) {
		TextView title = addTextViewWithTitle(constraintLayout, yourMessage, "Пересланное сообщение");
		setBias(yourMessage, title);

		AtomicReference<TextView> holder = new AtomicReference<>();

		constraintLayout.setBackground(context.getDrawable(yourMessage ? R.drawable.right_dialog : R.drawable.left_dialog));

		ViewGroup.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		constraintLayout.setLayoutParams(params); //TODO

		for (int i = 0; i < attachment.size(); i++) {
			Map map = (Map) attachment.get(i);

			int finalI = i;
			map.forEach((sender, text) -> {

				TextView name = addForwardedMessageName(sender, yourMessage);
				name.setText(name.getText() + ":");

				constraintLayout.addView(name);

				constraintSet.clone(constraintLayout);
				if (finalI == 0) {
					constraintSet.connect(name.getId(), ConstraintSet.TOP, title.getId(), ConstraintSet.BOTTOM, getDP(10));
				} else {
					constraintSet.connect(name.getId(), ConstraintSet.TOP, holder.get().getId(), ConstraintSet.BOTTOM);
				}
				constraintSet.applyTo(constraintLayout);

				holder.set(name);


				TextView textForwardedMessage = addForwardedMessageText(text, yourMessage);
				constraintLayout.addView(textForwardedMessage);

				constraintSet.clone(constraintLayout);

				constraintSet.connect(textForwardedMessage.getId(), ConstraintSet.TOP, holder.get().getId(), ConstraintSet.TOP);
				if (yourMessage) {
					constraintSet.connect(textForwardedMessage.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
					//constraintSet.connect(name.getId(), ConstraintSet.RIGHT, textForwardedMessage.getId(), ConstraintSet.LEFT , getDP(10));
				}
				constraintSet.applyTo(constraintLayout);


			});

		}

	}

	private TextView addForwardedMessageText(Object text, Boolean yourMessage) {
		TextView textView = new TextView(context);
		textView.setText(text.toString());
		textView.setId(View.generateViewId());
		//textView.setBackground(context.getDrawable(yourMessage ? R.drawable.right_dialog : R.drawable.left_dialog));
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(16);
		final int paddingVertical = getDP(13);
		final int paddingHorizontal = getDP(8);
		textView.setPadding(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal);

		return textView;
	}

	private TextView addForwardedMessageName(Object sender, Boolean yourMessage) {
		TextView textView = new TextView(context);
		VK.execute(new VKRequest("users.get").addParam("user_ids", sender.toString()).addParam("fields", "photo_200"), new VKApiCallback<JSONObject>() {
			@Override
			public void success(JSONObject jsonObject) {

				try {
					JSONArray response = jsonObject.getJSONArray("response");
					JSONObject item = response.getJSONObject(0);

					String name = item.getString("first_name") + " " + item.getString("last_name");
					textView.setText(name);


				} catch (JSONException e) {
					e.printStackTrace();
				}
			}


			@Override
			public void fail(@NotNull Exception e) {

			}
		});
		textView.setId(View.generateViewId());
		//textView.setBackground(context.getDrawable(yourMessage ? R.drawable.right_dialog : R.drawable.left_dialog));
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(16);
		final int paddingVertical = getDP(13);
		final int paddingHorizontal = getDP(8);
		textView.setPadding(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal);


		return textView;
	}

	private TextView addTextViewWithTitle(ConstraintLayout constraintLayout, boolean yourMessage, String frwMessageTitle) {
		TextView title = new TextView(context);
		title.setText(frwMessageTitle);
		title.setId(View.generateViewId());
		title.setBackground(context.getDrawable(yourMessage ? R.drawable.right_dialog : R.drawable.left_dialog));
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);
		final int paddingVertical = getDP(13);
		final int paddingHorizontal = getDP(8);
		title.setPadding(paddingVertical, paddingHorizontal, paddingVertical, paddingHorizontal);

		constraintLayout.addView(title);

		return title;
	}

	private void parseWall(List attachment, ConstraintLayout layout, boolean isYourText) {
		TextView textView = null;
		for (int i = 1; i < attachment.size(); i++) {
			if (i == 1 && !attachment.get(i).equals("NULL TEXT")) {
				textView = addTextViewWithText(layout, isYourText, attachment.get(i).toString());
			}

			if (i > 1) {
				addImageView(layout, attachment.get(i).toString(), isYourText, textView);
			}

		}
	}


	private void addAudioPlayer(ConstraintLayout constraintLayout, Attachment attachment) {
		//TODO ADD AUDIO PLAYER
		TextView textView = new TextView(context);
		textView.setText(attachment.toString());
		textView.setId(View.generateViewId());

		constraintLayout.addView(textView);

	}

	private ImageView addImageView(ConstraintLayout layout, String url, boolean isYourMessage, TextView messageTextView) {
		ImageView imageView = getImageView(context);
		Picasso.get().load(url).into(imageView, new Callback() {
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
		constraintSet.connect(imageView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
		constraintSet.connect(imageView.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
		constraintSet.setHorizontalBias(imageView.getId(), isYourMessage ? 1f : 0F);
		if (messageTextView != null) {
			constraintSet.connect(imageView.getId(), ConstraintSet.TOP, messageTextView.getId(), ConstraintSet.BOTTOM, getDP(8));
		}
		constraintSet.applyTo(constraintLayout);

		return imageView;
	}

	private ImageView getImageView(Context context) {
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
