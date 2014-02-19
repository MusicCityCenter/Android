package com.example.mccmap;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class floor2 extends Activity {
	
	static final int ZOOM_FLOOR2 = 17;
	
	private GoogleMap floor2;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        
        floor2 = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment))
        		.getMap();
        
        floor2.moveCamera(CameraUpdateFactory.newLatLngZoom(map.MCC, map.INIT_ZOOM));
        floor2.animateCamera(
        		CameraUpdateFactory.zoomTo(ZOOM_FLOOR2), map.ZOOM_ANIMATION_LENGTH, null);
    }
}
