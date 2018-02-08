package com.hn.linzi.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.utils.UI;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ta.util.netstate.TANetWorkUtil;
import com.hn.epub.Turnmain3;
import com.hn.linzi.R;
import com.hn.linzi.activity.DownloadManageActivity;
import com.hn.linzi.activity.LocalShuJiaActivity;
import com.hn.linzi.activity.MainMenuActivity;
import com.hn.linzi.data.LocalShuJiaData;
import com.hn.linzi.utils.BookDBHelper;
import com.hn.linzi.utils.LastReadDBHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LocalShuJiaAdapter extends BaseAdapter {

	private FlipViewController controller;

	private Context context;

	private LayoutInflater inflater;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	private Bitmap placeholderBitmap;

	private BookDBHelper bookDBHelper;
	private LastReadDBHelper lastReadDBHelper;

	private LocalShuJiaData localShuJiaData;
	private SharedPreferences sp;
	private Cursor cursor;
//	private static String openPath = "FZYDDL/localSHUJIA/";

	public LocalShuJiaAdapter(FlipViewController controller, Context context) {
		super();
		inflater = LayoutInflater.from(context);
		this.controller = controller;
		this.context = context;
		placeholderBitmap = BitmapFactory.decodeResource(
				context.getResources(), android.R.drawable.dark_header);
		localShuJiaData = new LocalShuJiaData(context);
		sp = context.getSharedPreferences("SP", context.MODE_PRIVATE);
		bookDBHelper = new BookDBHelper(context);
		lastReadDBHelper = new LastReadDBHelper(context);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
//		openPath = openPath + sp.getString("UserName", "") + "/";
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (localShuJiaData.IMG_DESCRIPTIONS.size() == 0) {
			return 1;
		}
		return localShuJiaData.IMG_DESCRIPTIONS.size();
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

		View layout = convertView;
		if (convertView == null) {
			layout = inflater.inflate(R.layout.localshujia, null);
		}

		if (localShuJiaData.IMG_DESCRIPTIONS.size() == 0) {
			layout = inflater.inflate(R.layout.kong_shujia, null);
			layout.findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
		} else {
			layout.findViewById(R.id.activity_bg).setBackgroundResource(sp.getInt("BGRes", R.drawable.mm_bg0));
			UI.<TextView>findViewById(layout, R.id.daohanglan).setText("个人书架");
			if (position == 0) {
				UI.<TextView> findViewById(layout, R.id.localshujia_bigtitle1)
						.setText("最近阅读");
				UI.<TextView> findViewById(layout, R.id.localshujia_bigtitle2)
						.setText("本地列表");
			} else {
				UI.<TextView> findViewById(layout, R.id.localshujia_bigtitle1)
						.setText("本地列表");
				UI.<TextView> findViewById(layout, R.id.localshujia_bigtitle2)
						.setVisibility(View.GONE);
			}

			final LocalShuJiaData.Data data = localShuJiaData.IMG_DESCRIPTIONS
					.get(position);
			UI.<TextView> findViewById(layout, R.id.localshujia_booktitle1)
					.setText(data.title1);
			UI.<TextView> findViewById(layout, R.id.localshujia_booktitle2)
					.setText(data.title2);
			UI.<TextView> findViewById(layout, R.id.localshujia_booktitle3)
					.setText(data.title3);
			UI.<TextView> findViewById(layout, R.id.localshujia_booktitle4)
					.setText(data.title4);
			UI.<TextView> findViewById(layout, R.id.localshujia_booktitle5)
					.setText(data.title5);
			UI.<TextView> findViewById(layout, R.id.localshujia_booktitle6)
					.setText(data.title6);

			UI.<LinearLayout> findViewById(layout, R.id.localshujia_btn1)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (data.title1.equals("")) {

							} else {
								cursor = bookDBHelper.select(
										null,
										"book_name=? and username=?",
										new String[] { data.title1,
												sp.getString("UserName", "") });
								cursor.moveToNext();
								int i = cursor.getColumnIndex("book_dlflag");
								if (cursor.getString(i).equals("true")) {
									if (cursor.getString(12).equals("pdf")) {
										Intent intent = getPdfFileIntent(data.bookFilePath1);
										context.startActivity(intent);
									} else if (cursor.getString(12).equals(
											"epub")) {
										if(TANetWorkUtil.isNetworkAvailable(context)){
											System.out.println("有网络");
											Intent intent = new Intent();
											intent.setClass(context,
													Turnmain3.class);
											intent.putExtra("bookurl", cursor
													.getString(8));
											context.startActivity(intent);
										}else {
											System.out.println("没网络");
											Toast.makeText(context, "无法进行网络认证，请检查网络后重试", Toast.LENGTH_SHORT).show();
										}
									}
									lastReadDBHelper.update(1, data.title1,
											data.imageFilePath1,
											data.bookFilePath1,
											sp.getString("UserName", ""));
								} else {
									Toast.makeText(context, "该图书未下载完成",
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
			UI.<LinearLayout> findViewById(layout, R.id.localshujia_btn2)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (data.title2.equals("")) {

							} else {
								cursor = bookDBHelper.select(
										null,
										"book_name=? and username=?",
										new String[] { data.title2,
												sp.getString("UserName", "") });
								cursor.moveToNext();
								int i = cursor.getColumnIndex("book_dlflag");
								if (cursor.getString(i).equals("true")) {
									if (cursor.getString(12).equals("pdf")) {
										Intent intent = getPdfFileIntent(data.bookFilePath2);
										context.startActivity(intent);
									} else if (cursor.getString(12).equals(
											"epub")) {

										if(TANetWorkUtil.isNetworkAvailable(context)){
											System.out.println("有网络");
											Intent intent = new Intent();
											intent.setClass(context,
													Turnmain3.class);
											intent.putExtra("bookurl", cursor
													.getString(8));
											context.startActivity(intent);
										}else {
											System.out.println("没网络");
											Toast.makeText(context, "无法进行网络认证，请检查网络后重试", Toast.LENGTH_SHORT).show();
										}
										
									}
									lastReadDBHelper.update(1, data.title2,
											data.imageFilePath2,
											data.bookFilePath2,
											sp.getString("UserName", ""));
								} else {
									Toast.makeText(context, "该图书未下载完成",
											Toast.LENGTH_SHORT).show();
								}
							}

						}
					});
			UI.<LinearLayout> findViewById(layout, R.id.localshujia_btn3)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (data.title3.equals("")) {

							} else {
								cursor = bookDBHelper.select(
										null,
										"book_name=? and username=?",
										new String[] { data.title3,
												sp.getString("UserName", "") });
								cursor.moveToNext();
								int i = cursor.getColumnIndex("book_dlflag");
								if (cursor.getString(i).equals("true")) {
									if (cursor.getString(12).equals("pdf")) {
										Intent intent = getPdfFileIntent(data.bookFilePath3);
										context.startActivity(intent);
									} else if (cursor.getString(12).equals(
											"epub")) {
										String bookname = cursor
												.getString(8)
												.substring(
														cursor.getString(8)
																.indexOf(
																		"localSHUJIA/") + 12,
														cursor.getString(8)
																.length());
										if(TANetWorkUtil.isNetworkAvailable(context)){
											System.out.println("有网络");
											Intent intent = new Intent();
											intent.setClass(context,
													Turnmain3.class);
											intent.putExtra("bookurl", cursor
													.getString(8));
											context.startActivity(intent);
										}else {
											System.out.println("没网络");
											Toast.makeText(context, "无法进行网络认证，请检查网络后重试", Toast.LENGTH_SHORT).show();
										}
									}
									lastReadDBHelper.update(1, data.title3,
											data.imageFilePath3,
											data.bookFilePath3,
											sp.getString("UserName", ""));
								} else {
									Toast.makeText(context, "该图书未下载完成",
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
			UI.<LinearLayout> findViewById(layout, R.id.localshujia_btn4)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (data.title4.equals("")) {

							} else {

								cursor = bookDBHelper.select(
										null,
										"book_name=? and username=?",
										new String[] { data.title4,
												sp.getString("UserName", "") });
								cursor.moveToNext();
								int i = cursor.getColumnIndex("book_dlflag");
								if (cursor.getString(i).equals("true")) {
									if (cursor.getString(12).equals("pdf")) {
										Intent intent = getPdfFileIntent(data.bookFilePath4);
										context.startActivity(intent);
									} else if (cursor.getString(12).equals(
											"epub")) {
										if(TANetWorkUtil.isNetworkAvailable(context)){
											System.out.println("有网络");
											Intent intent = new Intent();
											intent.setClass(context,
													Turnmain3.class);
											intent.putExtra("bookurl", cursor
													.getString(8));
											context.startActivity(intent);
										}else {
											System.out.println("没网络");
											Toast.makeText(context, "无法进行网络认证，请检查网络后重试", Toast.LENGTH_SHORT).show();
										}
									}
									lastReadDBHelper.update(1, data.title4,
											data.imageFilePath4,
											data.bookFilePath4,
											sp.getString("UserName", ""));
								} else {
									Toast.makeText(context, "该图书未下载完成",
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
			UI.<LinearLayout> findViewById(layout, R.id.localshujia_btn5)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (data.title5.equals("")) {

							} else {
								cursor = bookDBHelper.select(
										null,
										"book_name=? and username=?",
										new String[] { data.title5,
												sp.getString("UserName", "") });
								cursor.moveToNext();
								int i = cursor.getColumnIndex("book_dlflag");
								if (cursor.getString(i).equals("true")) {
									if (cursor.getString(12).equals("pdf")) {
										Intent intent = getPdfFileIntent(data.bookFilePath5);
										context.startActivity(intent);
									} else if (cursor.getString(12).equals(
											"epub")) {
										if(TANetWorkUtil.isNetworkAvailable(context)){
											System.out.println("有网络");
											Intent intent = new Intent();
											intent.setClass(context,
													Turnmain3.class);
											intent.putExtra("bookurl", cursor
													.getString(8));
											context.startActivity(intent);
										}else {
											System.out.println("没网络");
											Toast.makeText(context, "无法进行网络认证，请检查网络后重试", Toast.LENGTH_SHORT).show();
										}
									}
									lastReadDBHelper.update(1, data.title5,
											data.imageFilePath5,
											data.bookFilePath5,
											sp.getString("UserName", ""));
								} else {
									Toast.makeText(context, "该图书未下载完成",
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
			UI.<LinearLayout> findViewById(layout, R.id.localshujia_btn6)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (data.title6.equals("")) {

							} else {
								cursor = bookDBHelper.select(
										null,
										"book_name=? and username=?",
										new String[] { data.title6,
												sp.getString("UserName", "") });
								cursor.moveToNext();
								int i = cursor.getColumnIndex("book_dlflag");
								if (cursor.getString(i).equals("true")) {
									if (cursor.getString(12).equals("pdf")) {
										Intent intent = getPdfFileIntent(data.bookFilePath6);
										context.startActivity(intent);
									} else if (cursor.getString(12).equals(
											"epub")) {
										if(TANetWorkUtil.isNetworkAvailable(context)){
											System.out.println("有网络");
											Intent intent = new Intent();
											intent.setClass(context,
													Turnmain3.class);
											intent.putExtra("bookurl", cursor
													.getString(8));
											context.startActivity(intent);
										}else {
											System.out.println("没网络");
											Toast.makeText(context, "无法进行网络认证，请检查网络后重试", Toast.LENGTH_SHORT).show();
										}
									}
									lastReadDBHelper.update(1, data.title6,
											data.imageFilePath6,
											data.bookFilePath6,
											sp.getString("UserName", ""));
								} else {
									Toast.makeText(context, "该图书未下载完成",
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
			UI.<Button> findViewById(layout, R.id.localshujia_manage)
			.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(context, DownloadManageActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle bundle = new Bundle();
					bundle.putString("shujia", "shujia");
					intent.putExtras(bundle);
					context.startActivity(intent);
					((LocalShuJiaActivity) context).finish();
				}
			});
//			System.out.println("data.title1====" + data.title1);
//			System.out.println("data.bookFilePath1====" + data.bookFilePath1);
//			System.out.println("data.imageFilePath1====" + data.imageFilePath1);

			AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
			imageLoader.displayImage(data.imageFilePath1,
					UI.<ImageView> findViewById(layout, R.id.localshujia_bookimg1),
					options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath2,
					UI.<ImageView> findViewById(layout, R.id.localshujia_bookimg2),
					options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath3,
					UI.<ImageView> findViewById(layout, R.id.localshujia_bookimg3),
					options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath4,
					UI.<ImageView> findViewById(layout, R.id.localshujia_bookimg4),
					options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath5,
					UI.<ImageView> findViewById(layout, R.id.localshujia_bookimg5),
					options, aDisplayListener);
			imageLoader.displayImage(data.imageFilePath6,
					UI.<ImageView> findViewById(layout, R.id.localshujia_bookimg6),
					options, aDisplayListener);
			
		}

		UI.<Button> findViewById(layout, R.id.localshujia_back)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						((Activity) context).finish();
						Intent intent = new Intent();
						intent.setClass(context, MainMenuActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(intent);
					}
				});

		return layout;
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

	/**
	 * Get PDF file Intent
	 */
	public Intent getPdfFileIntent(String path) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		i.setDataAndType(uri, "application/pdf");
		return i;
	}

	private static class AnimateFirstDisplayListener extends
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
