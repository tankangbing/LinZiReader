package com.hn.epub;

import com.hn.linzi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
/**   
* @Description: 设置菜单页面
*/ 
public class Setup extends Activity {

	CheckBox cb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setup);
		
		
		TableLayout tb2 = (TableLayout)findViewById(R.id.menu_bottom_table2);
		TableLayout tb3 = (TableLayout)findViewById(R.id.menu_bottom_table3);
		TableLayout tb4 = (TableLayout)findViewById(R.id.menu_bottom_table4);
		TableLayout tb5 = (TableLayout)findViewById(R.id.menu_bottom_table5);
		TableLayout tb6 = (TableLayout)findViewById(R.id.menu_bottom_table6);
		
		RelativeLayout.LayoutParams lp2 = (RelativeLayout.LayoutParams)tb2.getLayoutParams();
		RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams)tb3.getLayoutParams();
		RelativeLayout.LayoutParams lp4 = (RelativeLayout.LayoutParams)tb4.getLayoutParams();
		RelativeLayout.LayoutParams lp5 = (RelativeLayout.LayoutParams)tb5.getLayoutParams();
		RelativeLayout.LayoutParams lp6 = (RelativeLayout.LayoutParams)tb6.getLayoutParams();
		int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
		if(rotation!=0)
		{
			lp2.height=dip2px(this,45);
			lp2.setMargins(0, 0, 0, 0);
			tb2.setLayoutParams(lp2);
			
			lp3.height=dip2px(this,45);
			lp3.setMargins(0, dip2px(this,46), 0, 0);
			tb3.setLayoutParams(lp3);
			
			lp4.height=dip2px(this,45);
			lp4.setMargins(0, dip2px(this,92), 0, 0);
			tb4.setLayoutParams(lp4);
			
			lp5.height=dip2px(this,40);
			lp5.setMargins(0, dip2px(this,138), 0, 0);
			tb5.setLayoutParams(lp5);
			
			lp6.height=dip2px(this,40);
			lp6.setMargins(0, dip2px(this,179), 0, 0);
			tb6.setLayoutParams(lp6);
		}
		else
		{
			lp2.height=dip2px(this,60);
			lp2.setMargins(0, 0, 0, 0);
			tb2.setLayoutParams(lp2);
			
			lp3.height=dip2px(this,60);
			lp3.setMargins(0, dip2px(this,61), 0, 0);
			tb3.setLayoutParams(lp3);
			
			lp4.height=dip2px(this,60);
			lp4.setMargins(0, dip2px(this,122), 0, 0);
			tb4.setLayoutParams(lp4);
			
			lp5.height=dip2px(this,60);
			lp5.setMargins(0, dip2px(this,183), 0, 0);
			tb5.setLayoutParams(lp5);
			
			lp6.height=dip2px(this,60);
			lp6.setMargins(0, dip2px(this,244), 0, 0);
			tb6.setLayoutParams(lp6);
		}
		
		LayoutParams p = getWindow().getAttributes(); 
		p.height = LayoutParams.WRAP_CONTENT; 
		p.width = LayoutParams.MATCH_PARENT;
		p.gravity = Gravity.BOTTOM; 
		
		SeekBar seekbar2 = (SeekBar)findViewById(R.id.seekBar2);//亮度
		seekbar2.setMax(100);		
		seekbar2.setProgress((int)(Turnmain3.bright*100));
		
		seekbar2.setOnSeekBarChangeListener(new sbListener2());	
		cb = (CheckBox)findViewById(R.id.checkBox1);//跟随系统		
		cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {		            
		            @Override
		            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {		
		            	int chk=0;
		            	if(arg1)
		            		chk=1;	
		            	MenuBottomActivity.mListener.MenuItemSelectedEvent(32,chk,0);		            			            		
		            }
		        });		
		
		TextView tv31 = (TextView)findViewById(R.id.textView1);//增加字体		
		tv31.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {			
				MenuBottomActivity.mListener.MenuItemSelectedEvent(7,1,0);
			}
		});
		TextView tv32 = (TextView)findViewById(R.id.textView2);//缩小字体		
		tv32.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {			
				MenuBottomActivity.mListener.MenuItemSelectedEvent(7,2,0);
			}
		});
		
		final Resources res = getResources();		
		//final ImageView ivcheck=(ImageView)findViewById(R.id.bgcheck);//背景check	

		final ImageView ivcheck = new ImageView(this);  
		ivcheck.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));  
		ivcheck.setImageResource(R.drawable.bg_checked);  
		TableRow tr=(TableRow)findViewById(R.id.menu_bottom_tablerow1);
		tr.addView(ivcheck);
		 
		final ImageView ivbg0=(ImageView)findViewById(R.id.bg0);
		
		ivbg0.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {				
				ivcheck.layout(ivbg0.getLeft(),ivbg0.getTop(), ivbg0.getLeft()+ivbg0.getWidth(), ivbg0.getTop()+ivbg0.getHeight());
				Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.bg_0);
				int c=bmp.getPixel(100, 100);
				MenuBottomActivity.mListener.MenuItemSelectedEvent(33,c,0);
			}  
		});  
		
		final ImageView ivbg1=(ImageView)findViewById(R.id.bg1);			
		ivbg1.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {		
				ivcheck.layout(ivbg1.getLeft(),ivbg1.getTop(), ivbg1.getLeft()+ivbg1.getWidth(), ivbg1.getTop()+ivbg1.getHeight());
				Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.bg_1);   
				int c=bmp.getPixel(100, 100);
				MenuBottomActivity.mListener.MenuItemSelectedEvent(33,c,1);
			}  
		});
		
		final ImageView ivbg2=(ImageView)findViewById(R.id.bg2);			
		ivbg2.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {
				ivcheck.layout(ivbg2.getLeft(),ivbg2.getTop(), ivbg2.getLeft()+ivbg2.getWidth(), ivbg2.getTop()+ivbg2.getHeight());
				Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.bg_2);   
				int c=bmp.getPixel(100, 100);
				MenuBottomActivity.mListener.MenuItemSelectedEvent(33,c,2);
			}  
		});  
		
		final ImageView ivbg3=(ImageView)findViewById(R.id.bg3);			
		ivbg3.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {
				ivcheck.layout(ivbg3.getLeft(),ivbg3.getTop(), ivbg3.getLeft()+ivbg3.getWidth(), ivbg3.getTop()+ivbg3.getHeight());	
				Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.bg_3);   
				int c=bmp.getPixel(100, 100);
				MenuBottomActivity.mListener.MenuItemSelectedEvent(33,c,3);
			}  
		});  
		
		final ImageView ivbg4=(ImageView)findViewById(R.id.bg4);			
		ivbg4.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {				
				ivcheck.layout(ivbg4.getLeft(),ivbg4.getTop(), ivbg4.getLeft()+ivbg4.getWidth(), ivbg4.getTop()+ivbg4.getHeight());
				Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.bg_4);   
				int c=bmp.getPixel(100, 100);
				MenuBottomActivity.mListener.MenuItemSelectedEvent(33,c,4);
			}  
		});  
		
		final ImageView ivbg5=(ImageView)findViewById(R.id.bg5);			
		ivbg5.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {
				ivcheck.layout(ivbg5.getLeft(),ivbg5.getTop(), ivbg5.getLeft()+ivbg5.getWidth(), ivbg5.getTop()+ivbg5.getHeight());	
				Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.bg_5);   
				int c=bmp.getPixel(100, 100);
				MenuBottomActivity.mListener.MenuItemSelectedEvent(33,c,5);
			}  
		});  
		
		final ImageView ivbg6=(ImageView)findViewById(R.id.bg6);			
		ivbg6.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {
				ivcheck.layout(ivbg6.getLeft(),ivbg6.getTop(), ivbg6.getLeft()+ivbg6.getWidth(), ivbg6.getTop()+ivbg6.getHeight());	
				Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.bg_6);   
				int c=bmp.getPixel(100, 100);
				MenuBottomActivity.mListener.MenuItemSelectedEvent(33,c,6);
			}  
		});  
		

		ivcheck.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {    
			 @Override    
			 public void onGlobalLayout() {    
				ivcheck.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
				SharedPreferences sharedPreferences = getSharedPreferences("reader", Activity.MODE_PRIVATE); 
				String value = sharedPreferences.getString("bkcolor2", "");
				if(value.equals("0"))
					ivcheck.layout(ivbg0.getLeft(),ivbg0.getTop(), ivbg0.getLeft()+ivbg0.getWidth(), ivbg0.getTop()+ivbg0.getHeight());	
				else if(value.equals("1"))
					ivcheck.layout(ivbg1.getLeft(),ivbg1.getTop(), ivbg1.getLeft()+ivbg1.getWidth(), ivbg1.getTop()+ivbg1.getHeight());
				else if(value.equals("2"))
					ivcheck.layout(ivbg2.getLeft(),ivbg2.getTop(), ivbg2.getLeft()+ivbg2.getWidth(), ivbg2.getTop()+ivbg2.getHeight());
				else if(value.equals("3"))
					ivcheck.layout(ivbg3.getLeft(),ivbg3.getTop(), ivbg3.getLeft()+ivbg3.getWidth(), ivbg3.getTop()+ivbg3.getHeight());
				else if(value.equals("4"))
					ivcheck.layout(ivbg4.getLeft(),ivbg4.getTop(), ivbg4.getLeft()+ivbg4.getWidth(), ivbg4.getTop()+ivbg4.getHeight());
				else if(value.equals("5"))
					ivcheck.layout(ivbg5.getLeft(),ivbg5.getTop(), ivbg5.getLeft()+ivbg5.getWidth(), ivbg5.getTop()+ivbg5.getHeight());
				else if(value.equals("6"))
					ivcheck.layout(ivbg6.getLeft(),ivbg6.getTop(), ivbg6.getLeft()+ivbg6.getWidth(), ivbg6.getTop()+ivbg6.getHeight());
			            }    
			 });
		
		final Button btn1=(Button)findViewById(R.id.button1);	
		final Button btn2=(Button)findViewById(R.id.button2);	
		final Button btn3=(Button)findViewById(R.id.button3);	
		
		if(Turnmain3.linespace==1.0f)
			btn1.setBackgroundColor(Color.argb(255, 200, 200, 200)); 
		if(Turnmain3.linespace==1.3f)
			btn2.setBackgroundColor(Color.argb(255, 200, 200, 200)); 
		if(Turnmain3.linespace==1.6f)
			btn3.setBackgroundColor(Color.argb(255, 200, 200, 200)); 
		
		btn1.setOnClickListener(new Button.OnClickListener(){
		          public void onClick(View v) {
		        	  btn1.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn2.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn3.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		              btn1.setBackgroundColor(Color.argb(255, 200, 200, 200));  
		              
		              for(int i=0;i<3;i++)
		              {
		            	  Turnmain3.linespace=1.0f;
		            	  Turnmain3.readview[i].setLineSpacing(0.0f, Turnmain3.linespace);
		              }
		              Turnmain3.changeLineSpace=true;
		              WriteData("linespace",Turnmain3.linespace+"");
		            }     
			});    				
		btn2.setOnClickListener(new Button.OnClickListener(){
		          public void onClick(View v) {    
		        	  btn1.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn2.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn3.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		              btn2.setBackgroundColor(Color.argb(255, 200, 200, 200));
		              for(int i=0;i<3;i++)
			          {
			            	  Turnmain3.linespace=1.3f;
			            	  Turnmain3.readview[i].setLineSpacing(0.0f, Turnmain3.linespace);
			          }
			          Turnmain3.changeLineSpace=true; 
			          WriteData("linespace",Turnmain3.linespace+"");
		            }     
			});				
		btn3.setOnClickListener(new Button.OnClickListener(){//创建监听    
		          public void onClick(View v) {    
		        	  btn1.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn2.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn3.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		              btn3.setBackgroundColor(Color.argb(255, 200, 200, 200));
		              
		              for(int i=0;i<3;i++)
		              {
		            	  Turnmain3.linespace=1.6f;
		            	  Turnmain3.readview[i].setLineSpacing(0.0f, Turnmain3.linespace);
		              }
		              Turnmain3.changeLineSpace=true;
		              WriteData("linespace",Turnmain3.linespace+"");
		            }     
			});
		
		
		final Button btn4=(Button)findViewById(R.id.button4);	
		final Button btn5=(Button)findViewById(R.id.button5);	
		final Button btn6=(Button)findViewById(R.id.button6);	
		final Button btn7=(Button)findViewById(R.id.button7);
		if(Turnmain3.waketime==2)
			btn4.setBackgroundColor(Color.argb(255, 200, 200, 200)); 
		if(Turnmain3.waketime==5)
			btn5.setBackgroundColor(Color.argb(255, 200, 200, 200));
		if(Turnmain3.waketime==10)
			btn6.setBackgroundColor(Color.argb(255, 200, 200, 200));
		if(Turnmain3.waketime==1000)
			btn7.setBackgroundColor(Color.argb(255, 200, 200, 200));
		btn4.setOnClickListener(new Button.OnClickListener(){//创建监听    
		          public void onClick(View v) {
		        	  btn4.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn5.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn6.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn7.setBackgroundColor(Color.argb(255, 120, 120, 120));
		              btn4.setBackgroundColor(Color.argb(255, 200, 200, 200));	
		              Turnmain3.waketime=2;
		              WriteData("waketime",Turnmain3.waketime+"");
		              if(Turnmain3.wakeLock.isHeld())
		            	  Turnmain3.wakeLock.release();
		              Turnmain3.wakeLock.acquire(Turnmain3.waketime*60*1000);
		            }     
			});    				
		btn5.setOnClickListener(new Button.OnClickListener(){//创建监听    
		          public void onClick(View v) {    
		        	  btn4.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn5.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn6.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn7.setBackgroundColor(Color.argb(255, 120, 120, 120));
		              btn5.setBackgroundColor(Color.argb(255, 200, 200, 200)); 
		              Turnmain3.waketime=5;
		              WriteData("waketime",Turnmain3.waketime+"");
		              if(Turnmain3.wakeLock.isHeld())
		            	  Turnmain3.wakeLock.release();
		              Turnmain3.wakeLock.acquire(Turnmain3.waketime*60*1000);
		            }     
			});				
		btn6.setOnClickListener(new Button.OnClickListener(){//创建监听    
		          public void onClick(View v) {    
		        	  btn4.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn5.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn6.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
		        	  btn7.setBackgroundColor(Color.argb(255, 120, 120, 120));
		              btn6.setBackgroundColor(Color.argb(255, 200, 200, 200)); 
		              Turnmain3.waketime=10;
		              WriteData("waketime",Turnmain3.waketime+"");
		              if(Turnmain3.wakeLock.isHeld())
		            	  Turnmain3.wakeLock.release();
		              Turnmain3.wakeLock.acquire(Turnmain3.waketime*60*1000);
		            }     
			});
		btn7.setOnClickListener(new Button.OnClickListener(){//创建监听    
	          public void onClick(View v) {    
	        	  btn4.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
	        	  btn5.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
	        	  btn6.setBackgroundColor(Color.argb(255, 120, 120, 120)); 
	        	  btn7.setBackgroundColor(Color.argb(255, 120, 120, 120));
	              btn7.setBackgroundColor(Color.argb(255, 200, 200, 200));  
	              Turnmain3.waketime=1000;
	              WriteData("waketime",Turnmain3.waketime+"");
	              if(Turnmain3.wakeLock.isHeld())
	            	  Turnmain3.wakeLock.release();
	              Turnmain3.wakeLock.acquire(Turnmain3.waketime*60*1000);
	            }     
		});
	}
	public void WriteData(String name,String value) 
	{ 
		SharedPreferences mySharedPreferences = getSharedPreferences("reader", Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		editor.putString(name, value);		
		editor.commit();		
	} 
	@SuppressLint("WrongCall")
	public class sbListener2 implements SeekBar.OnSeekBarChangeListener {//调整进度
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	    	MenuBottomActivity.mListener.MenuItemSelectedEvent(31,progress,0);
	    	cb.setChecked(false);
	    }
	    public void onStartTrackingTouch(SeekBar seekBar) {}
	    public void onStopTrackingTouch(SeekBar seekBar) {}
	}	
	public static int dip2px(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density ;
		return (int) (dpValue * scale + 0.5f) ;
	}
	
}
