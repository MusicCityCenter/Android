package com.example.mccmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class map extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
    }
	
	public boolean onTouchEvent(MotionEvent event)
	{
		//int action = event.getAction();
		float x=event.getX();
		float y=event.getY();
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			jumpto(x,y);
			
		}
		return super.onTouchEvent(event);
	}
	
	private void jumpto(float x, float y)
	{
		if(x>0&&y>640)
		{
			Intent intent= new Intent(map.this,floor1.class);
			startActivity(intent);
		}
		
		if(x>0&&y<640&&y>480)
		{
			Intent intent= new Intent(map.this,floor2.class);
			startActivity(intent);
		}
		
		if(x>0&&y<480&&y>360)
		{
			Intent intent= new Intent(map.this,floor3.class);
			startActivity(intent);
		}
		
		if(x>0&&y<360&&y>300)
		{
			Intent intent= new Intent(map.this,floor4.class);
			startActivity(intent);
		}
		
		if(x>0&&y>200&&y<300)
		{
			Intent intent= new Intent(map.this,floor5.class);
			startActivity(intent);
		}
		
		
		
	}
	
}
