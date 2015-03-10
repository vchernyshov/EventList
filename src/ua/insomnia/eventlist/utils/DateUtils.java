package ua.insomnia.eventlist.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.util.Log;

public class DateUtils {
	private static final String TAG = "DateUtils";

	public static String getCurrentDate(){
		String date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Calendar now = Calendar.getInstance();
		date = dateFormat.format(now.getTime());
		Log.i(TAG, "now = "+date);
		return date;		
	}
	
	public static String getNextDate(String currentDate){
		String nextDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(dateFormat.parse(currentDate));
		} catch (ParseException e) {
			Log.e(TAG, "Cant parse date string.\n"+e.toString());
		}
		c.add(Calendar.DAY_OF_YEAR, +1);
		
		nextDate = dateFormat.format(c.getTime());
		Log.i(TAG, "next date = "+nextDate);
		return nextDate;		
	}
	
	public static String getPreviousDate(String currentDate){
		String previousDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(dateFormat.parse(currentDate));
		} catch (ParseException e) {
			Log.e(TAG, "Cant parse date string.\n"+e.toString());
		}
		c.add(Calendar.DAY_OF_YEAR, -1);
		
		previousDate = dateFormat.format(c.getTime());
		Log.i(TAG, "previous date = "+previousDate);
		return previousDate;		
	}
	
	public static String getDateToShow(String date){
		Log.i(TAG, "input string = " +date);
		String res = null;
		String formatInput = "yyyy-MM-dd";
		String formatToShow = "d MMMM (EE)";
		SimpleDateFormat format = new SimpleDateFormat(formatInput, Locale.getDefault());
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatToShow, Locale.getDefault());
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(date));
		} catch (ParseException e) {
			Log.e(TAG, "Cant parse date string.\n"+e.toString());
		}
		res = dateFormat.format(c.getTime());
		Log.i(TAG, "output string = " +res);
		return res;
	}
	
}
