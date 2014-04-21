package org.magnum.mccmap;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.magnum.mcc.nav.FloorplanFragment;

public class MapFragment extends Fragment {

    private int screenHeight;
    private static final String ZOOM_EXTRA = "ZOOM_LEVEL";
    private static final int ZOOM_1 = 21;
    private static final int ZOOM_1M = 20;
    private static final int ZOOM_2 = 19;
    private static final int ZOOM_3 = 18;
    private static final int ZOOM_4 = 17;

    private ImageButton BtnFloor1;
    private ImageButton BtnFloor1m;
    private ImageButton BtnFloor2;
    private ImageButton BtnFloor3;
    private ImageButton BtnFloor4;


	public interface MapFragmentCallback {
		public void signalTransition(int transitionCode);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.map, container, false);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Bundle args = this.getArguments();
        if(args !=null){


        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);


        BtnFloor1 = (ImageButton) getView().findViewById(R.id.imageButton_1);
        BtnFloor1m = (ImageButton) getView().findViewById(R.id.imageButton_1m);
        BtnFloor2 = (ImageButton) getView().findViewById(R.id.imageButton_2);
        BtnFloor3 = (ImageButton) getView().findViewById(R.id.imageButton_3);
        BtnFloor4 = (ImageButton) getView().findViewById(R.id.imageButton_4);

        BtnFloor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadMapAtLevel(ZOOM_1);
            }
        });

        BtnFloor1m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadMapAtLevel(ZOOM_1M);
            }
        });

        BtnFloor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadMapAtLevel(ZOOM_2);
            }
        });

        BtnFloor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadMapAtLevel(ZOOM_3);
            }
        });

        BtnFloor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadMapAtLevel(ZOOM_4);
            }
        });

    }

    private void LoadMapAtLevel(int zoom_level) {
        Fragment newFragment = new FloorplanFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ZOOM_EXTRA, zoom_level);
        newFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_place, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

//    public boolean onTouchEvent(MotionEvent event)
//    {
//        float x = event.getX();
//        float y = event.getY();
//        if(event.getAction() == MotionEvent.ACTION_DOWN)
//        {
//            goToFloor(x,y);
//
//        }
//        return super.onTouchEvent(event);
//    }


}
