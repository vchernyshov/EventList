package ua.insomnia.eventlist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ua.insomnia.eventlist.adapters.EventLargeAdapterWithSeparator;
import ua.insomnia.eventlist.adapters.FragmentAdapter;
import ua.insomnia.eventlist.data.EventContract;
import ua.insomnia.eventlist.fragments.DetailFragment;
import ua.insomnia.eventlist.model.Event;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends StateActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	public final String TAG = getClass().getSimpleName();

	private static final int LOADER_SEARCH_ID = 102;
	private static final String LOADER_QUERY = "loader_query";
	private static final String POSITION = "position";
	private static final int SCREEN_PAGE_LIMIT = 2;

	//private EventLargeCursorAdapter listViewAdapter;
	private LinearLayout layout;
	private ListView serchListView;
	private ViewPager viewPager;
	private FragmentAdapter adapter;
	private TextViewFonts dateView;
	private int miidlePosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setContentView(R.layout.activity_main);

		serchListView = (ListView) findViewById(R.id.searchListView);
		layout = (LinearLayout) findViewById(R.id.viewPagerContainer);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		dateView = (TextViewFonts) findViewById(R.id.txtTopDateView);

		//listViewAdapter = new EventLargeCursorAdapter(this, null, 0);
		adapter = new FragmentAdapter(getSupportFragmentManager());

		serchListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		/*
		 * serchListView.setAdapter(listViewAdapter);
		 * serchListView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> adapterView, View
		 * view, int position, long id) { Cursor cursor = (Cursor) adapterView
		 * .getItemAtPosition(position); int index =
		 * cursor.getColumnIndex(EventTable._ID); long eventId =
		 * cursor.getLong(index);
		 * 
		 * Intent detail = new Intent(MainActivity.this, DetailActivity.class);
		 * detail.putExtra(DetailFragment.ARG_ID, eventId);
		 * startActivity(detail);
		 * 
		 * } });
		 */
		serchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				Event event = (Event) adapterView.getItemAtPosition(position);

				Intent detail = new Intent(MainActivity.this,
						DetailActivity.class);
				detail.putExtra(DetailFragment.ARG_ID, event.id);
				startActivity(detail);

			}
		});
		viewPager.setAdapter(adapter);

		miidlePosition = adapter.getMiddlePosition();

		viewPager.setOffscreenPageLimit(SCREEN_PAGE_LIMIT);
		viewPager.setCurrentItem(miidlePosition);

		dateView.setText(adapter.getPageTitle(miidlePosition));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				dateView.setText(adapter.getPageTitle(position));
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

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				Log.d(TAG, "onQueryTextSubmit - " + query);

				InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				if (MainActivity.this.getCurrentFocus() != null)
					inputMethod.hideSoftInputFromWindow(MainActivity.this
							.getCurrentFocus().getWindowToken(), 0);

				return true;
			}

			@Override
			public boolean onQueryTextChange(String query) {
				Log.d(TAG, "onQueryTextChange - " + query);
				if (query.length() > 0) {
					layout.setVisibility(View.INVISIBLE);
					serchListView.setVisibility(View.VISIBLE);
					search(query);
				} else {
					layout.setVisibility(View.VISIBLE);
					serchListView.setVisibility(View.INVISIBLE);
				}
				return true;
			}
		});

		return true;
	}

	private void search(String query) {
		Bundle args = new Bundle();
		args.putString(LOADER_QUERY, query);
		getSupportLoaderManager().restartLoader(LOADER_SEARCH_ID, args, this);
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
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setLogo(R.drawable.logo);
	}

	@SuppressLint("DefaultLocale")
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		switch (id) {
		case LOADER_SEARCH_ID:
			String query = args.getString(LOADER_QUERY);

			String selection = EventContract.EventTable.COLUMN_TITLE_SEARCH
					+ " LIKE ? ";
			String[] selectionArgs = new String[] { "%" + query.toLowerCase()
					+ "%" };
			String orderBy = EventContract.EventTable.COLUMN_DATE + " DESC";
			return new CursorLoader(this, EventContract.EventTable.CONTENT_URI,
					null, selection, selectionArgs, orderBy);
		default:
			return null;
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// listViewAdapter.swapCursor(cursor);
		EventLargeAdapterWithSeparator listViewAdapterS = new EventLargeAdapterWithSeparator(
				this);
		boolean sep1 = false;
		boolean sep2 = false;
		
		Integer currentDate = getCurrentDate();
		
		String futureEvents = getResources().getString(R.string.future_events);
		String pastEvents = getResources().getString(R.string.past_events);
		
		while (cursor.moveToNext()) {
			Event event = new Event(cursor);
			Integer date = Integer.valueOf(event.getDate2());
			if (date >= currentDate) {
				if (!sep1)
					listViewAdapterS.addSeparatorItem(futureEvents);
				sep1 = true;
			} else {
				if (!sep2)
					listViewAdapterS.addSeparatorItem(pastEvents);
				sep2 = true;
			}
			listViewAdapterS.addItem(event);
		}

		serchListView.setAdapter(listViewAdapterS);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// listViewAdapter.swapCursor(null);

	}
	
	@SuppressLint("SimpleDateFormat")
	private Integer getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		return Integer.valueOf(dateFormat.format(date));
	}
}
