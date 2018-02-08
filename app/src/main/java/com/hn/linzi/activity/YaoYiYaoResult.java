package com.hn.linzi.activity;

import java.io.UnsupportedEncodingException;

import com.umeng.analytics.MobclickAgent;
import com.hn.linzi.R;
import com.hn.linzi.adapter.YaoYiYaoAdapter;
import com.hn.linzi.data.YaoYiYaoData;
import com.hn.linzi.utils.ParseJson;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class YaoYiYaoResult extends ListActivity {
	
//	private MyAPP mAPP = null;  
//    
//    private MyHandler mHandler = null; 
	private Button back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		YaoYiYaoData data = null;
		ParseJson parseJson = new ParseJson();
		try {
			data = parseJson.parseYaoYiYao(getIntent().getStringExtra("json").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		YaoYiYaoAdapter adapter = new YaoYiYaoAdapter(this, data);
		setListAdapter(adapter);
		setContentView(R.layout.yaoyiyao_result);
		SharedPreferences sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
        findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
		back = (Button) findViewById(R.id.yaoyiyao_result_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
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
			setSelection(0);
			break;
		case android.R.id.home:
			finish();
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
