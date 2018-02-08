package com.hn.linzi.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.aphidmobile.utils.UI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.hn.linzi.data.ZhiChangListData;
import com.hn.linzi.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ZhiChangListAdapter extends BaseAdapter {

	private Context context;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private boolean typeflag;//true是全职，false是兼职
	
	private ZhiChangListData zhiChangListData;


	private int page;

	public ZhiChangListAdapter(Context context, ZhiChangListData zhiChangListData) {
		super();
		this.context = context;
		this.zhiChangListData = zhiChangListData;
		page = 1;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return zhiChangListData.List_Data.size();
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
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(
					R.layout.zhichang_list_item, null);
		}
		ZhiChangListData.Data data = zhiChangListData.List_Data
				.get(position);

		UI.<TextView>findViewById(convertView, R.id.zhichang_list_title).setText(data.title);
		UI.<TextView>findViewById(convertView, R.id.zhichang_list_companyname).setText("公司名称：" + data.name);
		UI.<TextView>findViewById(convertView, R.id.zhichang_list_pay).setText("￥" + data.pay);
		UI.<TextView>findViewById(convertView, R.id.zhichang_list_peoplenum).setText("招聘人数：" + data.num);
		UI.<TextView>findViewById(convertView, R.id.zhichang_list_time).setText("发布日期：" + data.time);
		if (typeflag) {
			UI.<TextView>findViewById(convertView, R.id.zhichang_list_paytime).setVisibility(View.GONE);
		}else {
			UI.<TextView>findViewById(convertView, R.id.zhichang_list_paytime).setVisibility(View.GONE);
		}
		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
		imageLoader.displayImage(data.cover,UI.<ImageView> findViewById(convertView, R.id.zhichang_list_img),
				options, aDisplayListener);
		

		return convertView;
	}

	public int getPage() {
		return page - 1;
	}

	public void getMoreData() {
		// newBooksListData.getMoreData(num);
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

	public boolean isTypeflag() {
		return typeflag;
	}

	public void setTypeflag(boolean typeflag) {
		this.typeflag = typeflag;
	}

	public static class AnimateFirstDisplayListener extends
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
