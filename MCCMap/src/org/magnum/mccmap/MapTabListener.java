package org.magnum.mccmap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Fragment;

public class MapTabListener implements ActionBar.TabListener {
	
	// I anticipate managing the transitions for the several Map tab fragments here
	private MapFragment mCurFrag;
	private static final String TAG_CURRENT_FRAG = "tag-current-frag";
	private final Activity mActivity; 
	
	public MapTabListener(Activity activity) {
		mActivity = activity;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// Usually do nothing
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if(mCurFrag == null) {
			mCurFrag = (MapFragment) Fragment.instantiate(mActivity, PrimaryMapFragment.class.getName());
			ft.add(android.R.id.content, mCurFrag, TAG_CURRENT_FRAG);
		} else {
			ft.attach(mCurFrag);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if(mCurFrag != null) {
			ft.detach(mCurFrag);
		}
	}

}
