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


import android.R;
import android.app.Activity;
import android.content.Intent;
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
	
	String description;
	String title;
	
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
	
	//to initialize two text
	private void initializeData(String url,String floorplanId,String eventId, String endId) throws Exception
	{
		
		//what about endId£¿ In the example, there is no endId at the list of JSON
		String body = getContent(url);
		JSONArray array = new JSONArray(body);
		for(int k = 0; k < array.length(); k++)
		{
			JSONObject obj = array.getJSONObject(k);
			String id=obj.getString("id");
			String floorplanid=obj.getString("floorplanId");
			if(id.equals(floorplanId)&&floorplanid.equals(floorplanId))
			{
				String eventname="Name: "+obj.getString("name");
				String room="Room: "+obj.getString("floorplanLocationId");
				description="Description: "+obj.getString("description");
				String day = obj.getString("day");
				String month = obj.getString("month");
				String year = obj.getString("year");
				String starttime = obj.getString("startTime");
				String endtime = obj.getString("endTime");
				String time= year + "-" + month + "-" + day+"\n" +"From "+time(starttime) + " to " + time(endtime) + "\n";
				title=eventname+"\n"+room+"\n"+time;
			}
		}
	}
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Do stuff to setup the UI
		setContentView(R.layout.eventview);
		
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
		
		
		initializeData(url,floorplanId,eventId,endId);
		
		TextView text1=(TextView)this.findViewById(R.id.textView2);
		text1.setText(title);
		text1.setGravity(Gravity.CENTER | Gravity.CENTER);
		//there can also be some other parameters to set for this text
		
		
		TextView text2=(TextView)this.findViewById(R.id.textView3);
		text2.setText(description);
		text2.setGravity(Gravity.CENTER | Gravity.CENTER);
		//there can also be some other parameters to set for this text
		
		
		
		
		// button to jump to nav
		Button button = (Button) this.findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EventViewActivity.this,
						MapRouteActivity.class);
				startActivity(intent);
				// if we need to put some data into this intent, add here.
			}
		});
	}
}
