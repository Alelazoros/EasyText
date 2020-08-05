package com.antonchuraev.vkmessenger.DisplayMessages.DialogsList;

public class Dialog {
	private long receiverId;
	private String name;
	private String lastMessage = "Сообщение";
	private int messageColor;
	private String photoURL;
	private boolean online;


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

	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
}
