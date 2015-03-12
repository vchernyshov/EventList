package ua.insomnia.eventlist;

import ua.insomnia.eventlist.fragments.DetailEventFragment;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class DetailActivity extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		initActionBar();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.detailContainer,
							DetailEventFragment.newInstance(getExtraId()))
					.commit();
		}

	}

	private long getExtraId() {
		return getIntent().getLongExtra(DetailEventFragment.ARG_ID, -1);
	}
	
	private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.logo);
		actionBar.setTitle(null);
		actionBar.setDisplayHomeAsUpEnabled(true);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
