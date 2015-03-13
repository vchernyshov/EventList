package ua.insomnia.eventlist.adapters;

import java.util.ArrayList;
import java.util.TreeSet;

import ua.insomnia.eventlist.R;
import ua.insomnia.eventlist.model.Event;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class EventAdapter extends BaseAdapter{
	
	//private static final String TAG = "EventAdapter";
	
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
	
	Context context;

	private ArrayList<Object> events = new ArrayList<Object>();
	private LayoutInflater inflater;

	private TreeSet<Integer> separatorSet = new TreeSet<Integer>();

	public EventAdapter(Context c) {
		this.inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		context = c;
	
	}

	public void addItem(final Object e) {
		this.events.add(e);
		notifyDataSetChanged();
	}

	public void addSeparatorItem(final Object e) {
		this.events.add(e);
		this.separatorSet.add(events.size() - 1);
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		return separatorSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	public int getCount() {
		return events.size();
	}

	public Object getItem(int position) {
		return events.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	private static class ViewHolder {
		TextViewFonts txt;
		TextViewFonts time;
		TextViewFonts title;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			holder = new ViewHolder();
			if (type == TYPE_ITEM) {
				//Log.d(TAG, "inflate event item at position "+position);
				convertView = inflater.inflate(R.layout.event_list_item, parent, false);
				holder.time = (TextViewFonts) convertView.findViewById(R.id.txtEventTime);
				holder.title = (TextViewFonts) convertView.findViewById(R.id.txtEventTitle);
			} else {
				//Log.d(TAG, "inflate date item");
				convertView = inflater.inflate(R.layout.event_list_item_sep, parent, false);
				holder.txt = (TextViewFonts) convertView.findViewById(R.id.txtDate);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (type == TYPE_ITEM) {
			//Log.d(TAG, "set data for event item at position "+position);
			Event e = (Event) getItem(position);
			holder.time.setText(e.getTime());
			holder.title.setText(e.title);
		} else {
			//Log.d(TAG, "set date");
			String s = (String) getItem(position);
			holder.txt.setText(s);
		}
		
		return convertView;
	}
	
	@Override
	public boolean isEnabled(int position) {
		int type = getItemViewType(position);
		if (type == TYPE_ITEM)
			return true;
		else 
			return false;
	}
	
}
