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

	private Context context;

	public EventLargeAdapter(Context context, int resource, List<Event> list) {
		super(context, resource, list);
		this.context = context;
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

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Event e = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.event_list_large_item,
					parent, false);
			holder = new ViewHolder(convertView);
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
