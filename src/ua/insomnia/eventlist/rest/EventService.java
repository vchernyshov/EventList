package ua.insomnia.eventlist.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ua.insomnia.eventlist.Event;
import ua.insomnia.eventlist.rest.EventProviderContract.EventTable;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class EventService extends IntentService {

	public static final String SERVICE_CALLBACK = "ua.insomnia.kpievent.SERVICE_CALLBACK";

	public static final int SERVICE_START = 100;
	public static final int SERVICE_STOP = 200;

	public static final int RESULT_CODE = 1;
	public static final String REGUEST_TYPE = "ua.insomnia.kpievent.REGUEST_TYPE";
	public static final String DATE_EXTRA = "ua.insomnia.kpievent.DATE_EXTRA";
	public static final String ID_EXTRA = "ua.insomnia.kpievent.ID_EXTRA";
	// public static final String OFFSET_EXTRA =
	// "ua.insomnia.kpievent.OFFSET_EXTRA";

	public static final int TYPE_ALL = 1;
	public static final int TYPE_BY_DATE = 2;
	public static final int TYPE_BY_ID = 3;
	public static final int TYPE_PARTLY = 4;

	public static final int TYPE_TEST = 100;

	ResultReceiver receiver;
	Api api = new Api();

	public EventService() {
		super("EventService");
	}

	@Override
	protected void onHandleIntent(Intent reguestIntent) {

		int type = reguestIntent.getIntExtra(EventService.REGUEST_TYPE, -1);
		try {
			test(reguestIntent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		receiver.send(EventService.SERVICE_STOP, Bundle.EMPTY);
		switch (type) {
		case EventService.TYPE_ALL:
			// call method to load all events from server
			api.getEvents(type, null, null);
			break;
		case EventService.TYPE_BY_DATE:
			// call method to load events from server by date
			String date = reguestIntent.getStringExtra(EventService.DATE_EXTRA);
			api.getEvents(type, date, null);
			break;
		case EventService.TYPE_BY_ID:
			// call method to load events from server by id
			Long id = reguestIntent.getLongExtra(EventService.ID_EXTRA, -1);
			api.getEvents(type, null, id);
			break;
		case EventService.TYPE_PARTLY:
			// call method to load events from server partly
			break;
		default:
			// let know activity about invalid method
			break;
		}
	}

	private void test(Intent reguestIntent) throws Exception {
		receiver = reguestIntent
				.getParcelableExtra(EventService.SERVICE_CALLBACK);
		receiver.send(EventService.SERVICE_START, Bundle.EMPTY);

		Thread.sleep(500);

		// add method to write data to db
		//putTestData();
		//putTestData2();
		
		generateTestEventList();
		
	}

	private void putTestData() throws Exception {
		ContentValues v = new ContentValues();
		v.put(EventTable._ID, 1);
		v.put(EventTable.DATE_TIME, new Date(
				convertDateStringToLong("2014-10-28 12:12")).getTime());
		v.put(EventTable.DATE_TIME_CREATED, new Date(
				convertDateStringToLong("2013-10-28 12:12")).getTime());
		v.put(EventTable.DESCRIPTION, "this is first data in table");
		v.put(EventTable.IMAGE, "testImage");
		v.put(EventTable.LOCATION, "testLocation");
		v.put(EventTable.PRICE, "testPrice");
		v.put(EventTable.TITLE, "testTitle");
		v.put(EventTable.TYPE, "testType");
		v.put(EventTable.VK_LINK, "testVkLink");
		v.put(EventTable.FB_LINK, "testFbLink");
		v.put(EventTable.SITE, "testSite");

		// Uri u = Uri.parse(EventTable.CONTENT_URI);
		Uri uri = getContentResolver().insert(EventTable.CONTENT_URI, v);
	}

	private void putTestData2() throws Exception {
		ContentValues v = new ContentValues();
		v.put(EventTable._ID, 2);
		v.put(EventTable.DATE_TIME, new Date(
				convertDateStringToLong("2014-10-30 12:30")).getTime());
		v.put(EventTable.DATE_TIME_CREATED, new Date(
				convertDateStringToLong("2013-10-28 12:12")).getTime());
		v.put(EventTable.DESCRIPTION, "this is second data in table");
		v.put(EventTable.IMAGE, "testImage");
		v.put(EventTable.LOCATION, "testLocation");
		v.put(EventTable.PRICE, "testPrice");
		v.put(EventTable.TITLE, "testTitle");
		v.put(EventTable.TYPE, "testType");
		v.put(EventTable.VK_LINK, "testVkLink");
		v.put(EventTable.FB_LINK, "testFbLink");
		v.put(EventTable.SITE, "testSite");

		// Uri u = Uri.parse(EventTable.CONTENT_URI);
		Uri uri = getContentResolver().insert(EventTable.CONTENT_URI, v);
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
			}
			ContentValues[] cv = new ContentValues[size];
			values.toArray(cv);
			int count = getContentResolver().bulkInsert(EventTable.CONTENT_URI,
					cv);
			Log.i("EventService", "count = "+count);
		}
	}
	
	private void generateTestEventList(){
		ArrayList<Event> testList = new ArrayList<Event>();
		for (int i = 1; i<=30; i++){
			Event e = new Event();
			e.id = i;
			e.dateTime = "2014-10-11 23:23:00";
			e.dateTimeWhenCreated = "2014-10-10 23:23:00";
			e.description = "this is test event "+i;
			e.entry = "free";
			e.fbLink = "fb";
			e.vkLink = "vk";
			e.location = "loc";
			e.site = "site";
			e.price = "free";
			e.title = "Test "+i;
			e.type = 1;
			//e.image = "drawable://"+R.drawable.testlogo;
			testList.add(e);
		}
		
		putEventsToDataBase(testList);
	}

}
