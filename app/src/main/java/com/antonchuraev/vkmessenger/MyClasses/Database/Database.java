package com.antonchuraev.vkmessenger.MyClasses.Database;

import android.icu.text.Transliterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Database {

	/**
	 * <id , { name;
	 * photoURL;
	 * online;}>
	 */
	private HashMap<Long, DatabaseFormat> longDialogHashMap = new HashMap<>();


	public void parseAndAddToDataBase(JSONObject response) throws JSONException {

		if (response.has("profiles")) {
			JSONArray profiles = response.getJSONArray("profiles");
			for (int i = 0; i < profiles.length(); i++) {
				DatabaseFormat newRecord = new DatabaseFormat();
				JSONObject profile = profiles.getJSONObject(i);

				newRecord.name = Transliterator.getInstance("Latin-Russian/BGN").transliterate(profile.getString("first_name") + " " + profile.getString("last_name"));

				newRecord.photoURL = profile.getString("photo_200");
				newRecord.online = profile.getInt("online") == 1;
				longDialogHashMap.put(profile.getLong("id"), newRecord);
			}
		}

		if (response.has("groups")) {
			JSONArray groups = response.getJSONArray("groups");
			for (int i = 0; i < groups.length(); i++) {
				DatabaseFormat newRecord = new DatabaseFormat();
				JSONObject group = groups.getJSONObject(i);
				newRecord.name = Transliterator.getInstance("Latin-Russian/BGN").transliterate(group.getString("name"));
				newRecord.photoURL = group.getString("photo_200");

				longDialogHashMap.put(group.getLong("id"), newRecord);
			}
		}


	}

	public HashMap<Long, DatabaseFormat> getLongDialogHashMap() {
		return longDialogHashMap;
	}

	public DatabaseFormat findById(long id) {
		return longDialogHashMap.get(id);
	}


}

