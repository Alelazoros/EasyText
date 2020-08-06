package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import org.json.JSONException;
import org.json.JSONObject;

enum AttachmentType {
	PHOTO, //TODO
	LINK, //TODO
	VOICE_MESSAGE, //TODO
	CALL, //TODO
	STICKER,  //TODO
	FORWARDED_MESSAGE  //TODO
}

class Message {
	private String text;
	private boolean hasAttachment;
	private AttachmentType attachmentType;
	private Object attachment;  //вложение
	private boolean yourMessage;


	public static Message getMessage(JSONObject jsonObject) throws JSONException {
		Message message = new Message();

		message.text = jsonObject.getString("text");
		message.hasAttachment = jsonObject.getJSONArray("attachments").length() != 0;
		//TODO attachments

		message.yourMessage = jsonObject.getInt("out") == 1;

		return message;
	}

	public String getText() {
		return text;
	}

	public boolean isHasAttachment() {
		return hasAttachment;
	}

	public AttachmentType getAttachmentType() {
		return attachmentType;
	}

	public Object getAttachment() {
		return attachment;
	}

	public boolean isYourMessage() {
		return yourMessage;
	}

	@Override
	public String toString() {
		return "Message{" +
				"text='" + text + '\'' +
				", hasAttachment=" + hasAttachment +
				", attachmentType=" + attachmentType +
				", attachment=" + attachment +
				", yourMessage=" + yourMessage +
				'}';
	}
}

