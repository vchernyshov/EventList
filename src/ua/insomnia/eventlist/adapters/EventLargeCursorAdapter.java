package ua.insomnia.eventlist.adapters;

import ua.insomnia.eventlist.R;
import ua.insomnia.eventlist.model.Event;
import ua.insomnia.textviewfonts.TextViewFonts;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class EventLargeCursorAdapter extends CursorAdapter {

	private LayoutInflater inflater;

	public EventLargeCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		Event event = new Event(cursor);

		holder.likesIm.setVisibility(View.INVISIBLE);
		holder.likesCount.setVisibility(View.INVISIBLE);
		holder.title.setText(event.title);
		Picasso.with(context).load(event.image).into(holder.logo);

		holder.time.setText(event.getTime());
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = inflater.inflate(R.layout.event_list_large_item, parent,
				false);
		ViewHolder holder = new ViewHolder(view);
		view.setTag(holder);
		return view;
	}
}
