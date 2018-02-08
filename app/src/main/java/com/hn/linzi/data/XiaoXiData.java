package com.hn.linzi.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.hn.linzi.activity.XiaoXiActivity;
import com.hn.linzi.utils.ParseJson;

public class XiaoXiData {
	
	public static final List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();
	
	private XiaoXiActivity xiaoXiActivity;
	private Context context;
	private SharedPreferences sp;
	private TelephonyManager tm;
	
	public XiaoXiData(XiaoXiActivity xiaoXiActivity){
		this.xiaoXiActivity = xiaoXiActivity;
		context = xiaoXiActivity.getApplicationContext();
		sp = context.getSharedPreferences("SP",
				context.MODE_PRIVATE);
		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		init();
	}
	
	public void init() {
		
		IMG_DESCRIPTIONS.clear();
		
		new Thread(){

			@Override
			public void run() {
				String contentURL = "http://data.iego.net:88/m/msgList.aspx?" + "&n=" + sp.getString("UserName", "") + "&d=" + sp.getString("KEY", "") + "&type=0&isRead=0&size=10&page=1";
						//n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&type=0&isRead=0&size=10&page=1";
				URL url;
				try {
					url = new URL(contentURL);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream(); 
						ParseJson parseJson = new ParseJson();
						parseJson.parseXiaoxiList(inStream, IMG_DESCRIPTIONS);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message message = new Message();
		        message.what = XiaoXiActivity.REFRESH;
		        xiaoXiActivity.handler.sendMessage(message);
				super.run();
			}
		}.start();

	}

	public static final class Data {

		public final String id;//消息标题
		public final String parentId;//图片路径
		public final String send;//发信息人
		public final String to;//发信息时间
		public final String type;//消息链接
		public final String isread;
		public final String title;
		public final String dt;
		
		public Data(String id, String parentId, String send, String to,
				String type, String isread, String title, String dt) {
			super();
			this.id = id;
			this.parentId = parentId;
			this.send = send;
			this.to = to;
			this.type = type;
			this.isread = isread;
			this.title = title;
			this.dt = dt;
		}
		
		
	}
}
