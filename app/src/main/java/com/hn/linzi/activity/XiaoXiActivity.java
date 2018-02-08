package com.hn.linzi.activity;

import com.umeng.analytics.MobclickAgent;
import com.hn.linzi.R;
import com.hn.linzi.adapter.XiaoXiAdapter;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XiaoXiActivity extends ListActivity {
	
	public static final int REFRESH = 222;
	private XiaoXiAdapter xiaoXiAdapter;
	private Button back;
	private LinearLayout linearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		xiaoXiAdapter = new XiaoXiAdapter(this,XiaoXiActivity.this);
		setListAdapter(xiaoXiAdapter);
        setContentView(R.layout.xiaoxi_main);
        linearLayout = (LinearLayout) findViewById(R.id.xiaoxi_allBG);
        
        TextView daohanlan = (TextView) findViewById(R.id.daohanglan);
        daohanlan.setText("消息");
        SharedPreferences sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
        findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
        back = (Button) findViewById(R.id.xiaoximain_back);
		back.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
	}

	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case XiaoXiActivity.REFRESH:
				System.out.println("接受handleMessage");
				setListAdapter(xiaoXiAdapter);
				if(xiaoXiAdapter.xiaoXiData.IMG_DESCRIPTIONS.size() == 0){
					System.out.println("111");
					setContentView(R.layout.kong_xiaoxi);
					
//					setContentView(R.layout.jiaxiaoxi);
					System.out.println("222");
					
					Button button = (Button) findViewById(R.id.xiaoximain_back);
					button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							finish();
						}
					});
				}
				break;
			}
			super.handleMessage(msg);
		}
	};
	
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
