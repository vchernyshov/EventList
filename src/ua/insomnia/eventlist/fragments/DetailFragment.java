package ua.insomnia.eventlist.fragments;

import ua.insomnia.eventlist.R;
import ua.insomnia.eventlist.data.EventContract;
import ua.insomnia.eventlist.model.Event;
import ua.insomnia.eventlist.rest.EventService;
import ua.insomnia.eventlist.rest.ServiceResultsReceiver;
import ua.insomnia.eventlist.rest.ServiceResultsReceiver.Receiver;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment implements Receiver,
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "DetailEventFragmnet";
	public static final String ARG_ID = "id";

	private Long id = null;
	private LinearLayout infoBlock;
	private View view;
	private SwipeRefreshLayout layout;
	private ServiceResultsReceiver mReceiver;
	private String shareString = "empty";

	public static Fragment newInstance(long id) {
		Log.d(TAG, "new Instance with id " + id);
		Fragment fragment = new DetailFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_ID, id);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		id = extras.getLong(ARG_ID);

		mReceiver = new ServiceResultsReceiver(new Handler());
		mReceiver.setReceiver(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.detail_fragment, container, false);

		infoBlock = (LinearLayout) view.findViewById(R.id.info_child);
		layout = (SwipeRefreshLayout) view.findViewById(R.id.info);
		layout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		layout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				layout.setRefreshing(true);
				update();
			}

		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getLoaderManager().initLoader(0, null, this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.detail_fragment, menu);
		/*MenuItem menuItem = menu.findItem(R.id.menu_item_share);
		ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat
				.getActionProvider(menuItem);

		if (shareActionProvider != null) {
			shareActionProvider.setShareIntent(createShareForecastIntent());
		} else {
			Log.d(TAG, "Share Action Provider is null");
		}*/
	}

	private Intent createShareForecastIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareString);
		return shareIntent;
	}

	private void initInfoBlock(Event localEvent) {

		String newPrice = "";
		if (localEvent.price.equals("0"))
			newPrice = getResources().getString(R.string.free_event);
		else
			newPrice = localEvent.price;

		String[] info = { localEvent.title, "", localEvent.description,
				localEvent.getDate(), localEvent.getTime(),
				localEvent.location, newPrice, localEvent.site };
		int[] localIcons = { R.drawable.ic_launcher, R.drawable.tag_ic,
				R.drawable.ic_launcher, R.drawable.calendar_ic,
				R.drawable.clock_ic, R.drawable.location_ic,
				R.drawable.money_ic, R.drawable.phone_ic };
		Picasso.with(getActivity()).load(localEvent.image)
				.into((ImageView) view.findViewById(R.id.imLogo));
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
		
		shareString = localEvent.title+"\n"+localEvent.dateTime;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(getActivity(),
				EventContract.EventTable.buildEventUri(id), null, null, null,
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		Event event = new Event(cursor);
		initInfoBlock(event);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}

	private void update() {
		Intent service = new Intent(getActivity(), EventService.class);
		service.putExtra(EventService.EXTRA_RECEIVER, mReceiver);
		service.putExtra(EventService.EXTRA_ID, id);
		getActivity().startService(service);
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle data) {
		if (resultCode == EventService.SERVICE_LOAD_FINISHED)
			layout.setRefreshing(false);

		if (resultCode == EventService.SERVICE_LOAD_ERROR) {
			layout.setRefreshing(false);
			Toast.makeText(getActivity(), "Bad Internet Connection",
					Toast.LENGTH_SHORT).show();
		}
	}

}
