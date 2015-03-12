package ua.insomnia.eventlist.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ua.insomnia.eventlist.data.EventContract;
import ua.insomnia.eventlist.data.EventContract.EventTable;
import ua.insomnia.eventlist.model.Event;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.SyncStateContract.Constants;
import android.util.Log;

public class EventService extends IntentService {
	public static final String SERVICE_CALLBACK = "ua.insomnia.kpievent.SERVICE_CALLBACK";
	public static final int SERVICE_START = 100;
	public static final int SERVICE_STOP = 200;
	public static final int SERVICE_LOAD_FINISHED = 300;
	public static final int RESULT_CODE = 1;
	public static final String REGUEST_TYPE = "ua.insomnia.kpievent.REGUEST_TYPE";
	public static final String DATE_EXTRA = "ua.insomnia.kpievent.DATE_EXTRA";

	ResultReceiver receiver;
	Api api = new Api();

	public EventService() {
		super("EventService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ResultReceiver receiver = intent.getParcelableExtra("receiver");
		String date = intent.getStringExtra(DATE_EXTRA);
		Respone respone = api.getEventByDateR(date, 1);
		ArrayList<Event> list = respone.getEvents();
		putEventsToDataBase2(list);
		receiver.send(SERVICE_LOAD_FINISHED, Bundle.EMPTY);
	}

	private long convertDateStringToLong(String date) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d = (Date) formatter.parse(date);
		long mills = d.getTime();
		return mills;
	}

	private void putEventsToDataBase(ArrayList<Event> list) {
		int size = list.size();
		if (size == 1) {
			Uri uri = getContentResolver().insert(EventTable.CONTENT_URI,
					list.get(0).toContentValues());
		} else {
			ArrayList<ContentValues> values = new ArrayList<ContentValues>();
			for (int i = 0; i < size; i++) {
				Event e = list.get(i);
				values.add(e.toContentValues());
				Log.i("EventService", e.getDate2());
			}
			ContentValues[] cv = new ContentValues[size];
			cv = (ContentValues[]) values.toArray();
			int count = getContentResolver().bulkInsert(
					EventContract.EventTable.CONTENT_URI, cv);
			Log.i("EventService", "count = " + count);
		}
	}

	private void putEventsToDataBase2(ArrayList<Event> list) {
		Uri uri;
		for (Event event : list) {
			if (getEventById(event.id) == null) {
				uri = getContentResolver().insert(EventTable.CONTENT_URI,
						event.toContentValues());
				Log.d("EventService", uri.toString());
			}
		}
	}

	private Event getEventById(long id) {
		Cursor cursor = getContentResolver().query(
				EventContract.EventTable.buildEventUri(id), null, null, null,
				null);
		if (cursor.moveToFirst())
			return new Event(cursor);
		else
			return null;
	}
}
