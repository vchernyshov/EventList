package ua.insomnia.eventlist.rest;

import java.util.ArrayList;

import ua.insomnia.eventlist.data.EventContract;
import ua.insomnia.eventlist.data.EventContract.EventTable;
import ua.insomnia.eventlist.model.Event;
import ua.insomnia.eventlist.utils.NetworkChecker;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class EventService extends IntentService {
	public static final String TAG = "EventService";
	
	public static final String SERVICE_CALLBACK = "ua.insomnia.kpievent.SERVICE_CALLBACK";
	public static final int SERVICE_START = 100;
	public static final int SERVICE_STOP = 200;
	public static final int SERVICE_LOAD_FINISHED = 300;
	public static final int SERVICE_LOAD_ERROR = 400;
	public static final int RESULT_CODE = 1;
	public static final String REGUEST_TYPE = "ua.insomnia.kpievent.REGUEST_TYPE";

	public static final String EXTRA_PAGE = "ua.insomnia.eventlist.rest.PAGE";
	public static final String EXTRA_DATE = "ua.insomnia.eventlist.rest.DATE";
	public static final String EXTRA_ID = "ua.insomnia.eventlist.rest.ID";
	public static final String EXTRA_RECEIVER = "ua.insomnia.eventlist.rest.RECEIVER";
	public static final String EXTRA_ACTION = "ua.insomnia.eventlist.rest.ACTION";
	public static final String EXTRA_SERVICE_STATUS = "ua.insomnia.eventlist.rest.SERVICE_STATUS";
	
	public static final int ACTION_UPDATE = 0;
	public static final int ACTION_LOAD = 0;
	public static final int ACTION_LOAD_MORE = 0;

	private static final int DEFAULT_PAGE_SIZE = 10;
	Response respone;
	ResultReceiver receiver;

	public EventService() {
		super("EventService");
		Log.d(TAG, "hashCode = " + this.hashCode());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
		String date = intent.getStringExtra(EXTRA_DATE);
		long id = intent.getLongExtra(EXTRA_ID, -1);
		int page = intent.getIntExtra(EXTRA_PAGE, 1);
		if (NetworkChecker.isNetworkAvailable(getApplicationContext())) {
			if (id == -1)
				respone = Api.getEventByDateR(date, page);
			else
				respone = Api.getEventByIdR(id);

			ArrayList<Event> list = respone.getEvents();
			putEventsToDataBase2(list);

			// Bundle data = new Bundle();
			// data.putInt(EXTRA_PAGE, respone.getNextPage());
			receiver.send(SERVICE_LOAD_FINISHED, Bundle.EMPTY);
			Log.d(TAG, "load finished hashCode = " + this.hashCode());

		} else {
			receiver.send(SERVICE_LOAD_ERROR, Bundle.EMPTY);
			Log.d(TAG, "load error hashCode = " + this.hashCode());
		}
	}

	private void putEventsToDataBase2(ArrayList<Event> list) {

		for (Event event : list) {
			if (getEventById(event.id) == null) {
				insertEvent(event);
			} else {
				updateEvent(event);
			}
		}
	}

	private Event getEventById(long id) {
		Cursor cursor = getContentResolver().query(
				EventContract.EventTable.buildEventUri(id), null, null, null,
				null);
		if (cursor.moveToFirst()) {
			Event event = new Event(cursor);
			cursor.close();
			return event;
		} else {
			cursor.close();
			return null;
		}
	}

	private void insertEvent(Event event) {
		Uri uri = getContentResolver().insert(EventTable.CONTENT_URI,
				event.toContentValues());
		Log.d("EventService", "insert with uri:\n" + uri.toString());
	}

	private int updateEvent(Event event) {
		int count = getContentResolver().update(
				EventContract.EventTable.buildEventUri(event.id),
				event.toContentValues(), null, null);
		Log.d("EventService", "update event with id " + event.id);
		return count;
	}
}
