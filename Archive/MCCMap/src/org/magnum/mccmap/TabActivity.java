package org.magnum.mccmap;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;

public class TabActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		makeTabs();
		
	}
	
	private void makeTabs() {
		ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    
	    Tab tab = actionBar.newTab();
	    tab.setText(R.string.map_tab_title)
	    	.setTabListener(new MapTabListener(this));
	    actionBar.addTab(tab);
	    
	    tab = actionBar.newTab();
	    tab.setText(R.string.events_tab_title)
	    	.setTabListener(new EventTabListener(this));
	    actionBar.addTab(tab);
	}
	
}
