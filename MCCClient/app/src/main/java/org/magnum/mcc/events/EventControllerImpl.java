package org.magnum.mcc.events;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
	
	/** The port on which the server is communicating */
	private int port_;
	private String targetUrl_;
	
	private Context ctx_;
	private List<Event> events_;

	private String content;
	
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
	public void getEventsOnDay(String month, String day, String year, EventsListener l) {		
		
		targetUrl_ = host_ + urlBase_+ "/events/full-test-1/on/" + month + "/" + day + "/"+ year;
		Log.d(TAG, targetUrl_);
        new GetEventsAsyncTask().execute(l);
				
		
	}
	
	/** The AsyncTask responsible for retrieving the set of events */
	private class GetEventsAsyncTask extends AsyncTask<EventsListener, Integer, Void> {
		List<Event> events = new ArrayList<Event>();

		@Override
		protected Void doInBackground(EventsListener... el) {
			try {
                Log.d(TAG, "URL: "+ targetUrl_);

				URL url = new URL(targetUrl_);
				ObjectMapper mapper = new ObjectMapper();
				events = mapper.readValue(url,
						new TypeReference<List<Event>>() {
						});
			} catch (IOException e) {
				Log.d(TAG, "IO Exception while forming/reading URL");
			}

			if(events != null) {
				events_ = events;
				el[0].setEvents(events_);
			}
			
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result);	
			events_ = events;
			Log.d(TAG,"size:"+ events_.size());
		}
	}
	

}
