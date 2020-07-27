package com.antonchuraev.vkmessenger.DisplayMessages.DialogsList;

import org.json.JSONException;
import org.json.JSONObject;

public class VKUserOrGroup {

	private long id;
	private String name;
	private String photoURL;

	public VKUserOrGroup(long id, String first_name, String last_name, String photo_200) {
		this.id = id;
		this.name = first_name + " " + last_name;
		this.photoURL = photo_200;

	}

	public VKUserOrGroup(long id, String name, String photo_200) {
		this.id = id;
		this.name = name;
		this.photoURL = photo_200;
	}


	public static VKUserOrGroup parseProfiles(JSONObject jsonObject) throws JSONException {
		return new VKUserOrGroup(jsonObject.getLong("id"), jsonObject.getString("first_name"), jsonObject.getString("last_name"), jsonObject.getString("photo_200"));
	}

	public static VKUserOrGroup parseGroups(JSONObject jsonObject) throws JSONException {
		return new VKUserOrGroup(jsonObject.getLong("id"), jsonObject.getString("name"), jsonObject.getString("photo_200"));
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhotoURL() {
		return photoURL;
	}
}
