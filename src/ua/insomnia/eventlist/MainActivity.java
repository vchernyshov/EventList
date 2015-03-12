package ua.insomnia.eventlist;

import java.util.ArrayList;

import ua.insomnia.eventlist.adapters.FragmentAdapter;
import ua.insomnia.eventlist.data.EventContract;
import ua.insomnia.eventlist.data.EventContract.EventTable;
import ua.insomnia.eventlist.model.Event;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class MainActivity extends StateActivity {

	public final String TAG = getClass().getSimpleName();

	private static final String POSITION = "position";
	private static final int SCREEN_PAGE_LIMIT = 2;

	private ViewPager viewPager;
	private FragmentAdapter adapter;
	private TextViewFonts dateView;
	private int miidlePosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		dateView = (TextViewFonts) findViewById(R.id.txtTopDateView);

		adapter = new FragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		miidlePosition = adapter.getMiddlePosition();

		viewPager.setOffscreenPageLimit(SCREEN_PAGE_LIMIT);
		viewPager.setCurrentItem(miidlePosition);

		dateView.setText(adapter.getPageTitle(miidlePosition));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				dateView.setText(adapter.getPageTitle(position));
				// Log.d(TAG, "onCreate listener: " +currentPosition);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

	}

	@Override
	public void onSaveState() {
		super.onSaveState();
		if (viewPager != null)
			saveInt(POSITION, viewPager.getCurrentItem());
	}

	@Override
	public void onRestoreState() {
		super.onRestoreState();

		int position = restoreInt(POSITION, miidlePosition);
		if (viewPager != null)
			viewPager.setCurrentItem(position);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		saveInt(POSITION, miidlePosition);
		Log.d(TAG, "onDestroy");
	}

	private void saveInt(String key, int value) {
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	private int restoreInt(String key, int defaultValue) {
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		return settings.getInt(key, defaultValue);
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.logo);
		actionBar.setTitle(null);
	}

	private void generateTestEventList() {
		ArrayList<Event> testList = new ArrayList<Event>();
		for (int i = 1; i <= 10; i++) {
			Event e = new Event(i);
			e.dateTime = "2014-10-1" + i;
			e.dateTimeWhenCreated = "2014-10-10 23:23:00";
			e.description = "this is test event " + i;
			e.entry = "free";
			e.fbLink = "fb";
			e.vkLink = "vk";
			e.location = "loc";
			e.site = "site";
			e.price = "free";
			e.title = "Test " + i;
			testList.add(e);
		}

		putEventsToDataBase(testList);
	}

	private void putEventsToDataBase(ArrayList<Event> list) {
		for (Event e : list) {
			Uri uri = getContentResolver().insert(EventTable.CONTENT_URI,
					e.toContentValues());
			Log.i("DB", uri.toString());
		}
	}

	private void getEventByDate(String date) {

		Cursor cursor = getContentResolver().query(
				EventContract.EventTable.buildEventUriWithDate(date), null,
				null, null, null);
		Log.i("DB", EventContract.EventTable.buildEventUriWithDate(date)
				.toString() + "\ncount" + cursor.getCount());
	}

	private void getEventById(long id) {
		String[] selection = { "" };
		Uri uri = EventContract.EventTable.buildEventUri(id);
		Cursor cursor = getContentResolver().query(uri, null, "", selection,
				null);
		Log.i("DB", "count" + cursor.getCount());
	}
}
