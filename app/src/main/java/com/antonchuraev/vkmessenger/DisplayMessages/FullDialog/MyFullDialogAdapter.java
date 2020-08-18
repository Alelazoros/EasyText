package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.antonchuraev.vkmessenger.R;

import java.util.List;

public class MyFullDialogAdapter extends ArrayAdapter {
	public static final String TAG = "EASY TEXT";
	float scale;

	List<Message> messagesList;
	Context context;

	ConstraintLayout constraintLayout;

	public MyFullDialogAdapter(@NonNull Context context, List messages) {
		super(context, R.layout.activity_my_full_dialog_list_adapter, messages);
		this.messagesList = messages;
		this.context = context;
		scale = context.getResources().getDisplayMetrics().density;
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
			addTextViewWithText(constraintLayout, message.getText(), message.isYourMessage());
		}

		if (message.isHasAttachment()) {
			//TODO
		}


		return view;
	}

	private void addTextViewWithText(ConstraintLayout layout, String text, boolean isYourMessage) {
		TextView messageTextView = getCustomTextView(context, isYourMessage, text);
		layout.addView(messageTextView);
	}

	private TextView getCustomTextView(Context context, boolean isYourMessage, String text) {
		TextView textView = new TextView(context);
		textView.setBackground(context.getDrawable(isYourMessage ? R.drawable.right_dialog : R.drawable.left_dialog));
		textView.setTextSize(16);
		textView.setText(text);
		textView.setTextColor(Color.DKGRAY);
		final int paddingVertical = 13;
		final int paddingHorizontal = 8;
		textView.setPadding(getDP(paddingVertical), getDP(paddingHorizontal), getDP(paddingVertical), getDP(paddingHorizontal));
		textView.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
		textView.setMaxWidth(getDP(350));

		return textView;
	}


	private int getDP(int i) {
		return (int) (i * scale + 0.5f);
	}
}
