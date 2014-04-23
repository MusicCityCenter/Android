package org.magnum.mccmap;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.magnum.mcc.events.Event;
import org.magnum.mcc.events.EventCompare;
import org.magnum.mcc.events.EventConf;
import org.magnum.mcc.events.EventController;
import org.magnum.mcc.events.EventControllerImpl;
import org.magnum.mcc.events.EventDetailFragment;
import org.magnum.mcc.events.EventFilter;
import org.magnum.mcc.events.EventsListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class EventFragment extends Fragment{

	/** Used for debugging */
	private final String TAG = this.getClass().getSimpleName();

	private EventController eventController_;
    private EventController eventControllerYesterday;
    private EventController eventControllerTomorrow;
    private Handler handler_ = new Handler();

	private String year;
	private String month;
	private String day;
	
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;

	private ListView listviewToday;
	private ListView listviewTomorrow;
	private ListView listviewYestoday;

	private JSONArray array;
	private List<String> eventnametoday = new ArrayList<String>();
	private List<String> eventnameyesterday = new ArrayList<String>();
	private List<String> eventnametomorrow = new ArrayList<String>();

	private List<Event> eventlistToday = new ArrayList<Event>();
	private List<Event> eventlistYesterday = new ArrayList<Event>();
	private List<Event> eventlistTomorrow = new ArrayList<Event>();


	private int starthour = -1 ;
	private int startmin = -1;
	private int endhour = -1;
	private int endmin = -1;
    private String currentLocID;

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.eventlist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mViewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        mPagerTitleStrip = (PagerTitleStrip) getView().findViewById(R.id.pagertitle);


        // load the layout of three views
        LayoutInflater mLi = LayoutInflater.from(getActivity());
        View view1 = mLi.inflate(R.layout.view1, null);
        View view2 = mLi.inflate(R.layout.view2, null);
        View view3 = mLi.inflate(R.layout.view3, null);

        listviewYestoday = (ListView) view1.findViewById(R.id.listview);
        listviewToday = (ListView) view2.findViewById(R.id.listview);
        listviewTomorrow = (ListView) view3.findViewById(R.id.listview);

        int port = UtilityClass.port;
        String server = UtilityClass.server;
        String baseUrl = UtilityClass.baseUrl;
        
        eventController_ = new EventControllerImpl(server, port, baseUrl);
        eventControllerYesterday = new EventControllerImpl(server, port, baseUrl);
        eventControllerTomorrow = new EventControllerImpl(server, port, baseUrl);

        getToday();
        eventController_.getEventsOnDay(month, day, year, new EventsListener() {
            @Override
            public void setEvents(List<Event> e) {
                eventlistToday = e;
                handler_.post(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(eventlistToday, eventnametoday);
                        listviewToday.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, eventnametoday));
                    }
                });
            }
        });

        getYesterday();
        eventControllerYesterday.getEventsOnDay(month, day, year, new EventsListener() {
            @Override
            public void setEvents(List<Event> e) {
                eventlistYesterday = e;
                handler_.post(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(eventlistYesterday, eventnameyesterday);
                        listviewYestoday.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, eventnameyesterday));
                    }
                });
            }
        });

        getTomorrow();
        eventControllerTomorrow.getEventsOnDay(month, day, year, new EventsListener() {
            @Override
            public void setEvents(List<Event> e) {
                eventlistTomorrow = e;
                handler_.post(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(eventlistTomorrow, eventnametomorrow);
                        listviewTomorrow.setAdapter(new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_list_item_1, eventnametomorrow));
                    }
                });
            }
        });


        // views for viewpager
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        final ArrayList<String> titles = new ArrayList<String>();
        titles.add("Yesterday");
        titles.add("Today");
        titles.add("Tomorrow");

        // load view to viewpager
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }
        };

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(1);

        Button conf = (Button) getView().findViewById(R.id.conf);
        conf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment newFragment = new EventConf();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_place, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Button filter = (Button) getView().findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Fragment newFragment = new EventFilter();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_place, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        listviewToday.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                startEventDetail(eventlistToday, arg2);
            }
        });

        listviewTomorrow.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                startEventDetail(eventlistTomorrow, arg2);
            }
        });

        listviewYestoday.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                startEventDetail(eventlistYesterday, arg2);

            }
        });
    }

    private void startEventDetail(List<Event> eventlist, int index){
        Fragment newFragment = new EventDetailFragment();

        Bundle bundle = new Bundle();
        Event t = eventlist.get(index);
        bundle.putString("floorplanId", t.getFloorplanId());
        bundle.putString("eventId", t.getId());
        bundle.putString("endId", t.getFloorplanLocationId());
        bundle.putString("day", t.getDay());
        bundle.putString("month", t.getMonth());
        bundle.putString("year", t.getYear());

        currentLocID = MainActivity.getCurrentLocationId();
        bundle.putString("myLocationId", currentLocID);
        Log.d(TAG, "myLocationId:" + currentLocID);

        newFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_place, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Update the listview according to fetched event data
    private void updateUI(List<Event> events, List<String> descriptors) {
        descriptors.clear();
        //Sort the events by start time
        Collections.sort((events), new EventCompare());

        for (Event e : events) {
            if (starthour == -1) {
                descriptors.add(formatEventDescriptor(e));
            } else {
                if (valueoftime(e.getStartTime()) >= starthour * 60 + startmin
                        && valueoftime(e.getEndTime()) <= endhour * 60
                        + endmin) {

                    descriptors.add(formatEventDescriptor(e));
                }
            }
        }

    }

	private String formatEventDescriptor(Event e) {
		return (e.getName() + "\n" + e.getYear() + "-" + e.getMonth() + "-"
				+ e.getDay() + " From " + UtilityClass.formatTime(e.getStartTime()) +
                                " to " + UtilityClass.formatTime(e.getEndTime()));
	}

	//to use for comparing time
	public int valueoftime(String t) {
		int i = Integer.parseInt(t);
		int h = i / 60;
		int m = i - 60 * h;
		return h * 60 + m;
	}

	//set the date of today
	private void getToday() {
        // Update to calculate the current day/month
        Calendar calendar = Calendar.getInstance();
        setDate(calendar);
	}

	// set the date to yesterday
	private void getYesterday() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        setDate(calendar);
	}

	// set the date to tomorrow
	private void getTomorrow() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        setDate(calendar);
	}

    private void setDate(Calendar calendar){
        day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        //In Calendar, January is represented by constant 0
        month = String.valueOf(calendar.get(Calendar.MONTH)+ 1);
        year = String.valueOf(calendar.get(Calendar.YEAR));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if(args !=null){

            currentLocID = args.getString("myLocationId");
            Log.d(TAG,"myLocationId:" + currentLocID);
            starthour = args.getInt("starthour",-1);
            startmin = args.getInt("startmin",-1);
            endhour = args.getInt("endhour",-1);
            endmin = args.getInt("endmin",-1);

        }

    }
	


}
