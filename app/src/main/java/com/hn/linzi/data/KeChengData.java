package com.hn.linzi.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Message;

import com.hn.linzi.activity.KechengActivity;
import com.hn.linzi.utils.KeChengDBHelper;
import com.hn.linzi.utils.ParseJson;

public class KeChengData {
	
	private KechengActivity categoryListActivity;
	
	
	public KeChengData(KechengActivity categoryListActivity) {
		this.categoryListActivity = categoryListActivity;
		init();
	}

	public List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();
	
	public void init() {
		SharedPreferences sp = categoryListActivity.getSharedPreferences("SP",
				categoryListActivity.MODE_PRIVATE);
		KeChengDBHelper keChengDBHelper = new KeChengDBHelper(categoryListActivity.getApplicationContext());
		Cursor cursor = keChengDBHelper.select(new String[]{"kecheng_name", "kecheng_imgurl", "kecheng_url", "kecheng_cat"}, 
				"username=?", new String[]{sp.getString("UserName", "")});
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> imgurl = new ArrayList<String>();
		ArrayList<String> url = new ArrayList<String>();
		ArrayList<String> cat = new ArrayList<String>();
		
		int num = 0;
		while(cursor.moveToNext()){
			num++;
			name.add(cursor.getString(cursor.getColumnIndex("kecheng_name")));
			imgurl.add(cursor.getString(cursor.getColumnIndex("kecheng_imgurl")));
			url.add(cursor.getString(cursor.getColumnIndex("kecheng_url")));
			cat.add(cursor.getString(cursor.getColumnIndex("kecheng_cat")));
			
			if(cursor.isLast() && num != 6){
				for (int i = num+1; i <= 6; i++) {
					name.add("");
					imgurl.add("");
					url.add("");
					cat.add("");
				}
				IMG_DESCRIPTIONS.add(new Data(name.get(0), imgurl.get(0), cat.get(0), url.get(0),
						name.get(1), imgurl.get(1), cat.get(1), url.get(1),
						name.get(2), imgurl.get(2), cat.get(2), url.get(2),
						name.get(3), imgurl.get(3), cat.get(3), url.get(3),
						name.get(4), imgurl.get(4), cat.get(4), url.get(4),
						name.get(5), imgurl.get(5), cat.get(5), url.get(5)));
				num = 0;
				name.clear();
				imgurl.clear();
				cat.clear();
				url.clear();
			}
			if(num == 6){
				IMG_DESCRIPTIONS.add(new Data(name.get(0), imgurl.get(0), cat.get(0), url.get(0),
						name.get(1), imgurl.get(1), cat.get(1), url.get(1),
						name.get(2), imgurl.get(2), cat.get(2), url.get(2),
						name.get(3), imgurl.get(3), cat.get(3), url.get(3),
						name.get(4), imgurl.get(4), cat.get(4), url.get(4),
						name.get(5), imgurl.get(5), cat.get(5), url.get(5)));
				num = 0;
				name.clear();
				imgurl.clear();
				cat.clear();
				url.clear();
			}
		}
		
		
//		new Thread(){
//
//			@Override
//			public void run() {
//				
//				String contentURL = "http://data.iego.net:88/lesson/recent.aspx?imei=123456-78-901234-5&n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&code=88888888&size=6&page=1";
//				URL url;
//				try {
//					url = new URL(contentURL);
//					HttpURLConnection conn = (HttpURLConnection) url
//							.openConnection();
//					conn.setRequestMethod("GET");
//					conn.setReadTimeout(5000);
//					if (conn.getResponseCode() == 200) {
//						InputStream inStream = conn.getInputStream();
//						ParseJson parseJson = new ParseJson();
//						parseJson.parseKeChengList(inStream, IMG_DESCRIPTIONS);
////						CategoryListActivity activity = new CategoryListActivity();
////						activity.parseKeChengList(inStream, IMG_DESCRIPTIONS);
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				contentURL = "http://data.iego.net:88/lesson/lessonListJson.aspx?imei=123456-78-901234-5&n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&code=88888888&size=18&page=1";
//				try {
//					url = new URL(contentURL);
//					HttpURLConnection conn = (HttpURLConnection) url
//							.openConnection();
//					conn.setRequestMethod("GET");
//					conn.setReadTimeout(5000);
//					if (conn.getResponseCode() == 200) {
//						InputStream inStream = conn.getInputStream();
//						ParseJson parseJson = new ParseJson();
//						parseJson.parseKeChengList(inStream, IMG_DESCRIPTIONS);
////						CategoryListActivity activity = new CategoryListActivity();
////						activity.parseKeChengList(inStream, IMG_DESCRIPTIONS);
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				Message message = new Message();
//		        message.what = KechengActivity.REFRESH;
//		        categoryListActivity.handler.sendMessage(message);
//				super.run();
//			}
//			
//		}.start();
	}
	
	public void getMoreData(final int i) {
		new Thread(){

			@Override
			public void run() {
				
				String contentURL = "http://data.iego.net:88/lesson/lessonListJson.aspx?imei=123456-78-901234-5&n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&code=88888888&size=18&page=" + i;
				try {
					URL url = new URL(contentURL);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream();
						ParseJson parseJson = new ParseJson();
						parseJson.parseKeChengList(inStream, IMG_DESCRIPTIONS);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new Message();
		        message.what = KechengActivity.ADAPTER;
		        categoryListActivity.handler.sendMessage(message);
				super.run();
			}
			
		}.start();
	}

	public static final class Data {

		public final String title1;// 标题
		public final String imageFilePath1;// 封面路径
		public final String cat1;//分类
		public final String url1;//课程链接
		public final String title2;
		public final String imageFilePath2;
		public final String cat2;
		public final String url2;
		public final String title3;
		public final String imageFilePath3;
		public final String cat3;
		public final String url3;
		public final String title4;
		public final String imageFilePath4;
		public final String cat4;
		public final String url4;
		public final String title5;
		public final String imageFilePath5;
		public final String cat5;
		public final String url5;
		public final String title6;
		public final String imageFilePath6;
		public final String cat6;
		public final String url6;
		
		public Data(String title1, String imageFilePath1, String cat1,
				String url1, String title2, String imageFilePath2, String cat2,
				String url2, String title3, String imageFilePath3, String cat3,
				String url3, String title4, String imageFilePath4, String cat4,
				String url4, String title5, String imageFilePath5, String cat5,
				String url5, String title6, String imageFilePath6, String cat6,
				String url6) {
			super();
			this.title1 = title1;
			this.imageFilePath1 = imageFilePath1;
			this.cat1 = cat1;
			this.url1 = url1;
			this.title2 = title2;
			this.imageFilePath2 = imageFilePath2;
			this.cat2 = cat2;
			this.url2 = url2;
			this.title3 = title3;
			this.imageFilePath3 = imageFilePath3;
			this.cat3 = cat3;
			this.url3 = url3;
			this.title4 = title4;
			this.imageFilePath4 = imageFilePath4;
			this.cat4 = cat4;
			this.url4 = url4;
			this.title5 = title5;
			this.imageFilePath5 = imageFilePath5;
			this.cat5 = cat5;
			this.url5 = url5;
			this.title6 = title6;
			this.imageFilePath6 = imageFilePath6;
			this.cat6 = cat6;
			this.url6 = url6;
		}
	}
}
