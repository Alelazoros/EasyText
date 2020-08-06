package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.antonchuraev.vkmessenger.R;

import java.util.List;

public class MyFullDialogAdapter extends ArrayAdapter {

	private final LayoutInflater inflater;

	TextView RightMessageTextView;
	TextView LeftMessageTextView;

	Context context;
	List<Message> messagesList;

	public MyFullDialogAdapter(@NonNull Context context, List messages) {
		super(context, R.layout.activity_my_full_dialog_list_adapter, messages);
		this.context = context;
		this.messagesList = messages;


		inflater = LayoutInflater.from(context);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_my_full_dialog_list_adapter, parent, false);
			holder = new ViewHolder();

			holder.rightTextView = convertView.findViewById(R.id.textViewMessageRight);
			holder.leftTextView = convertView.findViewById(R.id.textViewMessageLeft);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Message message = messagesList.get(position);

		if (message.isYourMessage()) {
			holder.leftTextView.setVisibility(View.INVISIBLE);
			holder.rightTextView.setVisibility(View.VISIBLE);

			holder.rightTextView.setText(message.getText());
			holder.leftTextView.setText(null);
		} else {
			holder.rightTextView.setVisibility(View.INVISIBLE);
			holder.leftTextView.setVisibility(View.VISIBLE);

			holder.leftTextView.setText(message.getText());
			holder.rightTextView.setText(null);
		}

		return convertView;
	}


	static class ViewHolder {
		TextView rightTextView;
		TextView leftTextView;
	}
}
