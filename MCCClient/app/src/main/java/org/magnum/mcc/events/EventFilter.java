package org.magnum.mcc.events;



import org.magnum.mccmap.EventFragment;
import org.magnum.mccmap.R;
import org.magnum.mccmap.R.id;
import org.magnum.mccmap.R.layout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class EventFilter extends Fragment {
    private int starthour;
    private int startmin;
    private int endhour;
    private int endmin;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        TimePicker starttime = (TimePicker) getView().findViewById(R.id.starttimePicker);
        TimePicker endtime = (TimePicker) getView().findViewById(R.id.endtimePicker2);

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


        Button ok = (Button) getView().findViewById(R.id.button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newFragment = new EventFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("starthour", starthour);
                bundle.putInt("startmin", startmin);
                bundle.putInt("endhour", endhour);
                bundle.putInt("endmin", endmin);

                //bundle.putString("starthour", Integer.toString(starthour));
                //bundle.putString("startmin", Integer.toString(startmin));
                //bundle.putString("endhour", Integer.toString(endhour));
                //bundle.putString("endmin", Integer.toString(endmin));


                Log.d("filter","starthour is "+Integer.toString(starthour));
                Log.d("filter","endthour is "+Integer.toString(endhour));

                //System.out.println("starttime is"+ starthour);
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_place, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.filter, container, false);
    }

}
