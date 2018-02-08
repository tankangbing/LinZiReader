package com.hn.linzi.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;

import com.hn.linzi.activity.PublicClassListActivity;
import com.hn.linzi.utils.ParseJson;

public class PublicClassListData {
	
	private PublicClassListActivity publicClassListActivity;
	private String contentURL;
	private String type;
	private String word;
	private Context context;
	
	public PublicClassListData(PublicClassListActivity publicClassListActivity, String url, String type, String word, Context context) {
		this.publicClassListActivity = publicClassListActivity;
		this.contentURL = url;
		this.type = type;
		this.word = word;
		this.context = context;
		init();
	}

	public List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();
	
	public void init() {
		new Thread(){

			@Override
			public void run() {
				
//				String contentURL = "http://data.iego.net:88/lesson/lessonListJson.aspx?imei=123456-78-901234-5&n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&code=88888888&size=18&page=1";
				URL url;
				try {
					System.out.println(contentURL);
					url = new URL(contentURL);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream();
						ParseJson parseJson = new ParseJson();
						parseJson.parsePublicClassList(inStream, IMG_DESCRIPTIONS); 
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Message message = new Message();
		        message.what = PublicClassListActivity.REFRESH;
		        publicClassListActivity.handler.sendMessage(message);
				super.run();
			}
			
		}.start();
	}
	
	public void getMoreData(final int i) {
		new Thread(){

			@Override
			public void run() {
				String moreData = "";
				SharedPreferences sp = context.getSharedPreferences("SP",
						context.MODE_PRIVATE);
				if(type.equals("sousuo")){
					try {
						moreData = "http://data.iego.net:88/m/lessonlistJson.aspx?n=" + sp.getString("UserName", "") +
								"&d=" +sp.getString("KEY","" ) + "&size=18&page=" + i + "&kwd=" + URLEncoder.encode(word, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
				}
				if(type.equals("quanbu")){
					moreData = "http://data.iego.net:88/m/lessonlistJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&size=18&page=" + i;
				}
				if(type.equals("fenlei")){
					try {
						moreData = "http://data.iego.net:88/m/lessonlistJson.aspx?n=" + sp.getString("UserName", "") +
								"&d=" +sp.getString("KEY","" ) + "&size=18&page=" + i + "&cat=" + URLEncoder.encode(word, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};
				}
//				String contentURL = "http://data.iego.net:88/lesson/lessonListJson.aspx?imei=123456-78-901234-5&n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&code=88888888&size=18&page=" + i;
				try {
					URL url = new URL(moreData);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream();
						ParseJson parseJson = new ParseJson();
						parseJson.parsePublicClassList(inStream, IMG_DESCRIPTIONS);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new Message();
		        message.what = PublicClassListActivity.ADAPTER;
		        publicClassListActivity.handler.sendMessage(message);
				super.run();
			}
			
		}.start();
	}

	public static final class Data {

		public final String title1;
		public final String imageFilePath1;
		public final String url1;
		public final String author1;
		public final String cat1;
		public final String title2;
		public final String imageFilePath2;
		public final String url2;
		public final String author2;
		public final String cat2;
		public final String title3;
		public final String imageFilePath3;
		public final String url3;
		public final String author3;
		public final String cat3;
		public final String title4;
		public final String imageFilePath4;
		public final String url4;
		public final String author4;
		public final String cat4;
		public final String title5;
		public final String imageFilePath5;
		public final String url5;
		public final String author5;
		public final String cat5;
		public final String title6;
		public final String imageFilePath6;
		public final String url6;
		public final String author6;
		public final String cat6;
		public Data(String title1, String imageFilePath1, String url1,
				String author1, String cat1, String title2,
				String imageFilePath2, String url2, String author2,
				String cat2, String title3, String imageFilePath3, String url3,
				String author3, String cat3, String title4,
				String imageFilePath4, String url4, String author4,
				String cat4, String title5, String imageFilePath5, String url5,
				String author5, String cat5, String title6,
				String imageFilePath6, String url6, String author6, String cat6) {
			super();
			this.title1 = title1;
			this.imageFilePath1 = imageFilePath1;
			this.url1 = url1;
			this.author1 = author1;
			this.cat1 = cat1;
			this.title2 = title2;
			this.imageFilePath2 = imageFilePath2;
			this.url2 = url2;
			this.author2 = author2;
			this.cat2 = cat2;
			this.title3 = title3;
			this.imageFilePath3 = imageFilePath3;
			this.url3 = url3;
			this.author3 = author3;
			this.cat3 = cat3;
			this.title4 = title4;
			this.imageFilePath4 = imageFilePath4;
			this.url4 = url4;
			this.author4 = author4;
			this.cat4 = cat4;
			this.title5 = title5;
			this.imageFilePath5 = imageFilePath5;
			this.url5 = url5;
			this.author5 = author5;
			this.cat5 = cat5;
			this.title6 = title6;
			this.imageFilePath6 = imageFilePath6;
			this.url6 = url6;
			this.author6 = author6;
			this.cat6 = cat6;
		}
 

	}
}
