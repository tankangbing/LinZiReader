package com.hn.linzi.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.aphidmobile.utils.UI;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class NewBooksMainActivity extends BaseActivity {

	protected static final int REFRESH = 4444;
	private GridView gridView;
	private ArrayList<HashMap<String, Object>> item;
	private EditText edit;
	private Button back;
	private Context thiscontext;
	private int w;
	private int[] colorList = {R.drawable.sc_btnbg1, R.drawable.sc_btnbg2, R.drawable.sc_btnbg3, R.drawable.sc_btnbg4};
	private SharedPreferences sp;
	private TelephonyManager tm;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classicbooks_main);
		thiscontext = this;
		TextView daohanlan = (TextView) findViewById(R.id.daohanglan);
		daohanlan.setText("电子书城");
		WindowManager wm = this.getWindowManager();
	    int width = wm.getDefaultDisplay().getWidth();
	    w = width/6 -6;
	    sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
		tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		gridView = (GridView)findViewById(R.id.cbmain_gridview);
		edit =(EditText)findViewById(R.id.cbmain_text);
		edit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if(edit.getText().toString().equals("")){
						Toast.makeText(thiscontext, "搜索不能为空", Toast.LENGTH_SHORT).show();
					}else{
						String url = "";
						try {
							url = "http://data.iego.net:85/m/booklist1Json.aspx?" + "&n=" + sp.getString("UserName", "") + "&d=" + sp.getString("KEY", "") + "&lib=books&size=18&page=1&kwd=" + URLEncoder.encode(edit.getText().toString(), "UTF-8");
									//n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&lib=books&size=18&page=1&kwd=" + URLEncoder.encode(edit.getText().toString(), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Bundle bundle = new Bundle();
						Intent intent = new Intent();
						bundle.putString("url", url);
						bundle.putString("type", "sousuo");
						bundle.putString("word", edit.getText().toString());
						intent.putExtras(bundle);
						intent.setClass(thiscontext, NewBooksListActivity.class);
						thiscontext.startActivity(intent);
					}
					return true;
				}
				return false;
			}
		});
		
		getClassic();
		
		
		back = (Button) findViewById(R.id.classicbook_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		ActionBar actionBar = getActionBar();
		actionBar.show();actionBar.hide();
		actionBar.setDisplayHomeAsUpEnabled(true); 
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

	private void getClassic() {
		new Thread(){

			@Override
			public void run() {
				String content = "http://data.iego.net:85/m/bookClassJson.aspx?lib=books";
				URL url;
				try {
					url = new URL(content);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream();
						ParseJson parseJson = new ParseJson();
						item = parseJson.parseClassicBooks(inStream);
						Message message = new Message();
				        message.what = NewBooksMainActivity.REFRESH;
				        handler.sendMessage(message);
					}
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.run();
			}
			
		}.start();
		
	}
	
	
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewBooksMainActivity.REFRESH:
				GridViewAdapter adapter = new GridViewAdapter(getApplicationContext());
				gridView.setAdapter(adapter);
				
				gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
						if(item.get("ItemName").toString().equals("全部")){
							String url = "http://data.iego.net:85/m/booklist1Json.aspx?" + "&n=" + sp.getString("UserName", "") + "&d=" + sp.getString("KEY", "") + "&lib=books&size=18&page=1";
									//n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&lib=books&size=18&page=1";
							Bundle bundle = new Bundle();
							Intent intent = new Intent();
							bundle.putString("url", url);
							bundle.putString("type", "quanbu");
							intent.putExtras(bundle);
							intent.setClass(thiscontext, NewBooksListActivity.class);
							thiscontext.startActivity(intent);
						}else{
							String url = "";
							try {
								url = "http://data.iego.net:85/m/booklist1Json.aspx?" + "&n=" + sp.getString("UserName", "") + "&d=" + sp.getString("KEY", "") + "&lib=books&size=18&page=1&class1="  + URLEncoder.encode( item.get("ItemName").toString(), "UTF-8");
										//n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&lib=books&size=18&page=1&class1="  + URLEncoder.encode( item.get("ItemName").toString(), "UTF-8");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Bundle bundle = new Bundle();
							Intent intent = new Intent();
							bundle.putString("url", url);
							bundle.putString("type", "fenlei");
							bundle.putString("word", item.get("ItemName").toString());
							intent.putExtras(bundle);
							intent.setClass(thiscontext, NewBooksListActivity.class);
							thiscontext.startActivity(intent);		
						}
					}
				});
				break;

			}
			super.handleMessage(msg);
		}
		
	};
	
	private class GridViewAdapter extends BaseAdapter {
		private Context mContext;

		public GridViewAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return item.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 定义一个ImageView,显示在GridView里
			final Button button;
			// if(convertView==null){
			
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
								R.layout.type_item, null);
			}
			button = UI.<Button>findViewById(convertView, R.id.type_btn);
//			button = new Button(mContext);
//			
//			button.setHeight(w);
//		button.setLayoutParams(new GridView.LayoutParams(w,w));
			// button.setScaleType(Button.ScaleType.CENTER_CROP);
//			button.setPadding(4, 4, 4, 4);
			
			
			// }else{
			// button = (Button) convertView;
			// }
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (button.getText().toString().equals("全部")) {
						String url = "http://data.iego.net:85/m/booklist1Json.aspx?" + "&n=" + sp.getString("UserName", "") + "&d=" + sp.getString("KEY", "") + "&lib=books&size=18&page=1";
						Bundle bundle = new Bundle();
						Intent intent = new Intent();
						bundle.putString("url", url);
						bundle.putString("type", "quanbu");
						intent.putExtras(bundle);
						intent.setClass(thiscontext,
								NewBooksListActivity.class);
						thiscontext.startActivity(intent);
					} else {
						String url = "";
						try {
							url = "http://data.iego.net:85/m/booklist1Json.aspx?" + "&n=" + sp.getString("UserName", "") + "&d=" + sp.getString("KEY", "") + "&lib=books&size=18&page=1&class1="
									+ URLEncoder.encode(button.getText().toString(),
											"UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Bundle bundle = new Bundle();
						Intent intent = new Intent();
						bundle.putString("url", url);
						bundle.putString("type", "fenlei");
						bundle.putString("word", button.getText().toString());
						intent.putExtras(bundle);
						intent.setClass(thiscontext,
								NewBooksListActivity.class);
						thiscontext.startActivity(intent);
					}
				}
			});
			
			button.setText(item.get(position).get("ItemName").toString());
//			button.setTextColor(Color.WHITE);
			int n = (int)(Math.random() * 4);
//			button.setTextSize(16);
			button.setBackgroundResource(colorList[n]);
			return convertView;
		}

	}
}
