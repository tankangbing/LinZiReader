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
import com.hn.linzi.utils.BookDBHelper;
import com.hn.linzi.utils.MyTools;
import com.hn.linzi.utils.ParseJson;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewBooksContentActivity extends BaseActivity {

	private TextView title;
	private TextView author;
	private TextView pubtime;
	private TextView pub;
	private TextView intro;
	private TextView directory;
	private Button download;
	private ImageView imageView;
	protected static final int REFRESH = 2323;
	private ArrayList<String> data;
	private Context context;
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println(getIntent().getExtras().getString("url"));
		setContentView(R.layout.classicbook_content);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		Button button = (Button) findViewById(R.id.claasicbookcontent_back);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView daohanlan = (TextView) findViewById(R.id.daohanglan);
		daohanlan.setText("详细信息");
		title = (TextView) findViewById(R.id.cbcontent_title);
		author = (TextView) findViewById(R.id.cbcontent_author);
		pubtime = (TextView) findViewById(R.id.cbcontent_pubtime);
		pub = (TextView) findViewById(R.id.cbcontent_pub);
		intro = (TextView) findViewById(R.id.cbcontent_intro);
		directory = (TextView) findViewById(R.id.cbcontent_directory);
		download = (Button) findViewById(R.id.cbcontent_download);
		imageView = (ImageView) findViewById(R.id.cbcontent_img);
		context = this;
		this.tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
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
					data = parseJson.parseClassicBookContent(response);
					Message message = new Message();
					message.what = NewBooksContentActivity.REFRESH;
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
			case NewBooksContentActivity.REFRESH:

				String html = "<font color='#7dc8ba'>[图书]</font>" + data.get(1);
				title.setText(Html.fromHtml(html));
				// author.setText("作者：" + data.get(2));
				// pubtime.setText("出版日期：" + data.get(3));
				// pub.setText("出版社:" + data.get(4));
				html = "<font color='#7dc8ba'>[简介]</font> " + "&nbsp;&nbsp;"
						+ data.get(5);
				intro.setText(Html.fromHtml(html));
				html = "<font color='#7dc8ba'>[作者]</font> " + "&nbsp;&nbsp;"
						+ data.get(2);
				author.setText(Html.fromHtml(html));
				html = "<font color='#7dc8ba'>[出版日期]</font> " + "&nbsp;&nbsp;"
						+ data.get(3);
				pubtime.setText(Html.fromHtml(html));
				html = "<font color='#7dc8ba'>[出版社]</font> " + "&nbsp;&nbsp;"
						+ data.get(4);
				pub.setText(Html.fromHtml(html));
				// directory.setText(Html.fromHtml(data.get(6), null, new
				// MxgsaTagHandler(context)));
				
				BookDBHelper bookDBHelper = new BookDBHelper(context);
				SharedPreferences sp = context.getSharedPreferences(
						"SP", context.MODE_PRIVATE);
				Cursor cursor = bookDBHelper.select(
						null,
						"book_name=? and username=?",
						new String[] { data.get(1),
								sp.getString("UserName", "") });
				if (cursor.moveToNext()) {
					download.setBackgroundResource(R.drawable.btn_yixiazaitushu);
				}else{
					download.setBackgroundResource(R.drawable.btn_xiazaitushu);
				}
				cursor.close();
				download.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						SharedPreferences sp = context.getSharedPreferences(
								"SP", context.MODE_PRIVATE);
						BookDBHelper bookDBHelper = new BookDBHelper(context);
						Cursor cursor = bookDBHelper.select(
								null,
								"book_name=? and username=?",
								new String[] { data.get(1),
										sp.getString("UserName", "") });
						if (cursor.moveToNext()) {
							Toast.makeText(context, "已有该图书" + data.get(1),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, "开始下载" + data.get(1),
									Toast.LENGTH_SHORT).show();
							String path = "";
							if(MyTools.getExtSDCardPaths().size() > 0){
								path = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/";
							}else {
								path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/";
							}
//							File DL = getExternalFilesDir("BOOKS");
//							String path = DL.getPath() + "/" + sp.getString("UserName", "") + "/";
							if (data.get(8).equals("epub")) {
								path = path + sp.getString("UserName", "")+ "_"
										+ data.get(1)
										+ ".epub";
							} else if (data.get(8).equals("pdf")) {
								path = path + sp.getString("UserName", "")+ "_"
										+ data.get(1)
										+ ".pdf";
							}

							// DownloadFile downloadFile = new
							// DownloadFile(context);
							String pdfurl = data.get(7) + "&n="
									+ sp.getString("UserName", "") + "&d="
									+ sp.getString("KEY", "") + "&code="
									+ sp.getString("code", "88888888")
									+ "&imei=" + tm.getDeviceId();
							// downloadFile.downloadPDF(pdfurl, data.get(1));
							bookDBHelper.insert(data.get(1), data.get(2),
									data.get(0), data.get(4), data.get(3), "",
									pdfurl, path, "false", "false",
									sp.getString("UserName", ""), data.get(8),
									data.get(9));
							Intent intent = new Intent();
							intent.setClass(context,
									DownloadManageActivity.class);
							context.startActivity(intent);
							finish();
						}
					}
				});
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
}
