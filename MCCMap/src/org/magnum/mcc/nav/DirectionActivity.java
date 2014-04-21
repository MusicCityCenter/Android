package org.magnum.mcc.nav;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DirectionActivity extends Activity {
	private List<String> directions = new ArrayList<String>();
	private TextView currentDirection;
	private ListView directionView;
	// Text to speech function
	private TextToSpeech TTSobj;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(org.magnum.mccmap.R.layout.activity_direction);
		
		currentDirection = (TextView) this.findViewById(org.magnum.mccmap.R.id.textView_current_direction);
		currentDirection.setBackgroundColor(Color.BLUE);
		currentDirection.setTextColor(Color.WHITE);
		directionView = (ListView) this.findViewById(org.magnum.mccmap.R.id.listView_direction);
		directionView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
					String command = directions.get(arg2);	
					TTSobj.speak(command, TextToSpeech.QUEUE_FLUSH, null);
			}
		});
		
		TTSobj=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
			      @Override
			      public void onInit(int status) {
			         if(status != TextToSpeech.ERROR){
			             TTSobj.setLanguage(Locale.UK);
			         }				
			       }
		 });
		retriveCommand();
		
		directionView.setAdapter(new ArrayAdapter<String>(
				DirectionActivity.this,
				android.R.layout.simple_list_item_1, directions));
		
		TTSobj.speak(directions.get(0), TextToSpeech.QUEUE_FLUSH, null);
	}
	
	//get string direction from server. For now, use dummy commands for test
	private void retriveCommand(){
		directions.add("Turn left.");
		directions.add("Walk 20 meters and turn right.");
		directions.add("Arrive destination.");
	}
	@Override
	public void onPause(){
	      if(TTSobj !=null){
	    	 TTSobj.stop();
	    	 TTSobj.shutdown();
	      }
	      super.onPause();
	   }
	

}
