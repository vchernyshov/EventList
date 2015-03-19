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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment implements Receiver,
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "DetailEventFragmnet";
	private static final String SHARE_HASHTAG = "#EventList";
	public static final String ARG_ID = "id";

	private Long id = null;
	private LinearLayout infoBlock;
	private View view;
	private SwipeRefreshLayout layout;
	private ServiceResultsReceiver mReceiver;
	private Event event;

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
		setHasOptionsMenu(true);
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
		layout.setColorSchemeColors(Color.RED);

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
		MenuItem menuItem = menu.findItem(R.id.action_share);
		ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat
				.getActionProvider(menuItem);

		if (shareActionProvider != null) {
			// shareActionProvider.setShareHistoryFileName(null);
			shareActionProvider.setShareIntent(createShareForecastIntent());

		} else {
			Log.d(TAG, "Share Action Provider is null");
		}
	}

	private Intent createShareForecastIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, buildShareString(event));
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

				Log.d("Event", info[i]);
				if (TextUtils.isEmpty(info[i])) 
					child.setVisibility(View.GONE);

				((ImageView) child.getChildAt(0))
						.setImageResource(localIcons[i]);
				((TextViewFonts) child.getChildAt(1)).setText(info[i]);
			}
		}
	}

	private String buildShareString(Event eventParam) {
		if (eventParam != null) {
			StringBuilder builder = new StringBuilder();
			builder.append(SHARE_HASHTAG + "\n\n");
			builder.append(eventParam.title + "\n");
			builder.append(eventParam.getDate() + " " + eventParam.getTime()
					+ "\n");
			builder.append(eventParam.location + "\n");
			if (!eventParam.fbLink.isEmpty())
				builder.append(eventParam.fbLink + "\n");
			if (!eventParam.vkLink.isEmpty())
				builder.append(eventParam.vkLink + "\n");
			if (!eventParam.site.isEmpty())
				builder.append(eventParam.site + "\n");
			return builder.toString();
		} else 
			return "empty";
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(getActivity(),
				EventContract.EventTable.buildEventUri(id), null, null, null,
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		event = new Event(cursor);
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
