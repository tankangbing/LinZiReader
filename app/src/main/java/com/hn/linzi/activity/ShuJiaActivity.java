package com.hn.linzi.activity;

import com.aphidmobile.flip.FlipViewController;
import com.hn.linzi.R;
import com.hn.linzi.adapter.ShuJiaAdapter;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ShuJiaActivity extends BaseActivity {
	
	private FlipViewController flipView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		flipView = new FlipViewController(this);
        
        flipView.setAdapter(new ShuJiaAdapter(flipView, this));
        
        setContentView(flipView);
        
//        flipView.setSelection(2);
		
		ActionBar actionBar = getActionBar();
		actionBar.show();actionBar.hide();
		actionBar.setDisplayHomeAsUpEnabled(true); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_top:
			flipView.setSelection(0);
			break;
//		case R.id.action_back:
//			flipView.setSelection(0);
//			break;
		
		}
		return super.onOptionsItemSelected(item);
	}

	
}
