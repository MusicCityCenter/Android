package org.magnum.mcc.nav;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.net.Uri;
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
	private String targetUrl_;
	private String targetFloorplanId_;
	private static String MAPPING_PATH = "/floorplan/mapping/";
	private static String NAV_PATH = "/path/";
	
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
		targetFloorplanId_ = floorplanId;
		targetUrl_ = host_ + urlBase_ + MAPPING_PATH + targetFloorplanId_;
		Log.d(TAG, "fpId: " + targetFloorplanId_);
		Log.d(TAG, "url: " + targetUrl_);
		new FloorplanNavDataAsyncTask().execute(fl);
	}

	
	/** Retrieves the shortest path from 'start' to 'end' from the server
	 * 	and marshals it to the NavigationListener for processing */
	@Override
	public void getShortestPath(String floorplanId, FloorplanLocation start,
			FloorplanLocation end, NavigationListener navListener) {
		
		targetFloorplanId_ = floorplanId;
		targetUrl_ = host_ + urlBase_ + NAV_PATH + targetFloorplanId_ + "/" 
						+ start.getId() + "/" + end.getId();
		Log.d(TAG, "fpId: " + targetFloorplanId_);
		Log.d(TAG, "url: " + targetUrl_);
		new ShortestPathAsyncTask().execute(navListener);
	}
	
	
	/** The AsyncTask responsible for retrieving the FloorplanNavigationData */
	private class FloorplanNavDataAsyncTask extends 
		AsyncTask<FloorplanListener, Integer, FloorplanNavigationData> {
		
		@Override
		protected void onPreExecute() {
			setProgressVisibility(true, "Retrieving Floorplan", "Shouldn't be long now!");
			
		}
		
		@Override
		protected FloorplanNavigationData doInBackground(FloorplanListener... fpListeners) {
			
			FloorplanNavigationData fpNavData = null;
			try {
				String fpNavContent = getContent(targetUrl_);
				JSONObject fpNavJSON = new JSONObject(fpNavContent);
				
				fpNavData = new FloorplanNavigationData(extractFloorplan(fpNavJSON), 
														extractMapping(fpNavJSON));
			} catch (ClientProtocolException e) {
				Log.d(TAG, "ClientProtocol Exception thrown");
				/** handle specific exceptions */
			} catch (IOException e) {
				Log.d(TAG, "IO Exception thrown");
				/** handle specific exceptions */
			} catch (JSONException e) {
				Log.d(TAG, "JSON Exception thrown");
				/** handle specific exceptions */
			}
			
			if(fpNavData != null) {
				fpNavData_ = fpNavData;
				fpListeners[0].setFloorplanNavigationData(targetFloorplanId_, fpNavData_);
			}

			return fpNavData_;
		}
		
		@Override
		protected void onPostExecute(FloorplanNavigationData floorplanData) {			
			setProgressVisibility(false, null, null);
			
			if(floorplanData != null) {
				showToast("Successfully loaded navigation data!");
			}
			else {
				showToast("Error loading navigation data");
			}
		}	
	}
	
	
	/** The AsyncTask responsible for retrieving the shortest path */
	private class ShortestPathAsyncTask extends 
		AsyncTask<NavigationListener, Integer, Path> {
		
		@Override
		protected Path doInBackground(NavigationListener... navListeners) {		
			
			Path shortestPath = null;
			try {
				String pathContent = getContent(targetUrl_);
				JSONArray pathJSON = new JSONArray(pathContent);
				shortestPath = new Path(extractPath(pathJSON));
				
			} catch (ClientProtocolException e) {
				Log.d(TAG, "ClientProtocol Exception thrown");
				/** handle specific exceptions */
			} catch (IOException e) {
				Log.d(TAG, "IO Exception thrown");
				/** handle specific exceptions */
			} catch (JSONException e) {
				Log.d(TAG, "JSON Exception thrown");
				/** handle specific exceptions */
			}
			
			if(shortestPath != null) {
				shortestPath_ = shortestPath;
				navListeners[0].setPath(shortestPath_);
			}
			
			return shortestPath_;
		}
		
		@Override
		protected void onPostExecute(Path shortestPath) {			
			setProgressVisibility(false, null, null);
			
			if(shortestPath != null) {
				showToast("Successfully loaded path!");
			}
			else {
				showToast("Error loading path");
			}
		}	
	}
	
	
	/** Sets the ProgressDialog with the proper input values */
	private void setProgressVisibility(final boolean visibility, 
									   final String majorMsg, final String minorMsg) {
		if(activity_ != null) {
			activity_.get().runOnUiThread(new Runnable() {
				public void run() {
					activity_.get().setProgressVisibility(visibility, majorMsg, minorMsg);
				}
			});
		}
	}
	
	
	/** Shows a toast displaying the input message */
	private void showToast(final CharSequence msg) {
		if(activity_ != null) {
			activity_.get().runOnUiThread(new Runnable() {
				public void run() {
					activity_.get().showToast(msg);
				}
			});
		}
	}
	
	
	private String getContent(String url) throws ClientProtocolException, IOException {
		StringBuilder sb = new StringBuilder();

		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpResponse response = client.execute(new HttpGet(url));
		HttpEntity entity = response.getEntity();
		
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"), 8192);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
		}
		
		return sb.toString();
	}
	
	private Floorplan extractFloorplan(JSONObject fpNavJSON) throws JSONException {
		JSONObject fpData = fpNavJSON.getJSONObject("floorplan");
		
		Map<String, FloorplanLocation> fpLocs = new HashMap<String, FloorplanLocation>();
		JSONArray locations = fpData.getJSONArray("locations");				
		for(int i = 0; i < locations.length(); ++i) {
			JSONObject fpLoc = locations.getJSONObject(i);
			String fpLocId = fpLoc.getString("id");
			String fpLocType = fpLoc.getString("type");
			fpLocs.put(fpLocId, new FloorplanLocation(fpLocId, fpLocType));
		}
		
		List<FloorplanEdge> fpEdges = new ArrayList<FloorplanEdge>();
		JSONArray edges = fpData.getJSONArray("edges");
		for(int i = 0; i < edges.length(); ++i) {
			JSONObject fpEdge = edges.getJSONObject(i);
			FloorplanLocation fpEdgeStart = fpLocs.get(fpEdge.getString("start"));
			FloorplanLocation fpEdgeEnd = fpLocs.get(fpEdge.getString("end"));
			Double fpEdgeLen = (Double) fpEdge.get("length");
			fpEdges.add(new FloorplanEdge(fpEdgeStart, fpEdgeEnd, fpEdgeLen));
		}
		
		return new Floorplan(fpLocs, fpEdges);
	}
	
	private FloorplanImageMapping extractMapping(JSONObject fpNavJSON)
			throws JSONException {
		JSONObject mappingData = fpNavJSON.getJSONObject("mapping");
		
		String imgUrl = mappingData.getString("imageUrl");
		
		Map<String, Coord> fpCoordMap = new HashMap<String, Coord>();
		JSONObject mapping = mappingData.getJSONObject("mapping");
		JSONArray coordArr = mapping.names();
		for(int i = 0; i < coordArr.length(); ++i) {
			JSONObject coord = mapping.getJSONObject(coordArr.getString(i));
			int x = coord.getInt("x");
			int y = coord.getInt("y");
			fpCoordMap.put(coordArr.getString(i), new Coord(x, y));
		}

		return new FloorplanImageMapping(imgUrl, fpCoordMap);
	}
	
	private List<FloorplanEdge> extractPath(JSONArray pathJSON) throws JSONException {
		
		List<FloorplanEdge> edges = new LinkedList<FloorplanEdge>();
		for(int i = 0; i < pathJSON.length(); ++i) {
			JSONObject edgeJSON = pathJSON.getJSONObject(i);
			FloorplanEdge edge = new FloorplanEdge(
					fpNavData_.getFloorplan().getLocations().get(edgeJSON.getString("from")),
					fpNavData_.getFloorplan().getLocations().get(edgeJSON.getString("to")),
					edgeJSON.getDouble("length"),
					edgeJSON.getDouble("angle"));	
			edges.add(edge);
		}
		
		return edges;
	}
}
