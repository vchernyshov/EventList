package ua.insomnia.eventlist.data;

import ua.insomnia.eventlist.data.EventContract.EventTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventDbHelper extends SQLiteOpenHelper {

	public final String TAG = getClass().getSimpleName();

	private static final String DATABASE_NAME = "eventlist.db";
	private static final int DATABASE_VERSION = 2;

	public EventDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// CREATE EVENTS TABLE
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + EventTable.TABLE_NAME + " (");
		sqlBuilder.append(EventContract.EventTable._ID + " INTEGER, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_TITLE + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_DESCRIPTION + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_DATE + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_DATE_TIME + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_DATE_TIME_CREATED + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_IMAGE + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_LOCATION + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_PRICE + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_VK_LINK + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_FB_LINK + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_SITE + " TEXT, ");
		sqlBuilder.append(EventContract.EventTable.COLUMN_TITLE_SEARCH + " TEXT");
		sqlBuilder.append(");");
		String sql = sqlBuilder.toString();
		Log.d(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
