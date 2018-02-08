package com.hn.epub;

import com.hn.linzi.R;

import android.app.Activity;
import android.content.Context;
//import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
/**   
* @Description: 底部菜单按钮
*/ 
public class MenuBottomActivity extends Activity {

	static OnMenuItemSelectedListener mListener = null;
	private Context mContext = null;
	private LayoutInflater mLayoutInflater = null;
	private PopupWindow mPopupWindow = null;
	private boolean mIsShowing = false;

	public MenuBottomActivity() {
	}

	public interface OnMenuItemSelectedListener {
		public void MenuItemSelectedEvent(int selection,int var2,int var3);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_bottom);		
	}

	
	public boolean isShowing() { return mIsShowing; }
	public MenuBottomActivity(Context context,OnMenuItemSelectedListener listener, LayoutInflater lo) {
		mListener = listener;		
		mContext = context;
		mLayoutInflater = lo;		
	}
	
	public synchronized void show(View v) {		
		mIsShowing = true;
		View mView= mLayoutInflater.inflate(R.layout.activity_menu_bottom, null);
		mPopupWindow = new PopupWindow(mView,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);//dip2px(mContext,300));
        
       	mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);   
       	
       	final TableLayout table5 = (TableLayout)mView.findViewById(R.id.table_btns);
       	final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) table5.getLayoutParams();

       	LinearLayout btn1 = (LinearLayout)mView.findViewById(R.id.btn1);
		btn1.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mListener.MenuItemSelectedEvent(5,0,0);	
			}
		});
		LinearLayout btn2 = (LinearLayout)mView.findViewById(R.id.btn2);
	
		btn2.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {			
				mListener.MenuItemSelectedEvent(6,0,0);
			}
		});
			
		LinearLayout btn3 = (LinearLayout)mView.findViewById(R.id.btn3);//设置		
		btn3.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {			
				mListener.MenuItemSelectedEvent(8,0,0);
			}
		});
		
		LinearLayout btn4 = (LinearLayout)mView.findViewById(R.id.btn4);//日间，夜间
		final TextView daynight = (TextView)mView.findViewById(R.id.tv_daynight);
			
		if(Turnmain3.daymode.equals("day"))
			daynight.setText("夜间");
		else
			daynight.setText("日间");
		btn4.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(daynight.getText().equals("日间"))
					daynight.setText("夜间");
				else
					daynight.setText("日间");
				mListener.MenuItemSelectedEvent(9,0,0);	
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
	public static int dip2px(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density ;
		return (int) (dpValue * scale + 0.5f) ;
	}

	
	
}
