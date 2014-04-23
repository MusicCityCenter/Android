package org.magnum.mcc.events;



import org.magnum.mccmap.EventFragment;
import org.magnum.mccmap.R;
import org.magnum.mccmap.R.id;
import org.magnum.mccmap.R.layout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EventConf extends Fragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Button ok = (Button) getView().findViewById(id.button);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Fragment newFragment = new EventFragment();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(id.fragment_place, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(layout.conf, container, false);
	}

}
