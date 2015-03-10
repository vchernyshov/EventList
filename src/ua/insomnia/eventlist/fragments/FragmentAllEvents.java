package ua.insomnia.eventlist.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ua.insomnia.eventlist.Event;
import ua.insomnia.eventlist.EventAdapter;
import ua.insomnia.eventlist.LoadMoreListView;
import ua.insomnia.eventlist.LoadMoreListView.OnLoadMoreListener;
import ua.insomnia.eventlist.MainActivity;
import ua.insomnia.eventlist.R;
import ua.insomnia.eventlist.rest.Api;
import ua.insomnia.eventlist.rest.Respone;
import ua.insomnia.eventlist.utils.ConnectionUtils;
import ua.insomnia.eventlist.utils.DateUtils;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

public class FragmentAllEvents extends Fragment {

	private static final String TAG = "AllEvents";
	private static final int LOAD_DAYS_AT_START = 10;
	private static final int LOAD_EVENTS_AT_START = 6;

	public static Fragment newInstance() {
		Log.i(TAG, "newInstance");
		FragmentAllEvents f = new FragmentAllEvents();
		return f;
	}

	ConnectionUtils con;
	LoadMoreListView listView;
	SwipeRefreshLayout layout;
	EventAdapter adapter;
	ArrayList<Event> currentList = new ArrayList<Event>();
	Map<String, ArrayList<Event>> map = new HashMap<String, ArrayList<Event>>();
	ArrayList<String> keys = new ArrayList<String>();

	int currentPage = 1;
	int totalDayCount = 0;
	int totalEventCount = 0;
	String currentDate = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i(TAG, "onCreateView");
		// setRetainInstance(true);
		con = new ConnectionUtils(getActivity());
		((MainActivity) getActivity()).invalidateOptionsMenu();

		currentDate = DateUtils.getCurrentDate();
		adapter = new EventAdapter(getActivity());

		View view = inflater.inflate(R.layout.base_fragment, null);
		listView = (LoadMoreListView) view.findViewById(R.id.listView);
		layout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

		layout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		layout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// new Load().execute();
				loadEvents();
			}

		});

		// new Load().execute();
		loadEvents();

		listView.setEmptyView(view.findViewById(R.id.emptyView));
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Event e = (Event) adapter.getItem(position);
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
		listView.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// new Load().execute();
				loadEvents();
			}
		});

		return view;
	}

	private void loadEvents() {
		if (con.isConnecting())
			new Load().execute();
		else
			Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
	}

	private class Load extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			map.clear();
			keys.clear();

			//while (totalEventCount < LOAD_EVENTS_AT_START) {
				Api api = new Api();
				Respone r = api.getEventByDateR(currentDate, currentPage);

				if (!r.getEvents().isEmpty()) {
					map.put(currentDate, r.getEvents());
					keys.add(currentDate);
				}

				totalEventCount = totalEventCount + r.getEvents().size();

				if (r.getNextPage() != null)
					currentPage = r.getNextPage();
				else {
					totalDayCount++;
					currentDate = DateUtils.getNextDate(currentDate);
					currentPage = 1;
				}
				
				//if (totalDayCount>5)
				//	break;
			//}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			for (int i = 0; i < map.size(); i++) {
				String localKey = keys.get(i);
				String dateToShow = DateUtils.getDateToShow(localKey);
				adapter.addSeparatorItem(dateToShow);
				ArrayList<Event> localList = new ArrayList<Event>();
				localList = map.get(localKey);
				for (Event e : localList)
					adapter.addItem(e);
			}
			layout.setRefreshing(false);
			adapter.notifyDataSetChanged();

			if (totalDayCount < LOAD_DAYS_AT_START)
				new Load().execute();
			else
				totalDayCount = 0;
			
			totalEventCount = 0;

			listView.onLoadMoreComplete();
		}
	}
}
