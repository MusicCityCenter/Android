package org.magnum.mccmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PrimaryEventFragment extends EventFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.eventlist, null);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
}
