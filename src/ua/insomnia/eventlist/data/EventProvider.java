package ua.insomnia.eventlist.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class EventProvider extends ContentProvider {

	private static final String TAG = "EventProvider";

	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private EventDbHelper mOpenHelper;

	private static final int EVENTS = 100;
	private static final int EVENTS_WITH_ID = 101;
	private static final int EVENTS_WITH_DATE = 102;

	static UriMatcher buildUriMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(EventContract.CONTENT_AUTHORITY,
				EventContract.PATH_EVENTS, EVENTS);
		matcher.addURI(EventContract.CONTENT_AUTHORITY,
				EventContract.PATH_EVENTS + "/#", EVENTS_WITH_ID);
		matcher.addURI(EventContract.CONTENT_AUTHORITY,
				EventContract.PATH_EVENTS + "/*", EVENTS_WITH_DATE);
		return matcher;
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new EventDbHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		final int match = sUriMatcher.match(uri);
		Log.d(TAG, "match = " + match);
		switch (match) {
		case EVENTS:
			return EventContract.EventTable.CONTENT_TYPE;
		case EVENTS_WITH_ID:
			return EventContract.EventTable.CONTENT_ITEM_TYPE;
		case EVENTS_WITH_DATE:
			return EventContract.EventTable.CONTENT_TYPE;
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		Uri returnUri;

		switch (match) {
		case EVENTS: {
			long _id = db.insert(EventContract.EventTable.TABLE_NAME, null,
					values);
			if (_id > 0)
				returnUri = EventContract.EventTable.buildEventUri(_id);
			else
				throw new android.database.SQLException(
						"Failed to insert row into " + uri);
			break;
		}
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);

		long _id = -1;
		int insertedCount = 0;

		switch (match) {
		case EVENTS: {
			try {
				db.beginTransaction();
				for (ContentValues cv : values) {
					_id = db.insert(EventContract.EventTable.TABLE_NAME, null,
							cv);
					if (_id > 0)
						insertedCount++;
					else
						throw new android.database.SQLException(
								"Failed to insert row into " + uri);
				}

				Uri resultUri = EventContract.EventTable.buildEventUri(_id);
				getContext().getContentResolver().notifyChange(resultUri, null);
			} finally {
				db.endTransaction();
			}
			break;
		}
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		return insertedCount;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case EVENTS:
			break;
		case EVENTS_WITH_ID:
			long _id = EventContract.EventTable.getIdFromUri(uri);
			if (TextUtils.isEmpty(selection))
				selection = EventContract.EventTable._ID + "=" + _id;
			else
				selection = selection + "AND" + EventContract.EventTable._ID
						+ "=" + _id;
			break;
		case EVENTS_WITH_DATE:
			String date = EventContract.EventTable.getDateFromUri(uri);
			Log.d(TAG, "date from uri - " + date);
			date = trimDate(date);
			Log.d(TAG, "date after trimming - " + date);
			if (TextUtils.isEmpty(selection))
				selection = EventContract.EventTable.COLUMN_DATE + "=" + date;
			else
				selection = selection + " AND "
						+ EventContract.EventTable.COLUMN_DATE + "=" + date;
			break;
		default:
			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		Cursor cursor = db.query(EventContract.EventTable.TABLE_NAME,
				projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(),
				EventContract.EventTable.CONTENT_URI);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int updatedCount = 0;

		switch (match) {
		case EVENTS:
			break;
		case EVENTS_WITH_ID:
			long _id = EventContract.EventTable.getIdFromUri(uri);
			if (TextUtils.isEmpty(selection))
				selection = EventContract.EventTable._ID + "=" + _id;
			else
				selection = selection + "AND" + EventContract.EventTable._ID
						+ "=" + _id;
			break;
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		
		updatedCount = db.update(EventContract.EventTable.TABLE_NAME,
				values, selection, selectionArgs);
		
		if (updatedCount != 0)
			getContext().getContentResolver().notifyChange(uri, null);
		return updatedCount;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int deletedCount = 0;

		switch (match) {
		case EVENTS:
			if (selection == null)
				selection = "1";
			break;
		case EVENTS_WITH_ID:
			long _id = EventContract.EventTable.getIdFromUri(uri);
			if (TextUtils.isEmpty(selection))
				selection = EventContract.EventTable._ID + "=" + _id;
			else
				selection = selection + "AND" + EventContract.EventTable._ID
						+ "=" + _id;
			break;
		case EVENTS_WITH_DATE:
			String date = EventContract.EventTable.getDateFromUri(uri);
			if (TextUtils.isEmpty(selection))
				selection = EventContract.EventTable.COLUMN_DATE + "=" + date;
			else
				selection = selection + " AND "
						+ EventContract.EventTable.COLUMN_DATE + "=" + date;
			break;
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);

		}

		deletedCount = db.delete(EventContract.EventTable.TABLE_NAME,
				selection, selectionArgs);

		if (deletedCount != 0)
			getContext().getContentResolver().notifyChange(uri, null);
		return deletedCount;
	}

	private String trimDate(String date) {
		return date.replaceAll("[-]", "");
	}

}
