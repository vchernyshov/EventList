package ua.insomnia.eventlist;

import org.json.JSONException;
import org.json.JSONObject;

import ua.insomnia.eventlist.rest.EventProviderContract.EventTable;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.Time;
import android.util.Log;

public class Event {

	public long id;
	public String title;
	public String description;
	public String image;
	public String dateTime;
	public String dateTimeWhenCreated;
	public String location;
	public String entry;
	public String price;
	public String vkLink;
	public String fbLink;
	public String site;
	public long type;

	public Event(JSONObject json) throws JSONException {
		id = json.getLong("id");
		title = json.getString("title");
		dateTime = json.getString("date_time");
		dateTimeWhenCreated = json.getString("created_at");
		location = json.getString("location");
		description = json.getString("description");
		price = json.getString("price");
		vkLink = json.getString("vk_link");
		fbLink = json.getString("fb_link");
		site = json.getString("site");
		image = json.getString("image");

	}

	public Event() {
		id = 1;
		title = "Event Title";
		dateTime = "2014-11-04 17:00";
	}

	public Event(Cursor cursor) {
		this.id = cursor.getLong(cursor.getColumnIndex(EventTable._ID));
		this.title = cursor.getString(cursor.getColumnIndex(EventTable.TITLE));
		this.description = cursor.getString(cursor
				.getColumnIndex(EventTable.DESCRIPTION));
		// this.dateTime = new
		// Date(cursor.getLong(cursor.getColumnIndex(EventTable.DATE_TIME)));
		// this.dateTimeWhenCreated = new
		// Date(cursor.getLong(cursor.getColumnIndex(EventTable.DATE_TIME_CREATED)));
		this.dateTime = cursor.getString(cursor
				.getColumnIndex(EventTable.DATE_TIME));
		this.dateTimeWhenCreated = cursor.getString(cursor
				.getColumnIndex(EventTable.DATE_TIME_CREATED));
		this.image = cursor.getString(cursor.getColumnIndex(EventTable.IMAGE));
		this.location = cursor.getString(cursor
				.getColumnIndex(EventTable.LOCATION));
		this.price = cursor.getString(cursor.getColumnIndex(EventTable.PRICE));
		this.vkLink = cursor.getString(cursor
				.getColumnIndex(EventTable.VK_LINK));
		this.fbLink = cursor.getString(cursor
				.getColumnIndex(EventTable.FB_LINK));
		this.site = cursor.getString(cursor.getColumnIndex(EventTable.SITE));
		//this.type = cursor.getLong(cursor.getColumnIndex(EventTable.TYPE));
	}

	public ContentValues toContentValues() {
		ContentValues v = new ContentValues();
		v.put(EventTable._ID, this.id);
		v.put(EventTable.TITLE, this.title);
		// v.put(EventTable.DATE_TIME, this.dateTime.getTime());
		// v.put(EventTable.DATE_TIME_CREATED,
		// this.dateTimeWhenCreated.getTime());
		v.put(EventTable.DATE_TIME, this.dateTime);
		v.put(EventTable.DATE_TIME_CREATED, this.dateTimeWhenCreated);
		v.put(EventTable.DESCRIPTION, this.description);
		v.put(EventTable.LOCATION, this.location);
		v.put(EventTable.IMAGE, this.image);
		v.put(EventTable.PRICE, this.price);
		v.put(EventTable.VK_LINK, this.vkLink);
		v.put(EventTable.FB_LINK, this.fbLink);
		v.put(EventTable.SITE, this.site);
		//v.put(EventTable.TYPE, this.type);
		return v;
	}

	public String getDate() {
		Time t = new Time(Time.getCurrentTimezone());
		t.format("yyyy-MM-dd");
		t.parse3339(dateTime);
		int year = t.year;
		int month = t.month+1;
		int day = t.monthDay;
		
		return year + "-" + month + "-" + day;
	}

	public String getTime() {
		Time t = new Time(Time.getCurrentTimezone());
		Log.i("TimeZone", t.timezone);
		
		t.format("HH:mm:ss.SSSZ");
		t.parse3339(dateTime);
		int hour = t.hour;
		int minutes = t.minute;
		String res = null;
		hour = hour + 2;
		res = hour + ":" + minutes;
		
		if (minutes < 10)
			res = hour + ":0" + minutes;
		if (hour < 10)
			res = "0"+hour + ":" + minutes;
		if (minutes < 10 && hour < 10)
			res = "0"+hour + ":0" + minutes;
		
		
		return res;
	}
}
