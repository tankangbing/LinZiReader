package com.hn.linzi.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.hn.linzi.data.HuoDongListData;
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

public class HuoDongListAdapter extends BaseAdapter {

	private Context context;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	private HuoDongListData huoDongListData;


	private int page;

	public HuoDongListAdapter(Context context, HuoDongListData huoDongListData) {
		super();
		this.context = context;
		this.huoDongListData = huoDongListData;
		page = 1;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return huoDongListData.List_Data.size();
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
		ViewHolder holder = null;
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(
					R.layout.huodong_list_item, null);
			holder = new ViewHolder();
			holder.author = (TextView) convertView.findViewById(R.id.huodong_list_author);
			holder.content = (TextView) convertView.findViewById(R.id.huodong_list_content);
			holder.credit = (TextView) convertView.findViewById(R.id.rotateTextView);
			holder.imageView = (ImageView) convertView.findViewById(R.id.huodong_list_img);
			holder.time = (TextView) convertView.findViewById(R.id.huodong_list_time);
			holder.title = (TextView) convertView.findViewById(R.id.huodong_list_title);
			holder.xuefenbg = (ImageView) convertView.findViewById(R.id.xuefenbg);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		HuoDongListData.Data data = huoDongListData.List_Data
				.get(position);

		holder.title.setText(data.title1);
		if(data.credit1.equals("0")){
			holder.xuefenbg.setVisibility(View.GONE);
			holder.credit.setText("");
		}else{
			holder.xuefenbg.setVisibility(View.VISIBLE);
			String credit = data.credit1;
			if(data.credit1.length() == 1){
				credit = data.credit1 + ".0";
			}
			if(data.credit1.length() == 2){
				credit = data.credit1 + " ";
			}
			credit = credit + "学分";
			holder.credit.setText(credit);
		}
		holder.time.setText(data.time1);
		holder.author.setText("组织单位：" + data.unit1);
		holder.content.setText(data.signupnum1);
		
		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
		imageLoader.displayImage(data.cover1,holder.imageView,
				options, aDisplayListener);
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView title;
		TextView credit;
		TextView time;
		TextView author;
		TextView content;
		ImageView imageView;
		ImageView xuefenbg;
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
