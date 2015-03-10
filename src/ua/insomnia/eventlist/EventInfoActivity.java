package ua.insomnia.eventlist;

import ua.insomnia.eventlist.rest.Api;
import ua.insomnia.eventlist.rest.Respone;
import ua.insomnia.eventlist.utils.ConnectionUtils;
import ua.insomnia.eventlist.utils.PictureUtils;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EventInfoActivity extends FragmentActivity {

	private long id;
	private Event e;
	LinearLayout infoBlock;
	View emptyView;
	View info;
	SwipeRefreshLayout layout;
	boolean isUpdate = false;
	ConnectionUtils con;
	
	ImageLoader imageLoader;
	DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar a = getActionBar();
		a.setIcon(R.drawable.logo);
		a.setTitle(null);
		a.setHomeButtonEnabled(true);
		// a.setHomeAsUpIndicator(R.drawable.btnBack);
		setContentView(R.layout.event_info_activity);
		
		con = new ConnectionUtils(this);
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisk(true).resetViewBeforeLoading(true)
						.showImageOnLoading(null)
						.build();
						//.showImageForEmptyUri(fallback)
						//.showImageOnFail(fallback)
						//.showImageOnLoading(fallback).build();

		infoBlock = (LinearLayout) findViewById(R.id.info_child);
		emptyView = (RelativeLayout) findViewById(R.id.empty);
		layout = (SwipeRefreshLayout) findViewById(R.id.info);
		layout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		layout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				isUpdate = true;
				layout.setRefreshing(true);
				//new Load().execute();
				loadEvents();
			}

		});

		Bundle extras = getIntent().getExtras();
		id = extras.getLong("id");

		//new Load().execute();
		loadEvents();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(((ImageView)findViewById(R.id.imLogo)));
	}

	private void initInfoBlock(Event localEvent) {
		String[] info = { localEvent.title, "", localEvent.description,
				localEvent.getDate(), localEvent.getTime(),
				localEvent.location, localEvent.price, localEvent.site };
		imageLoader.displayImage(e.image, ((ImageView)findViewById(R.id.imLogo)));
		
		for (int i = 0; i < infoBlock.getChildCount(); i++) {
			if (i == 0)
				((TextViewFonts) infoBlock.getChildAt(i)).setText(info[i]);
			else if (i == 2)
				((TextViewFonts) infoBlock.getChildAt(i)).setText(info[i]);
			else {
				LinearLayout child = (LinearLayout) infoBlock.getChildAt(i);

				if (info[i].isEmpty())
					child.setVisibility(View.GONE);

				((ImageView) child.getChildAt(0))
						.setImageResource(R.drawable.ic_launcher);
				((TextViewFonts) child.getChildAt(1)).setText(info[i]);
			}
		}
	}
	
	private void loadEvents() {
		if (con.isConnecting())
			new Load().execute();
	}

	private class Load extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!isUpdate) {
				infoBlock.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			Api api = new Api();
			Respone r = api.getEventByIdR(id);
			e = r.getEvents().get(0);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			initInfoBlock(e);
			if (!isUpdate) {
				infoBlock.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
			}
			layout.setRefreshing(false);
			isUpdate = false;
			super.onPostExecute(result);

		}

	}
}
