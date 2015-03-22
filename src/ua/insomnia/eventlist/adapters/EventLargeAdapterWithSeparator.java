package ua.insomnia.eventlist.adapters;

import java.util.ArrayList;
import java.util.TreeSet;

import ua.insomnia.eventlist.R;
import ua.insomnia.eventlist.model.Event;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class EventLargeAdapterWithSeparator extends BaseAdapter {

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
	
	private TreeSet<Integer> separatorSet = new TreeSet<Integer>();
	private ArrayList<Object> events = new ArrayList<Object>();

	private Context context;

	public EventLargeAdapterWithSeparator(Context context) {
		this.context = context;
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
		public final ImageView logo;
		public final ImageView likesIm;
		public final TextViewFonts time;
		public final TextViewFonts title;
		public final TextViewFonts likesCount;

		public ViewHolder(View view) {
			logo = (ImageView) view.findViewById(R.id.imEventLogo);
			likesIm = (ImageView) view.findViewById(R.id.imEventLikes);
			time = (TextViewFonts) view.findViewById(R.id.txtEventTime);
			title = (TextViewFonts) view.findViewById(R.id.txtEventTitle);
			likesCount = (TextViewFonts) view.findViewById(R.id.txtLikesCount);
		}
	}

	private static class ViewHolderS {
		public final TextViewFonts sep;

		public ViewHolderS(View view) {
			sep = (TextViewFonts) view.findViewById(R.id.txtDate);
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ViewHolderS holderS = null;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		int type = getItemViewType(position);
		if (type == TYPE_ITEM) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.event_list_large_item,
						parent, false);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			Event e = (Event) getItem(position);
			holder.likesIm.setVisibility(View.INVISIBLE);
			holder.likesCount.setVisibility(View.INVISIBLE);
			holder.title.setText(e.title);
			Picasso.with(context).load(e.image).into(holder.logo);
			holder.time.setText(e.getTime());
		} else {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.event_list_item_sep,
						parent, false);
				holderS = new ViewHolderS(convertView);
				convertView.setTag(holderS);
			} else
				holderS = (ViewHolderS) convertView.getTag();

			String s = (String) getItem(position);
			holderS.sep.setText(s);
		}
		return convertView;
	}

	@Override
	public boolean isEnabled(int position) {
		return (getItemViewType(position) == TYPE_ITEM) ? true : false;
	}
}
