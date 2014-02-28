/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.nav;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

// Work Item 2
public class MapRouteActivity extends Activity {

	private FloorplanNavigationData navData_;

	private Path path_;

	private NavController navController_;
	
	private Handler handler_ = new Handler();
	
	private ProgressDialog progress_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Do stuff to setup the UI
		progress_ = new ProgressDialog(this);
		
		// Obtain the request path data
		Intent i = getIntent();
		final String floorplanId = i.getStringExtra("floorplanId");
		final String startId = i.getStringExtra("startId");
		final String endId = i.getStringExtra("endId");

		// This should probably be pulled from shared preferences
		// but can be hardcoded to the MCC appengine server for now
		String server = "http://0-1-dot-mcc-backend.appspot.com";
		int port = 80;
		String baseUrl = "/mcc";
		
		// Update to use the NavController implementation created during
		// this build cycle
		navController_ = new MappingNavControllerImpl(server, port, 
													  baseUrl, this);
		
		navController_.loadFloorplan(floorplanId, new FloorplanListener() {
			
			@Override
			public void setFloorplanNavigationData(String floorplanId,
					FloorplanNavigationData fp) {
				
				navData_ = fp;
				
				// Change me to obtain the FloorplanLocation with the specified
				// startId from the navData_.getFloorplan() object
				FloorplanLocation start = null;
				
				// Change me to obtain the FloorplanLocation with the specified
				// endId from the navData_.getFloorplan() object
				FloorplanLocation end = null;
				
				navController_.getShortestPath(floorplanId, start, end, new NavigationListener() {
					
					@Override
					public void setPath(Path p) {
						path_ = p;
						
						// Now referesh the UI
						Runnable r = new Runnable() {
							@Override
							public void run() {
								updatePathDisplay();
							}
						};
						handler_.post(r);
					}
				});
			}
		});
	}
	
	private void updatePathDisplay(){
		// This method should display the map image in the
		// URL embedded in navData_.getMapping().getImageUrl();
		// and then draw the specified path_ on top of it. The
		// path_ contains a series of FloorplanEdge objects, each
		// of which has a start and end. The start and end locations
		// relative to the floor plan image can be obtained from the
		// navData_.getMapping() object. Pseudo-code:
		
		// Display me in an image view...map tiler..etc. 
		// To make this simple for this build cycle,  you might
		// want to just use a simple ImageView and this library
		// to make remotely loading the images easy:
		// https://github.com/koush/UrlImageViewHelper
		String imageUrl = navData_.getMapping().getImageUrl();
		
		
		for(FloorplanEdge edge : path_.getEdges()){
			FloorplanLocation start = edge.getStart();
			FloorplanLocation end = edge.getEnd();
			
			Coord startLoc = navData_.getMapping().getImageLocation(start);
			Coord endLoc = navData_.getMapping().getImageLocation(end);
			
			//Draw a line from startLoc.x,startLoc.y to endLoc.x,endLoc.y on 
			// top of the image specified in imageUrl
		}
	}
	
	
	/** Sets the progress dialog according to the parameters */
	public void setProgressVisibility(boolean visibility, String majorMsg, 
									  String minorMsg) {
		if(visibility)
			progress_ = ProgressDialog.show(this, majorMsg, minorMsg);
		else
			progress_.dismiss();
	}
	
	/** Shows a toast with the input message */
	public void showToast(CharSequence msg) {
		
		int duration = Toast.LENGTH_SHORT;
		Toast.makeText(this, msg, duration).show();
	}
}
