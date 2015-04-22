package ua.insomnia.eventlist.rest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.insomnia.eventlist.model.Event;
import android.net.Uri;
import android.util.Log;

public class Response {

	private static final String TAG = "Response";

	private String responeString;
	private Integer count;
	private String nextPage;
	private String previousPage;
	private ArrayList<Event> list = new ArrayList<Event>();

	public Response(String respone) {
		this.responeString = respone;
		try {
			parseResponeStirng();
		} catch (JSONException e) {
			Log.e(TAG, "Cant parse respone string.\n" + e.toString());
		}
		catch(NullPointerException e1) {
			Log.e(TAG, "Response string is null.\n" + e1.toString());
		}
		Log.d(TAG, "Respone = " + respone);
	}

	private void parseResponeStirng() throws JSONException, NullPointerException { 
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

		if (!json.has("count") && !json.has("next") && !json.has("previous")
				&& !json.has("results"))
			list.add(new Event(json));
	}

	public Integer getCount() {
		return this.count;
	}

	public Integer getNextPage() {
		if (nextPage !=  null) {
			Uri uri = Uri.parse(nextPage);
			String page = uri.getQueryParameter("page");
			Log.d(TAG, "nextPage = " + page);
			return Integer.valueOf(page);
		} else
			return Integer.valueOf(1);
	}

	public Integer getPreviousPage() {
		if (previousPage != null) {
			Uri uri = Uri.parse(previousPage);
			return Integer.valueOf(uri.getQueryParameter("page"));
		} else
			return Integer.valueOf(1);
	}

	public ArrayList<Event> getEvents() {
		return this.list;
	}

}
