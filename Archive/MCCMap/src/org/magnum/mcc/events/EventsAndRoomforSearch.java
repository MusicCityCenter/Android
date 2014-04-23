package org.magnum.mcc.events;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.magnum.mccmap.MainActivity;
import org.magnum.mccmap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class EventsAndRoomforSearch extends Activity {

	private ListView room;
	private ListView events;
	private String year;
	private String month;
	private String day;

	private String search;
	private String searchroom;
	private String searchevent;

	private List<String> searchrooms = new ArrayList<String>();
	private List<String> eventsname = new ArrayList<String>();
	private List<Event> evenstlist = new ArrayList<Event>();

	private Button back;

	// need to update the list according to database
	private String[] roomID = { "101", "102", "103", "104", "105", "106",
			"107", "108", "109", "110", "207", "Davidson Ballroom",
			"davidson ballroom" };

	// to check whether string is all digit number
	public boolean isDigit(String strNum) {
		return strNum.matches("[0-9]{1,}");
	}

	// set the date of today
	private void getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String s = sdf.format(date);
		year = s.substring(0, 4);
		if (s.substring(4).equals("0")) {
			month = s.substring(5);
		} else {
			month = s.substring(4, 6);
		}
		if (s.substring(6).equals("0")) {
			day = s.substring(7);
		} else {
			day = s.substring(6, 8);
		}
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

	private String formatEventDescriptor(Event e) {
		return (e.getName() + "\n" + e.getYear() + "-" + e.getMonth() + "-"
				+ e.getDay() + " From " + time(e.getStartTime()) + " to " + time(e
					.getEndTime()));
	}

	private Event jsontoevent(JSONObject obj) throws JSONException {
		Event e = new Event();
		e.setId(obj.getString("id"));
		e.setName(obj.getString("name"));
		e.setDescription(obj.getString("description"));
		e.setDay(obj.getString("day"));
		e.setMonth(obj.getString("month"));
		e.setYear(obj.getString("year"));
		e.setEndTime(obj.getString("endTime"));
		e.setStartTime(obj.getString("startTime"));
		e.setFloorplanId(obj.getString("floorplanId"));
		e.setFloorplanLocationId(obj.getString("floorplanLocationId"));
		return e;
	}

	private class Downloadjsonbyevent extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				Log.d(null, "start connect");
				StringBuilder sb = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = client.getParams();
				// set Timeout
				HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				HttpResponse response = client.execute(new HttpGet(params[0]));
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"),
							8192);

					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					reader.close();
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				Log.d(null, "connect ends");
				JSONArray array = new JSONArray(result);
				for (int i1 = 0; i1 < array.length(); i1++) {
					JSONObject obj = array.getJSONObject(i1);
					Event e = jsontoevent(obj);
					if (e.getName().toLowerCase()
							.contains(searchevent.toLowerCase())
							&& (!searchevent.equals(""))) {
						evenstlist.add(e);
						eventsname.add(formatEventDescriptor(e));
					}
				}
				if (eventsname.isEmpty()) {
					searchevent = "no relevant events";
					eventsname.add("no relevant events");
				}
			} catch (Exception e) {
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchresult);

		room = (ListView) this.findViewById(R.id.roomresult);
		events = (ListView) this.findViewById(R.id.eventresult);

		Log.d(null, "start!");
		Intent i = getIntent();
		search = i.getStringExtra("search");

		int port = 80;
		String server = "http://0-1-dot-mcc-backend.appspot.com";
		getToday();
		String baseUrl = "/mcc/events/full-test-1/on/" + month + "/" + day
				+ "/" + year;
		String url = server + baseUrl;

		if (Arrays.asList(roomID).contains(search)) {
			searchroom = search;
			searchevent = "no relevant events";
			searchrooms.add(search);
			eventsname.add("no relevant events");
		} else {
			searchroom = "no such room";
			searchevent = search;
			Downloadjsonbyevent task = new Downloadjsonbyevent();
			task.execute(url);
			searchrooms.add("no such room");
		}

		

		room.setAdapter(new ArrayAdapter<String>(EventsAndRoomforSearch.this,
				android.R.layout.simple_list_item_1, searchrooms));
		events.setAdapter(new ArrayAdapter<String>(EventsAndRoomforSearch.this,
				android.R.layout.simple_list_item_1, eventsname));

		room.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (!searchroom.equals("no such room")) {
					Intent i = new Intent(EventsAndRoomforSearch.this,
							EventListforRoomActivity.class);
					i.putExtra("day", day);
					i.putExtra("month", month);
					i.putExtra("year", year);
					i.putExtra("floorplanLocationId", searchroom);
					startActivity(i);
				}
			}

		});

		events.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (!searchevent.equals("no relevant events")) {
					Intent intent = new Intent(EventsAndRoomforSearch.this,
							EventDetailActivity.class);
					Event t = evenstlist.get(arg2);
					intent.putExtra("floorplanId", t.getFloorplanId());
					intent.putExtra("eventId", t.getId());
					intent.putExtra("endId", t.getFloorplanLocationId());
					intent.putExtra("day", t.getDay());
					intent.putExtra("month", t.getMonth());
					intent.putExtra("year", t.getYear());
					startActivity(intent);
				}
			}

		});

		/*
		back = (Button) this.findViewById(R.id.backbutton);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EventsAndRoomforSearch.this,
						MainActivity.class);
				startActivity(i);
			}

		});
		*/
	}

}
