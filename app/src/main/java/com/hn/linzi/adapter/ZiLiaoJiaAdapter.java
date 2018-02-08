package com.hn.linzi.adapter;

import java.io.File;

import com.aphidmobile.utils.UI;
import com.hn.linzi.R;
import com.hn.linzi.activity.NetActivity;
import com.hn.linzi.data.ZiLiaoJiaData;
import com.hn.linzi.utils.FileUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ZiLiaoJiaAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    private Bitmap placeholderBitmap;
    
    public ZiLiaoJiaData ziLiaoJiaData;
    
    public ZiLiaoJiaAdapter (Context context) {
		super();
		inflater = LayoutInflater.from(context);
		this.context = context;
		placeholderBitmap =
		          BitmapFactory.decodeResource(context.getResources(), android.R.drawable.dark_header);
		ziLiaoJiaData = new ZiLiaoJiaData(context);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ziLiaoJiaData.IMG_DESCRIPTIONS.size();
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
	    	layout = inflater.inflate(R.layout.ziliaojia_item, null);
	    }

	    final ZiLiaoJiaData.Data data = ziLiaoJiaData.IMG_DESCRIPTIONS.get(position);
		
	    UI.<TextView>findViewById(layout, R.id.ziliaojia_title).setText(data.name);
	    UI.<TextView>findViewById(layout, R.id.ziliaojia_type).setText(data.type);
	    UI.<RelativeLayout>findViewById(layout, R.id.ziliaojiao_bg).setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				if (data.type.equals("Library")) {
					System.out.println(data.url);
					Bundle bundle = new Bundle();
					Intent intent = new Intent();
					bundle.putString("url", data.url);
					intent.putExtras(bundle);
					intent.setClass(context, NetActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(intent);
				}else if (data.type.equals("Culture")) {
					if (data.dlflag.equals("true")) {
						File file = new File(data.path);
						FileUtils.openFile(file, context);
					}else {
						Toast.makeText(context, "还没下载完成", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	    
	    return layout;
	}

}
