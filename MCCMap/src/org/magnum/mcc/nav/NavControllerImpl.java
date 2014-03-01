package org.magnum.mcc.nav;

public class NavControllerImpl implements NavController {
	
	private final String TAG = this.getClass().getSimpleName();
	
	/** Server host name */
	private String host_;
	
	/** Base URL from which relevant end-points can be accessed */
	private String urlBase_;
	
	/** The paths to the mapping and path end-points */
	private static String MAPPING_PATH = "/floorplan/mapping/";
	private static String NAVIGATION_PATH = "/path/";
	
	/** The port on which the server is communicating */
	private int port_;
	
	/**
	 * This method set the host information that is used to build the HTTP requests
	 * sent below
	 * 
	 * @param host - the host name of the server
	 * @param port - the port on which the server is listening
	 * @param urlBase - the base URL of the relevant end-points
	 */

	@Override
	public void setServer(String host, int port, String urlBase) {
		host_ = host;
		port_ = port;
		urlBase_ = urlBase;
	}

	// This should communicate with the server endpoint: {urlBase}/floorplan/mapping/{floorplanId}
	// and use Jackson JSON to marshall the result into a FloorplanNavigationData object
	// that is asynchronously returned to the caller via the FloorplanListener that it provides.
	@Override
	public void loadFloorplan(String floorplanId, FloorplanListener fl) {
		FloorplanNavigationData fp = new FloorplanNavigationData();
		fl.setFloorplanNavigationData(floorplanId, fp);
	}
	
	// This should communicate with the server endpoint: {urlBase}/path/{floorplanId}/{startId}/{endId}
	// and use Jackson JSON to marshall the result into a List<FloorplanEdge> that is used to construct
	// a Path instance. The caller should be notified when the result is ready via the provided NavigationListener.
	@Override
	public void getShortestPath(String floorplanId, FloorplanLocation start,
			FloorplanLocation end, NavigationListener l) {
		

	}

}
