package com.hn.linzi.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.hn.linzi.R;
import com.hn.linzi.utils.Constants;
import com.hn.linzi.utils.PreferencesUtils;
import com.hn.linzi.views.BaseActivity;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class XueYaJi extends BaseActivity implements OnClickListener{

    private LinearLayout mBtnchat;
    private LinearLayout mBtnshow;
    private LinearLayout mBtnmreasure;
    private Button mXueyajiback;

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
        initView();
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }
    private void initView() {
        setContentView(R.layout.activity_xueyaji);
        LinearLayout activitybg = (LinearLayout) findViewById(R.id.activity_bg);
        mBtnchat = (LinearLayout) findViewById(R.id.btn_chat);
        mBtnshow = (LinearLayout) findViewById(R.id.btn_show);
        mBtnmreasure = (LinearLayout) findViewById(R.id.btn_mreasure);
        RelativeLayout xiaoxititle = (RelativeLayout) findViewById(R.id.xiaoxi_title);
        mXueyajiback = (Button) findViewById(R.id.xueyaji_back);
        TextView daohanglan = (TextView) findViewById(R.id.daohanglan);

        mXueyajiback.setOnClickListener(this);
        mBtnchat.setOnClickListener(this);
        mBtnshow.setOnClickListener(this);
        mBtnmreasure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mreasure:
//                Toast.makeText(getApplicationContext(), "Hello,I come from other thread!",Toast.LENGTH_SHORT ).show();
                //测量血压
                Intent intent1 = new Intent(XueYaJi.this, MeasureActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_show:
                //展示数据
                boolean isCe = PreferencesUtils.getBoolean(getApplicationContext(), Constants.IS_CE);
                if (isCe) {
                    Intent intent4 = new Intent(XueYaJi.this, ShowActivity.class);
                    startActivity(intent4);
                } else {
                    Toast.makeText(getApplicationContext(), "亲！还没数据，赶紧先去测量吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.btn_chat:
                //曲线图
                boolean isQu = PreferencesUtils.getBoolean(getApplicationContext(),Constants.IS_QU);
                if (isQu) {
                    Intent intent3 = new Intent(XueYaJi.this, CurveActivity.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(getApplicationContext(), "亲！还没数据，赶紧先去测量吧", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.xueyaji_back:
                finish();
                break;
            default:
                break;
        }
    }
}
