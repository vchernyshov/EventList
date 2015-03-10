package ua.insomnia.eventlist.fragments;

import java.lang.reflect.Field;

import ua.insomnia.eventlist.FragmentAdapter;
import ua.insomnia.eventlist.MainActivity;
import ua.insomnia.eventlist.R;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentAllEventsAtDay extends Fragment {

	private static final String TAG = "AllEventsAtDay";

	ViewPager pager;
	FragmentAdapter adapter;
	TextViewFonts dateView;

	public static Fragment newInstance() {
		Log.i(TAG, "newInstance");
		FragmentAllEventsAtDay f = new FragmentAllEventsAtDay();
		return f;
	}

	public static Fragment newInstance(int position) {
		Log.i(TAG, "newInstance");
		FragmentAllEventsAtDay f = new FragmentAllEventsAtDay();
		Bundle args = new Bundle();
		args.putInt("position", position);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		setRetainInstance(true);
		((MainActivity) getActivity()).invalidateOptionsMenu();

		/*
		 * Bundle extras = getArguments(); if (extras.containsKey("position")) {
		 * positionFromExtras = extras.getInt("position"); }
		 */
		View v = inflater.inflate(R.layout.fr_all_events_at_day, null);
		
		

		dateView = (TextViewFonts) v.findViewById(R.id.txtTopDateView);
		pager = (ViewPager) v.findViewById(R.id.pager);

		adapter = new FragmentAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		int middlePosition = adapter.getMiddlePosition();

		//pager.setOffscreenPageLimit(3);
		pager.setCurrentItem(middlePosition);
		dateView.setText(adapter.getPageTitle(middlePosition));
		pager.setOnPageChangeListener(new OnPageChangeListener() {

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

		return v;
	}
}
