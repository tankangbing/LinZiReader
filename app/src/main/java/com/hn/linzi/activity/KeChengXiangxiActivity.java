package com.hn.linzi.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.hn.linzi.R;
import com.hn.linzi.utils.KeChengDBHelper;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class KeChengXiangxiActivity extends BaseActivity implements OnClickListener {
	private ImageView bookimg;
	private TextView title;
	private TextView author;
	private TextView pub;
	private TextView time;
	private Button button;
	private Button back;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private String imageURL;
	private String pdfurl;
	private ArrayList<String> bookData;
	private Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_xiangxi);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		bookimg = (ImageView) findViewById(R.id.book_xiangxiImg);
		title = (TextView) findViewById(R.id.book_xiangxiTitle);
		author = (TextView) findViewById(R.id.book_xiangxiAuthor);
		pub = (TextView) findViewById(R.id.book_xiangxiPub);
		time = (TextView) findViewById(R.id.book_xiangxiTime);
		button = (Button) findViewById(R.id.book_xiangxiBtn);
		back = (Button) findViewById(R.id.xiangxi_back);
		TextView daohanglan = (TextView) findViewById(R.id.daohanglan);
		daohanglan.setText("课程信息");
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		button.setOnClickListener(this);
		bundle = this.getIntent().getExtras();
		imageURL = bundle.getString("imgurl");
		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
		imageLoader.displayImage(imageURL, bookimg, options, aDisplayListener);

		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		SharedPreferences sp = this.getSharedPreferences(
				"SP", this.MODE_PRIVATE);
		KeChengDBHelper keChengDBHelper = new KeChengDBHelper(
				this);
		Cursor cursor = keChengDBHelper.select(
				null,
				"kecheng_name=? and username=?",
				new String[] { bundle.getString("title"),
						sp.getString("UserName", "") });
		if (cursor.moveToNext()) {
			button.setBackgroundResource(R.drawable.btn_yijingshoucang);
		}else{
			button.setBackgroundResource(R.drawable.btn_shoucangkc);
		}
		cursor.close();

		title.setText(bundle.getString("title"));
		author.setText(bundle.getString("type"));
		pub.setText("");
		time.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
		Editor editor = sp.edit();
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		KeChengDBHelper keChengDBHelper = new KeChengDBHelper(
				getApplicationContext());
		switch (v.getId()) {
		case R.id.book_xiangxiBtn:
			// keChengDBHelper.select(new String[]{}, tiaojian, zhi)
			Cursor cursor = keChengDBHelper.select(
					null,
					"kecheng_name=? and username=?",
					new String[] { bundle.getString("title"),
							sp.getString("UserName", "") });
			if (cursor.moveToNext()) {
				Toast.makeText(getApplicationContext(), "已收藏该课程",
						Toast.LENGTH_SHORT).show();
			} else {
				keChengDBHelper.insert(bundle.getString("title"), "", imageURL,
						bundle.getString("contentUri"),
						bundle.getString("type"), sp.getString("UserName", ""));
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), KechengActivity.class);
				startActivity(intent);
			}
			break;
		}
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
