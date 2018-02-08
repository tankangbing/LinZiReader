package com.hn.linzi.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.hn.linzi.R;
import com.hn.linzi.adapter.KeChengDirUrlAdapter;
import com.hn.linzi.utils.KeChengDBHelper;
import com.hn.linzi.utils.ParseJson;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PublicClassContentActivity extends BaseActivity {

	private TextView title;
	private TextView author;
	private TextView type;
	private TextView intro;
	private Button shoucang;
	private ImageView imageView;
	protected static final int REFRESH = 2323;
	private ArrayList<String> data;
	private ArrayList<String> fatherList;
	private Context context;
	private List<List<UrlData>> childList = new ArrayList<List<UrlData>>();
	private boolean shoucangflag = false;
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.publicclass_content);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		context = this;
		Button button = (Button) findViewById(R.id.publicclass_back);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Button backtokc = (Button) findViewById(R.id.kc_manage);
		backtokc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, KechengActivity.class);
				context.startActivity(intent);
			}
		});
		TextView daohanlan = (TextView) findViewById(R.id.daohanglan);
		daohanlan.setText("课程简介");
		title = (TextView) findViewById(R.id.pcContent_title);
		author = (TextView) findViewById(R.id.pcContent_author);
		type = (TextView) findViewById(R.id.pcContent_type);
		intro = (TextView) findViewById(R.id.pcContent_intro);
		imageView = (ImageView) findViewById(R.id.pcContent_img);
		shoucang = (Button) findViewById(R.id.pcContent_shoucang);
		getContentData(getIntent().getExtras().getString("url"));
	}

	private void getContentData(String url) {
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					data = parseJson.parseKeChengContent(response);
					fatherList = parseJson.parseKechengFather(response);
					for (int i = 0; i < fatherList.size(); i++) {
						List<UrlData> data1 = parseJson.parseKechengChild(
								response, i);
						childList.add(data1);
					}
					Message message = new Message();
					message.what = PublicClassContentActivity.REFRESH;
					handler.sendMessage(message);
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

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PublicClassContentActivity.REFRESH:
				shoucangflag = true;
				SharedPreferences sp = context.getSharedPreferences(
						"SP", context.MODE_PRIVATE);
				KeChengDBHelper keChengDBHelper = new KeChengDBHelper(
						context);
				Cursor cursor = keChengDBHelper.select(
						null,
						"kecheng_name=? and username=?",
						new String[] { data.get(1),
								sp.getString("UserName", "") });
				if (cursor.moveToNext()) {
					shoucang.setBackgroundResource(R.drawable.btn_yijingshoucang);
				}else{
					shoucang.setBackgroundResource(R.drawable.btn_shoucangkc);
				}
				cursor.close();
				shoucang.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (shoucangflag) {
							final SharedPreferences sp = context.getSharedPreferences(
									"SP", context.MODE_PRIVATE);
							tm = (TelephonyManager) context
									.getSystemService(Context.TELEPHONY_SERVICE);
							KeChengDBHelper keChengDBHelper = new KeChengDBHelper(
									context);
							Cursor cursor = keChengDBHelper.select(
									null,
									"kecheng_name=? and username=?",
									new String[] { data.get(1),
											sp.getString("UserName", "") });
							if (cursor.moveToNext()) {
								Toast.makeText(getApplicationContext(), "已收藏该课程",
										Toast.LENGTH_SHORT).show();
							} else {
								shoucang.setBackgroundResource(R.drawable.btn_yijingshoucang);
								keChengDBHelper.insert(data.get(1), "",
										data.get(0),
										"http://data.iego.net:88/m/coursedetail8.aspx?lesson_id="
												+ data.get(5), type.getText()
												.toString(), sp.getString("UserName",
												""));
								// Intent intent = new Intent();
								// intent.setClass(getApplicationContext(),
								// KechengActivity.class);
								// startActivity(intent);
								Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT)
										.show();
							}
						}
					}
				});
				String html = "<font color='#FFFFFF'>[视频]</font>" + data.get(1);
				title.setText(Html.fromHtml(html));
				author.setText("讲师：" + data.get(2));
				type.setText("分类:" + data.get(3));
				// intro.setText(Html.fromHtml(data.get(4), null, new
				// MxgsaTagHandler(context)));

				KeChengDirUrlAdapter adapter = new KeChengDirUrlAdapter(
						context, fatherList, childList);
				ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
				listView.setAdapter(adapter);
				int groupCount = listView.getCount();
				for (int i = 0; i < groupCount; i++) {
					listView.expandGroup(i);
				}
				;
				ImageLoader imageLoader = ImageLoader.getInstance();
				DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
				options = new DisplayImageOptions.Builder().cacheInMemory(true)
						.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
						.build();
				AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
				imageLoader.displayImage(data.get(0), imageView, options,
						aDisplayListener);

				break;
			}
		}
	};

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

	public static class UrlData {
		public String name;
		public String url;

		public UrlData(String name, String url) {
			super();
			this.name = name;
			this.url = url;
		}

	}
}
