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
import org.magnum.mccmap.UtilityClass;

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


/**
 * This activity should show basic event information and have a button that
 * allows the user to launch the MapRouteActivity to see directions on how to
 * get to the event.
 * 
 * @author jules
 * @author yao
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

        return inflater.inflate(R.layout.eventview, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

		text_title = (TextView) getView().findViewById(R.id.title_text);
		text_descript = (TextView) getView().findViewById(R.id.text_description);
		text_time = (TextView) getView().findViewById(R.id.textView_time);
		text_location = (TextView) getView().findViewById(R.id.textView_location);
        routeBtn = (Button) getView().findViewById(R.id.button_route);


		String server = UtilityClass.server;
		int port = UtilityClass.port;
		String baseUrl = UtilityClass.baseUrl;
		
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

		// button to jump to nav

		routeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startMapRoute();
			}
		});
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {

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
						+ UtilityClass.formatTime(starttime) + " to " + UtilityClass.formatTime(endtime) + "\n";
				break;
			}
		}
		text_title.setText(eventName);
		text_descript.setText(eventDescription);
		text_time.setText(eventTime);
		text_location.setText(eventLocation);
	}


}
