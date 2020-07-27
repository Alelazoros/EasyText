package com.antonchuraev.vkmessenger.DisplayMessages.DialogsList;

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
			JSONObject conversation = items.getJSONObject(i).getJSONObject("conversation");
			JSONObject peer = conversation.getJSONObject("peer");
			String type = peer.getString("type");

			JSONObject last_message = items.getJSONObject(i).getJSONObject("last_message");
			String text = last_message.getString("text");
			Dialog dialog;

			switch (type) {
				case "user":
				case "group":

					long id = peer.getLong("id");
					VKUserOrGroup vkUser = database.searchById(id);

					dialog = new Dialog(vkUser.getName(), vkUser.getPhotoURL(), text);

					break;

				case "chat":

					JSONObject chat_settings = conversation.getJSONObject("chat_settings");
					String title = chat_settings.getString("title");

					String photo_200;

					if (conversation.has("photo")) {
						JSONObject photoURL = conversation.getJSONObject("photo");
						photo_200 = photoURL.getString("photo_200");
					} else {
						photo_200 = "https://sun1-14.userapi.com/ZP_Is2oDNT71N-xal1NmPvQ6pPRCPfd5uTy6qg/LP1ZSjd2jx0.jpg?ava=1";
					}


					dialog = new Dialog(title, photo_200, text);
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
