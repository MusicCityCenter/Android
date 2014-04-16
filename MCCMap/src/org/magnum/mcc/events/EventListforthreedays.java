package org.magnum.mcc.events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.magnum.mccmap.R;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class EventListforthreedays extends Activity {
	/** Used for debugging */
	private final String TAG = this.getClass().getSimpleName();

	private EventController eventController_;
	private EventsListener l;

	private String year;
	private String month;
	private String day;

	/** Called when the activity is first created. */
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;

	private ListView listviewToday;
	private ListView listviewTomorrow;
	private ListView listviewYestoday;

	private JSONArray array;
	private List<String> eventnametoday = new ArrayList<String>();
	private List<String> eventnameyesterday = new ArrayList<String>();
	private List<String> eventnametomorrow = new ArrayList<String>();

	private List<Event> eventlisttoday = new ArrayList<Event>();
	private List<Event> eventlistyesterday = new ArrayList<Event>();
	private List<Event> eventlisttomorrow = new ArrayList<Event>();


	private int starthour;
	private int startmin;
	private int endhour;
	private int endmin;

	private String formatEventDescriptor(Event e) {
		return (e.getName() + "\n" + e.getYear() + "-" + e.getMonth() + "-"
				+ e.getDay() + " From " + time(e.getStartTime()) + " to " + time(e
					.getEndTime()));
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

	//to use for comparing time
	public int valueoftime(String t) {
		int i = Integer.parseInt(t);
		int h = i / 60;
		int m = i - 60 * h;
		return h * 60 + m;
	}

	//set the date of today
	private void getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String s = sdf.format(date);
		year = s.substring(0, 4);
		if (s.substring(4).equals("0")) {
			month = s.substring(5);
		} else {
			month = s.substring(4, 6);
		}
		if (s.substring(6).equals("0")) {
			day = s.substring(7);
		} else {
			day = s.substring(6, 8);
		}
	}

	// get the url where yesterday's events store
	private String urlyesterday(String server) {
		String d, m, y;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		long t = c.getTimeInMillis();
		long l = t - 24 * 3600 * 1000;
		Date date = new Date(l);
		String s = sdf.format(date);
		y = s.substring(0, 4);
		if (s.substring(4).equals("0")) {
			m = s.substring(5);
		} else {
			m = s.substring(4, 6);
		}
		if (s.substring(6).equals("0")) {
			d = s.substring(7);
		} else {
			d = s.substring(6, 8);
		}

		String url = server + "/mcc/events/full-test-1/on/" + m + "/" + d + "/"
				+ y;

		return url;
	}

	// get the url where tomorrow's events store
	private String urltomorrow(String server) {
		String d, m, y;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		long t = c.getTimeInMillis();
		long l = t + 24 * 3600 * 1000;
		Date date = new Date(l);
		String s = sdf.format(date);
		y = s.substring(0, 4);
		if (s.substring(4).equals("0")) {
			m = s.substring(5);
		} else {
			m = s.substring(4, 6);
		}
		if (s.substring(6).equals("0")) {
			d = s.substring(7);
		} else {
			d = s.substring(6, 8);
		}

		String url = server + "/mcc/events/full-test-1/on/" + m + "/" + d + "/"
				+ y;

		return url;
	}

	private Event jsontoevent(JSONObject obj) throws JSONException {
		Event e = new Event();
		e.setId(obj.getString("id"));
		e.setName(obj.getString("name"));
		e.setDescription(obj.getString("description"));
		e.setDay(obj.getString("day"));
		e.setMonth(obj.getString("month"));
		e.setYear(obj.getString("year"));
		e.setEndTime(obj.getString("endTime"));
		e.setStartTime(obj.getString("startTime"));
		e.setFloorplanId(obj.getString("floorplanId"));
		e.setFloorplanLocationId(obj.getString("floorplanLocationId"));
		return e;
	}

	private class Downloadjsontoday extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				Log.d(TAG, "start connect");
				StringBuilder sb = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = client.getParams();
				// set Timeout
				HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				HttpResponse response = client.execute(new HttpGet(params[0]));
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"),
							8192);

					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					reader.close();
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				Log.d(null, "connect ends");
				array = new JSONArray(result);
				for (int i1 = 0; i1 < array.length(); i1++) {
					JSONObject obj = array.getJSONObject(i1);
					String startTime = obj.getString("startTime");
					String endTime = obj.getString("endTime");
					Event e = jsontoevent(obj);
					eventlisttoday.add(e);
					if (starthour == -1) {
						eventnametoday.add(formatEventDescriptor(e));
					} else {
						if (valueoftime(startTime) >= starthour * 60 + startmin
								&& valueoftime(endTime) <= endhour * 60
										+ endmin) {
							eventnametoday.add(formatEventDescriptor(e));
						}
					}
				}
			} catch (Exception e) {
			}
			
			Collections.sort((eventlisttoday), new EventCompare());
			eventnametoday.clear();
			for(Event e:eventlisttoday){
			   eventnametoday.add(formatEventDescriptor(e));
			}
			
			listviewToday.setAdapter(new ArrayAdapter<String>(
					EventListforthreedays.this,
					android.R.layout.simple_list_item_1, eventnametoday));
		}
	}

	private class Downloadjsonyesterday extends
			AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				Log.d(null, "start connect");
				StringBuilder sb = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = client.getParams();
				// set Timeout
				HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				HttpResponse response = client.execute(new HttpGet(params[0]));
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"),
							8192);

					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					reader.close();
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				Log.d(null, "connect ends");
				array = new JSONArray(result);
				for (int i1 = 0; i1 < array.length(); i1++) {
					JSONObject obj = array.getJSONObject(i1);
					String startTime = obj.getString("startTime");
					String endTime = obj.getString("endTime");
					Event e = jsontoevent(obj);
					eventlistyesterday.add(e);
					if (starthour == -1) {
						eventnameyesterday.add(formatEventDescriptor(e));
					} else {
						if (valueoftime(startTime) >= starthour * 60 + startmin
								&& valueoftime(endTime) <= endhour * 60
										+ endmin) {
							eventnameyesterday.add(formatEventDescriptor(e));
						}
					}
				}
			} catch (Exception e) {
			}
			
			Collections.sort((eventlistyesterday), new EventCompare());
			eventnameyesterday.clear();
			for(Event e:eventlistyesterday){
			   eventnameyesterday.add(formatEventDescriptor(e));
			}
			
			listviewYestoday.setAdapter(new ArrayAdapter<String>(
					EventListforthreedays.this,
					android.R.layout.simple_list_item_1, eventnameyesterday));
		}
	}

	private class Downloadjsontomorrow extends
			AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				Log.d(null, "start connect");
				StringBuilder sb = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = client.getParams();
				// set Timeout
				HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				HttpResponse response = client.execute(new HttpGet(params[0]));
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"),
							8192);

					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					reader.close();
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				Log.d(null, "connect ends");
				array = new JSONArray(result);
				for (int i1 = 0; i1 < array.length(); i1++) {
					JSONObject obj = array.getJSONObject(i1);
					String startTime = obj.getString("startTime");
					String endTime = obj.getString("endTime");
					Event e = jsontoevent(obj);
					eventlisttomorrow.add(e);
					if (starthour == -1) {
						eventnametomorrow.add(formatEventDescriptor(e));
					} else {
						if (valueoftime(startTime) >= starthour * 60 + startmin
								&& valueoftime(endTime) <= endhour * 60
										+ endmin) {
							eventnametomorrow.add(formatEventDescriptor(e));
						}
					}
				}
			} catch (Exception e) {
			}
			
			Collections.sort((eventlisttomorrow), new EventCompare());
			eventnametomorrow.clear();
			for(Event e:eventlisttomorrow){
			   eventnametomorrow.add(formatEventDescriptor(e));
			}
			
			listviewTomorrow.setAdapter(new ArrayAdapter<String>(
					EventListforthreedays.this,
					android.R.layout.simple_list_item_1, eventnametomorrow));
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlist);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagertitle);

		Intent i = getIntent();
		starthour = i.getIntExtra("starthour", -1);
		startmin = i.getIntExtra("startmin", -1);
		endhour = i.getIntExtra("endhour", -1);
		endmin = i.getIntExtra("endmin", -1);

		// load the layout of three views
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.view1, null);
		View view2 = mLi.inflate(R.layout.view2, null);
		View view3 = mLi.inflate(R.layout.view3, null);

		listviewYestoday = (ListView) view1.findViewById(R.id.listview);
		listviewToday = (ListView) view2.findViewById(R.id.listview);
		listviewTomorrow = (ListView) view3.findViewById(R.id.listview);

		int port = 80;
		String server = "http://0-1-dot-mcc-backend.appspot.com";
		getToday();
		String baseUrl = "/mcc/events/full-test-1/on/" + month + "/" + day
				+ "/" + year;
		Log.d(null, baseUrl);
		// String baseUrl = "/mcc/events/full-test-1/on/5/9/2014";
		String url = server + baseUrl;
		Log.d(null, "log into"+url);
		/*
		 * need to set today's date here do not need to receive date from
		 * Mainactivity
		 */

		Downloadjsontoday task = new Downloadjsontoday();
		task.execute(url);

		Downloadjsonyesterday task1 = new Downloadjsonyesterday();
		task1.execute(urlyesterday(server));

		Downloadjsontomorrow task2 = new Downloadjsontomorrow();
		task2.execute(urltomorrow(server));

		// views for viewpager
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);

		final ArrayList<String> titles = new ArrayList<String>();
		titles.add("yesterday");
		titles.add("today");
		titles.add("tomorrow");

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

		Button filter = (Button) this.findViewById(R.id.filter);
		filter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EventListforthreedays.this,
						EventFilter.class);
				startActivity(intent);
			}
		});

		Button conf = (Button) this.findViewById(R.id.conf);
		conf.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EventListforthreedays.this,
						Eventconference.class);
				startActivity(intent);
			}
		});

		listviewToday.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(EventListforthreedays.this,
						EventDetailActivity.class);
				try {
					Event t = eventlisttoday.get(arg2);
					intent.putExtra("floorplanId", t.getFloorplanId());
					intent.putExtra("eventId", t.getId());
					intent.putExtra("endId", t.getFloorplanLocationId());
					intent.putExtra("day", t.getDay());
					intent.putExtra("month", t.getMonth());
					intent.putExtra("year", t.getYear());
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		listviewTomorrow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(EventListforthreedays.this,
						EventDetailActivity.class);
				try {
					Event t = eventlisttomorrow.get(arg2);
					intent.putExtra("floorplanId", t.getFloorplanId());
					intent.putExtra("eventId", t.getId());
					intent.putExtra("endId", t.getFloorplanLocationId());
					intent.putExtra("day", t.getDay());
					intent.putExtra("month", t.getMonth());
					intent.putExtra("year", t.getYear());
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		listviewYestoday.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(EventListforthreedays.this,
						EventDetailActivity.class);
				try {
					Event t = eventlistyesterday.get(arg2);
					intent.putExtra("floorplanId", t.getFloorplanId());
					intent.putExtra("eventId", t.getId());
					intent.putExtra("endId", t.getFloorplanLocationId());
					intent.putExtra("day", t.getDay());
					intent.putExtra("month", t.getMonth());
					intent.putExtra("year", t.getYear());
					startActivity(intent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

}
