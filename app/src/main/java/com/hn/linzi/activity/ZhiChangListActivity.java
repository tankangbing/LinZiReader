package com.hn.linzi.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hn.linzi.adapter.ZhiChangListAdapter;
import com.hn.linzi.data.ZhiChangListData;
import com.hn.linzi.utils.ParseJson;
import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.views.XListView;
import com.hn.linzi.views.XListView.IXListViewListener;
import com.hn.linzi.R;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ZhiChangListActivity extends BaseActivity implements OnClickListener {
	private Button back;
	private Button wode;
	private Button zhaopin;
	private Button jianzhi;
	// private ListView listView;
	XListView listView;
	private LinearLayout linearLayout;
	private SharedPreferences sp;

	public final static int GetToken = 111;
	public final static int GetMoreData = 222;
	public final static int REFRESH = 333;
	public final static int PULL_REFRESH = 444;

	private ZhiChangListData zhaopinListData;
	private ZhiChangListData jianzhiListData;
	private ZhiChangListAdapter adapter;
	
	private ProgressDialog dialog;

	private String url = "";
	private String state = "official";

	// private PullToRefreshListView listView;

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ZhiChangListActivity.GetToken:
				System.out.println(token);
				getListData("official");
				break;
			case ZhiChangListActivity.GetMoreData:
				adapter.notifyDataSetChanged();
				onLoad();
				break;
			case ZhiChangListActivity.REFRESH:
				if (state.equals("official")) {
					adapter = new ZhiChangListAdapter(getApplicationContext(),
							zhaopinListData);
					adapter.setTypeflag(true);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
							// TODO Auto-generated method stub
							url = getString(R.string.url_zhichang_details) 
									+ "&n="
									+ sp.getString("UserName", "")
									+ "&d="
									+ sp.getString("KEY", "")
									+ "&jobid="
									+ zhaopinListData.List_Data.get(arg2-1).id;
							System.out.println(url);
							Bundle bundle = new Bundle();
							bundle.putString("url", url);
							Intent intent = new Intent();
							intent.putExtras(bundle);
							intent.setClass(getApplicationContext(), ZhiChangDetailsActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					});
				} else {
					adapter = new ZhiChangListAdapter(getApplicationContext(),
							jianzhiListData);
					adapter.setTypeflag(false);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
							// TODO Auto-generated method stub
							url = getString(R.string.url_zhichang_details) 
									+ "&n="
									+ sp.getString("UserName", "")
									+ "&d="
									+ sp.getString("KEY", "")
									+ "&jobid="
									+ jianzhiListData.List_Data.get(arg2-1).id;
							System.out.println(url);
							Bundle bundle = new Bundle();
							bundle.putString("url", url);
							Intent intent = new Intent();
							intent.putExtras(bundle);
							intent.setClass(getApplicationContext(), ZhiChangDetailsActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					});
				}
				listView.setAdapter(adapter);
				if (dialog.isShowing()) {
					dialog.dismiss();
					
				}
				break;
			case ZhiChangListActivity.PULL_REFRESH:
				
				break;
			}
			super.handleMessage(msg);
		}

	};

	private void onLoad() {
		
		listView.stopRefresh();
		listView.stopLoadMore();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日    HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		listView.setRefreshTime(time);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhichang_list);

		sp = getSharedPreferences("SP", MODE_PRIVATE);
		zhaopinListData = new ZhiChangListData();
		jianzhiListData = new ZhiChangListData();

		ActionBar actionBar = getActionBar();
		actionBar.hide();
		dialog = new ProgressDialog(this);
		linearLayout = (LinearLayout) findViewById(R.id.zhichang_list_type);
		back = (Button) findViewById(R.id.zhichang_main_back);
		wode = (Button) findViewById(R.id.zhichang_my);
		zhaopin = (Button) findViewById(R.id.zhichang_zhaopin);
		jianzhi = (Button) findViewById(R.id.zhichang_jianzhi);
		wode.setOnClickListener(this);
		zhaopin.setOnClickListener(this);
		jianzhi.setOnClickListener(this);
		back.setOnClickListener(this);

		listView = (XListView) findViewById(R.id.zhichang_main_list);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						System.out.println("刷新数据");
						onLoad();
					}
				}, 2000);

			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				LoadMoreData();
			}
		});
		
		System.out.println(sp.getString("DanWei", ""));
		System.out.println(sp.getString("UserName", ""));
		System.out.println(sp.getString("KEY", ""));

//		identification();
		getListData("official");
	}

	private String token = "";
	private int officialpage = 1;
	private int personalpage = 1;

	private void getListData(final String type) {
		// TODO Auto-generated method stub
		if (!dialog.isShowing()) {
			dialog.setTitle("正在加载数据，请稍候...");
			dialog.show();
			dialog.setCanceledOnTouchOutside(false);
		}
		
		if (type.equals("official")) {
			url = getString(R.string.url_zhichang_list)
					+ "n=" + sp.getString("UserName", "")
					+ "&d=" + sp.getString("KEY", "")
					+ "&page=" + officialpage + "&size=10" + "&type=full";
		} else {
			url = getString(R.string.url_zhichang_list)
					+ "n=" + sp.getString("UserName", "")
					+ "&d=" + sp.getString("KEY", "")
					+ "&page=" + personalpage + "&size=10" + "&type=part";
		}
		System.out.println(url);
		
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					if (type.equals("official")) {
						parseJson.parseZhiChangList(response, zhaopinListData.List_Data);
					} else {
						parseJson.parseZhiChangList(response, jianzhiListData.List_Data);
					}
					handler.sendEmptyMessage(ZhiChangListActivity.REFRESH);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "服务器异常，请稍后重试", Toast.LENGTH_LONG).show();
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "服务器异常，请稍后重试", Toast.LENGTH_LONG).show();
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
		queue.add(request);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zhichang_main_back:
			finish();
			break;

		case R.id.zhichang_my:
			Intent intent = new Intent();
			intent.setClass(this, MyZhiChangListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		case R.id.zhichang_zhaopin:
			state = "official";
			linearLayout.setBackgroundResource(R.drawable.zhaopin);
			if (!zhaopinListData.List_Data.isEmpty()) {
				handler.sendEmptyMessage(ZhiChangListActivity.REFRESH);
			} else {
				getListData("official");
			}
			break;

		case R.id.zhichang_jianzhi:
			state = "personal";
			linearLayout.setBackgroundResource(R.drawable.jianzhi);
			if (!jianzhiListData.List_Data.isEmpty()) {
				handler.sendEmptyMessage(ZhiChangListActivity.REFRESH);
			} else {
				getListData("personal");
			}
			break;
		}
	}
	
	private boolean LoadMoreData(){
		final Boolean flag;
		if (state.equals("official")) {
			officialpage++;
			url = getString(R.string.url_zhichang_list)
					+ "n=" + sp.getString("UserName", "")
					+ "&d=" + sp.getString("KEY", "")
					+ "&page=" + officialpage + "&size=10" + "&type=full";
			flag = true;
		} else {
			personalpage++;
			url = getString(R.string.url_zhichang_list)
					+ "n=" + sp.getString("UserName", "")
					+ "&d=" + sp.getString("KEY", "")
					+ "&page=" + personalpage + "&size=10" + "&type=part";
			flag = false;
		}
		

		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					if(response.indexOf("\"data\":[]") != -1){
						Toast.makeText(getApplicationContext(), "暂无更多数据", Toast.LENGTH_SHORT).show();
						onLoad();
						if (state.equals("official")) {
							officialpage--;
						} else {
							personalpage--;
						}
					}else{
						if (flag) {
							parseJson.parseZhiChangList(response,
									zhaopinListData.List_Data);
						} else {
							parseJson.parseZhiChangList(response,
									jianzhiListData.List_Data);
						}
						handler.sendEmptyMessage(ZhiChangListActivity.GetMoreData);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "暂无更多数据", Toast.LENGTH_SHORT).show();
				onLoad();
				if (state.equals("official")) {
					officialpage--;
				} else {
					personalpage--;
				}
			}
		});
		queue.add(request);
		return true;
	}
	
}
