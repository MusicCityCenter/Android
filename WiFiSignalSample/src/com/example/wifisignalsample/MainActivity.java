package com.example.wifisignalsample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private WifiManager mainWifi;
	private TextView status;
	private Button sampleBtn;
    private final int repeatNum = 3; //repeat scanning wifi signal and take average
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		status = (TextView) findViewById(R.id.status);
		sampleBtn = (Button) findViewById(R.id.button_sample);
		sampleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scanWifi();
			}
		});

		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

	}

	private void scanWifi() {
		mainWifi.startScan();
		List<ScanResult> wifiList = mainWifi.getScanResults();
		ArrayList<WifiConnectionBean> m4MessagesList = new ArrayList<WifiConnectionBean>();
		String result="";
		for (int i = 0; i < wifiList.size(); i++) {
			ScanResult scanResult = wifiList.get(i);
			WifiConnectionBean bean = new WifiConnectionBean();
			bean.setName(scanResult.SSID); 
			bean.setDescription(scanResult.capabilities);
			bean.setId(scanResult.SSID);
			bean.setLevel(String.valueOf(scanResult.level));
			bean.setSignalStrength(String.valueOf(scanResult.BSSID));

			result += "BSSID:" + scanResult.BSSID + "  Strength:"
					+ scanResult.level;

			Log.d("WifiSignal", "SSID:" + scanResult.SSID + "   BSSID:"
					+ scanResult.BSSID + "  level:" + scanResult.level
					+ "  capabilities:" + scanResult.capabilities);

			m4MessagesList.add(bean);
		}
		status.setText(result);

		Log.d("WifiSignal", "Scanning complete." + m4MessagesList.size()
				+ " connections found!");

	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
