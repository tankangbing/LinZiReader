package com.hn.linzi.views;

import com.umeng.analytics.MobclickAgent;
import com.hn.linzi.R;

import android.app.Activity;
import android.content.SharedPreferences;

public class BaseActivity extends Activity {
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		MobclickAgent.onResume(this);
		SharedPreferences sp = this.getSharedPreferences("SP",this.MODE_PRIVATE);
		if (findViewById(R.id.activity_bg) != null) {
			findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub.
		MobclickAgent.onPause(this);
		super.onPause();
	}
}
