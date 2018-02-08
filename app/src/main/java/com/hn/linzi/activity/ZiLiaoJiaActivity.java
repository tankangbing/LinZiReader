package com.hn.linzi.activity;

import com.umeng.analytics.MobclickAgent;
import com.hn.linzi.R;
import com.hn.linzi.adapter.ZiLiaoJiaAdapter;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ZiLiaoJiaActivity extends ListActivity {
	
//	private FlipViewController flipView;
	private Button back;
	private Button manage;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		ZiLiaoJiaAdapter adapter = new ZiLiaoJiaAdapter(this);
		if (adapter.ziLiaoJiaData.IMG_DESCRIPTIONS.size() == 0) {
			setContentView(R.layout.kong_zlj);
			System.out.println("没数据");
		} else {
			System.out.println("有数据");
			setContentView(R.layout.ziliaojia);
			setListAdapter(adapter);
			
			manage = (Button) findViewById(R.id.ziliaojia_manage);
			manage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, ZiLiaoJiaManageActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					Bundle bundle = new Bundle();
					bundle.putString("shujia", "shujia");
					intent.putExtras(bundle);
					context.startActivity(intent);
					((ZiLiaoJiaActivity) context).finish();
				}
			});
		}
		SharedPreferences sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
        findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
		back = (Button) findViewById(R.id.ziliaojia_back);   
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
//			flipView.setSelection(0);
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		MobclickAgent.onResume(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub.
		MobclickAgent.onPause(this);
		super.onPause();
	}
}
