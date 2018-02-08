package com.hn.linzi.adapter;

import com.aphidmobile.utils.UI;
import com.hn.linzi.R;
import com.hn.linzi.activity.XiaoXiActivity;
import com.hn.linzi.activity.XiaoXiContentActivity;
import com.hn.linzi.data.XiaoXiData;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XiaoXiAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    private Bitmap placeholderBitmap;
    
    public XiaoXiData xiaoXiData;
    
    private SharedPreferences sp;
	private TelephonyManager tm;
    
    public XiaoXiAdapter(Context context, XiaoXiActivity xiaoXiActivity) {
		super();
		inflater = LayoutInflater.from(context);
		this.context = context;
		placeholderBitmap =
		          BitmapFactory.decodeResource(context.getResources(), android.R.drawable.dark_header);
		xiaoXiData = new XiaoXiData(xiaoXiActivity);
		sp = context.getSharedPreferences("SP",
				context.MODE_PRIVATE);
		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return xiaoXiData.IMG_DESCRIPTIONS.size();
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
		View layout = convertView;
		System.out.println(position);
	    if (convertView == null) {
	    	layout = inflater.inflate(R.layout.xiaoxi_item, null);
	    }
	    
	    final XiaoXiData.Data data = xiaoXiData.IMG_DESCRIPTIONS.get(position);
	    
//	    if(position == 0){
//	    	layout = inflater.inflate(R.layout.xiaoxi1, null);
//	    	UI.<TextView>findViewById(layout, R.id.xiaoxi1Title).setText(data.title1);
//		    UI.<TextView>findViewById(layout, R.id.xiaoxi1Sender).setText(data.sender1 + "    " + data.time1);
//		    UI.<TextView>findViewById(layout, R.id.xiaoxi1Text).setText(data.title1);
//	    }
	    System.out.println("data.isread==========" + data.isread);
	    
	    if (data.isread.equals("1")) {
			//未读
	    	if(data.type.equals("1")){
	    		//系统
	    		UI.<ImageView>findViewById(layout, R.id.xiaoxi_img).setImageResource(R.drawable.weidu_xi);
	    	}
	    	if(data.type.equals("2")){
	    		//教师
	    		UI.<ImageView>findViewById(layout, R.id.xiaoxi_img).setImageResource(R.drawable.weidu_jiao);
	    	}
	    	if(data.type.equals("3")){
	    		//学生
	    		UI.<ImageView>findViewById(layout, R.id.xiaoxi_img).setImageResource(R.drawable.weidu_xue);
	    	}
		}
	    if (data.isread.equals("2")) {
			//已读
	    	if(data.type.equals("1")){
	    		//系统
	    		UI.<ImageView>findViewById(layout, R.id.xiaoxi_img).setImageResource(R.drawable.yidu_xi);
	    	}
	    	if(data.type.equals("2")){
	    		//教师
	    		UI.<ImageView>findViewById(layout, R.id.xiaoxi_img).setImageResource(R.drawable.yidu_jiao);
	    	}
	    	if(data.type.equals("3")){
	    		//学生
	    		UI.<ImageView>findViewById(layout, R.id.xiaoxi_img).setImageResource(R.drawable.yidu_xue);
	    	}
		}
	    
	    UI.<TextView>findViewById(layout, R.id.xiaoxi_title).setText(data.title);
	    UI.<TextView>findViewById(layout, R.id.xiaoxi_dt).setText(data.dt);
	    UI.<TextView>findViewById(layout, R.id.xiaoxi_send).setText(data.send);
	    UI.<LinearLayout>findViewById(layout, R.id.xiaoxi_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				Intent intent = new Intent();
				bundle.putString("title", data.title);
				bundle.putString("dt", data.dt);
				bundle.putString("send", data.send);
				bundle.putString("id", data.id);
				intent.putExtras(bundle);
				intent.setClass(context, XiaoXiContentActivity.class);
				context.startActivity(intent);
			}
		});
	    
//	    UI.<Button>findViewById(layout, R.id.xiaoxi_delete).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
////				http://data.iego.net:88/m/msgDel.aspx?n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&id=172
//				String contentURL = "http://data.iego.net:88/m/msgContent.aspx?" + "&n=" + sp.getString("UserName", "") + "&d=" + sp.getString("KEY", "") + "&id=" + data.id;
//						//n=201010412001&d=5744f175c7489fd2f3ccee928d6d7cc8&id=172";
//				URL url;
//				try {
//					url = new URL(contentURL);
//					HttpURLConnection conn = (HttpURLConnection) url
//							.openConnection();
//					conn.setRequestMethod("GET");
//					conn.setReadTimeout(5000);	
//					if (conn.getResponseCode() == 200) {
//						InputStream inStream = conn.getInputStream(); 
//						
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
	    
	    return layout;
	}

}
