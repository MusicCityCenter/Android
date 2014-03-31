package org.magnum.mcc.events;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import org.magnum.mccmap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class EventListforDateActivity extends Activity {
	/** Used for debugging */
	private final String TAG = this.getClass().getSimpleName();

	// store the original data fetch from server
	private JSONArray array;
	private ListView listview;
	// store the text need to be displayed in the listview
	private List<String> eventname = new ArrayList<String>();
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
		String baseUrl = "/mcc/events/full-test-1/on/" + month + "/" + day
				+ "/" + year;
		// String baseUrl = "/mcc/events/full-test-1/on/5/9/2014";
		queryURL = server + baseUrl;

		 Downloadjson task = new Downloadjson();
		 task.execute(queryURL);
		

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
				// TODO Auto-generated method stub
				Intent intent = new Intent(EventListforDateActivity.this,
						EventDetailActivity.class);
				try {
					intent.putExtra("floorplanId", array.getJSONObject(arg2)
							.getString("floorplanId"));
					intent.putExtra("eventId", array.getJSONObject(arg2)
							.getString("id"));
					intent.putExtra("endId", "null");
					intent.putExtra("day", day);
					intent.putExtra("month", month);
					intent.putExtra("year", year);
					startActivity(intent);
				} catch (JSONException e) {
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

	/*private class getEventListTask extends AsyncTask<String, Integer, Void> {
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
				String name = e.getName();
				String description = e.getDescription();
				String day = e.getDay();
				String month = e.getMonth();
				String year = e.getYear();
				String starttime = e.getStartTime();
				String endtime = e.getEndTime();
				eventname.add(name + "\n" + year + "-" + month + "-" + day
						+ " From " + time(starttime) + " to "
						+ time(endtime));
			}
		
		    listview.setAdapter(new ArrayAdapter<String>(
				EventListforDateActivity.this,
				android.R.layout.simple_list_item_1, eventname));
		}
	}*/
		
	

	private class Downloadjson extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
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
				//
				// String body=getContent(url);
				array = new JSONArray(result);

				for (int i1 = 0; i1 < array.length(); i1++) {
					JSONObject obj = array.getJSONObject(i1);
					String name = obj.getString("name");
					String description = obj.getString("description");
					String day = obj.getString("day");
					String month = obj.getString("month");
					String year = obj.getString("year");
					String starttime = obj.getString("startTime");
					String endtime = obj.getString("endTime");
					eventname.add(name + "\n" + year + "-" + month + "-" + day
							+ " From " + time(starttime) + " to "
							+ time(endtime));
				}
			} catch (Exception e) {
			}
			listview.setAdapter(new ArrayAdapter<String>(
					EventListforDateActivity.this,
					android.R.layout.simple_list_item_1, eventname));
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

}
