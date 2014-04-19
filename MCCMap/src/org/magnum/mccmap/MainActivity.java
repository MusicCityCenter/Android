package org.magnum.mccmap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.magnum.mcc.events.Event;
import org.magnum.mcc.events.EventController;
import org.magnum.mcc.events.EventControllerImpl;
import org.magnum.mcc.events.EventListforDateActivity;
import org.magnum.mcc.events.EventListforthreedays;
import org.magnum.mcc.events.EventsAdapter;
import org.magnum.mcc.events.EventsAndRoomforSearch;
import org.magnum.mcc.events.EventsListener;
import org.magnum.mcc.nav.MapRouteActivity;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;


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

public class MainActivity extends FragmentActivity {

	/*
	
	public static final String TAG = MainActivity.class.getSimpleName();
	
	
	// Fill me in with the current floorplanId...
	// this should probably be somethign that is 
	// a setting that comes from shared prefs
	private String floorplanId;
	
	// Update me to 
	private EventController eventController_;
	
	private List<Event> events_ = new ArrayList<Event>();
	
	private Handler handler_ = new Handler();
	
	private Button searchButton;
	private EditText searchBox;
	private String searchcontent;
	
	//Listview related variable
	private ListView headList;
	private ListView eventList;
	private ArrayList<HashMap<String, String>> listContent, listContent_title;
	private ListAdapter adapter_title, adapter;
	private HashMap<String, String> map_head, map_list;
	private static final String[] columnName = {"Name","Description","Time"};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        searchButton = (Button) this.findViewById(R.id.button_search);
        searchBox = (EditText) this.findViewById(R.id.editText_searchBox);
        eventList = (ListView) this.findViewById(R.id.listView_eventToday);
        headList = (ListView) this.findViewById(R.id.listView_head);
                         
		// This should probably be pulled from shared preferences
		// but can be hardcoded to the MCC appengine server for now
		String server = "http://0-1-dot-mcc-backend.appspot.com";
		int port = 80;
		String baseUrl = "/mcc";
		eventController_ = new EventControllerImpl(server,port,baseUrl);			
        
        // Update to calculate the current day/month
		Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //In Calendar, January is represented by constant 0
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        
        eventController_.getEventsOnDay(month, day, year, new EventsListener() {
			
			@Override
			public void setEvents(List<Event> events) {
				Log.d("MCC","eventsize:"+ events.size());
				events_ = events;
				handler_.post(new Runnable() {
					
					@Override
					public void run() {
						
						displayList();
						updateSearchBoxPossibilities();
					}
				});
			}
		});
        
        
        
        
        searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Is this correct?! I changed it from "Map.class" to 
				// MapRouteActivity.class to get it to compile, but it might be wrong 
				searchcontent = searchBox.getText().toString();
				Intent intent= new Intent(MainActivity.this, EventsAndRoomforSearch.class);
				intent.putExtra("search", searchcontent);
				startActivity(intent);
			}
		});        
        
    }
    
    private void displayList() {
		listContent = new ArrayList<HashMap<String, String>>();
		listContent_title = new ArrayList<HashMap<String, String>>();

		//Display the headings 

		map_head = new HashMap<String, String>();

		for(int i=0; i<columnName.length; i++)
		   map_head.put(columnName[i], columnName[i]);
		
		listContent_title.add(map_head);

		try {
			adapter_title = new SimpleAdapter(this, listContent_title, R.layout.row,
					new String[] { columnName[0], columnName[1], columnName[2] },
					new int[] {R.id.Name, R.id.Description, R.id.Time });
			headList.setAdapter(adapter_title);
		} catch (Exception e) {

		}

		// Display the contents
		Log.d("MCC","events in DisplayList:" + events_.size());
		for (Event e : events_) {
			map_list = new HashMap<String, String>();
			map_list.put(columnName[0], e.getName());
			map_list.put(columnName[1], e.getDescription());
			map_list.put(columnName[2], e.getStartTime() + "-"+ e.getEndTime());

			listContent.add(map_list);
		}

		try {
			adapter = new SimpleAdapter(this, listContent, R.layout.row,
					new String[] { columnName[0], columnName[1], columnName[2] },
					new int[] {R.id.Name, R.id.Description, R.id.Time });
			eventList.setAdapter(adapter);
		} catch (Exception e) {

		}
		
	}
    
    private void updateSearchBoxPossibilities(){
    	//Do anything that you need to do UI-wise
    	//now that the list of possible events has been retrieved from the
    	//server.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_action, menu);
        
//     // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        // Assumes current activity is the searchable activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_map:
            	Intent i1= new Intent(MainActivity.this, MapRouteActivity.class);
            	i1.putExtra("floorplanId", "full-test-1");
            	i1.putExtra("startId", "B-1-3");
            	i1.putExtra("endId", "S-1m-1");
				startActivity(i1);
                return true;
            case R.id.action_eventlist:
            	Intent i2= new Intent(MainActivity.this,EventListforthreedays.class);
            	// Update to calculate the current day/month
        		Calendar calendar = Calendar.getInstance();
                String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                //In Calendar, January is represented by constant 0
                String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                
                //i2.putExtra("day", "19");
				//i2.putExtra("month", "3");
				//i2.putExtra("year", year);
				startActivity(i2);
				return true;
            case R.id.action_about:
            	Intent i3= new Intent(MainActivity.this,AboutActivity.class);
            	startActivity(i3);
            	return true;
            case R.id.action_setting:
            	
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void listViewSetup() {
    	eventList = (ListView) this.findViewById(R.id.listView_eventToday);
    	eventList.setAdapter(new EventsAdapter(this, R.layout.row, events_));
    	eventList.setTextFilterEnabled(true);
    }
    
    private void searchBoxSetup() {
    	searchBox = (EditText) findViewById(R.id.editText_searchBox);
    	searchBox.addTextChangedListener(mEventListFilter);
    }
    
    private EventsListener mEventListener = new EventsListener() {
		@Override
		public void setEvents(List<Event> events) {
			Log.d(TAG,"EventsListener callback invoked, # of events: "+ events.size());
			events_ = events;
			listViewSetup();
			searchBoxSetup();
		}
	};
	
	private TextWatcher mEventListFilter = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {
			Log.d(TAG, "Filtering search box : " + s.toString());
			EventsAdapter evtAdapter = (EventsAdapter) eventList.getAdapter();
			evtAdapter.getFilter().filter(s);
			
		}
	};
	
	*/
	
	ActionBar actionbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);
		ActionBar.Tab map = bar.newTab().setText("MAP");
		ActionBar.Tab event = bar.newTab().setText("EVENT");
		ActionBar.Tab about = bar.newTab().setText("ABOUT");
		
		Fragment fragment_map = new MapFragment();
		Fragment fragment_event = new EventFragment();
		Fragment fragment_about = new AboutFragment();
		
		map.setTabListener(new myTabListener(fragment_map));
		event.setTabListener(new myTabListener(fragment_event));
		about.setTabListener(new myTabListener(fragment_about));
		bar.addTab(map);
		bar.addTab(event);
		bar.addTab(about);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
			getFragmentManager().popBackStack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater(); 
        inflater.inflate(R.menu.main, menu); 
        
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView(); 
        if(searchView == null){ 
            Log.e("SearchView","Fail to get Search View.");
            return true;
        }
        searchView.setIconifiedByDefault(false);
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

			@Override
			public boolean onQueryTextChange(String s) {
				// TODO Auto-generated method stub
				Fragment newFragment = new SearchResult();
				Bundle bundle = new Bundle();
				bundle.putString("search", s);
				newFragment.setArguments(bundle);
				
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.fragment_place, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
				
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
        	
        });
        
		return true;
	}
	
	
}
