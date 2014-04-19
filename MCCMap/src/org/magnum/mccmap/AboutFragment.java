package org.magnum.mccmap;



import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class AboutFragment extends Fragment{
	
	private ImageButton followTwitterBtn;
	private ImageButton followFacebookBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return inflater.inflate(R.layout.activity_about, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		followTwitterBtn = (ImageButton) getView().findViewById(R.id.imageButton_twitter);
        followFacebookBtn = (ImageButton) getView().findViewById(R.id.imageButton_facebook);
        
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
