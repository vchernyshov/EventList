package ua.insomnia.eventlist;

import ua.insomnia.eventlist.adapters.FragmentAdapter;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class MainActivity extends StateActivity {

	public final String TAG = getClass().getSimpleName();

	private static final String POSITION = "position";
	private static final int SCREEN_PAGE_LIMIT = 2;

	private ViewPager viewPager;
	private FragmentAdapter adapter;
	private TextViewFonts dateView;
	private int miidlePosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//initActionBar();
		setContentView(R.layout.activity_main);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		dateView = (TextViewFonts) findViewById(R.id.txtTopDateView);

		adapter = new FragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		miidlePosition = adapter.getMiddlePosition();

		viewPager.setOffscreenPageLimit(SCREEN_PAGE_LIMIT);
		viewPager.setCurrentItem(miidlePosition);

		dateView.setText(adapter.getPageTitle(miidlePosition));
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
	public void onSaveState() {
		super.onSaveState();
		if (viewPager != null)
			saveInt(POSITION, viewPager.getCurrentItem());
	}

	@Override
	public void onRestoreState() {
		super.onRestoreState();

		int position = restoreInt(POSITION, miidlePosition);
		if (viewPager != null)
			viewPager.setCurrentItem(position);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		saveInt(POSITION, miidlePosition);
		Log.d(TAG, "onDestroy");
	}

	private void saveInt(String key, int value) {
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	private int restoreInt(String key, int defaultValue) {
		SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
		return settings.getInt(key, defaultValue);
	}

	/*private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.logo);
		actionBar.setTitle(null);
	}*/
}
