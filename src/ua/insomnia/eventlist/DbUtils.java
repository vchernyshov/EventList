package ua.insomnia.eventlist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ua.insomnia.eventlist.rest.EventProviderContract.EventTable;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class DbUtils {
	
	public static Event getEventById(Context c, long id) {
		Uri uriWithID = ContentUris.withAppendedId(EventTable.CONTENT_URI, id);
		Cursor cursor = c.getContentResolver().query(uriWithID, null, null, null, null);
		return new Event(cursor);
	}
	
	public static ArrayList<Event> getEventsByDate(Context c, String date) {
		ArrayList<Event> list = new ArrayList<Event>();

		String selection = "date('date_time', 'unixepoch', 'localtime') =? ";
		String[] selectionArgs = {"date('"+convertDateStringToLong(date)+"', 'unixepoch', 'localtime')"};
		
		Cursor cursor = c.getContentResolver().query(EventTable.CONTENT_URI, null, selection, selectionArgs, null);
		
		cursor.moveToFirst();
		Log.i("sqlite", ""+cursor.getCount());
		while (!cursor.isAfterLast()) {
			
			list.add(new Event(cursor));
			cursor.moveToNext();
			
		}
		
		cursor.close();
		
		return list;
	}
	
	public static ArrayList<Event> getAllEvents(Context c) {
		ArrayList<Event> list = new ArrayList<Event>();
		Cursor cursor = c.getContentResolver().query(EventTable.CONTENT_URI, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			list.add(new Event(cursor));
			cursor.moveToNext();
		}
		
		cursor.close();
		return list;
	}
	
	private static long convertDateStringToLong(String date)  {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date d= null;
		try {
			d = (Date) formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long mills = d.getTime();
		return mills;
	}

}
