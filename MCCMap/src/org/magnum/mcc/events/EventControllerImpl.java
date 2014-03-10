package org.magnum.mcc.events;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EventControllerImpl implements EventController {
	/** Used for debugging */
    private final String TAG = this.getClass().getSimpleName();
	
	/** Server host name */
	private String host_;
	
	/** Base URL from which relevant end-points can be accessed */
	private String urlBase_;
	
	private String urlFull_;
	/** The port on which the server is communicating */
	private int port_;
	
	private Context ctx_;
	private List<Event> events_;
	
	public EventControllerImpl(String host, int port, String urlBase) {
			 host_ = host;
			 port_ = port;
			 urlBase_ = urlBase;
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
		
		urlFull_ = host_ + "/mcc/events/full-test-1/on/" + month + "/" + day + "/"+ year;
		
        new GetEventsAsyncTask().execute(l);
		
		
		
	}
	
	/** The AsyncTask responsible for retrieving the set of events */
	private class GetEventsAsyncTask extends 
		AsyncTask<EventsListener, Integer, List<Event>> {		
		
		@Override
		protected List<Event> doInBackground(EventsListener... eListener) {			
			List<Event> events = new ArrayList<Event>();
			
			try {
				URL url = new URL(urlFull_);
				ObjectMapper mapper = new ObjectMapper();
				events = mapper.readValue(url, new TypeReference<List<Event>>(){});
				if(events != null) {
					eListener[0].setEvents(events);
					Log.d(TAG, "events size:"+ events.size());
					Log.d(TAG, "event from server:"+ events.get(0).getStartTime());
				}
				
				
			} 
			catch (IOException e) {
				Log.d(TAG, "IO Exception while forming/reading URL");
			}

			return events;
		}
		
		@Override
		protected void onPostExecute(List<Event> events) {			
						
			if(events != null) {
				events_ = events;
				Log.d(TAG, "Successfully loaded navigation data!");			
			
			}
			else {
				Log.d(TAG, "Error loading navigation data");
			}
		}	
	} 

}
