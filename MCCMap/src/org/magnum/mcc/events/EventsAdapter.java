package org.magnum.mcc.events;

import java.util.List;

import org.magnum.mccmap.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * This class defines how to take Event objects and represent them in rows
 * of a ListView
 * 
 * @author austin
 *
 */
public class EventsAdapter extends ArrayAdapter<Event> {
	
	private LayoutInflater inflater;
	private List<Event> events;
	
	public EventsAdapter(Context context, int resource, List<Event> evts) {
		super(context, resource, evts);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout rowParent = (LinearLayout) inflater.inflate(R.layout.row, parent);
		
		TextView nameView = (TextView) rowParent.findViewById(R.id.eventrow_name);
		TextView descriptionView = (TextView) rowParent.findViewById(R.id.eventrow_description);
		TextView timeView = (TextView) rowParent.findViewById(R.id.eventrow_time);
		
		
		Event evt = events.get(position);
		nameView.setText(evt.getName());
		descriptionView.setText(evt.getDescription());
		timeView.setText(evt.getStartTime() + "-" + evt.getEndTime());
		
		return rowParent;
	}

}
