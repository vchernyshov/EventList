package ua.insomnia.eventlist.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ua.insomnia.CalendarView;
import ua.insomnia.eventlist.R;
import android.app.Activity;
import android.app.Service;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class CalendarAdapterV2 extends PagerAdapter {

	private ArrayList<Calendar> calendarList = new ArrayList<Calendar>();
	private ArrayList<String> titlesList = new ArrayList<String>();
	private LayoutInflater inflater;
	private Activity activity;

	public CalendarAdapterV2(Activity activity) {
		inflater = (LayoutInflater) activity
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		this.activity = activity;
		
		
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		
		c.set(Calendar.MONTH, (month - 2));
		calendarList.add(getMiddlePosition()-2, c);
		titlesList.add(getMiddlePosition()-2, getCurrentMonth(c));
		
		c.set(Calendar.MONTH, (month - 1));
		calendarList.add(getMiddlePosition()-1, c);
		titlesList.add(getMiddlePosition()-1, getCurrentMonth(c));
		
		c = Calendar.getInstance();
		calendarList.add(2, c);
		titlesList.add(getMiddlePosition(), getCurrentMonth(c));
		
		c.set(Calendar.MONTH, (month + 1));
		calendarList.add(getMiddlePosition()+1, c);
		titlesList.add(getMiddlePosition()+1, getCurrentMonth(c));
		
		c.set(Calendar.MONTH, (month + 2));
		calendarList.add(getMiddlePosition()+2, c);
		titlesList.add(getMiddlePosition()+2, getCurrentMonth(c));
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View layout = inflater.inflate(R.layout.calendar_fragment, view, false);

		CalendarView calendarView = (CalendarView) layout
				.findViewById(R.id.calendarView);

		calendarView.setSwipeEnable(false);
		calendarView.setCurrentCalendar(calendarList.get(position));
		calendarView.setOnDayClickListener((OnClickListener) activity);

		view.addView(layout, 0);
		return layout;
	}

	@Override
	public int getCount() {
		return calendarList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
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
