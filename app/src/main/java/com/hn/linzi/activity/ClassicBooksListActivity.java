package com.hn.linzi.activity;

import com.aphidmobile.flip.FlipViewController;
import com.hn.linzi.R;
import com.hn.linzi.adapter.ClassicBookListAdapter;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class ClassicBooksListActivity extends BaseActivity {

	private FlipViewController flipView;
	private ClassicBookListAdapter adapter;
	public final static int REFRESH = 111;
	public final static int GETDATA = 222;
	public final static int ADAPTER = 333;
	private String type;
	private Button back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Intent intent = getIntent();
		String url = intent.getExtras().getString("url");
		type = intent.getExtras().getString("type");
		flipView = new FlipViewController(this);
		adapter = new ClassicBookListAdapter(flipView, ClassicBooksListActivity.this, this, url, type, intent.getExtras().getString("word"));
//		flipView.setAdapter(adapter);
        setContentView(flipView);

		
		ActionBar actionBar = getActionBar();
		actionBar.show();actionBar.hide();
		actionBar.setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
	}
	
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ClassicBooksListActivity.REFRESH:
				System.out.println("接受handleMessage");
				flipView.setAdapter(adapter);
				break;
			case ClassicBooksListActivity.GETDATA:
				System.out.println("adapter.getCount()============="+adapter.getCount());
				System.out.println("GETDATA");
				adapter.getMoreData();
				break;
			case ClassicBooksListActivity.ADAPTER:
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
			finish();
			break;
		
		}
		return super.onOptionsItemSelected(item);
	}

	
	
}
