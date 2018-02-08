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
import com.hn.linzi.utils.BookDBHelper;
import com.hn.linzi.utils.MyTools;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BookXiangxiActivity extends BaseActivity implements OnClickListener {
	private ImageView bookimg;
	private TextView title;
	private TextView author;
	private TextView pub;
	private TextView time;
	private Button button;
	private Button back;
	// private ProgressBar bar;
	private boolean open;
	private String imageURL;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private String pdfurl;
	private String id;
	private ArrayList<String> bookData;
	private BookDBHelper bookDBHelper;

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
		TextView daohanglan = (TextView) findViewById(R.id.daohanglan);
		daohanglan.setText("图书信息");
		back = (Button) findViewById(R.id.xiangxi_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		button.setOnClickListener(this);
		Bundle bundle = this.getIntent().getExtras();
		bookData = bundle.getStringArrayList("bookData");
		id = bundle.getString("id");
		imageURL = bookData.get(2);
		bookDBHelper = new BookDBHelper(this);
		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
		imageLoader.displayImage(imageURL, bookimg, options, aDisplayListener);

		// findPDF();

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		title.setText(bookData.get(0));
		author.setText(bookData.get(1));
		pub.setText(bookData.get(3));
		time.setText(bookData.get(3));
		SharedPreferences sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
		Cursor cursor = bookDBHelper.select(
				null,
				"book_name=? and username=?",
				new String[] { bookData.get(0),
						sp.getString("UserName", "")});
		if (cursor.moveToNext()) {
			button.setBackgroundResource(R.drawable.btn_yixiazaitushu);
		}else{
			button.setBackgroundResource(R.drawable.btn_xiazaitushu);
		}
		cursor.close();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// switch (item.getItemId()) {
		// case R.id.action_my:
		// Toast.makeText(this, "我的", Toast.LENGTH_SHORT).show();
		// Intent intent = new Intent();
		// intent.setClass(this, Personal_Settings.class);
		// startActivity(intent);
		// break;
		//
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
		Editor editor = sp.edit();
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		switch (v.getId()) {
		case R.id.book_xiangxiBtn:
			String path = "";
			if(MyTools.getExtSDCardPaths().size() > 0){
				path = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/";
			}else {
				path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/";
			}
//			File DL = getExternalFilesDir("BOOKS");
//			String path = DL.getPath() + "/" + sp.getString("UserName", "") + "/";
//			String path = "";
			if (bookData.get(7).equals("epub")) {
				path = path + sp.getString("UserName", "")+ "_"
						+ bookData.get(1) + ".epub";
			} else if (bookData.get(7).equals("pdf")) {
				path = path + sp.getString("UserName", "")+ "_"
						+ bookData.get(1) + ".pdf";
			}
			String url = bookData.get(6) + "&n=" + sp.getString("UserName", "")
					+ "&d=" + sp.getString("KEY", "") + "&code="
					+ sp.getString("code", "88888888") + "&imei="
					+ tm.getDeviceId();
			System.out.println("url---------------" + url);

			Cursor cursor = bookDBHelper.select(
					null,
					"book_name=? and username=?",
					new String[] { bookData.get(0),
							sp.getString("UserName", "")});
			if (cursor.moveToNext()) {
				Toast.makeText(this, "已有该图书" + bookData.get(0),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "开始下载" + bookData.get(0),
						Toast.LENGTH_SHORT).show();
				
				bookDBHelper.insert(bookData.get(0), bookData.get(1),
						bookData.get(2), bookData.get(3), bookData.get(4), "",
						url, path, "false", "false",
						sp.getString("UserName", ""), bookData.get(7), id);

				Intent intent = new Intent();
				intent.setClass(this, DownloadManageActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}

			break;
		}
	}

}
