package com.hn.linzi.activity;

import com.hn.linzi.R;
import com.hn.linzi.adapter.KeChengManageAdapter;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class KeChengManageActivity extends BaseActivity{
	
	private KeChengManageActivity activity;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kcmanage);
		SharedPreferences sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
		findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
		ListView listView = (ListView) findViewById(R.id.kcmanage_list);
		KeChengManageAdapter adapter = new KeChengManageAdapter(this, listView);
		listView.setAdapter(adapter);
		Button button = (Button) findViewById(R.id.kcmanage_back);
		activity = this;
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(activity, KechengActivity.class);
//				activity.startActivity(intent);
//				activity.finish();
				finish();
			}
		});
	}
}
