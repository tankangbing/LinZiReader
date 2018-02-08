package com.hn.linzi.activity;

import java.io.File;

import com.hn.linzi.utils.FileUtils;
import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NetActivity extends BaseActivity {
	
	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;
	private ValueCallback<Uri> mUploadMessage;
	private Context context;
	private WebView webView;
	private FrameLayout video_fullView;// 全屏时视频加载view
	private View xCustomView;
	private ProgressDialog waitdialog = null;
	private CustomViewCallback xCustomViewCallback;
	private myWebChromeClient xwebchromeclient;
	private Button back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉应用标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.net);
		
		TextView textView = (TextView) findViewById(R.id.daohanglan);
		textView.setText("智泉教育");
		back = (Button) findViewById(R.id.ziliaojiaxianxi_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		waitdialog = new ProgressDialog(this);
		waitdialog.setTitle("提示");
		waitdialog.setMessage("页面加载中...");
		waitdialog.setIndeterminate(true);
		waitdialog.setCancelable(true);
		waitdialog.show();

		webView = (WebView) findViewById(R.id.net_webview);
		video_fullView = (FrameLayout) findViewById(R.id.video_fullView);

		WebSettings ws = webView.getSettings();
		ws.setPluginState(PluginState.ON);
		
		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		// ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕
		ws.setUseWideViewPort(true);// 可任意比例缩放
		ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
		ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptCanOpenWindowsAutomatically(true);
		ws.setJavaScriptEnabled(true);
//		ws.setGeolocationEnabled(true);// 启用地理定位
//		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
		ws.setDomStorageEnabled(true);
		ws.setSupportMultipleWindows(true);// 新加
		xwebchromeclient = new myWebChromeClient();
		webView.setWebChromeClient(xwebchromeclient);
		webView.setWebViewClient(new myWebViewClient());
		Bundle bundle = this.getIntent().getExtras();
		String url = bundle.getString("url");
		webView.loadUrl(url);
		
		if(url.indexOf("actsign") >= 0){
			Dialog dialog = new AlertDialog.Builder(this)
	        .setTitle("签到")
	        .setMessage("签到成功")
	        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	
	            }
	        })//设置确定按钮
	        .create();
			dialog.show();
		}
		webView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& webView.canGoBack()) { // 表示按返回键时的操作
						webView.goBack(); // 后退
	
						// webview.goForward();//前进
						return true; // 已处理
					}
				}
				return false;
			}
		});
	}

	public class myWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			waitdialog.dismiss();
		}
	}
	
	/**
	 * 本地相册选择图片
	 */
	String compressPath = "";
	
	private void chosePic() {
		FileUtils.delFile(compressPath);
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
		String IMAGE_UNSPECIFIED = "image/*";
		innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		startActivityForResult(wrapperIntent, REQ_CHOOSE);
		compressPath = Environment
				.getExternalStorageDirectory()
				.getPath()
				+ "/fuiou_wmp/temp";
		new File(compressPath).mkdirs();
		compressPath = compressPath + File.separator
				+ "compress.jpg";
	}
	
	/**
	 * 选择照片后结束
	 * 
	 * @param data
	 */
	private Uri afterChosePic(Intent data) {

		// 获取图片的路径：
		String[] proj = { MediaStore.Images.Media.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
		if(cursor == null ){
			Toast.makeText(this, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
			return null;
		}
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		String path = cursor.getString(column_index);
		if(path != null && (path.endsWith(".png")||path.endsWith(".PNG")||path.endsWith(".jpg")||path.endsWith(".JPG"))){
			File newFile = FileUtils.compressFile(path, compressPath);
			return Uri.fromFile(newFile);
		}else{
			Toast.makeText(this, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	/**
	 * 返回文件选择
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		
		if (null == mUploadMessage)
			return;
		Uri uri = null;
		if(requestCode == REQ_CAMERA ){
//			afterOpenCamera();
//			uri = cameraUri;
		}else if(requestCode == REQ_CHOOSE){
			uri = afterChosePic(intent);
		}
		mUploadMessage.onReceiveValue(uri);
		mUploadMessage = null;
		super.onActivityResult(requestCode, resultCode, intent);
	}

	public class myWebChromeClient extends WebChromeClient {
		private View xprogressvideo;
		// For Android 3.0+
		   public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {  
            if (mUploadMessage != null) return;
            mUploadMessage = uploadMsg;   
            chosePic();
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("*/*");
//                startActivityForResult( Intent.createChooser( i, "File Chooser" ), FILECHOOSER_RESULTCODE );
        }
         // For Android < 3.0
	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
	               openFileChooser( uploadMsg, "" );
	        }
	        // For Android  > 4.1.1
	      public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
	              openFileChooser(uploadMsg, acceptType);
	      }
   

		// 播放网络视频时全屏会被调用的方法
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			webView.setVisibility(View.INVISIBLE);
			// 如果一个视图已经存在，那么立刻终止并新建一个
			if (xCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			video_fullView.addView(view);
			xCustomView = view;
			xCustomViewCallback = callback;
			video_fullView.setVisibility(View.VISIBLE);
		}

		// 视频播放退出全屏会被调用的
		@Override
		public void onHideCustomView() {
			if (xCustomView == null)// 不是全屏播放状态
				return;

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			xCustomView.setVisibility(View.GONE);
			video_fullView.removeView(xCustomView);
			xCustomView = null;
			video_fullView.setVisibility(View.GONE);
			xCustomViewCallback.onCustomViewHidden();
			webView.setVisibility(View.VISIBLE);
		}

		// 视频加载时进程loading
		@Override
		public View getVideoLoadingProgressView() {
			if (xprogressvideo == null) {
				LayoutInflater inflater = LayoutInflater
						.from(NetActivity.this);
				xprogressvideo = inflater.inflate(
						R.layout.video_loading_progress, null);
			}
			return xprogressvideo;
		}
	}

	/**
	 * 判断是否是全屏
	 * 
	 * @return
	 */
	public boolean inCustomView() {
		return (xCustomView != null);
	}

	/**
	 * 全屏时按返加键执行退出全屏方法
	 */
	public void hideCustomView() {
		xwebchromeclient.onHideCustomView();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		super.onResume();
		webView.onResume();
		webView.resumeTimers();

		/**
		 * 设置为横屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
		webView.pauseTimers();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		video_fullView.removeAllViews();
		webView.getSettings().setBuiltInZoomControls(true);
//		webView.loadUrl("about:blank");
//		webView.stopLoading();
//		webView.setWebChromeClient(null);
//		webView.setWebViewClient(null);
		webView.setVisibility(View.GONE);
//		webView.destroy();
//		webView = null;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (inCustomView()) {
				// webViewDetails.loadUrl("about:blank");
				hideCustomView();
				return true;
			} else {
				webView.loadUrl("about:blank");
				NetActivity.this.finish();
			}
		}
		return false;
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
		super.finish();
	}
}
