package com.example.jsonevent;

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
import org.json.JSONObject;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	private String getContent(String url) throws Exception{
	    StringBuilder sb = new StringBuilder();
	    
	    HttpClient client = new DefaultHttpClient();
	    HttpParams httpParams = client.getParams();
	    //set Timeout
	    HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
	    HttpConnectionParams.setSoTimeout(httpParams, 5000);
	    HttpResponse response = client.execute(new HttpGet(url));
	    HttpEntity entity = response.getEntity();
	    if (entity != null) {
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);
	        
	    	String line = null;
	    	while ((line = reader.readLine())!= null){
	    		sb.append(line + "\n");
	    	}
	    	reader.close();
	    }    
	    return sb.toString();
} 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String server = "http://0-1-dot-mcc-backend.appspot.com";
		String baseUrl = "/mcc/events/full-test-1/on/5/9/2014";
		
		ListView listview=(ListView)this.findViewById(R.id.listview1);
		List<String> eventname=new ArrayList<String>();
		
		
		
		try{
			String url=server+baseUrl;
			String body=getContent(url);
			JSONArray array= new JSONArray(body);
			for(int i1=0;i1<array.length();i1++){
				JSONObject obj= array.getJSONObject(i1);
				String name=obj.getString("name");
				String description=obj.getString("description");
				String day=obj.getString("day");
				String month=obj.getString("month");
				String year=obj.getString("year");
				eventname.add(name+"\n"+year+"-"+month+"-"+day);
			}
		}catch(Exception e){} 
		
		listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,eventname));
	
		//button to click-expand
		Button button=(Button)this.findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(MainActivity.this,expand.class );
				startActivity(intent);
			}
		});
		
	}

	

}
