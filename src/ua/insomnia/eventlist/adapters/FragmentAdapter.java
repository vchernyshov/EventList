package ua.insomnia.eventlist.adapters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ua.insomnia.eventlist.fragments.ListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class FragmentAdapter extends FixedFragmentStatePagerAdapter {
	
	private static final String TAG = "FragmentAdapter";

	private String[] dates = new String[201];
	private String[] datesToShow = new String[201];

	public FragmentAdapter(FragmentManager fm) {
		super(fm);

		String format = "yyyy-MM-dd";
		String formatToShow = "d MMMM (EE)";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		SimpleDateFormat sdf2 = new SimpleDateFormat(formatToShow,
				Locale.getDefault());
		final Calendar c = Calendar.getInstance();

		c.add(Calendar.DAY_OF_YEAR, 0);
		dates[100] = sdf.format(c.getTime());
		datesToShow[100] = sdf2.format(c.getTime());

		for (int i = 0; i < 100; i++) {
			Calendar less = Calendar.getInstance();
			less.add(Calendar.DAY_OF_YEAR, -(100 - i));
			dates[i] = sdf.format(less.getTime());
			datesToShow[i] = sdf2.format(less.getTime());
		}
		int j = 1;
		for (int i = 101; i < dates.length; i++) {

			Calendar more = Calendar.getInstance();
			more.add(Calendar.DAY_OF_YEAR, j);
			dates[i] = sdf.format(more.getTime());
			datesToShow[i] = sdf2.format(more.getTime());
			j++;
		}
	}

	@Override
	public Fragment getItem(int position) {
		return ListFragment.newInstance(dates[position]);
	}

	@Override
	public int getCount() {
		return (Integer.MAX_VALUE);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return datesToShow[position];
	}

	public int getMiddlePosition() {
		return 100;
	}

	public int getPositionByDate(String date) {
		for (int i = 0; i < dates.length; i++) {
			if (date.equals(dates[i])) {
				Log.d(TAG, "new position = " + i);
				return i;
			}
		}
		Log.d(TAG, "new position = middlePosition");
		return getMiddlePosition();
	}

}
