package ua.insomnia.eventlist.adapters;

import java.util.List;

import ua.insomnia.eventlist.R;
import ua.insomnia.eventlist.model.Event;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class EventLargeAdapter extends ArrayAdapter<Event> {

	Context context;

	public EventLargeAdapter(Context context, int resource, List<Event> list) {
		super(context, resource, list);
		this.context = context;
	}

	private class ViewHolder {
		ImageView logo;
		ImageView likesIm;
		TextViewFonts time;
		TextViewFonts title;
		TextViewFonts likesCount;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Event e = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.event_list_large_item,
					null);
			holder.logo = (ImageView) convertView
					.findViewById(R.id.imEventLogo);
			holder.likesIm = (ImageView) convertView
					.findViewById(R.id.imEventLikes);
			holder.time = (TextViewFonts) convertView
					.findViewById(R.id.txtEventTime);
			holder.title = (TextViewFonts) convertView
					.findViewById(R.id.txtEventTitle);
			holder.likesCount = (TextViewFonts) convertView
					.findViewById(R.id.txtLikesCount);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.likesIm.setVisibility(View.INVISIBLE);
		holder.likesCount.setVisibility(View.INVISIBLE);
		holder.title.setText(e.title);
		Picasso.with(context).load(e.image).into(holder.logo);

		holder.time.setText(e.getTime());
		return convertView;
	}
}
