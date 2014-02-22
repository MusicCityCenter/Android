package com.example.mccmap;

import java.util.Set;

import org.magnum.mcc.events.Event;
import org.magnum.mcc.events.EventController;
import org.magnum.mcc.events.EventsListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

// Work Item 5
/**
 * This activity should be updated to automatically / asynchronously
 * retrieve the list of events for the current day using the EventController
 * implementation that is produced during this build cycle. The activity
 * should display a search box that allows the user to search through the
 * events by typing in search terms. If the user selects an event from the
 * list of results, it should launch the EventViewActivity. For this build
 * cycle, it is fine to just randomly select a starting FloorplanLocation from
 * the Floorplan object for the building.
 * 
 * @author jules
 *
 */

public class MainActivity extends Activity {

	
	// Fill me in with the current floorplanId...
	// this should probably be somethign that is 
	// a setting that comes from shared prefs
	private String floorplanId;
	
	// Update me to 
	private EventController eventController_;
	
	private Set<Event> events_;
	
	private Handler handler_ = new Handler();
	
	private Button button1,button2,button3,button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        button1=(Button) this.findViewById(R.id.button1);
        button2=(Button) this.findViewById(R.id.button2);
        button3=(Button) this.findViewById(R.id.button3);
        button4=(Button) this.findViewById(R.id.button4);
        
        // Update me to instantiate the EventController implementation
        // created in this build cycle
        eventController_ = null;
        
		// This should probably be pulled from shared preferences
		// but can be hardcoded to the MCC appengine server for now
		String server = "fill me in with the foo.appspot.blah url";
		int port = 80;
		String baseUrl = "/mcc";
		eventController_.setServer(server, port, baseUrl);
		
        
        // Update to calculate the current day/month
        int day = 1;
        int month = 1;
        eventController_.getEventsOnDay(month, day, new EventsListener() {
			
			@Override
			public void setEvents(Set<Event> events) {
				events_ = events;
				handler_.post(new Runnable() {
					
					@Override
					public void run() {
						updateSearchBoxPossibilities();
					}
				});
			}
		});
        
        
        button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(MainActivity.this,map.class);
				startActivity(intent);
			}
		});
        
        button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(MainActivity.this,wayfinding.class);
				startActivity(intent);
			}
		});
        
        button3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(MainActivity.this,restaurant.class);
				startActivity(intent);
			}
		});
        
        button4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(MainActivity.this,event_calendar.class);
				startActivity(intent);
			}
		});
        
    }
    
    private void updateSearchBoxPossibilities(){
    	//Do anything that you need to do UI-wise
    	//now that the list of possible events has been retrieved from the
    	//server.
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
