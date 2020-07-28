package com.antonchuraev.vkmessenger.DisplayMessages.DialogsList;

import com.antonchuraev.vkmessenger.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class DialogList {

	List<Dialog> dialogList;

	Database database;

	public DialogList(JSONObject response) throws JSONException {
		this.dialogList = new LinkedList<>();
		database = new Database(response);

		JSONArray items = response.getJSONArray("items");

		for (int i = 0; i < items.length(); i++) {
			Dialog dialog = new Dialog();

			JSONObject conversation = items.getJSONObject(i).getJSONObject("conversation");
			JSONObject peer = conversation.getJSONObject("peer");
			String type = peer.getString("type");

			JSONObject last_message = items.getJSONObject(i).getJSONObject("last_message");
			String text = last_message.getString("text");

			//TODO случай с вложением+сообщением
			try {
				if (text.equals("")) {
					dialog.setMessageColor(R.color.colorPrimary);
					text = "Сообщение";
					JSONArray attachments = last_message.getJSONArray("attachments");
					String attachmentsType = attachments.getJSONObject(0).getString("type");
					switch (attachmentsType) {
						case "wall":
							text = "Запись на стене";
							break;
						case "call":
							text = "Звонок";
							break;
						case "video":
							text = "Видео";
							break;
						case "audio":
							text = "Аудио";
							break;
						case "sticker":
							text = "Стикер";
							break;
					}


				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			switch (type) {
				case "user":

				case "group":

					long id = peer.getLong("local_id");
					VKUserOrGroup vkUser = database.searchById(id);

					dialog.setName(vkUser.getName());
					dialog.setPhotoURL(vkUser.getPhotoURL());
					dialog.setLastMessage(text);

					break;

				case "chat":

					JSONObject chat_settings = conversation.getJSONObject("chat_settings");
					String title = chat_settings.getString("title");

					String photo_200;

					if (chat_settings.has("photo")) {
						JSONObject photoURL = chat_settings.getJSONObject("photo");
						photo_200 = photoURL.getString("photo_200");
					} else {
						photo_200 = "https://vk.com/images/camera_200.png?ava=1"; //TODO
					}

					dialog.setName(title);
					dialog.setPhotoURL(photo_200);
					dialog.setLastMessage(text);
					break;

				default:
					throw new IllegalStateException("Unexpected value: " + type.toString());
			}
			dialogList.add(dialog);
		}


	}


	public List<Dialog> getDialogList() {
		return dialogList;
	}

}
