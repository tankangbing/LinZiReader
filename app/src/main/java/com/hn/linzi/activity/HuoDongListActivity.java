package com.hn.linzi.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hn.linzi.adapter.HuoDongListAdapter;
import com.hn.linzi.data.HuoDongListData;
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

public class HuoDongListActivity extends BaseActivity implements
		OnClickListener {
	private Button back;
	private Button faqihuodong;
	private Button wode;
	private Button xiaoyuan;
	private Button geren;
	// private ListView listView;
	XListView listView;
	private LinearLayout linearLayout;
	private SharedPreferences sp;

	public final static int GetToken = 111;
	public final static int GetMoreData = 222;
	public final static int REFRESH = 333;
	public final static int PULL_REFRESH = 444;

	private HuoDongListData officialListData;
	private HuoDongListData personalListData;
	private HuoDongListAdapter adapter;

	private ProgressDialog dialog;

	private String url = "";
	private String state = "official";

	// private PullToRefreshListView listView;

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HuoDongListActivity.GetToken:
				System.out.println(token);
				getListData("official");
				break;
			case HuoDongListActivity.GetMoreData:
				adapter.notifyDataSetChanged();
				onLoad();
				break;
			case HuoDongListActivity.REFRESH:
				if (state.equals("official")) {
					adapter = new HuoDongListAdapter(getApplicationContext(),
							officialListData);
				} else {
					adapter = new HuoDongListAdapter(getApplicationContext(),
							personalListData);
				}
				listView.setAdapter(adapter);
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				break;
			case HuoDongListActivity.PULL_REFRESH:

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
		setContentView(R.layout.huodong_list);

		sp = getSharedPreferences("SP", MODE_PRIVATE);
		officialListData = new HuoDongListData();
		personalListData = new HuoDongListData();

		ActionBar actionBar = getActionBar();
		actionBar.hide();
		dialog = new ProgressDialog(this);
		linearLayout = (LinearLayout) findViewById(R.id.huodong_list_type);
		linearLayout.setVisibility(View.GONE);
		back = (Button) findViewById(R.id.huodong_main_back);
		faqihuodong = (Button) findViewById(R.id.huodong_jiahao);
		wode = (Button) findViewById(R.id.huodong_my);
		xiaoyuan = (Button) findViewById(R.id.huodong_xiaoyuan);
		geren = (Button) findViewById(R.id.huodong_geren);
		faqihuodong.setOnClickListener(this);
		wode.setOnClickListener(this);
		xiaoyuan.setOnClickListener(this);
		geren.setOnClickListener(this);
		back.setOnClickListener(this);

		listView = (XListView) findViewById(R.id.huodong_main_list);
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

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				url = getString(R.string.url_huodong_details)
						+ "&n=" + sp.getString("UserName", "")
						+ "&d=" + sp.getString("KEY", "")
						+ "&actid=" + officialListData.List_Data.get(arg2 - 1).id1;
				System.out.println(url);
				Bundle bundle = new Bundle();
				bundle.putString("url", url);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(),
						HuoDongDetailsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
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

		// if (type.equals("official")) {
		// url = getString(R.string.url_neiwang)
		// + getString(R.string.url_huodong_xiaoyuanOfficialList);
		// url = url + "accesstoken=" + token + "&page=" + officialpage;
		// } else {
		// url = getString(R.string.url_neiwang)
		// + getString(R.string.url_huodong_xiaoyuanPersonalList);
		// url = url + "accesstoken=" + token + "&page=" + personalpage;
		// }
		url = getString(R.string.url_huodong_list) 
				+ "&n=" + sp.getString("UserName", "")
				+ "&d=" + sp.getString("KEY", "")
				+ "&page=" + officialpage;
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
						parseJson.parseHuoDongList(response,
								officialListData.List_Data);
					} else {
						parseJson.parseHuoDongList(response,
								personalListData.List_Data);
					}
					handler.sendEmptyMessage(HuoDongListActivity.REFRESH);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		case R.id.huodong_main_back:
			finish();
			break;

		case R.id.huodong_jiahao:
			// Intent intent = new Intent();
			// intent.setClass(this, FaQiHuoDongActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			break;

		case R.id.huodong_my:
			Intent intent = new Intent();
			intent.setClass(this, MyHuoDongListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		case R.id.huodong_xiaoyuan:
			state = "official";
			linearLayout.setBackgroundResource(R.drawable.xiaoyuanhuodong);
			if (!officialListData.List_Data.isEmpty()) {
				handler.sendEmptyMessage(HuoDongListActivity.REFRESH);
			} else {
				getListData("official");
			}
			break;

		case R.id.huodong_geren:
			state = "personal";
			linearLayout.setBackgroundResource(R.drawable.gerenhuodong);
			if (!personalListData.List_Data.isEmpty()) {
				handler.sendEmptyMessage(HuoDongListActivity.REFRESH);
			} else {
				getListData("personal");
			}
			break;
		}
	}

	private boolean LoadMoreData() {
		final Boolean flag;
		if (state.equals("official")) {
			officialpage++;
			url = getString(R.string.url_huodong_list) 
					+ "&n=" + sp.getString("UserName", "")
					+ "&d=" + sp.getString("KEY", "")
					+ "&page=" + officialpage;
			flag = true;
		} else {
			personalpage++;
			url = getString(R.string.url_huodong_list) 
					+ "&n=" + sp.getString("UserName", "")
					+ "&d=" + sp.getString("KEY", "")
					+ "&page=" + personalpage;
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
					System.out.println(response.indexOf("\"data\":[]"));
					if (response.indexOf("\"data\":[]") == -1) {
						if (flag) {
							parseJson.parseHuoDongList(response,
									officialListData.List_Data);
						} else {
							parseJson.parseHuoDongList(response,
									personalListData.List_Data);
						}
						handler.sendEmptyMessage(HuoDongListActivity.GetMoreData);
					} else {
						Toast.makeText(getApplicationContext(), "暂无更多数据",
								Toast.LENGTH_SHORT).show();
						onLoad();
						if (state.equals("official")) {
							officialpage--;
						} else {
							personalpage--;
						}
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
				Toast.makeText(getApplicationContext(), "暂无更多数据",
						Toast.LENGTH_SHORT).show();
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
