package ua.insomnia.eventlist.rest;

import android.net.Uri;

public class EventProviderContract {
	
	public static final String AUTHORITY = "ua.insomnia.eventlist.eventprovider";

	// EVENT TABLE CONTRACT
	public static final class EventTable {

		// TABLE COLUMNS
		public static final String TABLE_NAME = "events";
		public static final String _ID = "_id";
		public static final String TITLE = "title";
		public static final String DESCRIPTION = "description";
		public static final String DATE_TIME = "date_time";
		public static final String DATE_TIME_CREATED = "date_time_created";
		public static final String IMAGE = "image";
		public static final String LOCATION = "location";
		public static final String PRICE = "price";
		public static final String VK_LINK = "vk_link";
		public static final String FB_LINK = "fb_link";
		public static final String SITE = "site";
		public static final String TYPE = "type";
		
		// URI DEFS
		static final String SCHEME = "content://";
		public static final String URI_PREFIX = SCHEME + AUTHORITY;
		private static final String URI_PATH_EVENTS_LIST = "/" + TABLE_NAME;
		// Note the slash on the end of this one, as opposed to the URI_PATH_EVENTS, which has no slash.
		private static final String URI_PATH_EVENT= "/" + TABLE_NAME + "/";
		public static final int EVENT_ID_PATH_POSITION = 1;
		
		
		// content://ua.insomnia.kpievent.eventprovider/events
		public static final Uri CONTENT_URI = Uri.parse(URI_PREFIX + URI_PATH_EVENTS_LIST);
		// content://ua.insomnia.kpievent.eventprovider/events/ -- used for content provider insert() call
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + URI_PATH_EVENT);
		// content://ua.insomnia.kpievent.eventprovider/events/#
		public static final Uri CONTENT_ID_URI_PATTERN = Uri.parse(SCHEME + AUTHORITY + URI_PATH_EVENT + "#");
		
		public static final String[] ALL_COLUMNS;
		public static final String[] DISPLAY_COLUMNS;
		
		static {
			ALL_COLUMNS = new String[] {
					EventTable._ID,
					EventTable.TITLE,
					EventTable.DATE_TIME,
					EventTable.DATE_TIME_CREATED,
					EventTable.IMAGE,
					EventTable.LOCATION,
					EventTable.PRICE,
					EventTable.VK_LINK,
					EventTable.FB_LINK,
					EventTable.SITE,
					//EventTable.TYPE,
					EventTable.DESCRIPTION
			};
			DISPLAY_COLUMNS = ALL_COLUMNS;
			}
		
	}
}
