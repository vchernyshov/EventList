package ua.insomnia.eventlist.fragments;

import java.util.ArrayList;

import ua.insomnia.eventlist.Event;
import ua.insomnia.eventlist.EventLargeAdapter;
import ua.insomnia.eventlist.LoadMoreListView;
import ua.insomnia.eventlist.LoadMoreListView.OnLoadMoreListener;
import ua.insomnia.eventlist.MainActivity;
import ua.insomnia.eventlist.R;
import ua.insomnia.eventlist.rest.Api;
import ua.insomnia.eventlist.rest.EventProviderContract.EventTable;
import ua.insomnia.eventlist.rest.Respone;
import ua.insomnia.eventlist.utils.ConnectionUtils;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class BaseFragment extends Fragment {

	private static final String TAG = "BaseFragment";

	public static Fragment newInstance(String date) {
		Fragment f = new BaseFragment();
		Bundle args = new Bundle();
		args.putString("date", date);
		f.setArguments(args);
		return f;
	}

	ConnectionUtils con;

	LoadMoreListView listView;
	SwipeRefreshLayout layout;
	EventLargeAdapter adapter;
	View view;

	private String date;
	int currentPage = 1;
	Respone res;
	ArrayList<Event> oldList = new ArrayList<Event>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getArguments();
		date = extras.getString("date");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		// setRetainInstance(true);
		con = new ConnectionUtils(getActivity());
		view = inflater.inflate(R.layout.event_fragment_for_view_pager, null);

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
				// new Load().execute();
				loadEvents();
			}

		});

		listView.setEmptyView(view.findViewById(R.id.emptyView));

		listView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// new Load().execute();
				loadMoreEvents();
			}
		});

		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Event e = adapter.getItem(position);
				Uri uri = getActivity().getContentResolver().insert(EventTable.CONTENT_URI, e.toContentValues());
				Log.i(TAG, "uri - " + uri.toString());
				// Intent i = new Intent(getActivity(),
				// EventInfoActivity.class);
				// i.putExtra("id", e.id);
				// startActivity(i);

				FragmentTransaction tr = getActivity()
						.getSupportFragmentManager().beginTransaction();
				tr.replace(R.id.container, EventInfoFragment.newInstance(e.id));
				tr.addToBackStack(null);
				tr.commit();

				((MainActivity) getActivity()).isInfo = true;
				((MainActivity) getActivity()).invalidateOptionsMenu();
			}
		});

		adapter = new EventLargeAdapter(getActivity(),
				R.layout.event_list_large_item, new ArrayList<Event>());
		listView.setAdapter(adapter);
		// new Load().execute();
		loadEvents();

		return view;
	}

	private void loadEvents() {
		if (con.isConnecting())
			new Load().execute();
		else
			listView.setEmptyView(view.findViewById(R.id.noConnectionView));
	}

	private void loadMoreEvents() {
		if (con.isConnecting())
			new LoadMore().execute();
		else
			listView.setEmptyView(view.findViewById(R.id.noConnectionView));
	}

	private class Load extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Api api = new Api();
			res = api.getEventByDateR(date, currentPage);

			if (res.getNextPage() != null)
				currentPage = res.getNextPage();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!res.getEvents().isEmpty()) {
				oldList = res.getEvents();
				adapter.clear();
				adapter.addAll(res.getEvents());
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);
			} else {
				listView.setEmptyView(view.findViewById(R.id.noEventView));
			}

			layout.setRefreshing(false);
			super.onPostExecute(result);
		}

	}

	private class LoadMore extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			Api api = new Api();
			
			if (currentPage == 1)
				return null;
			else {
				res = api.getEventByDateR(date, currentPage);

				if (res.getNextPage() != null)
					currentPage = res.getNextPage();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!res.getEvents().isEmpty() && currentPage!=1) {
				adapter.addAll(res.getEvents());
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}

	}

}
