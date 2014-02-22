/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.events;

// Work item 4
public interface EventController {

	// This method should set the host information that is used to build the HTTP requests
	// sent below.
	public void setServer(String host, int port, String urlBase);
	
	/**
	 * Retrieves a list of events from the server that take place on the
	 * specified day. Usually, the client should use the current day/time
	 * to populate these values.
	 * 
	 * @param start
	 * @param end
	 * @param l
	 */
	public void getEventsOnDay(int month, int day, EventsListener l);
	
}
