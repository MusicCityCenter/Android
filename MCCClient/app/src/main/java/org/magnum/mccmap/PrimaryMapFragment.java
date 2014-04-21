package org.magnum.mccmap;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PrimaryMapFragment extends MapFragment {

	private MapFragmentCallback mCallback;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mCallback = (MapFragmentCallback) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement MapFragmentCallback!");
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.maproute_activity, null);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
}
