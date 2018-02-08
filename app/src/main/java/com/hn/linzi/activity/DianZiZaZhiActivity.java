package com.hn.linzi.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hn.linzi.R;
import com.hn.linzi.views.BaseActivity;

public class DianZiZaZhiActivity extends BaseActivity implements View.OnClickListener {

    private WebView mWebView;
    private WebSettings mWebSettings;
    private RelativeLayout remain_layout;
    private Button remain_hide;
    private Button remain_all;
    private Button remain_ret;
    private Boolean checkflag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        ActionBar actionBar = getActionBar();
		actionBar.hide();
    }


    private void initView() {
        setContentView(R.layout.activity_web);
        mWebView = (WebView)findViewById(R.id.my_web);
        remain_layout = (RelativeLayout) findViewById(R.id.remain_layout);
        remain_hide = (Button) findViewById(R.id.remain_hide);
        remain_all = (Button) findViewById(R.id.remain_all);
        remain_ret = (Button) findViewById(R.id.remain_ret);
        checkflag = true;
        remain_hide.setOnClickListener(this);
        remain_all.setOnClickListener(this);
        remain_ret.setOnClickListener(this);

        mWebView.clearCache(true);
    }

    private void initData() {
        mWebSettings = mWebView.getSettings();

//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        mWebSettings.setJavaScriptEnabled(true);

//设置自适应屏幕，两者合用
        mWebSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        mWebSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        mWebSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        mWebSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//webview添加硬件加速
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

//其他细节操作
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        mWebSettings.setAllowFileAccess(true); //设置可以访问文件
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWebSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        mWebSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
//        String s = "http://192.168.1.51:8088/smLibrary";//内网
//        String s = "http://58.22.219.218:9111/smLibrary";//外网
//        String s = "m.gzlib.vip.qikan.com";
 //       String s = "http://gzlib.vip.qikan.com/wap/IndexSimple.aspx?token=&amp;v=2&amp;f=m&amp;l=zh-cn";//电子杂志
        String s = "http://gzlib.vip.qikan.com/wap/default.aspx";
        mWebView.loadUrl(s);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    @Override
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remain_layout:
                remain_layout.setVisibility(View.GONE);
                remain_hide.setVisibility(View.VISIBLE);
                break;
            case R.id.remain_hide:
                remain_layout.setVisibility(View.VISIBLE);
                remain_hide.setVisibility(View.GONE);
                break;
            case R.id.remain_all:
                remain_layout.setVisibility(View.GONE);
                remain_hide.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setClass(this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if (checkflag) {
                    checkflag = false;
                    finish();
                }
                break;
            case R.id.remain_ret:
                remain_layout.setVisibility(View.GONE);
                remain_hide.setVisibility(View.VISIBLE);
                mWebView.goBack();//返回上一级
                if (checkflag) {
                    checkflag = false;
//                    finish();
                }
                break;
        }
    }
    @Override
    public void finish() {
        // TODO Auto-generated method stub
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
}
//===============================修改返回加载不了数据的bug 7-25====================================================================