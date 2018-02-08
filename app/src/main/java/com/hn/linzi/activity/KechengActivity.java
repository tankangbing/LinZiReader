package com.hn.linzi.activity;

import com.aphidmobile.flip.FlipViewController;
import com.hn.linzi.R;
import com.hn.linzi.adapter.KeChengAdapter;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

public class KechengActivity extends BaseActivity {

	private FlipViewController flipView;
	private KeChengAdapter adapter;
	public final static int REFRESH = 111;
	public final static int GETDATA = 222;
	public final static int ADAPTER = 333;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		flipView = new FlipViewController(this);
		adapter = new KeChengAdapter(flipView, KechengActivity.this, this);
		flipView.setAdapter(adapter);
        setContentView(flipView);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		super.onCreate(savedInstanceState);
	}
	
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case KechengActivity.REFRESH:
				System.out.println("接受handleMessage");
				flipView.setAdapter(adapter);
				break;
			case KechengActivity.GETDATA:
				System.out.println("adapter.getCount()============="+adapter.getCount());
				System.out.println("GETDATA");
				adapter.getMoreData();
				break;
			case KechengActivity.ADAPTER:
//				flipView.setAdapter(adapter, adapter.getPage());
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
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
		case android.R.id.home:
			Intent intent = new Intent();
			intent.setClass(this, MainMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		
		}
		return super.onOptionsItemSelected(item);
	}

	
	
}
