package ua.insomnia.eventlist.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ua.insomnia.eventlist.fragments.CalendarFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CalendarAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
	private ArrayList<String> titlesList = new ArrayList<String>();
	

	public CalendarAdapter(FragmentManager fm) {
		super(fm);

		Calendar c = Calendar.getInstance();
		CalendarFragment f;
		int month = c.get(Calendar.MONTH);
		
		c.set(Calendar.MONTH, (month - 2));
		f = (CalendarFragment) CalendarFragment.newInstance(c);
		fragmentList.add(getMiddlePosition()-2, f);
		titlesList.add(getMiddlePosition()-2, getCurrentMonth(c));
		
		c.set(Calendar.MONTH, (month - 1));
		f = (CalendarFragment) CalendarFragment.newInstance(c);
		fragmentList.add(getMiddlePosition()-1, f);
		titlesList.add(getMiddlePosition()-1, getCurrentMonth(c));
		
		c = Calendar.getInstance();
		f = (CalendarFragment) CalendarFragment.newInstance(c);
		fragmentList.add(2, CalendarFragment.newInstance(c));
		titlesList.add(getMiddlePosition(), getCurrentMonth(c));
		
		c.set(Calendar.MONTH, (month + 1));
		f = (CalendarFragment) CalendarFragment.newInstance(c);
		fragmentList.add(getMiddlePosition()+1, f);
		titlesList.add(getMiddlePosition()+1, getCurrentMonth(c));
		
		c.set(Calendar.MONTH, (month + 2));
		f = (CalendarFragment) CalendarFragment.newInstance(c);
		fragmentList.add(getMiddlePosition()+2, f);
		titlesList.add(getMiddlePosition()+2, getCurrentMonth(c));

	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titlesList.get(position);
	}

	public int getMiddlePosition() {
		return 2;
	}
	
	public String getCurrentMonth(Calendar c) {
		String format = "LLLL";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String month = sdf.format(c.getTime());
		return month.toUpperCase(Locale.getDefault());
	}

}
