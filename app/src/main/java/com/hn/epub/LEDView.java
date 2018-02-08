package com.hn.epub;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.hn.linzi.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/**   
* @Description: 显示页面底部电池、时间、进度
* 维持一个计时器，由于readview必须在初始化之后才能使用，所以viewpager第一次加载、翻转屏幕、改变字体、字间距需延后才能openchapter
*/ 
public class LEDView extends LinearLayout {

	private TextView timeView;
	private TextView percentView;
	private static final String DATE_FORMAT = "%02d:%02d";
	private static final int REFRESH_DELAY = 500;

	private final Handler mHandler = new Handler();
	
	Bitmap bmp ;
	Canvas c ;
	int t=0;
	private final Runnable mTimeRefresher = new Runnable() {

		@Override
		public void run() {
			t=t+1;
			Layout lo=Turnmain3.readview[2].getLayout();
			if(lo!=null)
			{
				if(Turnmain3.firstLoad)//第一次加载
				{
					if(Turnmain3.isRotate)//是否翻转
					{
						Turnmain3.isRotate=false;
						Turnmain3.openchapter(Turnmain3.curChapter,Turnmain3.beginPos);						
					}
					else
					{					
						Turnmain3.openchapter(Turnmain3.curChapter,Turnmain3.beginPos);
					}
					Turnmain3.firstLoad=false;
					
				}
				
				if(!Turnmain3.changeTextSize)//改变字体
				{			
					Turnmain3.openchapter(Turnmain3.curChapter,Turnmain3.beginPos);
					Turnmain3.changeTextSize=true;
				}
				if(Turnmain3.changeLineSpace)//改变字间距
				{			
					Turnmain3.openchapter(Turnmain3.curChapter,Turnmain3.beginPos);
					Turnmain3.changeLineSpace=false;
				}
			}
			
			Calendar calendar = Calendar.getInstance(TimeZone .getTimeZone("GMT+8"));
			final Date d = new Date();
			calendar.setTime(d);
			
			String txt="      " + String.format(DATE_FORMAT,calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));			
			SpannableString ss = new SpannableString(txt);
			
			c.drawColor(Color.TRANSPARENT, Mode.CLEAR);  
			int lvl2=(int)(Turnmain3.lvl*0.5);
			pt.setStyle(Style.STROKE);
				
			pt.setColor(Color.LTGRAY);
			int tt=Color.BLACK;
			c.drawRect(0, 0, 50, 28, pt);//电池主体
			pt.setStyle(Style.FILL);
			pt.setColor(Color.GRAY);
			c.drawRect(51, 10, 58, 18, pt);//电池头部			
			c.drawRect(1, 1, lvl2-2, 27, pt);//电量
			
			Drawable drawable =new BitmapDrawable(bmp);
	        drawable.setBounds(0, 0, 60, 28);//这里设置图片的大小    
	        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);             
	        ss.setSpan(imageSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        //int tt=timeView.getLayout().getLineForVertical(0);
	        timeView.setText(ss);
	        
			mHandler.postDelayed(this, REFRESH_DELAY);
		}
	};

	@SuppressLint("NewApi")
	public LEDView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LEDView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LEDView(Context context) {
		super(context);
		init(context);
	}
	LayoutInflater layoutInflater;
	Paint pt=new Paint(Paint.ANTI_ALIAS_FLAG);
	private void init(Context context) {
		layoutInflater = LayoutInflater.from(context);

		View view = layoutInflater.inflate(R.layout.ledview, this);
		timeView = (TextView) view.findViewById(R.id.ledview_clock_time);	
		timeView.setBackgroundColor(Color.GRAY);
		timeView.setPadding(30, 10, 20, 0);
		
		percentView = (TextView) view.findViewById(R.id.ledview_percent);	
		percentView.setBackgroundColor(Color.GRAY);
		percentView.setPadding(30, 0, 20, 0);
		bmp=Bitmap.createBitmap(60,30, Bitmap.Config.ARGB_8888);
		c= new Canvas(bmp);

	}

	

	public void start() {
		mHandler.post(mTimeRefresher);
	}

	public void stop() {
		mHandler.removeCallbacks(mTimeRefresher);
	}
	


}
