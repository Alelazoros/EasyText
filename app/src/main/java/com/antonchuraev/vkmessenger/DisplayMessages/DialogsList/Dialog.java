package com.antonchuraev.vkmessenger.DisplayMessages.DialogsList;

import org.json.JSONObject;

public class Dialog {

	private String name;
	private String lastMessage;
	private String photoURL;

	public Dialog(JSONObject response) {

	}

	public Dialog(String name, String photoURL, String lastMessage) {
		this.name = name;
		this.photoURL = photoURL;
		this.lastMessage = lastMessage;
	}

	public String getName() {
		return name;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public String getPhotoURL() {
		return photoURL;
	}
}
