/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.events;

import java.util.List;

import org.magnum.mcc.nav.MapRouteActivity;
import org.magnum.mccmap.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//Work Item 3
/**
 * This activity should show basic event information and have a button that
 * allows the user to launch the MapRouteActivity to see directions on how to
 * get to the event.
 * 
 * @author jules
 * @author weichen
 * @version 1.0
 * 
 */

public class EventDetailFragment extends Fragment {
	
	private final String TAG = this.getClass().getSimpleName();
	private EventController eventController_;
	private List<Event> eventList_;

	private String eventDescription;
	private String eventName;
	private String eventTime;
	private String eventLocation;

	private TextView text_title;
	private TextView text_descript;
	private TextView text_time;
	private TextView text_location;
	private ImageView eventPhoto;
    private Button routeBtn;

	private String floorplanId;
	private String endId;
	private String eventId;
	private String myLocationId;
    private String day;
    private String month;
    private String year;

	private Handler handler_ = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.eventview, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        if(args !=null){
            floorplanId = args.getString("floorplanId");
            eventId = args.getString("eventId");
            myLocationId = args.getString("myLocationId");
            endId = args.getString("endId");
            day = args.getString("day");
            month = args.getString("month");
            year = args.getString("year");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

		text_title = (TextView) getView().findViewById(R.id.title_text);
		text_descript = (TextView) getView().findViewById(R.id.text_description);
		text_time = (TextView) getView().findViewById(R.id.textView_time);
		text_location = (TextView) getView().findViewById(R.id.textView_location);
        routeBtn = (Button) getView().findViewById(R.id.button_route);

		// This should probably be pulled from shared preferences
		// but can be hardcoded to the MCC appengine server for now
		String server = "http://0-1-dot-mcc-backend.appspot.com";
		int port = 80;
		String baseUrl = "/mcc/events/full-test-1/on/" + month + "/" + day
				+ "/" + year;
		String url = server + baseUrl;

		Log.d(TAG, url);
		
		eventController_ = new EventControllerImpl(server, port, baseUrl);
		eventController_.getEventsOnDay(month, day, year, new EventsListener() {
            @Override
            public void setEvents(List<Event> e) {
                eventList_ = e;
                handler_.post(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            }
        });

		String[] parameter = { url, floorplanId, eventId, endId };

		// button to jump to nav

		routeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startMapRoute();
			}
		});
	}

	private void startMapRoute() {
		Intent intent = new Intent(getActivity(),
				MapRouteActivity.class);
        intent.putExtra("floorplanId", floorplanId);
		intent.putExtra("startId", myLocationId);
		intent.putExtra("endId", endId);
        
        Log.d(TAG,"floorplanId:"+ floorplanId+ " startId:"+ myLocationId+ " endId:"+ endId);
		startActivity(intent);
	}

	private void updateUI() {
		for (Event e : eventList_) {
			if (e.getId().equals(eventId)) 
			{
				eventName = e.getName();
				eventLocation = e.getFloorplanLocationId();
				eventDescription = e.getDescription();
				String day = e.getDay();
				String month = e.getMonth();
				String year = e.getYear();
				String starttime = e.getStartTime();
				String endtime = e.getEndTime();
				eventTime = year + "-" + month + "-" + day + "\n" + "From "
						+ time(starttime) + " to " + time(endtime) + "\n";
				break;
			}
		}
		text_title.setText(eventName);
		text_descript.setText(eventDescription);
		text_time.setText(eventTime);
		text_location.setText(eventLocation);
	}

	// change time to normal format
	public String time(String t) {
		int i = Integer.parseInt(t);
		int hour = i / 60;
		int min = i - 60 * hour;
		String h = String.valueOf(hour);
		String m = String.valueOf(min);
		if (min == 0)
			m = "00";
		if (min < 10 && min > 0)
			m = "0" + m;
		String time = h + ":" + m;
		return time;
	}

}
