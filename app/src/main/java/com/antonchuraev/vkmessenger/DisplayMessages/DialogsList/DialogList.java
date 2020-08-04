package com.antonchuraev.vkmessenger.DisplayMessages.DialogsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class DialogList {

	public List<Dialog> dialogList = new LinkedList<>();
	Database database = new Database();

	public DialogList() throws JSONException {
	}

	public void addToListFromJSONObject(JSONObject response) throws JSONException {
		database.parseAndAddToDataBase(response);
		dialogList.addAll(parseListFromJSONObject(response));
	}

	private List parseListFromJSONObject(JSONObject response) throws JSONException {
		List returnStatement = new LinkedList();

		JSONArray items = response.getJSONArray("items");

		for (int i = 0; i < items.length(); i++) {
			Dialog dialog = getDialogDate(items.getJSONObject(i));
			returnStatement.add(dialog);
		}
		return returnStatement;
	}

	private Dialog getDialogDate(JSONObject jsonObject) throws JSONException {
		Dialog dialog = new Dialog();


		return dialog;
	}


	public List<Dialog> getDialogList() {
		return dialogList;
	}


}
