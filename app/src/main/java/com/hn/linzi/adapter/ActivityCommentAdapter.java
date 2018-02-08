package com.hn.linzi.adapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aphidmobile.utils.UI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.hn.linzi.activity.HuoDongDetailsActivity;
import com.hn.linzi.activity.ImagePagerActivity;
import com.hn.linzi.data.ActivityCommentData;
import com.hn.linzi.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityCommentAdapter extends BaseAdapter {

	private Context context;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private SharedPreferences sp;
	
	private final static int PIC_BROWER = 11;

	private ActivityCommentData commentData;
	private HuoDongDetailsActivity activity;

	private int page;
	private String activityID;

	public ActivityCommentAdapter(Context context,
			ActivityCommentData commentData, HuoDongDetailsActivity activity, String activityID) {
		super();
		this.context = context;
		this.commentData = commentData;
		this.activityID = activityID;
		this.activity = activity;
		sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE);
		page = 1;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentData.Comment_Data.size();
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.comment_item, null);
		}
		ActivityCommentData.Data data = commentData.Comment_Data.get(position);

		UI.<TextView> findViewById(convertView, R.id.comment_author).setText(
				data.name);
		UI.<TextView> findViewById(convertView, R.id.comment_content).setText(
				data.content);
		UI.<TextView> findViewById(convertView, R.id.comment_time).setText(
				data.created_at);
		if(activity.getCommentState().equals("new")){
			UI.<TextView>findViewById(convertView, R.id.comment_floor).setText(data.total - position + "楼");
		}else{
			UI.<TextView>findViewById(convertView, R.id.comment_floor).setText(position + 1 + "楼");
		}
		final String commentID = data.id;
		if (sp.getString("UserName", "").equals(data.account)) {
			UI.<ImageButton>findViewById(convertView, R.id.comment_delete).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Dialog dialog = new AlertDialog.Builder(activity)
		            .setTitle("删除评论")
		            .setMessage("确认删除？")
		            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	String deleteCommentUrl = context.getString(R.string.url_huodong_deletecomment)
		                			+ "&n=" + sp.getString("UserName", "")
		        					+ "&d=" + sp.getString("KEY", "")
		        					+ "&reviewid=" + commentID;
							RequestQueue queue = Volley
									.newRequestQueue(context);
							StringRequest request = new StringRequest(Request.Method.POST,
									deleteCommentUrl, new Listener<String>() {
										@Override
										public void onResponse(String response) {
											// TODO Auto-generated method stub
											System.out.println(response);
											try {
												if(response.lastIndexOf("成功") > -1){
													activity.handler.sendEmptyMessage(HuoDongDetailsActivity.OnDeleteComment);
													Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
												}else{
													Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
												}
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}

									}, new ErrorListener() {

										@Override
										public void onErrorResponse(VolleyError error) {
											// TODO Auto-generated method stub

										}
									});
							queue.add(request);
		                }
		            })//设置确定按钮
		            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                }
		            })//设置取消按钮
		            .create();
					dialog.show();
					
					
				}
			});
		}else{
			UI.<ImageButton>findViewById(convertView, R.id.comment_delete).setVisibility(View.GONE);
		}
		
		String[] images = new String[]{};
		if (!data.image1.equals("")) {
			images = new String[] {data.image1};
			if (!data.image2.equals("")) {
				images = new String[] {data.image1,data.image2};
				if (!data.image3.equals("")) {
					images = new String[] {data.image1,data.image2,data.image3};
					if (!data.image4.equals("")) {
						images = new String[] {data.image1,data.image2,data.image3,data.image4};
						if (!data.image5.equals("")) {
							images = new String[] {data.image1,data.image2,data.image3,data.image4,data.image5};
						}
					}
				}
			}
		}
		
		if (images.length > 0) {
			final String[] urls = images;
			UI.<GridView>findViewById(convertView, R.id.comment_gridView).setVisibility(View.VISIBLE);
			UI.<GridView>findViewById(convertView, R.id.comment_gridView).setAdapter(new MyGridAdapter(urls, context));
			UI.<GridView>findViewById(convertView, R.id.comment_gridView).setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					imageBrower(arg2,urls,"LOOK");
				}
				
			});
		}else{
			UI.<GridView>findViewById(convertView, R.id.comment_gridView).setVisibility(View.GONE);
		}

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
	
	private void imageBrower(int position, String[] urls, String type) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra(ImagePagerActivity.TYPE, type);
		activity.startActivityForResult(intent, HuoDongDetailsActivity.PIC_BROWER);
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
