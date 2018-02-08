package com.hn.epub;


import com.hn.linzi.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
* @Description: 顶部菜单按钮
*/ 
public class MenuTopActivity extends Activity {

	private OnMenuItemSelectedListener mListener = null;
	private Context mContext = null;
	private LayoutInflater mLayoutInflater = null;
	private PopupWindow mPopupWindow = null;
	private boolean mIsShowing = false;

	public MenuTopActivity() {

	}

	public interface OnMenuItemSelectedListener {
		public void MenuItemSelectedEvent(int selection,int var2,int var3);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_top);		
	}
	public boolean isShowing() { return mIsShowing; }
	public MenuTopActivity(Context context,OnMenuItemSelectedListener listener, LayoutInflater lo) {		
		mContext = context;
		mListener = listener;
		mLayoutInflater = lo;		
	}
	public synchronized void show(View v) {	
		mIsShowing = true;
		
		View mView= mLayoutInflater.inflate(R.layout.activity_menu_top, null);        
		
		mPopupWindow = new PopupWindow(mView,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
       	mPopupWindow.showAtLocation(v, Gravity.TOP, 0, 0);       	
      
    	ImageView btn1 = (ImageView)mView.findViewById(R.id.menu_top_btn1);
		btn1.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mListener.MenuItemSelectedEvent(1,0,0);	
			}
		});
		
		ImageView btn2 = (ImageView)mView.findViewById(R.id.menu_top_btn2);
		btn2.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mListener.MenuItemSelectedEvent(3,0,0);	
			}
		});
		
		ImageView btn3 = (ImageView)mView.findViewById(R.id.menu_top_btn3);
		btn3.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mListener.MenuItemSelectedEvent(4,0,0);	
			}
		});
	}
	public synchronized void hide() {
		mIsShowing = false;
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
		return;
	}
}
