package ua.insomnia.eventlist;

import ua.insomnia.eventlist.fragments.FragmentAllEvents;
import ua.insomnia.eventlist.fragments.FragmentAllEventsAtDay;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends FragmentActivity {

	FrameLayout container;
	FragmentManager mFragmentManager;
	FragmentTransaction fragmentTransaction;
	FragmentAllEventsAtDay atDay;
	FragmentAllEvents all;

	String TAG_1 = "FRAGMENT_1";
	String TAG_2 = "FRAGMENT_2";
	String TAG_3 = "TAG_3";

	boolean isFirst = true;
	public boolean isInfo = false;
	ActionBar a;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		a = getActionBar();
		a.setIcon(R.drawable.logo);
		a.setTitle(null);
		setContentView(R.layout.main_activity);
		
		atDay = new FragmentAllEventsAtDay();
		all = new FragmentAllEvents();

		mFragmentManager = getSupportFragmentManager();
		if (savedInstanceState == null) {
			fragmentTransaction = mFragmentManager.beginTransaction();

			fragmentTransaction.add(R.id.container, atDay, TAG_1);
			fragmentTransaction.commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		if (isInfo) {
			menu.setGroupVisible(R.id.group_grid, false);
			menu.setGroupVisible(R.id.group_list, false);
			return super.onCreateOptionsMenu(menu);
		}
		if (isFirst) {
			menu.setGroupVisible(R.id.group_grid, false);
			menu.setGroupVisible(R.id.group_list, true);
			return super.onCreateOptionsMenu(menu);
		} else{
			menu.setGroupVisible(R.id.group_grid, true);
			menu.setGroupVisible(R.id.group_list, false);
			return super.onCreateOptionsMenu(menu);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_show_events_list:
			replaceFr2();
			return true;
		case R.id.action_show_events_grid:
			replaceFr1();
			return true;
		case android.R.id.home:
			if (mFragmentManager.getBackStackEntryCount()>0) {
				mFragmentManager.popBackStack();
				a.setDisplayHomeAsUpEnabled(false);
				invalidateOptionsMenu();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void replaceFr1() {

		isFirst = true;

		/*
		 * fragmentTransaction = mFragmentManager.beginTransaction();
		 * fragmentTransaction.replace(R.id.container,
		 * FragmentAllEventsAtDay.newInstance()); fragmentTransaction.commit();
		 */

		FragmentAllEventsAtDay fr = (FragmentAllEventsAtDay) mFragmentManager
				.findFragmentByTag(TAG_1);
		if (fr == null) {
			fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container, atDay, TAG_1);
			//fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}

	private void replaceFr2() {

		isFirst = false;

		/*
		 * fragmentTransaction = mFragmentManager.beginTransaction();
		 * fragmentTransaction.replace(R.id.container,
		 * FragmentAllEvents.newInstance()); fragmentTransaction.commit();
		 */

		FragmentAllEvents fr = (FragmentAllEvents) mFragmentManager
				.findFragmentByTag(TAG_2);
		if (fr == null) {
			fragmentTransaction = mFragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container, all, TAG_2);
			//fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}
}
