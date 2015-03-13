package ua.insomnia.eventlist.rest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.insomnia.eventlist.model.Event;
import android.util.Log;

public class Respone {

	private static final String TAG = "Respone";

	private String responeString;
	private Integer count;
	private String nextPage;
	private String previousPage;
	private ArrayList<Event> list = new ArrayList<Event>();

	public Respone(String respone) {
		this.responeString = respone;
		try {
			parseResponeStirng();
		} catch (JSONException e) {
			Log.e(TAG, "Cant parse respone string.\n" + e.toString());
		}
		Log.d(TAG, "Respone = " + respone);
	}
	
	private void parseResponeStirng() throws JSONException {
		JSONObject json = new JSONObject(responeString);
		if (json.has("count"))
			if (json.getString("count") != "null")
				this.count = Integer.valueOf(json.getInt("count"));
		if (json.has("next"))
			if (json.getString("next") != "null")
				this.nextPage = json.getString("next");
		if (json.has("previous"))
			if (json.getString("previous") != "null")
				this.previousPage = json.getString("previous");
		if (json.has("results")) {
			JSONArray events = json.getJSONArray("results");
			for (int i = 0; i < events.length(); i++)
				this.list.add(new Event(events.getJSONObject(i)));
		}
		
		if (!json.has("count") && !json.has("next") && !json.has("previous") && !json.has("results"))
			list.add(new Event(json));
	}

	public Integer getCount() {
		return this.count;
	}

	public String getNextPage() {
		return this.nextPage;
	}

	public String getPreviousPage() {
		return this.previousPage;
	}

	public ArrayList<Event> getEvents() {
		return this.list;
	}

}
