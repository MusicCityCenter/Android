package org.magnum.mccmap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.magnum.mcc.events.Event;
import org.magnum.mcc.events.EventController;
import org.magnum.mcc.events.EventControllerImpl;
import org.magnum.mcc.events.EventsListener;
import org.magnum.mcc.nav.MapRouteActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;



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

public class MainActivity extends FragmentActivity implements IBeaconConsumer {

	private final String TAG = this.getClass().getSimpleName();

	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
    // Determine location after multiple beacon search
    private int sampleCount;
    private int sampleNeed = 3;
    private HashMap<Integer,List<Integer>> beaconSignal = new HashMap<Integer, List<Integer>>();

    public static String currentLocationId;
    private TextView LocationTextView_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationTextView_ = (TextView) this.findViewById(R.id.textView_currentLocation);

        verifyBluetooth();
        iBeaconManager.bind(this);

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

    private void verifyBluetooth() {

		try {
			if (!IBeaconManager.getInstanceForApplication(this).checkAvailability()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");			
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, null);
				//If bluetooth not available, ues wifi to localization
                currentLocationId = "101";
                LocationTextView_.setText("Room 101");

				builder.show();
			}			
		}
		catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");			
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, null);
//			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//				@Override
//				public void onDismiss(DialogInterface dialog) {
//					finish();
//		            System.exit(0);
//				}
//
//			});
			builder.show();
			
		}
		
	}
    
    
    
    @Override
    public void onIBeaconServiceConnect() {

        //for test only
        final HashMap<Integer, String> beaconMapping = new HashMap<Integer, String>();
        beaconMapping.put(1673,"Room 101");
        beaconMapping.put(1661,"Room 104");

        //for test only
        final HashMap<String, String> IdMapping = new HashMap<String, String>();
        IdMapping.put("Room 101","101");
        IdMapping.put("Room 104","104");

        iBeaconManager.setRangeNotifier(new RangeNotifier() {
        @Override 
        public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
            if (iBeacons.size() > 0) {

                for(IBeacon bea:iBeacons)
                {
                    if(beaconSignal.containsKey(bea.getMinor())){
                        beaconSignal.get(bea.getMinor()).add(bea.getRssi());
                    }
                    else{
                        List<Integer> temp = new ArrayList<Integer>();
                        temp.add(bea.getRssi());
                        beaconSignal.put(bea.getMinor(),temp);
                    }
                }
                sampleCount++;
                if(sampleCount>= sampleNeed)
                {
                    sampleCount = 0;
                    int maxRssi = -100;
                    int index = 0;

                    Iterator iter = beaconSignal.entrySet().iterator();
                    while (iter.hasNext()) {
                        HashMap.Entry entry = (HashMap.Entry) iter.next();
                        int key = (Integer) entry.getKey();
                        List<Integer> val = (List<Integer>)entry.getValue();
                        int sum = 0;
                        for(Integer num:val){
                            sum += num;
                        }
                        int avg = sum/val.size();
                        if(avg > maxRssi){
                            maxRssi = avg;
                            index = key;
                        }
                    }
                    logToDisplay(beaconMapping.get(index));

                    currentLocationId = IdMapping.get(beaconMapping.get(index));
                    Log.d(TAG,"current location: "+ currentLocationId);
                    beaconSignal.clear();
                }

            }

        }

        });

        try {
            iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    private void logToDisplay(final String location) {
    	runOnUiThread(new Runnable() {
    	    public void run() {
    	    	LocationTextView_.setText(location);
                LocationTextView_.setTextColor(Color.BLUE);

    	    }
    	});
    }
    public static String getCurrentLocationId(){
        return currentLocationId;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
            	Intent i1= new Intent(MainActivity.this, MapRouteActivity.class);
            	i1.putExtra("floorplanId", "full-test-1");
            	i1.putExtra("startId", "104");
            	i1.putExtra("endId", "108");
				startActivity(i1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    
    @Override 
    protected void onDestroy() {
        super.onDestroy();
        iBeaconManager.unBind(this);
    }
    @Override 
    protected void onPause() {
    	super.onPause();
    	if (iBeaconManager.isBound(this)) iBeaconManager.setBackgroundMode(this, true);    		
    }
    @Override 
    protected void onResume() {
    	super.onResume();
    	if (iBeaconManager.isBound(this)) iBeaconManager.setBackgroundMode(this, false);    		
    }
}
