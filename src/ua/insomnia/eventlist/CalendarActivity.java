package ua.insomnia.eventlist;

import ua.insomnia.eventlist.adapters.CalendarAdapter;
import ua.insomnia.eventlist.fragments.CalendarFragment.OnDateSelectedListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CalendarActivity extends StateActivity implements OnDateSelectedListener{
	
	public static final String ARG_DATE = "date";

	private Button btnPrevMonth;
	private Button btnNextMonth;
	private TextView monthTitle;
	private ViewPager viewPager;
	private CalendarAdapter adapter;
	private int middlePosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setContentView(R.layout.activity_calendar);

		viewPager = (ViewPager) findViewById(R.id.calendarViewPager);
		btnPrevMonth = (Button) findViewById(R.id.btnPrevMonth);
		btnNextMonth = (Button) findViewById(R.id.btnNextMonth);
		monthTitle = (TextView) findViewById(R.id.txtMonthTitle);

		adapter = new CalendarAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		middlePosition = adapter.getMiddlePosition();
		viewPager.setOffscreenPageLimit(1);
		viewPager.setCurrentItem(middlePosition);

		monthTitle.setText(adapter.getPageTitle(middlePosition));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				monthTitle.setText(adapter.getPageTitle(position));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		btnPrevMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
			}
		});

		btnNextMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			}
		});
	}

	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setLogo(R.drawable.logo);
	}

	@Override
	public void onDateSelected(String date) {
		Log.d(TAG, "onDateSelected. date = " + date);
		Intent result = new Intent();
		result.putExtra(ARG_DATE, date);
		setResult(RESULT_OK, result);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				finish();
			}
		}, 500);
		
	}
}
