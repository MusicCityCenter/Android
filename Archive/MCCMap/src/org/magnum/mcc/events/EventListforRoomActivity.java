package org.magnum.mcc.events;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EventListforRoomActivity extends Activity {
	private JSONArray array;
	private JSONArray array1;
	private ListView listview;
	private List<String> eventname = new ArrayList<String>();
	
	private String day;
	private String month;
	private String year;
	private String floorplanLocationId;
	
	private TextView roomtitle;

	private class Downloadjson extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
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
				//
				// String body=getContent(url);
				array = new JSONArray(result);
				array1 = new JSONArray();
				for (int i1 = 0; i1 < array.length(); i1++) {
					JSONObject obj = array.getJSONObject(i1);
					String name = obj.getString("name");
					String description = obj.getString("description");
					String day = obj.getString("day");
					String month = obj.getString("month");
					String year = obj.getString("year");
					if (obj.getString("floorplanLocationId").equals(floorplanLocationId)) {
						eventname.add(name + "\n" + year + "-" + month + "-"
								+ day);
						array1.put(obj);
					}
				}
				if(eventname.isEmpty()){
					eventname.add("no event today");
				}

			} catch (Exception e) {
			}
			listview.setAdapter(new ArrayAdapter<String>(EventListforRoomActivity.this,
					android.R.layout.simple_list_item_1, eventname));
		}

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlist_forroom);

		Intent i = getIntent();
		day = i.getStringExtra("day");
		month = i.getStringExtra("month");
		year = i.getStringExtra("year");
		floorplanLocationId = i.getStringExtra("floorplanLocationId");

		roomtitle =  (TextView) this.findViewById(R.id.roomtitle);
		roomtitle.setText("Room  "+ floorplanLocationId);
		
		String server = "http://0-1-dot-mcc-backend.appspot.com";
		String baseUrl = "/mcc/events/full-test-1/on/" + month + "/" + day
				+ "/" + year;
		String url = server + baseUrl;

		listview = (ListView) this.findViewById(R.id.listview1);

		Downloadjson task = new Downloadjson();
		task.execute(url);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EventListforRoomActivity.this,
						EventDetailActivity.class);
				try {
					if(!eventname.get(0).equals("no event today")){
						intent.putExtra("floorplanId", array1.getJSONObject(arg2)
								.getString("floorplanId"));
						intent.putExtra("eventId", array1.getJSONObject(arg2)
								.getString("id"));
						intent.putExtra("endId", "null");
						intent.putExtra("day", day);
						intent.putExtra("month", month);
						intent.putExtra("year", year);
						startActivity(intent);	
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		
	}
}
