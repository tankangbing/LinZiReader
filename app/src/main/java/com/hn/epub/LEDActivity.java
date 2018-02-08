package com.hn.epub;

import com.hn.linzi.R;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;

/**   
* @Description: 
*/ 
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LEDActivity extends Activity {

	private LEDView ledView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_led);
		ledView = (LEDView) findViewById(R.id.ledview);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ledView.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		ledView.stop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_led, menu);
		return true;
	}

}
