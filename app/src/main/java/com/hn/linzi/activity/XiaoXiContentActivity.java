package com.hn.linzi.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.hn.linzi.R;
import com.hn.linzi.utils.ParseJson;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class XiaoXiContentActivity extends BaseActivity {
	public static final int REFRESH = 333;
	private TextView content;
	private String text;
	private Button back;
	private String id;
	private SharedPreferences sp;
	private TelephonyManager tm;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.show(); actionBar.hide();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.xiaoxi_content);
		back = (Button) findViewById(R.id.xiaoxicontent_back);
		sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
		tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView title = (TextView)findViewById(R.id.xiaoxiContent_title);
		content = (TextView)findViewById(R.id.xiaoxiContent_content);
		TextView dt = (TextView)findViewById(R.id.xiaoxiContent_dt);
		TextView send = (TextView)findViewById(R.id.xiaoxiContent_send);
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		title.setText(bundle.getString("title"));
		dt.setText("发送时间：" + bundle.getString("dt"));
		send.setText("发件人：" + bundle.getString("send"));
		getContent();
	}

	private void getContent() {
		new Thread(){

			@Override
			public void run() {
				String contentURL = "http://data.iego.net:88/m/msgContent.aspx?" + "&n=" + sp.getString("UserName", "") + "&d=" + sp.getString("KEY", "") + "&id=" + id;
						//n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&id=172";
				URL url;
				try {
					url = new URL(contentURL);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream(); 
						ParseJson parseJson = new ParseJson();
						text = parseJson.parseXiaoxiContent(inStream);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message message = new Message();
		        message.what = XiaoXiContentActivity.REFRESH;
		        handler.sendMessage(message);
				super.run();
			}
			
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent();
			intent.setClass(this, MainMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		
		}
		return super.onOptionsItemSelected(item);
	}

	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case XiaoXiContentActivity.REFRESH:
				content.setText(text);
				break;
			}
			super.handleMessage(msg);
		}
	};
}
