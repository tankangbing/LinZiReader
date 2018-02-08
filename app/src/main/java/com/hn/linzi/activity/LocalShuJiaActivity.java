package com.hn.linzi.activity;

import com.aphidmobile.flip.FlipViewController;
import com.hn.linzi.R;
import com.hn.linzi.adapter.LocalShuJiaAdapter;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class LocalShuJiaActivity extends BaseActivity {
	
	private FlipViewController flipView;
	
	private MyHandler handler = null;  
	
	private static final int CHANGED = 0x0010;
	
	private MyReceiver myReceiver;
	
	private TextView title1;
	private TextView title2;
	private TextView title3;
	private TextView title4;
	private TextView title5;
	private TextView title6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		flipView = new FlipViewController(this);
        
        flipView.setAdapter(new LocalShuJiaAdapter(flipView, this));
        
        setContentView(flipView);
        
//        flipView.setSelection(2);
        
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.wr.download");
		getApplicationContext().registerReceiver(myReceiver, intentFilter);
	}
	
	protected void onResume() {
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.wr.download");
		getApplicationContext().registerReceiver(myReceiver, intentFilter);
		super.onResume();
	}

	public final class MyHandler extends Handler {  
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
            if(msg.what == CHANGED) { // 更新UI  
            	
            }  
        }  
    }  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	protected void onStop() {
		getApplicationContext().unregisterReceiver(myReceiver);
		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_top:
			flipView.setSelection(0);
			break;
		case android.R.id.home:
			finish();
			Intent intent = new Intent();
			intent.setClass(this, MainMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			Intent intent = new Intent();
			intent.setClass(this, MainMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public class MyReceiver extends BroadcastReceiver{
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub 
			title1 = (TextView)findViewById(R.id.localshujia_booktitle1);
	        title2 = (TextView)findViewById(R.id.localshujia_booktitle2);
	        title3 = (TextView)findViewById(R.id.localshujia_booktitle3);
	        title4 = (TextView)findViewById(R.id.localshujia_booktitle4);
	        title5 = (TextView)findViewById(R.id.localshujia_booktitle5);
	        title6 = (TextView)findViewById(R.id.localshujia_booktitle6);
			Bundle bundle = intent.getExtras();
			String name = bundle.getString("name", "");
			System.out.println(name + "                     下载完成");
			System.out.println("title1====" + title1.getText().toString());
			System.out.println("title2====" + title2.getText().toString());
			System.out.println("title3====" + title3.getText().toString());
			System.out.println("title4====" + title4.getText().toString());
			System.out.println("title5====" + title5.getText().toString());
			System.out.println("title6====" + title6.getText().toString());
		}
		
	}
}
