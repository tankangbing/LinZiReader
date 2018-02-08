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
import com.hn.linzi.activity.ClassicBooksContentActivity;
import com.hn.linzi.activity.ClassicBooksListActivity;
import com.hn.linzi.data.ClassicBookListData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class ClassicBookListAdapter extends BaseAdapter {

	private FlipViewController controller;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;		// DisplayImageOptions是用于设置图片显示的类

	private Context context;

	private LayoutInflater inflater;

	private Bitmap placeholderBitmap;
	 
	private ClassicBookListData classicBookListData;
	
	private ClassicBooksListActivity activity;
	
	private TelephonyManager tm;
	
	private int num;
	
	private int page;

	public ClassicBookListAdapter(FlipViewController controller, Context context, ClassicBooksListActivity activity, String url, String type, String word) {
		super();
		inflater = LayoutInflater.from(context);
		this.controller = controller;
		this.context = context;
		this.activity = activity;
		this.tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		classicBookListData = new ClassicBookListData(activity, url, type, word, context);
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
		if (classicBookListData.IMG_DESCRIPTIONS.size() == 0) {
			return 1;
		}
		return classicBookListData.IMG_DESCRIPTIONS.size();
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
		System.out.println("position=          " + position);
		if(classicBookListData.IMG_DESCRIPTIONS.size() == 0){
			layout = inflater.inflate(R.layout.kong_data, null);
			layout.findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
			UI.<TextView>findViewById(layout, R.id.daohanglan).setText("中外经典");
		}else{
			layout = inflater.inflate(R.layout.classicbooks_list, null);
			layout.findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
			UI.<TextView>findViewById(layout, R.id.daohanglan).setText("中外经典");
			final ClassicBookListData.Data data = classicBookListData.IMG_DESCRIPTIONS
					.get(position);
			
			UI.<TextView>findViewById(layout, R.id.cblist_bookauthor1).setText(data.author1);
			UI.<TextView>findViewById(layout, R.id.cblist_bookauthor2).setText(data.author2);
			UI.<TextView>findViewById(layout, R.id.cblist_bookauthor3).setText(data.author3);
			UI.<TextView>findViewById(layout, R.id.cblist_bookauthor4).setText(data.author4);
			UI.<TextView>findViewById(layout, R.id.cblist_bookauthor5).setText(data.author5);
			UI.<TextView>findViewById(layout, R.id.cblist_bookauthor6).setText(data.author6);
			
			AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
			imageLoader.displayImage(data.imageFilePath1, UI.<ImageView>findViewById(layout, R.id.cblist_bookimg1), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath2, UI.<ImageView>findViewById(layout, R.id.cblist_bookimg2), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath3, UI.<ImageView>findViewById(layout, R.id.cblist_bookimg3), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath4, UI.<ImageView>findViewById(layout, R.id.cblist_bookimg4), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath5, UI.<ImageView>findViewById(layout, R.id.cblist_bookimg5), options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath6, UI.<ImageView>findViewById(layout, R.id.cblist_bookimg6), options, aDisplayListener);
			
			UI.<TextView> findViewById(layout, R.id.cblist_booktitle1).setText(
					data.title1);
			UI.<LinearLayout> findViewById(layout, R.id.cblist_btn1)
					.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url1+ "&imei="
									+ tm.getDeviceId());
					intent.putExtras(bundle);
					System.out.println("http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url1+ "&imei="
									+ tm.getDeviceId());
					intent.setClass(context, ClassicBooksContentActivity.class);
					context.startActivity(intent);
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.cblist_booktitle2).setText(data.title2);
			UI.<LinearLayout>findViewById(layout, R.id.cblist_btn2).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url2+ "&imei="
									+ tm.getDeviceId());
					intent.putExtras(bundle);
					System.out.println("http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url2+ "&imei="
									+ tm.getDeviceId());
					intent.setClass(context, ClassicBooksContentActivity.class);
					context.startActivity(intent);
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.cblist_booktitle3).setText(data.title3);
			UI.<LinearLayout>findViewById(layout, R.id.cblist_btn3).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url3+ "&imei="
									+ tm.getDeviceId());
					intent.putExtras(bundle);
					System.out.println("http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url3+ "&imei="
									+ tm.getDeviceId());
					intent.setClass(context, ClassicBooksContentActivity.class);
					context.startActivity(intent);
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.cblist_booktitle4).setText(data.title4);
			UI.<LinearLayout>findViewById(layout, R.id.cblist_btn4).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url4+ "&imei="
									+ tm.getDeviceId());
					intent.putExtras(bundle);
					System.out.println("http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url4+ "&imei="
									+ tm.getDeviceId());
					intent.setClass(context, ClassicBooksContentActivity.class);
					context.startActivity(intent);
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.cblist_booktitle5).setText(data.title5);
			UI.<LinearLayout>findViewById(layout, R.id.cblist_btn5).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url5+ "&imei="
									+ tm.getDeviceId());
					intent.putExtras(bundle);
					System.out.println("http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url5+ "&imei="
									+ tm.getDeviceId());
					intent.setClass(context, ClassicBooksContentActivity.class);
					context.startActivity(intent);
				}
			});
			
			UI.<TextView>findViewById(layout, R.id.cblist_booktitle6).setText(data.title6);
			UI.<LinearLayout>findViewById(layout, R.id.cblist_btn6).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url6+ "&imei="
									+ tm.getDeviceId());
					intent.putExtras(bundle);
					System.out.println("http://data.iego.net:85/m/bookInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lib=classic&id=" + data.url6+ "&imei="
									+ tm.getDeviceId());
					intent.setClass(context, ClassicBooksContentActivity.class);
					context.startActivity(intent);
				}
			});
			
			if(((position-page+2 == 3 || position+2 == 5) && position > page)){
				page = position;
				num++;
				Message message = new Message();
		        message.what = ClassicBooksListActivity.GETDATA;
		        
		        Bundle b = new Bundle();
				b.putInt("position", position);
		        message.setData(b);
		        activity.handler.sendMessage(message); 
			}
		}
		UI.<Button> findViewById(layout, R.id.claasicbook_back2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				activity.finish();
			}
		});
		
		return layout;
	}
	
	public int getPage() {
		return page-1;
	}
	
	public void getMoreData() {
		classicBookListData.getMoreData(num);
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
