package com.example.wifisignalsample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	private WifiManager mainWifi;
	private TextView status;
	private EditText tagText;
	private Button sampleBtn;
	private Button saveBtn;
	private Button positionBtn;
	private Button resetBtn;
	private Map<String, Map<String, Integer>> StrengthDataset = new HashMap<String, Map<String, Integer>>();
	private List<Map<String, Integer>> sampleSequence = new ArrayList<Map<String, Integer>>();

	private Map<String, Integer> temp = new HashMap<String, Integer>();

	private ListView list, list_head;
	private ArrayList<HashMap<String, String>> mylist, mylist_title;
	private ListAdapter adapter_title, adapter;
	private HashMap<String, String> map1, map2;

	private ArrayList<WifiConnectionBean> m4MessagesList = new ArrayList<WifiConnectionBean>();

	private final int repeatNum = 3; // repeat scanning wifi signal and take
										// average
	private int count = 0; //record the number of sample have been done

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		status = (TextView) findViewById(R.id.textView_status);
		tagText = (EditText) findViewById(R.id.editText_tag);
		list = (ListView) findViewById(R.id.listView_result);
		list_head = (ListView) findViewById(R.id.listView_head);

		sampleBtn = (Button) findViewById(R.id.button_sample);
		saveBtn = (Button) findViewById(R.id.button_save);
		positionBtn = (Button) findViewById(R.id.button_where);
		resetBtn = (Button) findViewById(R.id.button_reset);
		
		sampleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				temp = scanWifi();
				count ++;
				showList();
			}
		});

		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				averageSignal();
				count =0;
			}
		});

		positionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String, Integer> myposition = scanWifi();
				findNearest(myposition);
			}
		});

		resetBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StrengthDataset.clear();
				sampleSequence.clear();
				temp.clear();
				count = 0;
			}
		});
		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

	}

	private Map<String, Integer> scanWifi() {
		m4MessagesList = new ArrayList<WifiConnectionBean>();
		
		mainWifi.startScan();
		List<ScanResult> wifiList = mainWifi.getScanResults();
		
		Map<String, Integer> WifiSignal = new HashMap<String, Integer>();

		for (int i = 0; i < wifiList.size(); i++) {
			ScanResult scanResult = wifiList.get(i);

			WifiSignal.put(scanResult.BSSID, scanResult.level);

			WifiConnectionBean bean = new WifiConnectionBean();
			bean.setName(scanResult.SSID);
			bean.setDescription(scanResult.capabilities);
			bean.setSignalStrength(scanResult.level);
			bean.setBSSID(scanResult.BSSID);

			Log.d("WifiSignal", "SSID:" + scanResult.SSID + "   BSSID:"
					+ scanResult.BSSID + "  level:" + scanResult.level
					+ "  capabilities:" + scanResult.capabilities);

			m4MessagesList.add(bean);
		}

		Collections.sort(m4MessagesList, new WifiCompare());

		Log.d("WifiSignal", "Scanning complete." + m4MessagesList.size()
				+ " connections found!");

		sampleSequence.add(WifiSignal);

		return WifiSignal;

	}

	private void averageSignal() {
		String tag = tagText.getText().toString();
		Map<String, List<Integer>> total = new HashMap<String, List<Integer>>();
		Map<String, Integer> average = new HashMap<String, Integer>();
		for(Map<String, Integer> w:sampleSequence){
			for (Map.Entry<String, Integer> m : w.entrySet())
			{
				String ssid = m.getKey();
				int level = m.getValue();
				if(total.containsKey(ssid))
					total.get(ssid).add(level);
				else{
					List<Integer> t = new ArrayList<Integer>();
					t.add(level);
					total.put(ssid, t);
				}
				
			}
		}
		
		for (Map.Entry<String, List<Integer>> w : total.entrySet())
		{
			String ssid = w.getKey();
			List<Integer> levellist = w.getValue();
			int sum=0;
			for(Integer i:levellist){
				sum+=i;
			}
			sum = sum/levellist.size();
			average.put(ssid, sum);
			Log.d("Average result. SSID:", ssid + "strength:"+ sum);
		}
		
		StrengthDataset.put(tag, temp);
 
	}

	private void showList() {
		mylist = new ArrayList<HashMap<String, String>>();
		mylist_title = new ArrayList<HashMap<String, String>>();

		/********** Display the headings ************/

		map1 = new HashMap<String, String>();

		map1.put("Name", "Name");
		map1.put("SSID", "SSID");
		map1.put("signal_strength", " Strength");
		mylist_title.add(map1);

		try {
			adapter_title = new SimpleAdapter(this, mylist_title, R.layout.row,
					new String[] { "Name", "SSID", "signal_strength" },
					new int[] {R.id.Name, R.id.SSID, R.id.signal_strength });
			list_head.setAdapter(adapter_title);
		} catch (Exception e) {

		}

		/********** Display the contents ************/

		for (WifiConnectionBean w : m4MessagesList) {

			map2 = new HashMap<String, String>();

			map2.put("Name", w.getName());
			map2.put("SSID", w.getBSSID());
			map2.put("signal_strength", String.valueOf(w.getSignalStrength()));

			mylist.add(map2);
		}

		try {
			adapter = new SimpleAdapter(this, mylist, R.layout.row,
					new String[] { "Name", "SSID", "signal_strength" },
					new int[] {R.id.Name, R.id.SSID, R.id.signal_strength });
			list.setAdapter(adapter);
		} catch (Exception e) {

		}
		status.setText(count+ "th sample");

	}

	private void findNearest(Map<String, Integer> myposition) {
		int mindist = 10000;
		String minDistTag="";
		//for each location
		for (Map.Entry<String, Map<String, Integer>> entry : StrengthDataset
				.entrySet()) {
			String key = entry.getKey();
			Log.d("key:", key);
			Map<String, Integer> value = entry.getValue();
			int distance = 0;
			
			//for each unique wifi signal
			for (Map.Entry<String, Integer> m : myposition.entrySet())
			{
				String ssid = m.getKey();
				int level = m.getValue();
				if(value.get(ssid)!=null){
					int target = value.get(ssid);
					distance+= Math.abs(level-target);
					
				}
			}
			Log.d("Distance:", String.valueOf(distance));
			if(distance< mindist){
				
				mindist = distance;
				minDistTag = key;
			}
		}
		status.setText("Current position:" + minDistTag);	
		
	}

}
