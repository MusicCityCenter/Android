package org.magnum.mcc.events;

import org.magnum.mccmap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Eventconference extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conf);
		
		/*
		 * staff about which conference an event belongs to need to be built
		 * 
		*/
		
		Button ok = (Button) this.findViewById(R.id.button);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Eventconference.this, EventListforthreedays.class);
				startActivity(i);
			}
		});
	}
}
