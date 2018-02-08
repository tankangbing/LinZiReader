package com.hn.linzi.activity;

import com.ta.TAActivity;
import com.ta.util.download.DownloadManager;
import com.ta.util.download.IDownloadService;
import com.umeng.analytics.MobclickAgent;
import com.hn.linzi.R;
import com.hn.linzi.adapter.ZiLiaoJiaDownloadAdapter;
import com.hn.linzi.utils.MyTools;
import com.hn.linzi.utils.ZiLiaoJiaDBHelper;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ZiLiaoJiaManageActivity extends TAActivity {

	// @TAInjectView(id = R.id.dlmanage_list)
	private ListView downloadList;
	// @TAInjectView(id = R.id.btn_add)
	// private Button addButton;
	private ZiLiaoJiaDownloadAdapter downloadListAdapter;
	private int urlIndex = 0;
	private IDownloadService downloadService;
	private ZiLiaoJiaDBHelper ziLiaoJiaDBHelper;
	private boolean nowList;
	private RelativeLayout listBg;
	private SharedPreferences sp;
	private Context context;

	ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			downloadService = (IDownloadService) service;
		}
	};
	private DownloadManager downloadManager;

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onAfterOnCreate(savedInstanceState);
		// setTitle(R.string.thinkandroid_download_title);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		sp = this.getSharedPreferences("SP", this.MODE_PRIVATE);
		String path = "";
		if (MyTools.getExtSDCardPaths().size() > 0) {
			path = MyTools.getExtSDCardPaths().get(0)
					+ "/Android/data/com.wr.reader2/files/BOOKS/";
		} else {
			path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/Android/data/com.wr.reader2/files/BOOKS/";
		}
		System.out.println(path);
		downloadManager = DownloadManager.getDownloadManager(path);
		setContentView(R.layout.dlmanage);
		findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
		context = this;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// downloadManager.close();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onAfterSetContentView() {
		// TODO Auto-generated method stub
		super.onAfterSetContentView();
		TextView textView = (TextView) findViewById(R.id.daohanglan);
		textView.setText("资料夹管理");
		listBg = (RelativeLayout) findViewById(R.id.downbg);
		downloadList = (ListView) findViewById(R.id.dlmanage_list);
		downloadListAdapter = new ZiLiaoJiaDownloadAdapter(this, downloadList);
		downloadList.setAdapter(downloadListAdapter);
		ziLiaoJiaDBHelper = new ZiLiaoJiaDBHelper(this);

		Button xiazaizhong = (Button) findViewById(R.id.xiazaizhong);
		Button yixiazai = (Button) findViewById(R.id.toyixiazai);
		xiazaizhong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("xiazaizhong");
				if (nowList) {
					showDownloadList();
				} else {

				}

			}
		});

		yixiazai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("yixiazai");
				// TODO Auto-generated method stub
				if (nowList) {

				} else {
					showYiXiaZaiList();
				}
			}
		});
		showDownloadList();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			showYiXiaZaiList();
			String oldurl = bundle.getString("oldurl", "false");
			if (!oldurl.equals("false")) {
				downloadListAdapter.removeItem(oldurl);
			}
		}

		if (bundle != null) {
			String shujia = bundle.getString("shujia", "false");
			if (shujia.equals("false")) {
				showDownloadList();
			}
		}
		Button back = (Button) findViewById(R.id.manage_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle back = getIntent().getExtras();
				if (back != null) {
					String shujia = back.getString("shujia", "false");
					if (!shujia.equals("false")) {
						Intent intent = new Intent();
						intent.setClass(context, ZiLiaoJiaActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(intent);
					}
				}
				finish();
			}
		});
		Button shujia = (Button) findViewById(R.id.manage_backtoshujia);
		shujia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ZiLiaoJiaActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				finish();
			}
		});
	}

	private void showYiXiaZaiList() {
		listBg.setBackgroundResource(R.drawable.yixiazai);
		// TODO Auto-generated method stub
		nowList = true;
		Cursor cursor = ziLiaoJiaDBHelper.select(null, "username=?",
				new String[] { sp.getString("UserName", "") });
		System.out.println("1111");
		downloadListAdapter.setBarFlag(false);
		while (cursor.moveToNext()) {
			String url = cursor.getString(cursor.getColumnIndex("item_url"));
			if (cursor.getString(cursor.getColumnIndex("item_dlflag")).equals(
					"true")) {
				downloadListAdapter.addItem(url);
			} else {
				downloadListAdapter.removeItem(url);
			}
		}
		cursor.close();
	}

	private void showDownloadList() {
		listBg.setBackgroundResource(R.drawable.xiazaizhong);
		// TODO Auto-generated method stub
		nowList=false;
		Cursor cursor = ziLiaoJiaDBHelper.select(null, "username=?",
				new String[]{sp.getString("UserName", "")});
		downloadListAdapter.setBarFlag(true);
		while(cursor.moveToNext()){
			String url = cursor.getString(cursor.getColumnIndex("item_url"));
			System.out.println("url=" + url);
			if(cursor.getString(cursor.getColumnIndex("item_dlflag")).equals("false")){
				if(cursor.getString(cursor.getColumnIndex("item_restartflag")).equals("true")){
					System.out.println("删除任务重新开始");
					downloadManager.deleteHandler(url);
					downloadListAdapter.removeItem(url);
				}
//				System.out.println("path=====" + downloadManager.getRootPath());
				downloadManager.addHandler(url);
				downloadListAdapter.addItem(url);
				ziLiaoJiaDBHelper.update(cursor.getInt(cursor.getColumnIndex("item_id")), 
						cursor.getString(cursor.getColumnIndex("item_name")),cursor.getString(cursor.getColumnIndex("item_type")), 
						cursor.getString(cursor.getColumnIndex("item_url")), cursor.getString(cursor.getColumnIndex("item_path")),
						cursor.getString(cursor.getColumnIndex("item_dlflag")), "false", sp.getString("UserName", ""));
			}else{
				downloadListAdapter.removeItem(url);
			}
		}
		cursor.close();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy(); // 注意先后
	}

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