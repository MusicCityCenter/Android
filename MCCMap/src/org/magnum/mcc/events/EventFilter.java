package org.magnum.mcc.events;



import org.magnum.mccmap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class EventFilter extends Activity {
	private int starthour;
	private int startmin;
	private int endhour;
	private int endmin;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter);
		
		TimePicker starttime = (TimePicker) this.findViewById(R.id.starttimePicker);
		TimePicker endtime = (TimePicker) this.findViewById(R.id.endtimePicker2);
		
		starttime.setIs24HourView(true);
		endtime.setIs24HourView(true);
		
		starttime.setOnTimeChangedListener(new OnTimeChangedListener() {
	        @Override
	        public void onTimeChanged(TimePicker arg0, int hour, int minute) {
	            starthour = hour;
	            startmin = minute;     
	         }  
	   });
		
		endtime.setOnTimeChangedListener(new OnTimeChangedListener() {
	        @Override
	        public void onTimeChanged(TimePicker arg0, int hour, int minute) {
	            endhour = hour;
	            endmin = minute;     
	         }  
	   });
		
		
		Button ok = (Button) this.findViewById(R.id.button);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EventFilter.this, EventListforthreedays.class);
				i.putExtra("starthour", starthour);
				i.putExtra("startmin", startmin);
				i.putExtra("endhour", endhour);
				i.putExtra("endmin", endmin);
				startActivity(i);
			}
		});
	}
	
	
}
