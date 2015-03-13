package ua.insomnia.eventlist.rest;

import java.util.ArrayList;

import ua.insomnia.eventlist.data.EventContract;
import ua.insomnia.eventlist.data.EventContract.EventTable;
import ua.insomnia.eventlist.model.Event;
import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class EventService extends IntentService {
	public static final String SERVICE_CALLBACK = "ua.insomnia.kpievent.SERVICE_CALLBACK";
	public static final int SERVICE_START = 100;
	public static final int SERVICE_STOP = 200;
	public static final int SERVICE_LOAD_FINISHED = 300;
	public static final int RESULT_CODE = 1;
	public static final String REGUEST_TYPE = "ua.insomnia.kpievent.REGUEST_TYPE";

	public static final String EXTRA_DATE = "ua.insomnia.kpievent.DATE_EXTRA";
	public static final String EXTRA_ID = "ua.insomnia.eventlist.ID";
	public static final String EXTRA_RECEIVER = "ua.insomnia.eventlist.RECEIVER";

	Respone respone;
	ResultReceiver receiver;
	Api api = new Api();

	public EventService() {
		super("EventService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
		String date = intent.getStringExtra(EXTRA_DATE);
		long id = intent.getLongExtra(EXTRA_ID, -1);
		if (id == -1) 
			respone = api.getEventByDateR(date, 1);
		 else
			 respone = api.getEventByIdR(id);
		
		ArrayList<Event> list = respone.getEvents();
		putEventsToDataBase2(list);
		
		
		
		receiver.send(SERVICE_LOAD_FINISHED, Bundle.EMPTY);
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
		if (cursor.moveToFirst())
			return new Event(cursor);
		else
			return null;
	}

	private void insertEvent(Event event) {
		Uri uri = getContentResolver().insert(EventTable.CONTENT_URI,
				event.toContentValues());
		Log.d("EventService", "insert with uri:\n"+uri.toString());
	}
	
	private int updateEvent(Event event) {
		int count = getContentResolver().update(
				EventContract.EventTable.buildEventUri(event.id),event.toContentValues(), null, null);
		Log.d("EventService", "update event with id " + event.id);
		return count;
	}
}
