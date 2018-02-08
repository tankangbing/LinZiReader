package com.hn.linzi.adapter;

import com.hn.linzi.activity.ClassicBooksMainActivity;
import com.hn.linzi.activity.KechengActivity;
import com.hn.linzi.activity.LocalShuJiaActivity;
import com.hn.linzi.activity.MapActivity;
import com.hn.linzi.activity.MipcaActivityCapture;
import com.hn.linzi.activity.PublicClassMainActivity;
import com.hn.linzi.activity.XueYaJi;
import com.hn.linzi.activity.YaoYiYaoActivity;
import com.hn.linzi.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

public class MainMenuAdapter extends BaseAdapter {
	
	private Context context;
	private String[] btnName = new String[]{"书架","课程","摇一摇","扫一扫","资料夹","中外经典","公开课堂","特色资源"};
	private int[] imgRes = new int[]{R.drawable.mm_shujia,R.drawable.mm_kecheng,R.drawable.mm_yaoyiyao,
									 R.drawable.mm_saoyisao,R.drawable.mm_gongkai,R.drawable.mm_zhongwai,
									 R.drawable.mm_xueyaji,R.drawable.mm_zazhi};
	private SharedPreferences sp;
	private String[] need;
	
	public MainMenuAdapter(Context context){
		this.context = context;
		sp = this.context.getSharedPreferences("SP", this.context.MODE_PRIVATE);
		need = sp.getString("menuKey", "1-2-3-4-5-6-7-8").split("-");
		System.out.println("need.length=" + need.length);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return need.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_menu_item, null);
        }
		final int i = Integer.parseInt(need[position]);
		ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.mm_imgbtn);
		imageButton.setImageResource(imgRes[i-1]);
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				Uri uri;
				switch (i) {
				case 1:
					intent.setClass(context, LocalShuJiaActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
					break;
				case 2:
					intent.setClass(context, KechengActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
					break;
				case 3:
					intent.setClass(context, YaoYiYaoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
					break;
				case 4:
					intent.setClass(context, MipcaActivityCapture.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
					break;
				case 5:
					intent.setClass(context, PublicClassMainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
					break;
				case 6:
					intent.setClass(context, ClassicBooksMainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
					break;
				case 7:
					intent.setClass(context, XueYaJi.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
					break;
					case 8:
//						intent.setClass(context, ZuiXinTuShu.class);
						intent.setClass(context, MapActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(intent);
						break;
				}
			}
		});
		return convertView;
	}

}
