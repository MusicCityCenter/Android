/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.events;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
import org.magnum.mcc.nav.MapRouteActivity;



import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//Work Item 3
/**
* This activity should show basic event information and
* have a button that allows the user to launch the MapRouteActivity
* to see directions on how to get to the event.
* 
* @author jules
* @author weichen 
* @version 1.0
* 
*/


public class EventViewActivity extends Activity {

	private EventController eventController_;
	
	
	private String description;
	private String title;
	private TextView text1;
	private TextView text2;

	// asynctask to optimize network communication
	private class Downloadjson extends AsyncTask<String, Integer, String> {

		private String url_;
		private String floorplanId_;
		private String eventId_;
		private String endId_;

		@Override
		protected String doInBackground(String... params) {
			url_ = params[0];
			floorplanId_ = params[1];
			eventId_ = params[2];
			endId_ = params[3];

			try {
				StringBuilder sb = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = client.getParams();
				// set Timeout
				HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				HttpResponse response = client.execute(new HttpGet(url_));
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
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			// webbody = result;
			try {
				JSONArray array = new JSONArray(result);
				for (int k = 0; k < array.length(); k++) {
					JSONObject obj = array.getJSONObject(k);
					String id = obj.getString("id");
					String floorplanid = obj.getString("floorplanId");
					if (id.equals(eventId_) && floorplanid.equals(floorplanId_)) // id.equals(floorplanId)&&floorplanid.equals(floorplanId)
					{
						String eventname = "Name: " + obj.getString("name");
						String room = "Room: "
								+ obj.getString("floorplanLocationId");
						description = "Description: " + "\n"
								+ obj.getString("description");
						String day = obj.getString("day");
						String month = obj.getString("month");
						String year = obj.getString("year");
						String starttime = obj.getString("startTime");
						String endtime = obj.getString("endTime");
						String time = year + "-" + month + "-" + day + "\n"
								+ "From " + time(starttime) + " to "
								+ time(endtime) + "\n";
						title = eventname + "\n" + room + "\n" + time;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			text1.setText(title);
			text1.setGravity(Gravity.CENTER | Gravity.CENTER);

			text2.setText(description);
			text2.setGravity(Gravity.CENTER | Gravity.CENTER);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Do stuff to setup the UI
		setContentView(org.magnum.mccmap.R.layout.eventview);
		text1 = (TextView) this.findViewById(R.id.textView_title1);
		text2 = (TextView) this.findViewById(R.id.textView_description1);

		// Obtain the request path data
		Intent i = getIntent();
		final String floorplanId = i.getStringExtra("floorplanId");
		final String eventId = i.getStringExtra("eventId");
		final String endId = i.getStringExtra("endId");

		// This should probably be pulled from shared preferences
		// but can be hardcoded to the MCC appengine server for now
		String server = "http://0-1-dot-mcc-backend.appspot.com";
		int port = 80;
		String baseUrl = "/mcc/events/full-test-1/on/5/9/2014";
		String url = server + baseUrl;

		//eventController_= new EventControllerImpl(server, port, baseUrl);
		
		
		String[] parameter = { url, floorplanId, eventId, endId };
		// String[] parameter =
		// {url,"full-test-1","3157b10f-34be-4761-8dfa-c7bbf5444ffd", endId};

		Downloadjson task = new Downloadjson();
		task.execute(parameter);

		// button to jump to nav
		Button button = (Button) this
				.findViewById(org.magnum.mccmap.R.id.button_route1);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EventViewActivity.this,
						MapRouteActivity.class);
				intent.putExtra("floorplanId", floorplanId);
				intent.putExtra("startId", value); // value should be current
													// location
				intent.putExtra("endId", endId);
				startActivity(intent);
			}
		});
	}
}
