package com.antonchuraev.vkmessenger.DisplayMessages.DialogsList;

public class Dialog {

	private String name;
	private String lastMessage;
	private int messageColor;
	private String photoURL;

	public Dialog() {

	}

	public Dialog(String name, String photoURL, String lastMessage) {
		this.name = name;
		this.photoURL = photoURL;
		this.lastMessage = lastMessage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public int getMessageColor() {
		return messageColor;
	}

	public void setMessageColor(int messageColor) {
		this.messageColor = messageColor;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}
}
