package com.antonchuraev.vkmessenger.MyClasses.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Message {
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

		JSONArray attachments = jsonObject.getJSONArray("attachments");
		if (attachments.length() != 0) {
			message = new Message(true);

			for (int i = 0; i < attachments.length(); i++) {
				JSONObject attachmentJSON = attachments.getJSONObject(i);
				Attachment attachment = new Attachment();
				switch (attachments.getJSONObject(i).getString("type")) { //TODO all attachments
					case "photo":
						attachment.attachmentType = Attachment.AttachmentType.PHOTO;

						JSONObject photo = attachmentJSON.getJSONObject("photo");
						JSONArray sizes = photo.getJSONArray("sizes");
						JSONObject lastSizePhoto = sizes.getJSONObject(sizes.length() - 1);

						attachment.attachment = lastSizePhoto.getString("url");
						break;

					case "audio":
						attachment.attachmentType = Attachment.AttachmentType.AUDIO;
						JSONObject audio = attachmentJSON.getJSONObject("audio");
						attachment.attachment = "title{" + audio.getString("title") + "}artist{" + audio.getString("artist") + "}";
						break;

					case "link":
						attachment.attachmentType = Attachment.AttachmentType.LINK;
						attachment.attachment = attachmentJSON.getJSONObject("link").getString("url");
						break;

					case "wall":
						attachment.attachmentType = Attachment.AttachmentType.WALL;
						String wall = new String();
						String tittle = "null", text = "null";
						List photoUrl = new LinkedList();
						text = attachmentJSON.getJSONObject("wall").getString("text");

						if (attachmentJSON.getJSONObject("wall").has("from")) {
							tittle = attachmentJSON.getJSONObject("wall").getJSONObject("from").getString("name");
						}


						if (attachmentJSON.getJSONObject("wall").has("attachments")) {
							JSONArray jsonArray = attachmentJSON.getJSONObject("wall").getJSONArray("attachments");
							for (int j = 0; j < jsonArray.length(); j++) {

								if (jsonArray.getJSONObject(i).has("photo")) {
									JSONArray photoArray = jsonArray.getJSONObject(i).getJSONObject("photo").getJSONArray("sizes");
									photoUrl.add(photoArray.getJSONObject(photoArray.length() - 1).getString("url"));
								}

							}
						}


						//TODO OTHER AUDIO?
						attachment.attachment = "tittle{" + tittle + "}text{" + text + "photo{" + photoUrl.toString() + "}";
						break;
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

