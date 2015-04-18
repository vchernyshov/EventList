package ua.insomnia.eventlist;

import java.util.Calendar;

import ua.insomnia.CalendarView;
import ua.insomnia.OnMonthChange;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CalendarActivityV2 extends StateActivity {

	public static final String ARG_DATE = "date";

	private Button btnPrevMonth;
	private Button btnNextMonth;
	private TextView monthTitle;
	private CalendarView calendarView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setContentView(R.layout.activity_calendar_v2);

		btnPrevMonth = (Button) findViewById(R.id.btnPrevMonth);
		btnNextMonth = (Button) findViewById(R.id.btnNextMonth);
		monthTitle = (TextView) findViewById(R.id.txtMonthTitle);

		calendarView = (CalendarView) findViewById(R.id.calendarView);

		monthTitle.setText(calendarView.getCurrentMonth());

		btnPrevMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar c = (Calendar) calendarView.getPrevioseCalendar()
						.clone();
				calendarView.setCurrentCalendar(c);
				monthTitle.setText(calendarView.getCurrentMonth());
			}
		});

		btnNextMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Calendar c = (Calendar) calendarView.getNextCalendar().clone();
				calendarView.setCurrentCalendar(c);
				monthTitle.setText(calendarView.getCurrentMonth());
			}
		});

		calendarView.setOnDayClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "onDateSelected. date = " + (String) v.getTag());
				Intent result = new Intent();
				result.putExtra(ARG_DATE, (String) v.getTag());
				setResult(RESULT_OK, result);
				finish();
			}
		});

		calendarView.setOnMonthListener(new OnMonthChange() {

			@Override
			public void onChange(String month) {
				monthTitle.setText(month);
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
}
