package org.magnum.mccmap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TestFrag extends Fragment {
	
	private String fragText_;
	
	public static TestFrag newInstance(String fragText) {
		TestFrag tf = new TestFrag();
		Bundle b = new Bundle();
		b.putString("text", fragText);
		tf.setArguments(b);
		return tf;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragText_ = savedInstanceState.getString("text");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_test, null);
		TextView teeVee = (TextView) v.findViewById(R.id.frag_text);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
