package org.magnum.mccmap;

import android.app.Fragment;

public class MapFragment extends Fragment {

	public interface MapFragmentCallback {
		public void signalTransition(int transitionCode);
	}
	
}
