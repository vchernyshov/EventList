package ua.insomnia.eventlist.fragments;

import ua.insomnia.eventlist.Event;
import ua.insomnia.eventlist.MainActivity;
import ua.insomnia.eventlist.R;
import ua.insomnia.eventlist.rest.Api;
import ua.insomnia.eventlist.rest.EventProviderContract.EventTable;
import ua.insomnia.eventlist.rest.Respone;
import ua.insomnia.eventlist.utils.ConnectionUtils;
import ua.insomnia.eventlist.utils.PictureUtils;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.app.ActionBar;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EventInfoFragment extends Fragment {

	private Long id = null;
	private Event e;
	LinearLayout infoBlock;
	View emptyView;
	View info;
	View view;
	SwipeRefreshLayout layout;
	boolean isUpdate = false;
	ConnectionUtils con;

	ImageLoader imageLoader;
	DisplayImageOptions options;

	public static Fragment newInstance(long paramId) {
		EventInfoFragment f = new EventInfoFragment();
		Bundle args = new Bundle();
		args.putLong("id", paramId);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		((MainActivity)getActivity()).isInfo = false;
		((MainActivity) getActivity()).invalidateOptionsMenu();
		PictureUtils
		.cleanImageView(((ImageView) view.findViewById(R.id.imLogo)));
		Log.i("INFO", "onPause");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.event_info_activity, null);

		con = new ConnectionUtils(getActivity());

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).resetViewBeforeLoading(false)
				.showImageOnLoading(null).build();
		// .showImageForEmptyUri(fallback)
		// .showImageOnFail(fallback)
		// .showImageOnLoading(fallback).build();

		Bundle extras = getArguments();
		id = extras.getLong("id");

		infoBlock = (LinearLayout) view.findViewById(R.id.info_child);
		emptyView = (RelativeLayout) view.findViewById(R.id.empty);
		layout = (SwipeRefreshLayout) view.findViewById(R.id.info);
		layout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		layout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				isUpdate = true;
				layout.setRefreshing(true);
				// new Load().execute();
				loadEvents();
			}

		});

		// new Load().execute();
		loadEvents();

		return view;
	}

	private void initInfoBlock(Event localEvent) {
		
		String newPrice = "";
		if (localEvent.price.equals("0"))
			newPrice = getResources().getString(R.string.free_event);
		
		String[] info = { localEvent.title, "", localEvent.description,
				localEvent.getDate(), localEvent.getTime(),
				localEvent.location, newPrice, localEvent.site };
		int [] localIcons = {R.drawable.ic_launcher, R.drawable.tag_ic, R.drawable.ic_launcher, R.drawable.calendar_ic, R.drawable.clock_ic, R.drawable.location_ic, R.drawable.money_ic, R.drawable.phone_ic};
		imageLoader.displayImage(e.image,
				((ImageView) view.findViewById(R.id.imLogo)));

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
						.setImageResource(localIcons[i]);
				((TextViewFonts) child.getChildAt(1)).setText(info[i]);
			}
		}
	}

	private void loadEvents() {
		String selection = EventTable._ID +" =?";
		String [] selectionArgs = {id.toString()}; 
		Cursor cursor = getActivity().getContentResolver().query(EventTable.CONTENT_URI, null, selection, selectionArgs, null);
		
		e = new Event(cursor);
		
		initInfoBlock(e);
		
		
		layout.setRefreshing(false);
		isUpdate = false;
		
		infoBlock.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
		/*if (con.isConnecting())
			new Load().execute();
		else
			Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();*/
	}

	private class Load extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*if (!isUpdate) {
				//infoBlock.setVisibility(View.GONE);
				//emptyView.setVisibility(View.VISIBLE);
			}*/
		}

		@Override
		protected Void doInBackground(Void... params) {
			Api api = new Api();
			Respone r = null;
			if (id != null) {
				r = api.getEventByIdR(id);
				e = r.getEvents().get(0);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			initInfoBlock(e);
			/*if (!isUpdate) {
				infoBlock.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
			}*/
			layout.setRefreshing(false);
			isUpdate = false;
			
			infoBlock.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			super.onPostExecute(result);

		}

	}

}
