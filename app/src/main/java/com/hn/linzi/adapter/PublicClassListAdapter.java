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
import com.hn.linzi.activity.KechengActivity;
import com.hn.linzi.activity.PublicClassContentActivity;
import com.hn.linzi.activity.PublicClassListActivity;
import com.hn.linzi.data.PublicClassListData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
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

public class PublicClassListAdapter extends BaseAdapter {

	private FlipViewController controller;

	private Context context;

	private LayoutInflater inflater;

	private Bitmap placeholderBitmap;
	 
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	
	private PublicClassListData publicClassListData;
	
	private PublicClassListActivity activity;
	
	private TelephonyManager tm;
	
	private int num;
	
	private int page;

	public PublicClassListAdapter(FlipViewController controller, Context context, PublicClassListActivity publicClassListActivity, String url, String type, String word) {
		super();
		inflater = LayoutInflater.from(context);
		this.controller = controller;
		this.context = context;
		this.activity = publicClassListActivity;
		this.tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		publicClassListData = new PublicClassListData(publicClassListActivity, url, type, word, context);
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

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(publicClassListData.IMG_DESCRIPTIONS.size() == 0){
			return 1;
		}
		return publicClassListData.IMG_DESCRIPTIONS.size();
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
		if(publicClassListData.IMG_DESCRIPTIONS.size() == 0){
			layout = inflater.inflate(R.layout.kong_data, null);
			layout.findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
			UI.<TextView> findViewById(layout, R.id.daohanglan).setText("公开课堂");
			UI.<Button>findViewById(layout, R.id.claasicbook_back2).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					activity.finish();
				}
			});
		}else{
			layout = inflater.inflate(R.layout.kc_list, null);
			layout.findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
			UI.<TextView> findViewById(layout, R.id.daohanglan).setText("公开课堂");
			UI.<Button>findViewById(layout, R.id.kc_manage).setBackgroundResource(R.drawable.backtosjkc);
			UI.<Button>findViewById(layout, R.id.kc_manage).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(context, KechengActivity.class);
					context.startActivity(intent);
				}
			});
			final PublicClassListData.Data data = publicClassListData.IMG_DESCRIPTIONS
					.get(position);
			UI.<TextView>findViewById(layout, R.id.kecheng_Title1).setText(data.title1);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat1).setText(data.author1);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn1).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title1.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:88/m/lessonInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lesson_id=" + data.url1);
					intent.putExtras(bundle);
					intent.setClass(context, PublicClassContentActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title2).setText(data.title2);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat2).setText(data.author2);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn2).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title2.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:88/m/lessonInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lesson_id=" + data.url2);
					intent.putExtras(bundle);
					intent.setClass(context, PublicClassContentActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title3).setText(data.title3);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat3).setText(data.author3);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn3).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title3.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:88/m/lessonInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lesson_id=" + data.url3);
					intent.putExtras(bundle);
					intent.setClass(context, PublicClassContentActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title4).setText(data.title4);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat4).setText(data.author4);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn4).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title4.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:88/m/lessonInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lesson_id=" + data.url4);
					intent.putExtras(bundle);
					intent.setClass(context, PublicClassContentActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title5).setText(data.title5);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat5).setText(data.author5);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn5).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title5.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:88/m/lessonInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lesson_id=" + data.url5);
					intent.putExtras(bundle);
					intent.setClass(context, PublicClassContentActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.kecheng_Title6).setText(data.title6);
			UI.<TextView>findViewById(layout, R.id.kecheng_Cat6).setText(data.author6);
			UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn6).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!data.title6.equals("")){
						Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:88/m/lessonInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lesson_id=" + data.url6);
					intent.putExtras(bundle);
					intent.setClass(context, PublicClassContentActivity.class);
					context.startActivity(intent);
					}
					
				}
			});
			
			AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
			imageLoader.displayImage(data.imageFilePath1, UI.<ImageView>findViewById(layout, R.id.kecheng_Img1), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath2, UI.<ImageView>findViewById(layout, R.id.kecheng_Img2), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath3, UI.<ImageView>findViewById(layout, R.id.kecheng_Img3), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath4, UI.<ImageView>findViewById(layout, R.id.kecheng_Img4), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath5, UI.<ImageView>findViewById(layout, R.id.kecheng_Img5), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath6, UI.<ImageView>findViewById(layout, R.id.kecheng_Img6), options, aDisplayListener);
			
			if(((position-page+2 == 3 || position+2 == 5) && position > page)){
				page = position;
				num++;
				Message message = new Message();
		        message.what = PublicClassListActivity.GETDATA;
		        
		        Bundle b = new Bundle();
				b.putInt("position", position);
		        message.setData(b);
		        activity.handler.sendMessage(message); 
			}
			UI.<Button>findViewById(layout, R.id.kechenglist_back).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					activity.finish();
				}
			});
			
			if(data.title2.equals("")){
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn2).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img2).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title2).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat2).setVisibility(View.GONE);
			}
			if(data.title3.equals("")){
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn3).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img3).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title3).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat3).setVisibility(View.GONE);
			}
			if(data.title4.equals("")){
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn4).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img4).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title4).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat4).setVisibility(View.GONE);
			}
			if(data.title5.equals("")){
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn5).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img5).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title5).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat5).setVisibility(View.GONE);
			}
			if(data.title6.equals("")){
				UI.<LinearLayout>findViewById(layout, R.id.kecheng_Btn6).setBackgroundColor(Color.parseColor("#f7f6f6"));
				UI.<ImageView>findViewById(layout, R.id.kecheng_Img6).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Title6).setVisibility(View.GONE);
				UI.<TextView>findViewById(layout, R.id.kecheng_Cat6).setVisibility(View.GONE);
			}
		}
		
		return layout;
	}
	
	public int getPage() {
		return page-1;
	}
	
	public void getMoreData() {
		publicClassListData.getMoreData(num);
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
	
	private static class AnimateFirstDisplayListener extends
	SimpleImageLoadingListener {

static final List<String> displayedImages = Collections
		.synchronizedList(new LinkedList<String>());

@Override
public void onLoadingComplete(String imageUri, View view,
		Bitmap loadedImage) {
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
