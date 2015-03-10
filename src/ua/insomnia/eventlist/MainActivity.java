package ua.insomnia.eventlist;

import ua.insomnia.textviewfonts.TextViewFonts;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends FragmentActivity {

	private static final int SCREEN_PAGE_LIMIT = 5;

	private ViewPager viewPager;
	private FragmentAdapter adapter;
	private TextViewFonts dateView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		dateView = (TextViewFonts) findViewById(R.id.txtTopDateView);

		adapter = new FragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		int middlePosition = adapter.getMiddlePosition();

		viewPager.setOffscreenPageLimit(SCREEN_PAGE_LIMIT);
		viewPager.setCurrentItem(middlePosition);
		dateView.setText(adapter.getPageTitle(middlePosition));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.logo);
		actionBar.setTitle(null);
	}
}
