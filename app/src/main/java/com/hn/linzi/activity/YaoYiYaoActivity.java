package com.hn.linzi.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.hn.linzi.R;
import com.hn.linzi.utils.StreamTool;
import com.hn.linzi.views.BaseActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class YaoYiYaoActivity extends BaseActivity {

	private SensorManager sensorManager;
	private Vibrator vibrator;
	private ImageView yaoyiyao;
	private boolean yaoflag;
	private Button back;
	// private static final String TAG = "TestSensorActivity";
	private static final int SENSOR_SHAKE = 10;
	private SharedPreferences sp;
	private TelephonyManager tm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		yaoflag = true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yaoyiyao);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		yaoyiyao = (ImageView) findViewById(R.id.yaoyiyao);
		sp = this.getSharedPreferences("SP",
				this.MODE_PRIVATE);
		tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		TextView daohanglan = (TextView) findViewById(R.id.daohanglan);
		daohanglan.setText("摇一摇");
		back = (Button) findViewById(R.id.yaoyiyao_back);
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
	protected void onResume() {
		yaoflag = true;
		super.onResume();
		if (sensorManager != null) {// 注册监听器
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
			// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (sensorManager != null) {// 取消监听器
			sensorManager.unregisterListener(sensorEventListener);
		}
	}

	/**
	 * 重力感应监听
	 */
	private SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			// 传感器信息改变时执行该方法
			float[] values = event.values;
			float x = values[0]; // x轴方向的重力加速度，向右为正
			float y = values[1]; // y轴方向的重力加速度，向前为正
			float z = values[2]; // z轴方向的重力加速度，向上为正
			// Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" +
			// z);
			// 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
			int medumValue = 19;// 如果不敏感请自行调低该数值,低于10的话就不行了,因为z轴上的加速度本身就已经达到10了
			if (Math.abs(x) > medumValue || Math.abs(y) > medumValue
					|| Math.abs(z) > medumValue) {
				if (yaoflag) {
					yaoflag = false;
					Animation shake01 = AnimationUtils.loadAnimation(
							YaoYiYaoActivity.this, R.anim.shake);
					yaoyiyao.startAnimation(shake01);
					vibrator.vibrate(200);
					Message msg = new Message();
					msg.what = SENSOR_SHAKE;
					handler.sendMessage(msg);
					Toast.makeText(getApplicationContext(), "正在接收数据",
							Toast.LENGTH_SHORT).show();
				} else {
					TimerTask task = new TimerTask() {
						public void run() {
							yaoflag = true;
						}
					};
					Timer timer = new Timer();
					timer.schedule(task, 5000);
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	/**
	 * 动作执行
	 */
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SENSOR_SHAKE:
				new Thread() {

					@Override
					public void run() {
						String contentURL = "http://data.iego.net:85/shack/listJson8.aspx?" + "&n=" + sp.getString("UserName", "") + "&d=" + sp.getString("KEY", "") + "&imei=" + tm.getDeviceId();
								//n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&imei=123456-78-901234-5";
						URL curl;
						System.out.println(contentURL);
						try {
							curl = new URL(contentURL);
							HttpURLConnection conn = (HttpURLConnection) curl
									.openConnection();
							conn.setRequestMethod("GET");
							conn.setReadTimeout(5000);
							if (conn.getResponseCode() == 200) {
								InputStream inStream = conn.getInputStream();
								byte[] data = StreamTool.read(inStream);
								String json = new String(data);
								System.out.println(new String(data));
								Intent intent = new Intent();
								intent.putExtra("json", json);
								intent.setClass(YaoYiYaoActivity.this,
										YaoYiYaoResult.class);
								startActivity(intent);
							} else {
								Toast.makeText(getApplicationContext(),
										"没有接收到数据", Toast.LENGTH_SHORT).show();
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						super.run();
					}

				}.start();
				break;
			}
		}

	};
	
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

}
