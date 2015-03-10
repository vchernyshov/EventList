package ua.insomnia.eventlist.rest;

import ua.insomnia.eventlist.rest.EventProviderContract.EventTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	public final String TAG = getClass().getSimpleName();

	private static final String DATABASE_NAME = "eventlist.db";
	private static final int DATABASE_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// CREATE EVENTS TABLE
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("CREATE TABLE " + EventTable.TABLE_NAME + " (");
		sqlBuilder.append(EventTable._ID + " INTEGER, ");
		sqlBuilder.append(EventTable.TITLE + " TEXT, ");
		sqlBuilder.append(EventTable.DESCRIPTION + " TEXT, ");
		sqlBuilder.append(EventTable.DATE_TIME + " TEXT, ");
		sqlBuilder.append(EventTable.DATE_TIME_CREATED + " TEXT, ");
		sqlBuilder.append(EventTable.IMAGE + " TEXT, ");
		sqlBuilder.append(EventTable.LOCATION + " TEXT, ");
		sqlBuilder.append(EventTable.PRICE + " TEXT, ");
		sqlBuilder.append(EventTable.VK_LINK + " TEXT, ");
		sqlBuilder.append(EventTable.FB_LINK + " TEXT, ");
		sqlBuilder.append(EventTable.SITE + " TEXT");
		//sqlBuilder.append(EventTable.TYPE + " TEXT");
		sqlBuilder.append(");");
		String sql = sqlBuilder.toString();
		Log.i(TAG, "Creating DB table with string: '" + sql + "'");
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Gets called when the database is upgraded, i.e. the version number
		// changes
	}

}
