package com.hn.linzi.activity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.hn.linzi.R;
import com.hn.linzi.data.MapData;
import com.hn.linzi.utils.BookDBHelper;
import com.hn.linzi.views.BaseActivity;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainMenuActivity extends BaseActivity implements OnClickListener {
	// private FlipViewController flipView;
//	private ImageButton btn1;
//	private ImageButton btn2;
//	private ImageButton btn3;
//	private ImageButton btn4;
//	private ImageButton btn5;
//	private ImageButton btn6;
//	private ImageButton btn7;
//	private ImageButton btn8;
//	private ImageButton btn9;
	private Button personal;
	private Context context;

	private GridView gridView;

	private SharedPreferences sp;
	private TelephonyManager tm;

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient client;
	protected float lat = 23.128977f; 		// latitude:  纬度，横
	protected float lon = 113.378731f;		// longitude: 经度，竖
	private double mDlatitude;
	private double mDlongitude;
	private static final double EARTH_RADIUS = 6378137.0;
	private TextView mTvShinei;
	private double mS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.main_menu);
		context = this;
		this.sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE);
		this.tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

//		btn1 = (ImageButton) findViewById(R.id.menu_Btn1);
//		btn2 = (ImageButton) findViewById(R.id.menu_Btn2);
//		btn3 = (ImageButton) findViewById(R.id.menu_Btn3);
//		btn4 = (ImageButton) findViewById(R.id.menu_Btn4);
//		btn5 = (ImageButton) findViewById(R.id.menu_Btn5);
//		btn6 = (ImageButton) findViewById(R.id.menu_Btn6);
//		btn7 = (ImageButton) findViewById(R.id.menu_Btn7);
//		btn8 = (ImageButton) findViewById(R.id.menu_Btn8);
//		btn9 = (ImageButton) findViewById(R.id.menu_Btn9);
//		btn1.setOnClickListener(this);
//		btn2.setOnClickListener(this);
//		btn3.setOnClickListener(this);
//		btn4.setOnClickListener(this);
//		btn5.setOnClickListener(this);
//		btn6.setOnClickListener(this);
//		btn7.setOnClickListener(this);
//		btn8.setOnClickListener(this);
//		btn9.setOnClickListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		TextView daohanlan = (TextView) findViewById(R.id.daohanglan);
		daohanlan.setText("灵芝悦读");

		personal = (Button) findViewById(R.id.main_menu_list_set);
		personal.setOnClickListener(this);
		initMap();
//		initData();//从后台获取经纬度
		gridView = (GridView) findViewById(R.id.mm_gridview);
		MenuAdapter adapter = new MenuAdapter(context);
		gridView.setAdapter(adapter);
	}

	private void initData() {
		String url = "http://192.168.1.15:8088/hospitalWeiSystem/hospital/app/getHospitalInformation_client";
		OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
		final Request request = new Request.Builder()
				.url(url)
				.build();
//new call
		Call call = mOkHttpClient.newCall(request);
//请求加入调度
		call.enqueue(new Callback()
		{
			@Override
			public void onFailure(Request request, IOException e)
			{
			}

			@Override
			public void onResponse(final Response response) throws IOException
			{
				String htmlStr =  response.body().string();
				gsonforResult(htmlStr);
			}
		});
	}
	private void gsonforResult(String result) {
		Gson gson = new Gson();
		MapData mapData = gson.fromJson(result, MapData.class);
		String coordinateXx = mapData.getCoordinateXx();
		String coordinateYy = mapData.getCoordinateYy();
		String hospitalName = mapData.getHospitalName();
	}
	//==========================定位=================================================
	private void initMap() {
		//获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmap_shinei);
//		mTvShinei = (TextView) findViewById(R.id.tv_shinei);

		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(lat, lon)));
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 地设置地图显示的缩放级别[3, 20]
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(17));
//        定位
		initLocation();
		beginLocation();
	}
	private void beginLocation() {
		// 3. 设置定义参数，开始定位；
		LocationClientOption option = new LocationClientOption();

		option.setScanSpan(5000);			// 每5秒获取一次新的位置
		option.setIsNeedAddress(true);		// 返回详细的地址信息
		option.setCoorType("bd09ll");		// 设置坐标系类型
		// 定位模式：高精度
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

		client.setLocOption(option);
		client.start();		// 开始定位
	}
	private void initLocation() {
		// 打开定位图层，显示我的位置
		mBaiduMap.setMyLocationEnabled(true);
		// mode - 定位图层显示方式, 默认为 LocationMode.NORMAL 普通态
		// enableDirection - 是否允许显示方向信息
		// customMarker - 设置用户自定义定位图标，可以为 null
		MyLocationConfiguration configuration = new MyLocationConfiguration(
				MyLocationConfiguration.LocationMode.FOLLOWING, false, null);
		mBaiduMap.setMyLocationConfigeration(configuration);


		// 1. 创建搜索对象
		client = new LocationClient(this);
		// 2. 设置监听器
		client.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					return;
				}

				// 4. 在监听器的回调方法中，接收定位结果；
				// 纬度
				mDlatitude = location.getLatitude();
				// 经度
				mDlongitude = location.getLongitude();
				String address = location.getAddrStr();		// 详细的地址信息
				String city = location.getCity();			// 城市
				getDistance(113.378743, 23.128953, mDlongitude, mDlatitude);
				String info = "city: " + city
						+ "   address： " + address+"\n"
						+ "   latitude: " + mDlatitude+"\n"
						+ "   longitude: " + mDlongitude+"\n"
						+"    距离办公室："+mS;
//				if(mS>50){
//					Toast.makeText(getApplicationContext(),"你已经超出使用范围",Toast.LENGTH_SHORT).show();
//				}

				System.out.println("-------: " + info);

//				mTvShinei.setText(info);
				// 设置当前所有位置
				MyLocationData data = new MyLocationData.Builder()
						.latitude(mDlatitude)
						.longitude(mDlongitude)
						// 显示定位精度
						.accuracy(location.getRadius())
						.build();
				mBaiduMap.setMyLocationData(data);
			}
		});
	}

	private void getDistance(double longitude, double latitude, double dlongitude, double dlatitude) {
		double Lat1 = rad(latitude);
		double Lat2 = rad(dlatitude);
		double a = Lat1 - Lat2;
		double b = rad(longitude) - rad(dlongitude);
		mS = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(Lat1) * Math.cos(Lat2)
				* Math.pow(Math.sin(b / 2), 2)));
		mS = mS * EARTH_RADIUS;
		mS = Math.round(mS * 10000) / 10000;

//        Toast.makeText(getApplicationContext(), mPoiInfo.name + ":" + mPoiInfo.address,Toast.LENGTH_SHORT).show();
		System.out.println("===============================================: " + mS);
	}
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		client.stop();
	}
	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}
//==========================定位=================================================

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
		if (sp.getBoolean("menuChange", false)) {
			MenuAdapter adapter = new MenuAdapter(context);
			gridView.setAdapter(adapter);
			Editor editor = sp.edit();
			editor.putBoolean("menuChange", false);
			editor.commit();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Dialog dialog = new AlertDialog.Builder(MainMenuActivity.this)
					.setTitle(R.string.dialog_title)
					.setMessage(R.string.dialog_message)
					.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							SharedPreferences sp = context.getSharedPreferences("SP",
									context.MODE_PRIVATE);
							BookDBHelper bookDBHelper = new BookDBHelper(getApplicationContext());
							Cursor cursor = bookDBHelper.select(null, "book_dlflag=? and username=?", new String[]{"false", sp.getString("UserName", "")});
							while(cursor.moveToNext()){
								System.out.println("退出重下");
								bookDBHelper.update(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
										cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
										"false", "true", sp.getString("UserName", ""));
							}
							((MainMenuActivity)MainMenuActivity.this).quit();
						}
					})//设置确定按钮
					.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					})//设置取消按钮
					.create();
			dialog.show();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		Uri uri;
		switch (v.getId()) {
//		case R.id.menu_Btn1:
//			intent.setClass(this, LocalShuJiaActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.menu_Btn2:
//			intent.setClass(this, KechengActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.menu_Btn3:
//			intent.setClass(this, ZiLiaoJiaActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.menu_Btn4:
////			intent.setClass(this, XiaoXiActivity.class);
//			intent.setClass(this, ZhiChangListActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.menu_Btn5:
//			intent.setClass(this, YaoYiYaoActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.menu_Btn6:
//			intent.setClass(this, MipcaActivityCapture.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.menu_Btn7:
//			intent.setClass(this, ClassicBooksMainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.menu_Btn8:
//			intent.setClass(this, PublicClassMainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.menu_Btn9:
//			intent.setClass(this, HuoDongListActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
//		case R.id.menu_Btn10:
//			bundle = new Bundle();
//			bundle.putString("url", "http://data.iego.net:85/act/actLIst.aspx?"
//					+ "n=" + sp.getString("UserName", "")
//					+ "&d=" + sp.getString("KEY","" )
//					+ "&imei=" + tm.getDeviceId());
//			intent.putExtras(bundle);
//			intent.setClass(this, NetActivity.class);
//			startActivity(intent);

//			intent.setClass(this, HuoDongListActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			break;
			case R.id.main_menu_list_set:
				intent.setClass(this, Personal_Settings.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
		}
		overridePendingTransition(R.anim.my_scale_action,
				R.anim.my_alpha_action);
	}

	public void quit(){
		this.finish();
	}

	class MenuAdapter extends BaseAdapter {

		private Context context;
		private int[] imgRes = new int[]{R.drawable.mm_shujia,R.drawable.mm_kecheng,R.drawable.mm_yaoyiyao,
				R.drawable.mm_saoyisao,R.drawable.mm_gongkai,R.drawable.mm_zhongwai,
				R.drawable.mm_xueyaji,R.drawable.mm_zazhi};
		private SharedPreferences sp;
		private String[] need;

		public MenuAdapter(Context context){
			this.context = context;
			sp = this.context.getSharedPreferences("SP", this.context.MODE_PRIVATE);
			need = sp.getString("menuKey", "1-2-3-4-5-6-7-8").split("-");
			System.out.println("need.length=" + need.length);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return need.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.main_menu_item, null);
			}
			final int i = Integer.parseInt(need[position]);
			ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.mm_imgbtn);
			imageButton.setImageResource(imgRes[i-1]);
			imageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					Uri uri;
					switch (i) {
						case 1:if(mS>50){
							Toast.makeText(getApplicationContext(),"你已经超出使用范围"+mS,Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(getApplicationContext(), "正常使用范围" + mS, Toast.LENGTH_SHORT).show();
							intent.setClass(context, LocalShuJiaActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
						}
							break;
						case 2:
							intent.setClass(context, KechengActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
							break;
						case 3:
							intent.setClass(context, YaoYiYaoActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
							break;
						case 4:
							intent.setClass(context, MipcaActivityCapture.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
							break;
						case 5:
							intent.setClass(context, PublicClassMainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
							break;
						case 6:
							intent.setClass(context, ClassicBooksMainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
							break;
						case 7:
							intent.setClass(context, XueYaJi.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
							break;
						case 8:
						intent.setClass(context, DianZiZaZhiActivity.class);
//							intent.setClass(context, MapActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							context.startActivity(intent);
							break;
					}
				}
			});
			return convertView;
		}

	}

}
