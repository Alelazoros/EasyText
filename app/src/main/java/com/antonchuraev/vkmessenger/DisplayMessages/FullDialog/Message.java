package com.antonchuraev.vkmessenger.DisplayMessages.FullDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

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
	private List<Attachment> attachmentList;  //вложение
	private boolean yourMessage;


	public Message(boolean hasAttachment) {
		if (hasAttachment) {
			this.attachmentList = new LinkedList<>();
		}

	}

	public Message() {
	}

	public static Message setMessage(JSONObject jsonObject) throws JSONException {
		Message message;  //TODO REFACTOR

		//TODO attachments
		JSONArray attachments = jsonObject.getJSONArray("attachments");
		if (attachments.length() != 0) {
			message = new Message(true);

			for (int i = 0; i < attachments.length(); i++) {
				Attachment attachment = new Attachment();
				switch (attachments.getJSONObject(i).getString("type")) {
					case "photo":
						attachment.attachmentType = AttachmentType.PHOTO;

						JSONObject photo = attachments.getJSONObject(i).getJSONObject("photo");
						JSONArray sizes = photo.getJSONArray("sizes");
						JSONObject lastSizePhoto = sizes.getJSONObject(sizes.length() - 1);
						String url = lastSizePhoto.getString("url");

						attachment.attachment = url;
						break;
					//TODO ALL OTHERS

				}

				message.getAttachmentList().add(attachment);
			}

		} else {
			message = new Message();
		}

		message.text = jsonObject.getString("text");
		message.yourMessage = jsonObject.getInt("out") == 1;

		return message;
	}

	public String getText() {
		return text;
	}

	public boolean isHasAttachment() {
		return attachmentList != null;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public boolean isYourMessage() {
		return yourMessage;
	}

	@Override
	public String toString() {
		return "Message{" +
				"text='" + text + '\'' +
				", attachment=" + attachmentList +
				", yourMessage=" + yourMessage +
				'}';
	}
}

class Attachment {
	public AttachmentType attachmentType;
	public Object attachment;

	@Override
	public String toString() {
		return "Attachment{" +
				"attachmentType=" + attachmentType +
				", attachment=" + attachment +
				'}';
	}
}
