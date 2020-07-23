package com.antonchuraev.vkmessenger.DisplayMessages.OnlineFriends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.antonchuraev.vkmessenger.MyClasses.VKUser.VKUser;
import com.antonchuraev.vkmessenger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyListAdapter extends ArrayAdapter {

	List<VKUser> users;

	TextView name;
	ImageView photo;


	Context context;
	int resource;

	public MyListAdapter(@NonNull Context context, int resource , @NonNull List<VKUser> users) {
		super(context,resource, users);
		this.context = context;
		this.resource = resource;
		this.users = users;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(resource, null, false);

		name = view.findViewById(R.id.textViewUserName);
		photo = view.findViewById(R.id.imageViewPhoto);

		name.setText(users.get(position).getFullName());
		Picasso.get().load(users.get(position).getPhotoURL()).into(photo);

		return view;
	}
}
