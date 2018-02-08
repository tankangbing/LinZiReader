package com.hn.linzi.activity;
//===============================fan凡益旧版本====================================================================
//import android.app.ActionBar;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.FrameLayout;
//
//import com.sanminge.reader.views.BaseActivity;
//import com.sanminge.reader2.R;
//
//public class WebActivity extends BaseActivity{
//
//	private WebView webView;
//	private ProgressDialog waitdialog = null;
//	private Context context;
//	private View xCustomView;
//	private FrameLayout video_fullView;// 全屏时视频加载view
//    private WebChromeClient.CustomViewCallback xCustomViewCallback;
//    private myWebChromeClient xwebchromeclient;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_web);
//		webView = (WebView)findViewById(R.id.my_web);
//
//		ActionBar actionBar = getActionBar();
//		actionBar.hide();
//
//		waitdialog = new ProgressDialog(this);
//		waitdialog.setTitle("提示");
//		waitdialog.setMessage("页面加载中...");
//		waitdialog.setIndeterminate(true);
//		waitdialog.setCancelable(true);
//		waitdialog.show();
//
//		WebSettings ws = webView.getSettings();
//        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
//        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕
//
//        ws.setUseWideViewPort(true);
//        ws.setSupportZoom(false); // 不可任意比例缩放
//
//        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
//
//        ws.setSavePassword(true);
//        ws.setSaveFormData(true);// 保存表单数据
//        ws.setJavaScriptEnabled(true);// js
//        ws.setDomStorageEnabled(true);
//        ws.setSupportMultipleWindows(true);// 新加
//        xwebchromeclient = new myWebChromeClient();
//        webView.setWebChromeClient(xwebchromeclient);
//        webView.setWebViewClient(new myWebViewClient());
//        webView.loadUrl("http://192.168.1.58:8088/smLibrary");
//
//        webView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { // 表示按返回键时的操作
//                        webView.goBack(); // 后退
//
//                        // webview.goForward();//前进
//                        return true; // 已处理
//                    }
//                }
//                return false;
//            }
//        });
//
//	}
//	public class myWebChromeClient extends WebChromeClient {
//        private View xprogressvideo;
//
//        // 播放网络视频时全屏会被调用的方法
//        @Override
//        public void onShowCustomView(View view, CustomViewCallback callback) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            webView.setVisibility(View.INVISIBLE);
//            // 如果一个视图已经存在，那么立刻终止并新建一个
//            if (xCustomView != null) {
//                callback.onCustomViewHidden();
//                return;
//            }
//            video_fullView.addView(view);
//            xCustomView = view;
//            xCustomViewCallback = callback;
//            video_fullView.setVisibility(View.VISIBLE);
//        }
//
//        // 视频播放退出全屏会被调用的
//        @Override
//        public void onHideCustomView() {
//            if (xCustomView == null)// 不是全屏播放状态
//                return;
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            xCustomView.setVisibility(View.GONE);
//            video_fullView.removeView(xCustomView);
//            xCustomView = null;
//            video_fullView.setVisibility(View.GONE);
//            xCustomViewCallback.onCustomViewHidden();
//            webView.setVisibility(View.VISIBLE);
//        }
//
//        // 视频加载时进程loading
//        @Override
//        public View getVideoLoadingProgressView() {
//            if (xprogressvideo == null) {
//                LayoutInflater inflater = LayoutInflater.from(WebActivity.this);
//                xprogressvideo = inflater.inflate(
//                        R.layout.video_loading_progress, null);
//            }
//            return xprogressvideo;
//        }
//    }
//
//    public class myWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return false;
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            waitdialog.dismiss();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        webView.onPause();
//        webView.pauseTimers();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // video_fullView.removeAllViews();
//        webView.clearCache(true);
//        webView.stopLoading();
//        webView.setWebChromeClient(null);
//        webView.setWebViewClient(null);
//        webView.destroy();
//        webView = null;
//    }
//
//	@Override
//	public void finish() {
//		// TODO Auto-generated method stub
//		ViewGroup view = (ViewGroup) getWindow().getDecorView();
//		view.removeAllViews();
//		super.finish();
//	}
//}
//===============================fan凡益旧版本====================================================================



//===============================修改返回加载不了数据的bug 7-25====================================================================

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

import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.R;


public class WebActivity extends BaseActivity implements View.OnClickListener {

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
//        String s = "http://192.168.1.11:8088/smLibrary/";
        String s = "http://58.22.219.218:9111/smLibrary";
//        String http ="http://192.168.1.49:8020/WebAppv01/index-list.html?userName=duzhe&branchName=%E4%B8%AD%E5%BF%83%E9%A6%86";
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