package com.hn.linzi.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.utils.UI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.hn.linzi.R;
import com.hn.linzi.activity.KeChengManageActivity;
import com.hn.linzi.activity.KechengActivity;
import com.hn.linzi.activity.MainMenuActivity;
import com.hn.linzi.activity.NetActivity;
import com.hn.linzi.data.KeChengData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KeChengAdapter extends BaseAdapter {

	private FlipViewController controller;

	private Context context;

	private LayoutInflater inflater;

	private Bitmap placeholderBitmap;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;		// DisplayImageOptions是用于设置图片显示的类
	
	private KeChengData keChengData;
	
	private KechengActivity activity;
	
	private TelephonyManager tm;
	
	private int num;
	
	private int page;
	
//	public int count;

	public KeChengAdapter(FlipViewController controller, Context context, KechengActivity activity) {
		super();
		inflater = LayoutInflater.from(context);
		this.controller = controller;
		this.context = context;
		this.activity = activity;
		this.tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		keChengData = new KeChengData(activity);
		System.out.println("keChengData.IMG_DESCRIPTIONS.size()= " + keChengData.IMG_DESCRIPTIONS.size());
		placeholderBitmap = BitmapFactory.decodeResource(
				context.getResources(), android.R.drawable.dark_header);
		num = 1;
		page = 1;
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	
//	public void setCount(int i) {
//		count = keChengData.IMG_DESCRIPTIONS.size() + i;
//	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(keChengData.IMG_DESCRIPTIONS.size() == 0){
			return 1;
		}
		return keChengData.IMG_DESCRIPTIONS.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final SharedPreferences sp = context.getSharedPreferences("SP",
				context.MODE_PRIVATE);
		View layout = convertView;
		System.out.println("position=          "+ position);

//		if (convertView == null) {
			layout = inflater.inflate(R.layout.kc_list, null);
//		}
		
		if(keChengData.IMG_DESCRIPTIONS.size() == 0){
			layout = inflater.inflate(R.layout.kong_kecheng, null);
			layout.findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
		}else{
			UI.<TextView>findViewById(layout, R.id.daohanglan).setText("课程");
			layout.findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
			final KeChengData.Data data = keChengData.IMG_DESCRIPTIONS
					.get(position);
			UI.<Button>findViewById(layout, R.id.kc_manage).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, KeChengManageActivity.class);
					context.startActivity(intent);
					activity.finish();
				}
			});
			UI.<TextView>findViewById(layout, R.id.kecheng_Title1).setText(data.title1);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat1).setText(data.cat1);
			
			AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
			imageLoader.displayImage(data.imageFilePath1, UI.<ImageView>findViewById(layout, R.id.kecheng_Img1), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath2, UI.<ImageView>findViewById(layout, R.id.kecheng_Img2), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath3, UI.<ImageView>findViewById(layout, R.id.kecheng_Img3), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath4, UI.<ImageView>findViewById(layout, R.id.kecheng_Img4), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath5, UI.<ImageView>findViewById(layout, R.id.kecheng_Img5), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath6, UI.<ImageView>findViewById(layout, R.id.kecheng_Img6), options, aDisplayListener);
			
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn1).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title1.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", data.url1 + "&n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" )+ "&imei=" +tm.getDeviceId()
							 + "&code=" + sp.getString("code", ""));
					intent.putExtras(bundle);
					intent.setClass(context, NetActivity.class);
					context.startActivity(intent);
					}
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title2).setText(data.title2);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat2).setText(data.cat2);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn2).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title2.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", data.url2 + "&n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" )+ "&imei=" +tm.getDeviceId()
							 + "&code=" + sp.getString("code", ""));
					intent.putExtras(bundle);
					intent.setClass(context, NetActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title3).setText(data.title3);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat3).setText(data.cat3);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn3).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title3.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", data.url3 + "&n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" )+ "&imei=" +tm.getDeviceId()
							 + "&code=" + sp.getString("code", ""));
					intent.putExtras(bundle);
					intent.setClass(context, NetActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title4).setText(data.title4);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat4).setText(data.cat4);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn4).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title4.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", data.url4 + "&n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" )+ "&imei=" +tm.getDeviceId()
							 + "&code=" + sp.getString("code", ""));
					intent.putExtras(bundle);
					intent.setClass(context, NetActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title5).setText(data.title5);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat5).setText(data.cat5);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn5).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title5.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", data.url5 + "&n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" )+ "&imei=" +tm.getDeviceId()
							 + "&code=" + sp.getString("code", ""));
					intent.putExtras(bundle);
					intent.setClass(context, NetActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title6).setText(data.title6);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat6).setText(data.cat6);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn6).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title6.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", data.url6 + "&n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" )+ "&imei=" +tm.getDeviceId()
							 + "&code=" + sp.getString("code", ""));
					intent.putExtras(bundle);
					intent.setClass(context, NetActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
//			if(data.title1.equals("")){
//				UI.<ImageView>findViewById(layout, R.id.kecheng_Img1).setVisibility(View.GONE);
//				UI.<TextView>findViewById(layout, R.id.kecheng_Title1).setVisibility(View.GONE);
//				UI.<TextView>findViewById(layout, R.id.kecheng_Cat1).setVisibility(View.GONE);
//			}
			if(data.title2.equals("")){
//				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn2).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img2).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title2).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat2).setVisibility(View.GONE);
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn2).setVisibility(View.GONE);
			}
			if(data.title3.equals("")){
//				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn3).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img3).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title3).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat3).setVisibility(View.GONE);
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn3).setVisibility(View.GONE);
			}
			if(data.title4.equals("")){
//				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn4).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img4).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title4).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat4).setVisibility(View.GONE);
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn4).setVisibility(View.GONE);
			}
			if(data.title5.equals("")){
//				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn5).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img5).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title5).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat5).setVisibility(View.GONE);
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn5).setVisibility(View.GONE);
			}
			if(data.title6.equals("")){
//				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn6).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img6).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title6).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat6).setVisibility(View.GONE);
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn6).setVisibility(View.GONE);
			}
		}
		
		
		UI.<Button> findViewById(layout, R.id.kechenglist_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Activity) context).finish();
				Intent intent = new Intent();
				intent.setClass(context, MainMenuActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}
		});
		
		return layout;
	}
	
	public int getPage() {
		return page-1;
	}
	
	public void getMoreData() {
		keChengData.getMoreData(num);
	}

	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	} 
	
	/**
	 * 图片加载第一次显示监听器
	 * @author Administrator
	 *
	 */
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
