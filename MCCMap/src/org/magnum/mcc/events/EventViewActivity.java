/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// Work Item 3
/**
 * This activity should show basic event information and
 * have a button that allows the user to launch the MapRouteActivity
 * to see directions on how to get to the event.
 * 
 * @author jules
 *
 */
public class EventViewActivity extends Activity {

	private EventController eventController_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Do stuff to setup the UI

		// Obtain the request path data
		Intent i = getIntent();
		final String floorplanId = i.getStringExtra("floorplanId");
		final String eventId = i.getStringExtra("eventId");
		final String endId = i.getStringExtra("endId");

		// This should probably be pulled from shared preferences
		// but can be hardcoded to the MCC appengine server for now
		String server = "fill me in with the foo.appspot.blah url";
		int port = 80;
		String baseUrl = "/mcc";
	}

}
