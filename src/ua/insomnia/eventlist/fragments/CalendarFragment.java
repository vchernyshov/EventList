package ua.insomnia.eventlist.fragments;

import java.util.Calendar;

import ua.insomnia.CalendarView;
import ua.insomnia.eventlist.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class CalendarFragment extends Fragment {

	private static final String TAG = "CalendarFragment";
	private static final String ARG_LONG_DATE = "long_date";

	private Calendar calendar;
	private CalendarView calendarView;

	public static Fragment newInstance(Calendar c) {
		Log.d(TAG, "newInstance");
		Fragment fragment = new CalendarFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_LONG_DATE, c.getTimeInMillis());
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();

		long timeInMillis = extras.getLong(ARG_LONG_DATE);
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMillis);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.calendar_fragment, container,
				false);
		calendarView = (CalendarView) view.findViewById(R.id.calendarView);
		calendarView.setSwipeEnable(false);
		calendarView.setCurrentCalendar(calendar);
		calendarView.setOnDayClickListener((OnClickListener) getActivity());
		return view;
	}
}
