package com.hn.linzi.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hn.linzi.R;
import com.hn.linzi.views.BaseActivity;

/**
 * Created by Administrator on 2017/9/11 0011.
 */

public class MapActivity extends BaseActivity {

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
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_shinei);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmap_shinei);
        mTvShinei = (TextView) findViewById(R.id.tv_shinei);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(lat, lon)));
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 地设置地图显示的缩放级别[3, 20]
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(17));
//        定位
        initLocation();
        beginLocation();
        //通过地址查坐标
//        initData();
        ActionBar actionBar = getActionBar();
        actionBar.hide();
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
                if(mS>50){
                    Toast.makeText(getApplicationContext(),"你已经超出使用范围",Toast.LENGTH_SHORT).show();
                }

                System.out.println("-------: " + info);

                mTvShinei.setText(info);
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
//=======================通过地址查坐标============================================
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


//=======================通过地址查坐标============================================


    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        client.stop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
