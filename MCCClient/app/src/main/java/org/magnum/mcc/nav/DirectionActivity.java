package org.magnum.mcc.nav;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.magnum.mccmap.MainActivity;
import org.magnum.mccmap.R;
import org.magnum.mccmap.UtilityClass;

/**
 * This fragment shows the turn-by-turn direction as well as the photos for navigation.
 *
 * @author yao
 *
 */
public class DirectionActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

	private TextView currentDirection;
	private ListView directionView;
    private ImageView edgeImagView;
    private Button arriveBtn;

	// Text to speech function
	private TextToSpeech TTSobj;

    private Path path_;
    private Bitmap bmp_;
    private String targetUrl_;
    private List<String> directions = new ArrayList<String>();
    private ArrayAdapter adapter;

    private FloorplanNavigationData navData_;
    private NavController navController_;
    private Handler handler_ = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(org.magnum.mccmap.R.layout.activity_direction);

        Intent i = getIntent();
        final String floorplanId = i.getStringExtra("floorplanId");
        final String startId = i.getStringExtra("startId");
        final String endId = i.getStringExtra("endId");

        String server = UtilityClass.server;
        int port = UtilityClass.port;
        String baseUrl = UtilityClass.baseUrl;
        String NAV_PATH = UtilityClass.NAV_PATH;
        targetUrl_ = server + baseUrl + NAV_PATH + floorplanId + "/"
                + startId + "/" + endId;
        Log.d(TAG, "URL: " + targetUrl_);

		currentDirection = (TextView) this.findViewById(org.magnum.mccmap.R.id.textView_current_direction);
		currentDirection.setBackgroundColor(Color.BLUE);
		currentDirection.setTextColor(Color.WHITE);

        arriveBtn = (Button) this.findViewById(R.id.button_arrive);
        arriveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i= new Intent(DirectionActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        edgeImagView = (ImageView) this.findViewById(R.id.imageView_path);
		directionView = (ListView) this.findViewById(org.magnum.mccmap.R.id.listView_direction);
		directionView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,final int arg2,long arg3) {
					String command = directions.get(arg2);	
					TTSobj.speak(command, TextToSpeech.QUEUE_FLUSH, null);

                    for (int j = 0; j < arg0.getChildCount(); j++)
                        arg0.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                    arg1.setBackgroundColor(Color.LTGRAY);
                    currentDirection.setText(directions.get(arg2));

                    navController_.getPictureOnPath(floorplanId, path_.getEdges().get(arg2),new EdgeImageListener() {

                        @Override
                        public void setImage(Bitmap bmp) {
                            bmp_ = bmp;

                            // Now referesh the UI
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    edgeImagView.setImageBitmap(bmp_);
                                }
                            };
                            handler_.post(r);
                        }
                    });
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


        navController_ = new MappingNavControllerImpl(server, port,
                baseUrl);

        navController_.loadFloorplan(floorplanId, new FloorplanListener() {

            @Override
            public void setFloorplanNavigationData(String floorplanId,
                                                   FloorplanNavigationData fp) {

                navData_ = fp;

                FloorplanLocation start =
                        navData_.getFloorplan().getLocations().get(startId);


                FloorplanLocation end =
                        navData_.getFloorplan().getLocations().get(endId);

                if(start != null && end != null) {
                    navController_.getShortestPath(floorplanId, start, end,
                            new NavigationListener() {

                                @Override
                                public void setPath(Path p) {
                                    path_ = p;

                                    // Now referesh the UI
                                    Runnable r = new Runnable() {
                                        @Override
                                        public void run() {
                                            updatePathDisplay();
                                        }
                                    };
                                    handler_.post(r);
                                }
                            });
                }
            }
        });

		adapter = new ArrayAdapter<String>(DirectionActivity.this,
                android.R.layout.simple_list_item_1, directions);
		directionView.setAdapter(adapter);
		
		// TTSobj.speak(directions.get(0), TextToSpeech.QUEUE_FLUSH, null);
	}

    private void updatePathDisplay(){

        List<FloorplanEdge> edges = path_.getEdges();

        for(int i=1; i < edges.size(); i++){
            FloorplanEdge e = edges.get(i);
            Log.d(TAG,"length:"+ e.getLength()+ " angle"+ e.getAngle()+ " start:"+ e.getStart().getId());

            String description ="";
            double difAngle = edges.get(i).getAngle()- edges.get(i-1).getAngle();

            if( difAngle<=180&& difAngle>0  ||  difAngle<-180)
                description += "Turn left ";
            else
                description += "Turn right ";

            directions.add("Go straight.");

            directions.add(description);
        }

        adapter.notifyDataSetChanged();
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
