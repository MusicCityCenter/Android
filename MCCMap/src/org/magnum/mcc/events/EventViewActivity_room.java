/* 
**
** Copyright 2014, Jules White
**
** 
*/
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
import org.json.JSONObject;
import org.magnum.mcc.nav.MapRouteActivity;



import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

// 
/**
 * This activity should show basic event information in a certain room and
 * have a button that allows the user to launch the MapRouteActivity
 * to see directions on how to get to the room.
 * 
 * 
 * @author weichen 
 * @version 1.0
 * 
 * there are two EventView in UI design, this one is built to show all events for a certain room 
 *
 */
public class EventViewActivity_room extends Activity {

	private EventController eventController_;
	
	//list to store JSON information
	private List<String> eventname = new ArrayList<String>();
	private List<List<String>> moreinfo = new ArrayList<List<String>>();
	private ExpandableListView expandListView;
	private JSONArray array;
	
	//a http connection to link webpage where JSON is stored
	private String getContent(String url) throws Exception {
		StringBuilder sb = new StringBuilder();

		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		// set Timeout
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpResponse response = client.execute(new HttpGet(url));
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"), 8192);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			reader.close();
		}
		return sb.toString();
	}
	
	
	private class Downloadjson extends AsyncTask<String, Integer, String>
	{

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
				//String body = getContent(url);
				array = new JSONArray(result);
				for (int k = 0; k < array.length(); k++) {
					JSONObject obj = array.getJSONObject(k);
					String name = obj.getString("name");
					String description = obj.getString("description");
					String day = obj.getString("day");
					String month = obj.getString("month");
					String year = obj.getString("year");
					String starttime = obj.getString("startTime");
					String endtime = obj.getString("endTime");

					eventname.add(name + "\n" + year + "-" + month + "-" + day);
					List<String> detail = new ArrayList<String>();
					detail.add("from " + time(starttime) + " to " + time(endtime) + "\n"
							+ "description: " + description);
					moreinfo.add(detail);
				}
			} catch (Exception e) {}
			
			ExpandableListAdapter adapter = new BaseExpandableListAdapter() {

				@Override
				public Object getChild(int groupPosition, int childPosition) {
					// TODO Auto-generated method stub
					return moreinfo.get(groupPosition).get(childPosition);
				}

				@Override
				public long getChildId(int groupPosition, int childPosition) {
					// TODO Auto-generated method stub
					return childPosition;
				}

				@Override
				public int getChildrenCount(int groupPosition) {
					// TODO Auto-generated method stub
					return moreinfo.get(groupPosition).size();
				}

				@Override
				public View getChildView(int groupPosition, int childPosition,
						boolean isLastChild, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub
					String string = moreinfo.get(groupPosition).get(childPosition);
					TextView textview = getTextViewchild(string);
					return textview;
				}

				public TextView getTextViewchild(String s) {
					// Layout parameters for the ExpandableListView
					AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
							ViewGroup.LayoutParams.FILL_PARENT, 100);

					TextView text = new TextView(EventViewActivity_room.this);
					text.setLayoutParams(lp);
					// Center the text vertically
					text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
					// Set the text starting position
					text.setPadding(36, 0, 0, 0);

					text.setText(s);
					return text;
				}

				@Override
				public Object getGroup(int groupPosition) {
					// TODO Auto-generated method stub
					return eventname.get(groupPosition);
				}

				@Override
				public int getGroupCount() {
					// TODO Auto-generated method stub
					return eventname.size();
				}

				@Override
				public long getGroupId(int groupPosition) {
					// TODO Auto-generated method stub
					return groupPosition;
				}

				@Override
				public View getGroupView(int groupPosition, boolean isExpanded,
						View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub
					String string = eventname.get(groupPosition);
					TextView textview = getTextViewgroup(string);
					//textview.setLayoutParams("");
					return textview;
				}

				public TextView getTextViewgroup(String s) {
					// Layout parameters for the ExpandableListView
					AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
							ViewGroup.LayoutParams.FILL_PARENT, 80);

					TextView text = new TextView(EventViewActivity_room.this);
					text.setLayoutParams(lp);
					// Center the text vertically
					text.setGravity(Gravity.CENTER | Gravity.CENTER);
					// Set the text starting position
					text.setPadding(36, 0, 0, 0);

					text.setText(s);
					return text;
				}

				@Override
				public boolean hasStableIds() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean isChildSelectable(int groupPosition,
						int childPosition) {
					// TODO Auto-generated method stub
					return false;
				}

			};
			
			expandListView.setAdapter(adapter);
		}
		
	}
	
	//change time to normal format
	public String time(String t)
		{
			 int i = Integer.parseInt(t);
			 int hour=i/60;
			 int min=i-60*hour;
			 String h=String.valueOf(hour);
			 String m=String.valueOf(min);
			 if(min==0)  m="00";
			 if(min<10&&min>0)  m="0"+m;
			 String time=h+":"+m;
			 return time;
			 
		}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// Do stuff to setup the UI
		setContentView(org.magnum.mccmap.R.layout.eventviewactivity);
		
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
		//just hardcoded right now, to be modified when server is done
	
		Downloadjson task = new Downloadjson();
		task.execute(url);
		
		
		//button to maproute
		Button button=(Button) this.findViewById(org.magnum.mccmap.R.id.button_route2);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(EventViewActivity_room.this,MapRouteActivity.class);
				startActivity(intent);
				//if we need to put some data into this intent, add here.
			}
		});
	}

}
