/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.nav;


// Work Item 1

public interface NavController {

	// This method should set the host information that is used to build the HTTP requests
	// sent below.
	public void setServer(String host, int port, String urlBase);
	
	// This should communicate with the server endpoint: {urlBase}/floorplan/mapping/{floorplanId}
	// and use Jackson JSON to marshall the result into a FloorplanNavigationData object
	// that is asynchronously returned to the caller via the FloorplanListener that it provides.
	public void loadFloorplan(String floorplanId, FloorplanListener fl);
	
	// This should communicate with the server endpoint: {urlBase}/path/{floorplanId}/{startId}/{endId}
	// and use Jackson JSON to marshall the result into a List<FloorplanEdge> that is used to construct
	// a Path instance. The caller should be notified when the result is ready via the provided NavigationListener.
	public void getShortestPath(String floorplanId, FloorplanLocation start, FloorplanLocation end, NavigationListener l);
	
}
