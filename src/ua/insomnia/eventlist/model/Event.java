package ua.insomnia.eventlist.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import ua.insomnia.eventlist.data.EventContract.EventTable;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.Time;

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

	public Event() {}

	public Event(Cursor cursor) {
		this.id = cursor.getLong(cursor.getColumnIndex(EventTable._ID));
		this.title = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_TITLE));
		this.description = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_DESCRIPTION));
		this.dateTime = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_DATE_TIME));
		this.dateTimeWhenCreated = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_DATE_TIME_CREATED));
		this.image = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_IMAGE));
		this.location = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_LOCATION));
		this.price = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_PRICE));
		this.vkLink = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_VK_LINK));
		this.fbLink = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_FB_LINK));
		this.site = cursor.getString(cursor
				.getColumnIndex(EventTable.COLUMN_SITE));
	}

	@SuppressLint("DefaultLocale")
	public ContentValues toContentValues() {
		ContentValues v = new ContentValues();
		v.put(EventTable._ID, this.id);
		v.put(EventTable.COLUMN_TITLE, this.title);
		v.put(EventTable.COLUMN_DATE, getDate2());
		v.put(EventTable.COLUMN_DATE_TIME, this.dateTime);
		v.put(EventTable.COLUMN_DATE_TIME_CREATED, this.dateTimeWhenCreated);
		v.put(EventTable.COLUMN_DESCRIPTION, this.description);
		v.put(EventTable.COLUMN_LOCATION, this.location);
		v.put(EventTable.COLUMN_IMAGE, this.image);
		v.put(EventTable.COLUMN_PRICE, this.price);
		v.put(EventTable.COLUMN_VK_LINK, this.vkLink);
		v.put(EventTable.COLUMN_FB_LINK, this.fbLink);
		v.put(EventTable.COLUMN_SITE, this.site);
		v.put(EventTable.COLUMN_TITLE_SEARCH, this.title.toLowerCase());
		return v;
	}

	public String getDate() {
		Time t = new Time(Time.getCurrentTimezone());
		t.format("yyyy-MM-dd");
		t.parse3339(dateTime);
		
		SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
		Date date = new Date(t.toMillis(true));
		return format.format(date);
	}

	public String getDate2() {
		Time t = new Time(Time.getCurrentTimezone());
		t.format("yyyy-MM-dd");
		t.parse3339(dateTime);
		int year = t.year;
		int month = t.month + 1;
		int day = t.monthDay;

		String strDay = "";
		String strMonth = "";

		if (day < 10)
			strDay = "0" + day;
		else
			strDay = "" + day;
		if (month < 10)
			strMonth = "0" + month;
		else
			strMonth = "" + month;

		return year + "" + strMonth + "" + strDay;
	}

	public String getTime() {
		Time t = new Time(Time.getCurrentTimezone());
		t.format("HH:mm:ss.SSSZ");
		t.parse3339(dateTime);
		int hour = t.hour;
		int minutes = t.minute;
		String res = null;
		hour = hour + 3;
		res = hour + ":" + minutes;

		if (minutes < 10)
			res = hour + ":0" + minutes;
		if (hour < 10)
			res = "0" + hour + ":" + minutes;
		if (minutes < 10 && hour < 10)
			res = "0" + hour + ":0" + minutes;

		return res;
	}
}
