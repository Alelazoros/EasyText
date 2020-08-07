package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.antonchuraev.vkmessenger.R;

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

	}

	@Override
	public int getItemCount() {
		return messagesList.size();
	}


	static class ViewHolder extends RecyclerView.ViewHolder {
		TextView rightTextView;
		TextView leftTextView;


		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			rightTextView = itemView.findViewById(R.id.textViewMessageRight);
			leftTextView = itemView.findViewById(R.id.textViewMessageLeft);
		}
	}
}
