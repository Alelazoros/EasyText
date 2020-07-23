package com.antonchuraev.vkmessenger.MyClasses.VKUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VKUsersList {

	private List<VKUser> vkUsers;


	public VKUsersList(JSONArray vkUsersJSONArray) throws JSONException {
		this.vkUsers = new LinkedList<>();
		for (int i = 0; i < vkUsersJSONArray.length() ; i++) {
			JSONObject jsonObject = vkUsersJSONArray.getJSONObject(i);
			VKUser vkUser = VKUser.parse(jsonObject);
			vkUsers.add(vkUser);
		}
	}

	public List<VKUser> getVkUsers(){
		return vkUsers;
	}

	public VKUser getVkUser(int position){
		return vkUsers.get(position);
	}


}
