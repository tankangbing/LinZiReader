package com.hn.linzi.adapter;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.utils.UI;
import com.hn.linzi.R;
import com.hn.linzi.data.ShuJiaData;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class ShuJiaAdapter extends BaseAdapter {

	private FlipViewController controller;

    private Context context;

    private LayoutInflater inflater;

    private Bitmap placeholderBitmap;
    
    public ShuJiaAdapter(FlipViewController controller, Context context) {
		super();
		inflater = LayoutInflater.from(context);
		this.controller = controller;
		this.context = context;
		placeholderBitmap =
		          BitmapFactory.decodeResource(context.getResources(), android.R.drawable.dark_header);
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ShuJiaData.IMG_DESCRIPTIONS.size();
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
	    	layout = inflater.inflate(R.layout.shujia, null);
	    }

	    final ShuJiaData.Data data = ShuJiaData.IMG_DESCRIPTIONS.get(position);
		
	    UI.<TextView>findViewById(layout, R.id.bookTitle1).setText(data.title1);
	    UI.<TextView>findViewById(layout, R.id.bookZuozhe1).setText(data.author1);
	    UI.<TextView>findViewById(layout, R.id.bookJianjie1).setText(data.introduction1);
	    UI.<ImageButton>findViewById(layout, R.id.bookImg1).setBackgroundColor(Color.YELLOW);
	    UI.<TextView>findViewById(layout, R.id.bookTitle2).setText(data.title2);
	    UI.<TextView>findViewById(layout, R.id.bookZuozhe2).setText(data.author2);
	    UI.<TextView>findViewById(layout, R.id.bookJianjie2).setText(data.introduction2);
	    UI.<ImageButton>findViewById(layout, R.id.bookImg2).setBackgroundColor(Color.YELLOW);
	    UI.<TextView>findViewById(layout, R.id.bookTitle3).setText(data.title3);
	    UI.<TextView>findViewById(layout, R.id.bookZuozhe3).setText(data.author3);
	    UI.<TextView>findViewById(layout, R.id.bookJianjie3).setText(data.introduction3);
	    UI.<ImageButton>findViewById(layout, R.id.bookImg3).setBackgroundColor(Color.YELLOW);

	    return layout;
	}

}
