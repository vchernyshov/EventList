package ua.insomnia.eventlist.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class EventContract {
	
	public static final String CONTENT_AUTHORITY = "ua.insomnia.eventlist";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EVENTS = "events";


	public static final class EventTable implements BaseColumns{
		
		
		public static final String TABLE_NAME = "events";
		
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String COLUMN_DATE = "date";
		public static final String COLUMN_DATE_TIME = "date_time";
		public static final String COLUMN_DATE_TIME_CREATED = "date_time_created";
		public static final String COLUMN_IMAGE = "image";
		public static final String COLUMN_LOCATION = "location";
		public static final String COLUMN_PRICE = "price";
		public static final String COLUMN_VK_LINK = "vk_link";
		public static final String COLUMN_FB_LINK = "fb_link";
		public static final String COLUMN_SITE = "site";
		public static final String COLUMN_TYPE = "type";
		
		
		public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;
		
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;
        
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENTS).build();
        
        public static Uri buildEventUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        
        public static Uri buildEventUriWithDate(String date) {
        	return CONTENT_URI.buildUpon().appendPath(date).build();
        }
        
        public static long getIdFromUri(Uri uri) {
        	return Long.parseLong(uri.getLastPathSegment());
        }
        
        public static String getDateFromUri(Uri uri) {
        	return String.valueOf(uri.getLastPathSegment());
        }
	}
}
