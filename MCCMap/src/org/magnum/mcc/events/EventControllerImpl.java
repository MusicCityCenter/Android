package org.magnum.mcc.events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventControllerImpl implements EventController {
	/** Used for debugging */
    private final String TAG = this.getClass().getSimpleName();
	
	/** Server host name */
	private String host_;
	
	/** Base URL from which relevant end-points can be accessed */
	private String urlBase_;
	
	/** The port on which the server is communicating */
	private int port_;
	
	private Context ctx_;
	private List<Event> events_;
	
	//private WeakReference<EventViewActivity> activity_;
	private String content;
	
	public EventControllerImpl(String host, int port, String urlBase) {
			 host_ = host;
			 port_ = port;
			 urlBase_ = urlBase;
			 //activity_ = new WeakReference<EventViewActivity>(activity);
	}
	
	/** This method set the host information that is used to build the HTTP requests
	    sent below.
	*/
	@Override
	public void setServer(String host, int port, String urlBase) {
		 host_ = host;
		 port_ = port;
		 urlBase_ = urlBase;
	}

	/**
	 * Retrieves a list of events from the server that take place on the
	 * specified day. Usually, the client should use the current day/time
	 * to populate these values.
	 * 
	 * @param month
	 * @param day
	 * @param l
	 */
	@Override
	public void getEventsOnDay(int month, int day, int year, EventsListener l) {		
		
		String path = host_ + "/mcc/events/full-test-1/on/" + month + "/" + day + "/"+ year;
//		GetEventsAsyncTask getEvtsTask = new GetEventsAsyncTask(l);
//        getEvtsTask.execute(path);
		
		if(events_ != null) {
			l.setEvents(events_);
			Log.d(TAG, "events size:"+ events_.size());
		}
		
	}
	
	/** The AsyncTask responsible for retrieving the set of events */
	private class GetEventsAsyncTask extends AsyncTask<String, Integer, String> {		

		
		@Override
		protected String doInBackground(String... path) {			
			//List<Event> events = new ArrayList<Event>();
			
			/*
			try {
				URL url = new URL(path[0]);
				ObjectMapper mapper = new ObjectMapper();
				events = mapper.readValue(url, new TypeReference<List<Event>>(){});
				//Log.d(TAG, "event from server:"+ events.get(0).getDescription());
			} 
			catch (IOException e) {
				Log.d(TAG, "IO Exception while forming/reading URL");
			}

			return events;
			*/
			
			try {
				StringBuilder sb = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = client.getParams();
				// set Timeout
				HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				HttpResponse response = client.execute(new HttpGet(path[0]));
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
			
			content = result;
			if(content!=null){
				setEventlist(content);
			}
			
			/*
			if(events != null) {
				events_ = events;
				Log.d(TAG, "Successfully loaded navigation data!");
				evtListener.setEvents(events_);
			}
			else {
				Log.d(TAG, "Error loading navigation data");
				evtListener.setEvents(new ArrayList<Event>());
			}
			*/
		}	
	} 
	
	
	private void setEventlist(String content){
		try {
			JSONArray array = new JSONArray(content);
			for (int k = 0; k < array.length(); k++) {
				Event oneevent = new Event();
				JSONObject obj = array.getJSONObject(k);
				String id = obj.getString("id");
				String floorplanid = obj.getString("floorplanId");
				String name = obj.getString("name");
				String description = obj.getString("description");
				String day = obj.getString("day");
				String month = obj.getString("month");
				String year = obj.getString("year");
				String starttime = obj.getString("startTime");
				String endtime = obj.getString("endTime");
				String floorplanLocationId = obj.getString("floorplanLocationId");
				
				oneevent.setDay(day);
				oneevent.setDescription(description);
				oneevent.setEndTime(time(endtime));
				oneevent.setFloorplanId(floorplanid);
				oneevent.setFloorplanLocationId(floorplanLocationId);
				oneevent.setId(id);
				oneevent.setMonth(month);
				oneevent.setName(name);
				oneevent.setStartTime(time(starttime));
				oneevent.setYear(year);
				
				events_.add(oneevent);
			}
		} catch (Exception e) {}
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
