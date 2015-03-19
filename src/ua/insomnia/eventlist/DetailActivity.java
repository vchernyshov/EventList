package ua.insomnia.eventlist;

import ua.insomnia.eventlist.fragments.DetailFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

public class DetailActivity extends StateActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
		setContentView(R.layout.activity_detail);
		if (savedInstanceState == null) {	
			getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.detailContainer,
							DetailFragment.newInstance(getExtraId()))
					.commit();
		}

	}

	private long getExtraId() {
		return getIntent().getLongExtra(DetailFragment.ARG_ID, -1);
	}
	
	/*private void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.logo);
		actionBar.setTitle(null);
		actionBar.setDisplayHomeAsUpEnabled(true);

	}*/
	
	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setLogo(R.drawable.logo);
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
