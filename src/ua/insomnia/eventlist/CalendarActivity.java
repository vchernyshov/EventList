package ua.insomnia.eventlist;

import ua.insomnia.eventlist.adapters.CalendarAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CalendarActivity extends StateActivity implements  OnClickListener{
	
	public static final String ARG_DATE = "date";

	private Button btnPrevMonth;
	private Button btnNextMonth;
	private TextView monthTitle;
	private ViewPager viewPager;
	private CalendarAdapter adapter;
	//private CalendarAdapterV2 adapter;
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
		//adapter = new CalendarAdapterV2(this);
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
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
			}
		});

		btnNextMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
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
	public void onClick(View v) {
		Log.d(TAG, "onDateSelected. date = " + (String)v.getTag());
		Intent result = new Intent();
		result.putExtra(ARG_DATE, (String)v.getTag());
		setResult(RESULT_OK, result);
		finish();
	}
}
