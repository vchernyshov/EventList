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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

public class EventLargeCursorAdapter extends CursorAdapter {

	private LayoutInflater inflater;
	private ImageLoader imageLoader;

	// private DisplayImageOptions options;

	public EventLargeCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//imageLoader = ImageLoader.getInstance();
		/*
		 * options = new DisplayImageOptions.Builder()
		 * .showImageOnLoading(R.drawable.bad_connection)
		 * .cacheInMemory(true).cacheOnDisk(true).build();
		 */
	}

	private static class ViewHolder {
		ImageView logo;
		ImageView likesIm;
		TextViewFonts time;
		TextViewFonts title;
		TextViewFonts likesCount;
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) convertView.getTag();
		Event e = new Event(cursor);

		holder.logo = (ImageView) convertView.findViewById(R.id.imEventLogo);
		holder.likesIm = (ImageView) convertView
				.findViewById(R.id.imEventLikes);
		holder.time = (TextViewFonts) convertView
				.findViewById(R.id.txtEventTime);
		holder.title = (TextViewFonts) convertView
				.findViewById(R.id.txtEventTitle);
		holder.likesCount = (TextViewFonts) convertView
				.findViewById(R.id.txtLikesCount);
		convertView.setTag(holder);

		holder.likesIm.setVisibility(View.INVISIBLE);
		holder.likesCount.setVisibility(View.INVISIBLE);
		holder.title.setText(e.title);
		Picasso.with(context).load(e.image).into(holder.logo);
		//imageLoader.displayImage(e.image, holder.logo);

		holder.time.setText(e.getTime());
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = inflater.inflate(R.layout.event_list_large_item, parent,
				false);
		ViewHolder holder = new ViewHolder();
		view.setTag(holder);
		return view;
	}

}
