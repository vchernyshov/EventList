package ua.insomnia.eventlist.widgets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ua.insomnia.eventlist.R;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class MyCalendar extends LinearLayout {
	
	//colors
	private int colorDayLessThanPresent = Color.GREEN;
	private int colorPresentDay = Color.RED;
	private int colorDayMoreThanPresent = Color.BLUE;

	private int firstDayOfMonth;
	private int currentDate;
	private int daysInMonth;
	private String[] days = { "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Нд" };

	public MyCalendar(Context context) {
		super(context);
		init();
	}

	public MyCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOrientation(LinearLayout.VERTICAL);
		buildGrid();
		findFirstDayOfMonth();
		findDaysInMonth();
		findCurentDateOfMonth();
		print();

	}

	private void buildGrid() {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < 6; i++)
			addViewInLayout(inflate(getContext(), R.layout.row, null), 0,
					params);
	}

	private void findFirstDayOfMonth() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		firstDayOfMonth = c.get(Calendar.DAY_OF_WEEK);
	}

	private void findDaysInMonth() {
		Calendar c = Calendar.getInstance();
		daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	private void findCurentDateOfMonth() {
		Calendar c = Calendar.getInstance();
		currentDate = c.get(Calendar.DAY_OF_MONTH);
	}

	private int getRealFirstDayOfMonth() {
		if (firstDayOfMonth == 1)
			return 6;
		if (firstDayOfMonth == 2)
			return 0;
		if (firstDayOfMonth == 3)
			return 1;
		if (firstDayOfMonth == 4)
			return 2;
		if (firstDayOfMonth == 5)
			return 3;
		if (firstDayOfMonth == 6)
			return 4;
		else
			return 5;
	}
	
	private String createTag(int day) {
		String tag = null;
		String format = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, day);
		tag = sdf.format(c.getTime());
		return tag;
	}

	private void print() {
		int valueToPrint = 1;
		int startIndex = getRealFirstDayOfMonth();

		LinearLayout txtDay = (LinearLayout) getChildAt(0);
		for (int j = 0; j < 7; j++) {
			TextViewFonts txt = (TextViewFonts) txtDay.getChildAt(j);
			txt.setAllCaps(true);
			txt.setText(days[j]);
		}

		for (int i = 1; i < 6; i++) {
			LinearLayout h = (LinearLayout) getChildAt(i);
			for (int j = startIndex; j < 7; j++) {
				TextViewFonts txt = (TextViewFonts) h.getChildAt(j);
				final String log = (String)txt.getTag();
				txt.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.i("MyCalendar", log);
					}
				});
				if (valueToPrint <= daysInMonth) {
					if (valueToPrint == currentDate)
						txt.setTextColor(colorPresentDay);
					if (valueToPrint > currentDate)
						txt.setTextColor(colorDayMoreThanPresent);
					if (valueToPrint < currentDate)
						txt.setTextColor(colorDayLessThanPresent);
					txt.setText("" + valueToPrint);
					
					String tag = createTag(valueToPrint);
					txt.setTag(tag);
					valueToPrint++;
				}
				startIndex = 0;
			}

		}
	}

}
