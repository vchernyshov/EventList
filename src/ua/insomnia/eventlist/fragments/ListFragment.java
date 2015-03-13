package ua.insomnia.eventlist.fragments;

import ua.insomnia.eventlist.DetailActivity;
import ua.insomnia.eventlist.R;
import ua.insomnia.eventlist.adapters.EventLargeCursorAdapter;
import ua.insomnia.eventlist.data.EventContract;
import ua.insomnia.eventlist.data.EventContract.EventTable;
import ua.insomnia.eventlist.rest.AppResultsReceiver;
import ua.insomnia.eventlist.rest.AppResultsReceiver.Receiver;
import ua.insomnia.eventlist.rest.EventService;
import ua.insomnia.eventlist.utils.ConnectionUtils;
import ua.insomnia.eventlist.widgets.LoadMoreListView;
import ua.insomnia.eventlist.widgets.LoadMoreListView.OnLoadMoreListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListFragment extends Fragment implements Receiver, LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "ListFragment";
	private static final String ARG_DATE = "date";
	private AppResultsReceiver mReceiver;
	private LoadMoreListView listView;
	private SwipeRefreshLayout layout;
	private EventLargeCursorAdapter adapter;
	private String date;
	int currentPage = 1;

	public static Fragment newInstance(String date) {
		Log.d(TAG, "newInstance with date " + date);
		Fragment fragment = new ListFragment();
		Bundle args = new Bundle();
		args.putString(ARG_DATE, date);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		date = extras.getString(ARG_DATE);

		mReceiver = new AppResultsReceiver(new Handler());
		mReceiver.setReceiver(this);

		Intent service = new Intent(getActivity(), EventService.class);
		service.putExtra(EventService.EXTRA_RECEIVER, mReceiver);
		service.putExtra(EventService.EXTRA_DATE, date);
		getActivity().startService(service);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.event_fragment_for_view_pager,
				null);

		listView = (LoadMoreListView) view.findViewById(R.id.listView);
		layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		layout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		layout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				layout.setRefreshing(true);
				
				Intent service = new Intent(getActivity(), EventService.class);
				service.putExtra(EventService.EXTRA_RECEIVER, mReceiver);
				service.putExtra(EventService.EXTRA_DATE, date);
				getActivity().startService(service);
			}

		});

		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		adapter = new EventLargeCursorAdapter(
				getActivity(), null, 0);
		listView.setAdapter(adapter);
		
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				Intent service = new Intent(getActivity(), EventService.class);
				service.putExtra(EventService.EXTRA_RECEIVER, mReceiver);
				service.putExtra(EventService.EXTRA_DATE, date);
				getActivity().startService(service);
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
				int index = cursor.getColumnIndex(EventTable._ID);
				long eventId = cursor.getLong(index);
				
				Intent detail = new Intent(getActivity(), DetailActivity.class);
				detail.putExtra(DetailFragment.ARG_ID, eventId);
				getActivity().startActivity(detail);
				
			}
		});
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle data) {
		if (resultCode == EventService.SERVICE_LOAD_FINISHED) {
			//Cursor cursor = getActivity().getContentResolver().query(
			//		EventContract.EventTable.buildEventUriWithDate(date), null,
			//		null, null, null);
			//adapter.changeCursor(cursor);
			layout.setRefreshing(false);
			listView.onLoadMoreComplete();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mReceiver.setReceiver(null);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(getActivity(), EventContract.EventTable.buildEventUriWithDate(date), null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);

	}

}
