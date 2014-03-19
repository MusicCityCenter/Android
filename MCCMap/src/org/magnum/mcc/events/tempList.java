package org.magnum.mcc.events;


import org.magnum.mccmap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tempList extends Activity {

	private Button button1, button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.templist);

		button1 = (Button) this.findViewById(R.id.button_allevent);
		button2 = (Button) this.findViewById(R.id.button_roomevent);

		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(tempList.this,
						EventListforDate.class);
				intent.putExtra("day", "3");
				intent.putExtra("month", "19");
				intent.putExtra("year", "2014");
				startActivity(intent);
			}
		});

		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(tempList.this,
						EventListforRoom.class);
				intent.putExtra("day", "3");
				intent.putExtra("month", "19");
				intent.putExtra("year", "2014");
				intent.putExtra("floorplanLocationId", "101");
				startActivity(intent);
			}
		});
	}

}
