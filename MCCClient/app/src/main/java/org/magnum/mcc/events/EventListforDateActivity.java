package org.magnum.mcc.events;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.magnum.mccmap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventListforDateActivity extends Activity {
	/** Used for debugging */
	private final String TAG = this.getClass().getSimpleName();

	// store the original data fetch from server
	private JSONArray array;
	private ListView listview;
	// store the text need to be displayed in the listview
	private List<String> eventname = new ArrayList<String>();
	private List<Event> eventlist = new ArrayList<Event>();
	
	private String queryURL;

	private ToggleButton buttonAll;
	private ToggleButton buttonNear;
	private ToggleButton buttonSoon;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlist_fordate);

		buttonNear = (ToggleButton) findViewById(R.id.toggleButton_nearme);
		buttonSoon = (ToggleButton) findViewById(R.id.toggleButton_soon);
		buttonAll = (ToggleButton) findViewById(R.id.toggleButton_all);
		listview = (ListView) this.findViewById(R.id.listview_eventlist_today);
		// All is set by default
		buttonAll.setChecked(true);

		Intent i = getIntent();
		final String day = i.getStringExtra("day");
		final String month = i.getStringExtra("month");
		final String year = i.getStringExtra("year");

		String server = "http://0-1-dot-mcc-backend.appspot.com";
		// String baseUrl = "/mcc/events/full-test-1/on/" + month + "/" + day
		//		+ "/" + year;
	 String baseUrl = "/mcc/events/full-test-1/on/5/9/2014";
		queryURL = server + baseUrl;

		 getEventListTask t = new getEventListTask();
		 t.execute(queryURL);

		final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(final RadioGroup radioGroup,
					final int i) {
				for (int j = 0; j < radioGroup.getChildCount(); j++) {
					final ToggleButton view = (ToggleButton) radioGroup
							.getChildAt(j);
					view.setChecked(view.getId() == i);
				
				}
			}
		};
		((RadioGroup) findViewById(R.id.toggleGroup))
				.setOnCheckedChangeListener(ToggleListener);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				Intent intent = new Intent(EventListforDateActivity.this,
						EventDetailFragment.class);
				try {
					Event t = eventlist.get(arg2);
					intent.putExtra("floorplanId", t.getFloorplanId());
					intent.putExtra("eventId", t.getId());
					intent.putExtra("endId", t.getFloorplanLocationId());
					intent.putExtra("day", t.getDay());
					intent.putExtra("month", t.getMonth());
					intent.putExtra("year", t.getYear());
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

	}

	public void onToggle(View view) {
		((RadioGroup) view.getParent()).check(view.getId());
		
		// app specific stuff ..
	}
	
	public void onToggleByTime(View view) {
		((RadioGroup) view.getParent()).check(view.getId());
		
		Collections.sort((eventlist), new EventCompare());
		eventname.clear();
		for(Event e:eventlist){
		   eventname.add(formatEventDescriptor(e));
		}
		
		listview.setAdapter(new ArrayAdapter<String>(
				EventListforDateActivity.this,
				android.R.layout.simple_list_item_1, eventname));
	}

	private class getEventListTask extends AsyncTask<String, Integer, Void> {
		List<Event> events = new ArrayList<Event>();

		@Override
		protected Void doInBackground(String... params) {
			try {
				URL url = new URL(queryURL);
				ObjectMapper mapper = new ObjectMapper();
				events = mapper.readValue(url,
						new TypeReference<List<Event>>() {
						});
			} catch (IOException e) {
				Log.d(TAG, "IO Exception while forming/reading URL");
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result);
			for (Event e : events) {			
				eventname.add(formatEventDescriptor(e));
				eventlist.add(e);
			}
		
		    listview.setAdapter(new ArrayAdapter<String>(
				EventListforDateActivity.this,
				android.R.layout.simple_list_item_1, eventname));
		}
	}

	
	private String formatEventDescriptor(Event e){
		return (e.getName() + "\n" + e.getYear() + "-" + e.getMonth() + "-" + e.getDay()
				+ " From " + time(e.getStartTime()) + " to "
				+ time(e.getEndTime()));
	}
	
	// change time to normal format
	public String time(String t) {
		int i = Integer.parseInt(t);
		int hour = i / 60;
		int min = i - 60 * hour;
		String h = String.valueOf(hour);
		String m = String.valueOf(min);
		if (min == 0)
			m = "00";
		if (min < 10 && min > 0)
			m = "0" + m;
		String time = h + ":" + m;
		return time;
	}

}
