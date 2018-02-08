package com.hn.linzi.activity;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.hn.linzi.utils.ParseJson;
import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.R;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ZhiChangDetailsActivity extends BaseActivity {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private String responseurl;

	private String ActivityCode;//活动返回码
	private SharedPreferences sp;
	private String url;
	private Map<String, String> DetailsData;

	private Button back;
	private ImageView fengmian;
	private TextView zhichang_name;
	private TextView zhichang_pay;
	private TextView zhichang_companyname;
	private TextView zhichang_peopleCount;
	private ImageButton zhichang_signup;
	private TextView zhichang_time;
	private TextView zhichang_detail;
	
	public final static int GetData = 111;
	public final static int GetActivityCode = 555;
	
	private String commentState = "new";

	public String getCommentState() {
		return commentState;
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ZhiChangDetailsActivity.GetData:
				setDetailsData();
				break;
			case ZhiChangDetailsActivity.GetActivityCode:
				if (ActivityCode.equals("1000")) {
					Toast.makeText(getApplicationContext(), "成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.zhichang_detail);
		String name = new SimpleDateFormat("yyyyMMddhhmmss")
		.format(new Date());
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		url = bundle.getString("url");
		DetailsData = new HashMap<String, String>();
		fengmian = (ImageView) findViewById(R.id.zhichang_detail_fengmian);
		zhichang_name = (TextView) findViewById(R.id.zhichang_detail_title);
		zhichang_pay = (TextView) findViewById(R.id.zhichang_detail_pay);
		zhichang_companyname = (TextView) findViewById(R.id.zhichang_detail_companyname);
		zhichang_peopleCount = (TextView) findViewById(R.id.zhichang_detail_peopleNum);
		zhichang_detail = (TextView) findViewById(R.id.zhichang_detail_detail);
		zhichang_signup = (ImageButton) findViewById(R.id.zhichang_detail_signup);
		zhichang_time = (TextView) findViewById(R.id.zhichang_detail_time);
		back = (Button) findViewById(R.id.zhichang_detail_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		getDetailsData();
		super.onCreate(savedInstanceState);
	}

	private void setDetailsData() {
		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
		imageLoader.displayImage(DetailsData.get("cover"), fengmian, options,
				aDisplayListener);
		
		zhichang_name.setText(DetailsData.get("position"));
		zhichang_pay.setText("￥" + DetailsData.get("pay"));
		zhichang_companyname.setText(DetailsData.get("company"));
		zhichang_peopleCount.setText(DetailsData.get("limit") + "人");
		zhichang_time.setText(DetailsData.get("dt"));
		zhichang_detail.setText(DetailsData.get("intro"));
		
		zhichang_signup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (DetailsData.get("focusflag").equals("false")) {
					String shoucangUrl = getString(R.string.url_zhichang_shoucang)
							+ "&jobid=" + DetailsData.get("id")
							+ "&n=" + sp.getString("UserName", "")
							+ "&d=" + sp.getString("KEY", "");
					RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
					StringRequest request = new StringRequest(shoucangUrl, new Listener<String>() {
						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							System.out.println(response);
							if (response.indexOf("成功") > -1) {
								Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
								zhichang_signup.setOnClickListener(null);
							}else {
								Toast.makeText(getApplicationContext(), "收藏失败，请重试", Toast.LENGTH_SHORT).show();
							}
						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// TODO Auto-generated method stub

						}
					});
					queue.add(request);
					
				}else {
					Toast.makeText(getApplicationContext(), "已经收藏过该职位了", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

	private void getDetailsData() {
		// TODO Auto-generated method stub
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					parseJson.parseZhiChangDetail(response, DetailsData, sp.getString("UserName", ""));
					handler.sendEmptyMessage(ZhiChangDetailsActivity.GetData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub

			}
		});
		queue.add(request);
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
}
