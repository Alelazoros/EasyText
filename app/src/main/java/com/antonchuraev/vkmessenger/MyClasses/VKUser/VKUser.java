package com.antonchuraev.vkmessenger.MyClasses.VKUser;

import org.json.JSONException;
import org.json.JSONObject;

public class VKUser {
	private long id;
	private String firstName;
	private String lastName;

	private String photoURL;


	public VKUser(long id, String firstName, String lastName , String photoURL) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.photoURL = photoURL;
	}

	public static VKUser parse(JSONObject jsonObject) throws JSONException {
		return  new VKUser(jsonObject.getLong("id"),jsonObject.getString("first_name"),jsonObject.getString("last_name"),jsonObject.getString("photo_200"));
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFullName(){
		return firstName + " " +lastName;
	}

	public String getPhotoURL() {
		return photoURL;
	}
}
