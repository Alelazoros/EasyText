package com.antonchuraev.vkmessenger.MyClasses.Dialog;

import com.antonchuraev.vkmessenger.MyClasses.Database.Database;
import com.antonchuraev.vkmessenger.MyClasses.Database.DatabaseFormat;
import com.antonchuraev.vkmessenger.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class DialogList {

	public List<Dialog> dialogList = new LinkedList<>();
	Database database = new Database();


	public void addToListFromJSONObject(JSONObject response) throws JSONException {
		database.parseAndAddToDataBase(response);
		dialogList.addAll(parseListFromJSONObject(response));
	}

	private List parseListFromJSONObject(JSONObject response) throws JSONException {
		List dialogList = new LinkedList();

		if (response.has("items")) {
			JSONArray items = response.getJSONArray("items");
			for (int i = 0; i < items.length(); i++) {
				JSONObject conversation = items.getJSONObject(i).getJSONObject("conversation");
				JSONObject peer = conversation.getJSONObject("peer");
				String type = peer.getString("type");

				Dialog dialog = new Dialog();

				//set all from "conversation"
				switch (type) {
					case "group":
						DatabaseFormat databaseFormatGroup = database.findById(peer.getLong("local_id"));
						dialog.setType(Type.GROUP);
						dialog.setName(databaseFormatGroup.name);
						dialog.setPhotoURL(databaseFormatGroup.photoURL);
						break;
					case "user":
						DatabaseFormat databaseFormatUser = database.findById(peer.getLong("local_id"));
						dialog.setType(Type.USER);
						dialog.setName(databaseFormatUser.name);
						dialog.setOnline(databaseFormatUser.online);
						dialog.setPhotoURL(databaseFormatUser.photoURL);
						break;
					case "chat":
						JSONObject chatSettings = conversation.getJSONObject("chat_settings");
						dialog.setType(Type.CHAT);
						dialog.setName(chatSettings.getString("title"));
						dialog.setOnline(false);

						if (chatSettings.has("photo")) {
							dialog.setPhotoURL(chatSettings.getJSONObject("photo").getString("photo_200"));
						}
						break;
				}
				dialog.setReceiverId(peer.getLong("local_id"));

				//set all from "last_message"
				JSONObject lastMessage = items.getJSONObject(i).getJSONObject("last_message");
				String text = lastMessage.getString("text");

				if (!text.equals("")) {
					dialog.setLastMessage(text);
				} else {
					JSONArray attachments = lastMessage.getJSONArray("attachments");
					dialog.setMessageColor(R.color.colorPrimary);

					if (attachments.length() > 0) {
						String attachmentType = attachments.getJSONObject(0).getString("type");
						switch (attachmentType) {
							case "video":
								dialog.setLastMessage("Видео");
								break;
							case "link":
								dialog.setLastMessage("Ссылка");
								break;
							case "call":
								dialog.setLastMessage("Звонок");
								break;
							case "photo":
								dialog.setLastMessage("Фото");
								break;
							case "sticker":
								dialog.setLastMessage("Стикер");
								break;
							case "audio":
								dialog.setLastMessage("Аудио");
								break;
							case "audio_message":
								dialog.setLastMessage("Голосовое сообщение");
								break;
						}
					}
				}


				dialogList.add(dialog);
			}


			return dialogList;
		}

		return dialogList;
	}


	public List<Dialog> getDialogList() {
		return dialogList;
	}


}
