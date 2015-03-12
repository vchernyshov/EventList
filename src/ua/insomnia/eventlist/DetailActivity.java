package ua.insomnia.eventlist;

import ua.insomnia.eventlist.fragments.DetailEventFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class DetailActivity extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
}
