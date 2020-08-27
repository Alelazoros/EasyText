package com.antonchuraev.vkmessenger.MyClasses.Dialog;

import java.io.Serializable;

enum Type {
	USER,
	CHAT,
	GROUP
}

public class Dialog implements Serializable {
	private long receiverId;
	private String name;
	private String lastMessage = "Сообщение";
	private int messageColor;
	private String photoURL;
	private Type type;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}


	@Override
	public String toString() {
		return "Dialog{" +
				"receiverId=" + receiverId +
				", name='" + name + '\'' +
				", lastMessage='" + lastMessage + '\'' +
				", messageColor=" + messageColor +
				", photoURL='" + photoURL + '\'' +
				", type=" + type +
				", online=" + online +
				'}';
	}
}
