package com.hn.linzi.activity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hn.linzi.R;
import com.hn.linzi.utils.MyApplication;
import com.hn.linzi.utils.UpdataAPK;
import com.hn.linzi.utils.UpdataAPK.DownloadBinder;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Personal_Settings extends BaseActivity implements OnClickListener {

	private Button back;
	private Button nowUser;
	private Button swapUser;
	private Button jiancha;
	private Button about;
	private Button dlmanage;
	private Button customMenu;
	private Button changeBG;
	private Activity activity;
	private MyApplication app;
	private DownloadBinder binder;
	private boolean isBinded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_settings);

		ActionBar actionBar = getActionBar();
		actionBar.hide();
		SharedPreferences sp = getSharedPreferences("SP", MODE_PRIVATE);
		activity = this;
		app = (MyApplication) getApplication();
		TextView daohanlan = (TextView) findViewById(R.id.daohanglan);
		daohanlan.setText("个人设置");
		back = (Button) findViewById(R.id.personal_back);
		nowUser = (Button) findViewById(R.id.personal_nowUser);
		swapUser = (Button) findViewById(R.id.personal_swapUser);
		jiancha = (Button) findViewById(R.id.personal_jiancha);
//		about = (Button) findViewById(R.id.personal_about);
		dlmanage = (Button) findViewById(R.id.personal_dlmanage);
		customMenu = (Button) findViewById(R.id.personal_CustomMenu);
		changeBG = (Button) findViewById(R.id.personal_ChangeBG);

		nowUser.setText("ID：" + sp.getString("UserName", ""));

		back.setOnClickListener(this);
		swapUser.setOnClickListener(this);
		jiancha.setOnClickListener(this);
//		about.setOnClickListener(this);
		dlmanage.setOnClickListener(this);
		customMenu.setOnClickListener(this);
		changeBG.setOnClickListener(this);
	}

	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			isBinded = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			binder = (DownloadBinder) service;
			System.out.println("服务启动!!!");
			// 开始下载
			isBinded = true;
			binder.start();

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.personal_back:
			activity.finish();
			break;
		case R.id.personal_swapUser:
			activity.finish();
			Bundle bundle = new Bundle();
			bundle.putBoolean("swapUser", true);
			Intent intent = new Intent();
			intent.setClass(activity, LoginActivity.class);
			intent.putExtras(bundle);
			activity.startActivity(intent);
			break;
		case R.id.personal_jiancha:
			RequestQueue queue = Volley.newRequestQueue(this);
			StringRequest request = new StringRequest(
					"http://data.iego.net:85/help/update.aspx",
					new Listener<String>() {

						@Override
						public void onResponse(String response) {
							// TODO Auto-generated method stub
							System.out.println(response);
							String[] breakresponse = response.split(" ");
							try {
								PackageManager manager = activity
										.getPackageManager();
								PackageInfo info = manager.getPackageInfo(
										activity.getPackageName(), 0);
								int version = info.versionCode;
								// float nowversion = Float.parseFloat(version);
								float newversion = Float
										.parseFloat(breakresponse[0]);
								app.setApkurl(breakresponse[1]);
								System.out.println("version=" + version);
								System.out.println("最新版本：" + newversion);
								if (newversion <= version) {
									Toast.makeText(activity, "已经为最新版本",
											Toast.LENGTH_SHORT).show();
								} else {
									System.out.println("对话框");
									Dialog dialog = new AlertDialog.Builder(
											Personal_Settings.this)
											.setTitle("检测到新版本")
											.setMessage("是否下载？")
											.setPositiveButton(
													"下载",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int whichButton) {
															app.setDownload(true);
															Intent it = new Intent(
																	Personal_Settings.this,
																	UpdataAPK.class);
															startService(it);
															bindService(
																	it,
																	conn,
																	Context.BIND_AUTO_CREATE);
														}
													})// 设置确定按钮
											.setNegativeButton(
													"取消",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int whichButton) {
														}
													})// 设置取消按钮
											.create();
									dialog.show();

								}
							} catch (Exception e) {
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
			break;
//		case R.id.personal_about:
//			Intent about = new Intent();
//			about.setClass(activity, AboutActivity.class);
//			activity.startActivity(about);
//			break;
		case R.id.personal_dlmanage:
			CharSequence[] items = { "本地图书管理", "课程管理", "资料夹管理", "取消" };
			new AlertDialog.Builder(Personal_Settings.this).setTitle("")
					.setItems(items, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							switch (which) {
							case 0:
								intent.setClass(activity, DownloadManageActivity.class);
								activity.startActivity(intent);
								break;
							case 1:
								intent.setClass(activity, KeChengManageActivity.class);
								activity.startActivity(intent);
								break;
							case 2:
								intent.setClass(activity, ZiLiaoJiaManageActivity.class);
								activity.startActivity(intent);
								break;
							case 3:
								
								break;
							}
						}
					}).create().show();
			break;
		case R.id.personal_CustomMenu:
			Intent customMenu = new Intent();
			customMenu.setClass(activity, CustomMenuActivity.class);
			activity.startActivity(customMenu);
			break;
		case R.id.personal_ChangeBG:
			Intent changeBG = new Intent();
			changeBG.setClass(activity, ChangeBGActivity.class);
			activity.startActivity(changeBG);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isBinded) {
			System.out.println(" onDestroy   unbindservice");
			unbindService(conn);
		}
		if (binder != null && binder.isCanceled()) {
			System.out.println(" onDestroy  stopservice");
			Intent it = new Intent(this, UpdataAPK.class);
			stopService(it);
		}
	}
}
