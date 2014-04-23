package org.magnum.mccmap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

// This activity display the contact info of MCC
public class AboutActivity extends Activity {
	private ImageButton followTwitterBtn;
	private ImageButton followFacebookBtn;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
        followTwitterBtn = (ImageButton) this.findViewById(R.id.imageButton_twitter);
        followFacebookBtn = (ImageButton) this.findViewById(R.id.imageButton_facebook);
        
        followFacebookBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);	
				intent.setData(Uri.parse("http://facebook.com/NashvilleMusicCityCenter"));	
				startActivity(intent);
			}
		});        
        
        followTwitterBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);	
				intent.setData(Uri.parse("http://twitter.com/NashvilleMCC"));	
				startActivity(intent);
			}
		});        
	}
}
