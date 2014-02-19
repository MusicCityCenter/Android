package com.example.mccmap;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;

public class map extends Activity {
	
	// The coordinates that roughly center the MCC
	static final LatLng MCC = new LatLng(36.157158, -86.777326);
	
	// For camera animation
	static final int INIT_ZOOM = 15;
	static final int ZOOM_ANIMATION_LENGTH = 1500;
	
	// Floor zoom levels:
	/* 	for now, they go to a different zoom level
	 *  on the standard GoogleMap. However, in the future
	 *  they will indicate which GroundOverlay to display
	 *  on the MapView, in place of (or in combination with)
	 *  the standard GoogleMap  */
	static final String ZOOM_EXTRA = "ZOOM_LEVEL";
	static final int ZOOM_1 = 21;
	static final int ZOOM_1M = 20;
	static final int ZOOM_2 = 19;
	static final int ZOOM_3 = 18;
	static final int ZOOM_4 = 17;
	
	private int screenHeight;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
    }
	
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			goToFloor(x,y);
			
		}
		return super.onTouchEvent(event);
	}
	
	private void goToFloor(float x, float y)
	{	
		if(x > 0) {
			if(y < screenHeight/5) {
				showMapAtLevel(ZOOM_1);
			}
			else if(y < 2*screenHeight/5) {
				showMapAtLevel(ZOOM_1M);
			}
			else if(y < 3*screenHeight/5) {
				showMapAtLevel(ZOOM_2);
			}
			else if(y < 4*screenHeight/5) {
				showMapAtLevel(ZOOM_3);
			}
			else {
				showMapAtLevel(ZOOM_4);
			}
		}
	}
	
	private void showMapAtLevel(int zoom_level) {
        Intent mapIntent= new Intent(map.this, MapActivity.class);
        mapIntent.putExtra(ZOOM_EXTRA, zoom_level);
		startActivity(mapIntent);
	}
}
