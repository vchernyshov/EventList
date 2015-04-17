package ua.insomnia.eventlist.fragments;

import java.util.Calendar;

import ua.insomnia.CalendarView;
import ua.insomnia.eventlist.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class CalendarFragment extends Fragment {

	private static final String TAG = "CalendarFragment";
	private static final String ARG_LONG_DATE = "long_date";
	
	private Calendar calendar;
	private CalendarView calendarView;
	private OnDateSelectedListener listener;

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
		
		long l = extras.getLong(ARG_LONG_DATE);		
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(l);
				
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.calendar_fragment, container, false);
		calendarView = (CalendarView) view.findViewById(R.id.calendarView);
		calendarView.setSwipeEnable(false);
		calendarView.setCurrentCalendar(calendar);
		Log.d(TAG, "currentMonth = " + calendarView.getCurrentMonth());
		calendarView.setOnDayClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent result = new Intent();
				result.putExtra(ARG_DATE, (String)v.getTag());
				getActivity().setResult(getActivity().RESULT_OK, result);
			    getActivity().finish();*/
				
				Toast.makeText(getActivity(), (String) v.getTag(),
						Toast.LENGTH_SHORT).show();
				
				if (listener != null)
					listener.onDateSelected((String)v.getTag());
			}
		});
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		 try {
	            listener = (OnDateSelectedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnDateSelectedListener");
	        }
	}
	
	
	public interface OnDateSelectedListener {
		public void onDateSelected(String date);
	}
}
