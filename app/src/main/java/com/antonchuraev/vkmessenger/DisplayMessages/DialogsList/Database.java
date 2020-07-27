package com.antonchuraev.vkmessenger.DisplayMessages.DialogsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Database {

	private List<VKUserOrGroup> vkUsers;

	public Database(JSONObject response) throws JSONException {
		this.vkUsers = new ArrayList<VKUserOrGroup>();

		JSONArray profiles = response.getJSONArray("profiles");
		JSONArray groups = response.getJSONArray("groups");

		for (int i = 0; i < profiles.length(); i++) {
			vkUsers.add(VKUserOrGroup.parseProfiles(profiles.getJSONObject(i)));
		}

		for (int i = 0; i < groups.length(); i++) {
			vkUsers.add(VKUserOrGroup.parseGroups(groups.getJSONObject(i)));
		}

	}

	public VKUserOrGroup searchById(long id) {
		for (int i = 0; i < vkUsers.size(); i++) {
			if (vkUsers.get(i).getId() == id) {
				return vkUsers.get(i);
			}
		}
		return new VKUserOrGroup(0, "Пользователь не найден", "https://sun1-14.userapi.com/ZP_Is2oDNT71N-xal1NmPvQ6pPRCPfd5uTy6qg/LP1ZSjd2jx0.jpg?ava=1");
	}

}
