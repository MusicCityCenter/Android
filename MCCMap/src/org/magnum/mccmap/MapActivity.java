package org.magnum.mccmap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.os.Bundle;

public class MapActivity extends Activity {
	
	// The map that shows the MCC
	private GoogleMap mccMap;
	// The zoom_level to show.
	/*	In the future, this will determine which GroundOverlay or 
	 *  TileOverlay to show in place of the standard GoogleMap floors
	 */
	private int zoom_level;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        
        zoom_level = (Integer) this.getIntent().getExtras().get(Map.ZOOM_EXTRA);
        
		mccMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment)).getMap();
		mccMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Map.MCC, Map.INIT_ZOOM));
        mccMap.animateCamera(
        		CameraUpdateFactory.zoomTo(zoom_level), Map.ZOOM_ANIMATION_LENGTH, null);
    }

}
