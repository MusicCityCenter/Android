/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.nav;


import java.util.ArrayList;
import java.util.List;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.magnum.mccmap.R;
import org.magnum.mccmap.SearchResult;
import org.magnum.mccmap.UtilityClass;

public class MapRouteActivity extends Activity {
	
	/** Used for debugging */
    private final String TAG = this.getClass().getSimpleName();

	private FloorplanNavigationData navData_;

	private Path path_;

	private NavController navController_;
	
	private Handler handler_ = new Handler();
	
	private ProgressDialog progress_;
	
	private MapImageView mapImageView_;
	
	private String serverBase_;

    private Button directionBtn_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(org.magnum.mccmap.R.layout.maproute_activity);

		// Do stuff to setup the UI
		progress_ = new ProgressDialog(this);
		mapImageView_ = (MapImageView) findViewById(org.magnum.mccmap.R.id.mapImageView);
		mapImageView_.setDrawingCacheEnabled(true);
		
		// Obtain the request path data
		Intent i = getIntent();
		final String floorplanId = i.getStringExtra("floorplanId");
		final String startId = i.getStringExtra("startId");
		final String endId = i.getStringExtra("endId");
		
		Log.d(TAG, "floorplan: " + floorplanId);
		Log.d(TAG, "start: " + startId);
		Log.d(TAG, "end: " + endId);


		String server = UtilityClass.server;
		int port = UtilityClass.port;
		String baseUrl = UtilityClass.baseUrl;
		
		serverBase_ = server;

        directionBtn_ = (Button) this.findViewById(R.id.button_turn_by_turn);
        directionBtn_.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent i= new Intent(MapRouteActivity.this,DirectionActivity.class);
                i.putExtra("floorplanId",floorplanId);
                i.putExtra("startId", startId);
                i.putExtra("endId", endId);
                startActivity(i);
            }
        });

		navController_ = new MappingNavControllerImpl(server, port, 
													  baseUrl, this);
		
		navController_.loadFloorplan(floorplanId, new FloorplanListener() {
			
			@Override
			public void setFloorplanNavigationData(String floorplanId,
					FloorplanNavigationData fp) {
				
				navData_ = fp;

				FloorplanLocation start = 
						navData_.getFloorplan().getLocations().get(startId);

				FloorplanLocation end = 
						navData_.getFloorplan().getLocations().get(endId);
				
				if(start != null && end != null) {

					navController_.getShortestPath(floorplanId, start, end, 
							new NavigationListener() {
						
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
		String imageUrl = serverBase_ + navData_.getMapping().getImageUrl();
        Log.d(TAG,"imageUrl:"+ imageUrl);

        UrlImageViewHelper.setUseBitmapScaling(true);
     //   UrlImageViewHelper.setUrlDrawable(mapImageView_, imageUrl);
        mapImageView_.setImageDrawable(getResources().getDrawable(R.drawable.level1));
		
		List<Coord> pathCoords = new ArrayList<Coord>();
		for(FloorplanEdge edge : path_.getEdges()){
			pathCoords.add(navData_.getMapping().getImageLocation(edge.getStart()));
			pathCoords.add(navData_.getMapping().getImageLocation(edge.getEnd()));
		}
		
		// Sets the coords and requires the view to redraw
		mapImageView_.setPath(pathCoords);
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
