package org.magnum.mcc.nav;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.AsyncTask;
import android.util.Log;

public class MappingNavControllerImpl implements NavController {
	
	/** Used for debugging */
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
	
	/** A weak reference to the activity for progress notification */
	private WeakReference<MapRouteActivity> activity_;
	
	/** The FloorplanNavigationData that is retrieved from the server */
	private FloorplanNavigationData fpNavData_ = null;
	
	/** The Path that is retrieved from the server on getShortestPath */
	private Path shortestPath_ = null;
	
	
	/** Constructs a new MappingNavigation Controller responsible for 
	 * 	loading Floorplan data from the server
	 * @param host - the host name of the server
	 * @param port - the port on which the server is listening
	 * @param urlBase - the base URL of the relevant end-points
	 * @param activity - a handle to the MapRouteActivity for progress updates */
	public MappingNavControllerImpl(String host, int port, 
									String urlBase, MapRouteActivity activity) {
		host_ = host;
		port_ = port;
		urlBase_ = urlBase;
		activity_ = new WeakReference<MapRouteActivity>(activity);
	}
	
	
	/** Sets the server properties of the controller */
	@Override
	public void setServer(String host, int port, String urlBase) {
		 host_ = host;
		 port_ = port;
		 urlBase_ = urlBase;
	}

	
	/** Loads a FloorplanNavigationData object asynchronously and marshals it 
	 * 	to the input FloorplanListener */
	@Override
	public void loadFloorplan(String floorplanId, FloorplanListener fl) {
		new FloorplanNavDataAsyncTask().execute(MAPPING_PATH + floorplanId);
		
		if(fpNavData_ != null) {
			fl.setFloorplanNavigationData(floorplanId, fpNavData_);
		}
	}

	
	/** Retrieves the shortest path from 'start' to 'end' from the server
	 * 	and marshals it to the NavigationListener for processing */
	@Override
	public void getShortestPath(String floorplanId, FloorplanLocation start,
			FloorplanLocation end, NavigationListener l) {
		
		String path = NAVIGATION_PATH + floorplanId + "/" 
						+ start.getId() + "/" + end.getId();
		new ShortestPathAsyncTask().execute(path);
		
		if(shortestPath_ != null) {
			l.setPath(shortestPath_);
		}
	}
	
	
	/** The AsyncTask responsible for retrieving the FloorplanNavigationData */
	private class FloorplanNavDataAsyncTask extends 
		AsyncTask<String, Integer, FloorplanNavigationData> {
		
		@Override
		protected void onPreExecute() {
			setProgressVisibility(true, "Retrieving Floorplan", "Shouldn't be long now!");
		}
		
		@Override
		protected FloorplanNavigationData doInBackground(String... arg0) {			
			FloorplanNavigationData floorplanData = null;
			
			try {
				URL url = new URL(host_ + urlBase_ + arg0[0]);
				ObjectMapper mapper = new ObjectMapper();
				floorplanData = mapper.readValue(url, FloorplanNavigationData.class);	
			} 
			catch (IOException e) {
				Log.d(TAG, "IO Exception while forming/reading URL");
			}

			return floorplanData;
		}
		
		@Override
		protected void onPostExecute(FloorplanNavigationData floorplanData) {			
			setProgressVisibility(false, null, null);
			
			if(floorplanData != null) {
				fpNavData_ = floorplanData;
				showToast("Successfully loaded navigation data!");
			}
			else {
				showToast("Error loading navigation data");
			}
		}	
	}
	
	
	/** The AsyncTask responsible for retrieving the shortest path */
	private class ShortestPathAsyncTask extends 
		AsyncTask<String, Integer, Path> {
		
		@Override
		protected void onPreExecute() {
			setProgressVisibility(true, "Retrieving Best Path",	"Shouldn't be long now!");
		}
		
		@Override
		protected Path doInBackground(String... arg0) {			
			List<FloorplanEdge> path = null;
			
			try {
				URL url = new URL(host_ + urlBase_ + arg0[0]);
				ObjectMapper mapper = new ObjectMapper();
				path = mapper.readValue(url, new TypeReference<List<FloorplanEdge>>(){});	
			} 
			catch (IOException e) {
				Log.d(TAG, "IO Exception while forming/reading URL");
			}
			
			Path shortestPath = null;
			if(path != null) {
				shortestPath = new Path(path);
			}

			return shortestPath;
		}
		
		@Override
		protected void onPostExecute(Path shortestPath) {			
			setProgressVisibility(false, null, null);
			
			if(shortestPath != null) {
				shortestPath_ = shortestPath;
				showToast("Successfully loaded path!");
			}
			else {
				showToast("Error loading path");
			}
		}	
	}
	
	
	/** Sets the ProgressDialog with the proper input values */
	private void setProgressVisibility(boolean visibility, 
									   String majorMsg, String minorMsg) {
		if(activity_ != null) {
			activity_.get().setProgressVisibility(visibility, majorMsg, minorMsg);
		}
	}
	
	
	/** Shows a toast displaying the input message */
	private void showToast(CharSequence msg) {
		if(activity_ != null) {
			activity_.get().showToast(msg);
		}
	}
}
