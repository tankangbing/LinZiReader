package com.hn.linzi.adapter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.aphidmobile.utils.UI;
import com.ta.common.TAStringUtils;
import com.ta.util.TALogger;
import com.ta.util.download.DownLoadCallback;
import com.ta.util.download.DownloadManager;
import com.hn.linzi.R;
import com.hn.linzi.activity.NetActivity;
import com.hn.linzi.utils.FileUtils;
import com.hn.linzi.utils.MyTools;
import com.hn.linzi.utils.ZiLiaoJiaDBHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ZiLiaoJiaDownloadAdapter extends BaseAdapter
{

	private Context mContext;
	private ArrayList<HashMap<Integer, String>> dataList;
	private DownloadManager downloadManager;
	private ListView downloadList;
	private final ZiLiaoJiaDBHelper ziLiaoJiaDBHelper;
	private boolean barFlag;
	private SharedPreferences sp;

	public ZiLiaoJiaDownloadAdapter(Context context, ListView listView)
	{
		mContext = context;
		this.downloadList = listView;
		sp = mContext.getSharedPreferences("SP",
				mContext.MODE_PRIVATE);
		dataList = new ArrayList<HashMap<Integer, String>>();
		ziLiaoJiaDBHelper = new ZiLiaoJiaDBHelper(context);
		String path = context.getExternalFilesDir("BOOKS").getPath() + "/";//不能换
		System.out.println("downloadManager path=" + path);
		downloadManager = DownloadManager.getDownloadManager(path);
		downloadManager.setDownLoadCallback(new DownLoadCallback()
		{
			@Override
			public void onSuccess(String url)
			{
				// TODO Auto-generated method stub
				updataBook(url);
				removeItem(url);
			}

			@Override
			public void onLoading(String url, long totalSize, long currentSize,
					long speed)
			{
				Cursor cursor = ziLiaoJiaDBHelper.select(new String[]{"item_name"}, "item_url=? and username=?", new String[]{url, sp.getString("UserName", "")});
				cursor.moveToNext();
				String name = cursor.getString(cursor.getColumnIndex("item_name"));
				cursor.close();
				// TODO Auto-generated method stub
				super.onLoading(url, totalSize, currentSize, speed);
				long downloadPercent = currentSize * 100 / totalSize;
				View taskListItem = downloadList.findViewWithTag(url);
				ViewHolder viewHolder = new ViewHolder(taskListItem, name);
				viewHolder.setData(url, speed + "kbs" + "|"
						+ totalSize + "|" + currentSize,
						downloadPercent + "");
				TALogger.d(ZiLiaoJiaDownloadAdapter.this, "speed" + speed + "kbps"
						+ "downloadPercent" + downloadPercent);
			}
		});
	}
	
	public void updataBook(String url){
		String SDCard = "";
		if(MyTools.getExtSDCardPaths().size() > 0){
			SDCard = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/";
		}else {
			SDCard = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/";
		}
//		File DL = mContext.getExternalFilesDir("BOOKS");
//		String SDCard = DL.getPath();
//		String SDCard = Environment.getExternalStorageDirectory().getAbsolutePath();
		String path = SDCard + TAStringUtils.getFileNameFromUrl(url);
		final Cursor cursor = ziLiaoJiaDBHelper.select(null, "item_url=? and username=?",new String[]{url, sp.getString("UserName", "")});
		cursor.moveToNext();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String nowtime = df.format(curDate);
		
		File file = new File(path);
		System.out.println(file.getName());
		if(path.indexOf(".") >= 0){
			System.out.println("path");
			path = SDCard + sp.getString("UserName", "")+ "_" + nowtime + "_" + cursor.getString(cursor.getColumnIndex("item_name"));
			System.out.println(path);
			String s[] = url.split("\\.");
			path = path + "." + s[s.length-1];
			System.out.println(path);
		}
		file.renameTo(new File(path));
		file = new File(path);
		System.out.println(file.getName());
		
		System.out.println(path);
		SharedPreferences sp = mContext.getSharedPreferences("SP",
				mContext.MODE_PRIVATE);
		ziLiaoJiaDBHelper.update(cursor.getInt(cursor.getColumnIndex("item_id")), 
				cursor.getString(cursor.getColumnIndex("item_name")),cursor.getString(cursor.getColumnIndex("item_type")), url, path,
				"true", "false", sp.getString("UserName", ""));
		Toast.makeText(mContext, cursor.getString(1) + "下载完成", Toast.LENGTH_SHORT).show();
		cursor.close();
	}

	@Override
	public int getCount()
	{
		return dataList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public void addItem(String url)
	{
		addItem(url, false);
	}

	public void addItem(String url, boolean isPaused)
	{
		HashMap<Integer, String> item = new HashMap<Integer, String>();
		item.put(ViewHolder.KEY_URL, url);
		dataList.add(item);
		this.notifyDataSetChanged();
	}

	public void removeItem(String url)
	{
		String tmp;
		for (int i = 0; i < dataList.size(); i++)
		{
			tmp = dataList.get(i).get(ViewHolder.KEY_URL);
			if (tmp.equals(url))
			{
				dataList.remove(i);
				this.notifyDataSetChanged();
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.dllist_item, null);
		}

		HashMap<Integer, String> itemData = dataList.get(position);
		final String url = itemData.get(ViewHolder.KEY_URL);
		convertView.setTag(url);
		Cursor cursor = ziLiaoJiaDBHelper.select(new String[]{"item_name"}, "item_url=? and username=?", new String[]{url, sp.getString("UserName", "")});
		cursor.moveToNext();
		final String name = cursor.getString(cursor.getColumnIndex("item_name"));
		cursor.close();
		ViewHolder viewHolder = new ViewHolder(convertView, name);
		viewHolder.setData(itemData);

		if(barFlag){
			viewHolder.progressBar.setVisibility(View.VISIBLE);
		}else{
			viewHolder.progressBar.setVisibility(View.GONE);
			UI.<RelativeLayout>findViewById(convertView, R.id.layout).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					openBook(name);
				}
			});;
			
			UI.<RelativeLayout>findViewById(convertView, R.id.layout).setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
				 	// TODO Auto-generated method stub
					System.out.println("长按");
					Dialog dialog = new AlertDialog.Builder(mContext)
		            .setTitle("删除图书")
		            .setMessage("是否删除" + name)
		            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	Cursor cursor = ziLiaoJiaDBHelper.select(null, "item_url=? and username=?", new String[]{url, sp.getString("UserName", "")});
		    				cursor.moveToNext();
		    				
		    				if (cursor.getString(cursor.getColumnIndex("item_type")).equals("Culture")) {
		    					File file = new File(cursor.getString(cursor.getColumnIndex("item_path")));
		    					file.delete();
							}
		    				
		    				ziLiaoJiaDBHelper.delete(cursor.getInt(cursor.getColumnIndex("item_id")));
		    				String name = cursor.getString(cursor.getColumnIndex("item_name"));
		    				downloadManager.deleteHandler(url);
		    				cursor.close();
		    				
		    				removeItem(url);
		                }
		            })//设置确定按钮
		            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                }
		            })//设置取消按钮
		            .create();
					dialog.show();
					return true;
				}
			});
		}
		
		viewHolder.continueButton.setOnClickListener(new DownloadBtnListener(
				url, viewHolder));
		viewHolder.pauseButton.setOnClickListener(new DownloadBtnListener(url,
				viewHolder));
		viewHolder.deleteButton.setOnClickListener(new DownloadBtnListener(url,
				viewHolder));
		viewHolder.deleteButton.setVisibility(View.GONE);
		return convertView;
	}
	

	protected void openBook(String name) {
		// TODO Auto-generated method stub
		Cursor cursor = ziLiaoJiaDBHelper.select(
				null,
				"item_name=? and username=?",
				new String[] { name,
						sp.getString("UserName", "") });
		cursor.moveToNext();
		
		if (cursor.getString(cursor.getColumnIndex("item_type")).equals("Culture")) {
			File file = new File(cursor.getString(cursor.getColumnIndex("item_path")));
			FileUtils.openFile(file, mContext);
		}else if (cursor.getString(cursor.getColumnIndex("item_type")).equals("Library")) {
			Bundle bundle = new Bundle();
			Intent intent = new Intent();
			bundle.putString("url", cursor.getString(cursor.getColumnIndex("item_url")));
			intent.putExtras(bundle);
			intent.setClass(mContext, NetActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mContext.startActivity(intent);
		}
		cursor.close();
	}

	public boolean isBarFlag() {
		return barFlag;
	}

	public void setBarFlag(boolean barFlag) {
		this.barFlag = barFlag;
	}

	private class DownloadBtnListener implements View.OnClickListener
	{
		private String url;
		private ViewHolder mViewHolder;

		public DownloadBtnListener(String url, ViewHolder viewHolder)
		{
			this.url = url;
			this.mViewHolder = viewHolder;
		}

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.btn_start:

				downloadManager.continueHandler(url);
				mViewHolder.continueButton.setVisibility(View.GONE);
				mViewHolder.pauseButton.setVisibility(View.VISIBLE);

				break;
			case R.id.btn_pause:

				downloadManager.pauseHandler(url);
				mViewHolder.continueButton.setVisibility(View.VISIBLE);
				mViewHolder.pauseButton.setVisibility(View.GONE);

				break;
			case R.id.btn_delete:
				Dialog dialog = new AlertDialog.Builder(mContext)
	            .setTitle("删除图书")
	            .setMessage("是否删除该图书")
	            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	Cursor cursor = ziLiaoJiaDBHelper.select(null, "item_url=? and username=?", new String[]{url, sp.getString("UserName", "")});
	    				cursor.moveToNext();
	    				ziLiaoJiaDBHelper.delete(cursor.getInt(0));
	    				String name = cursor.getString(cursor.getColumnIndex("item_name"));
	    				downloadManager.deleteHandler(url);
	    				cursor.close();
	    				
	    				removeItem(url);
	                }
	            })//设置确定按钮
	            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                }
	            })//设置取消按钮
	            .create();
				dialog.show();

				break;
			}
		}
	}
	
	public Intent getPdfFileIntent(String path) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		i.setDataAndType(uri, "application/pdf");
		return i;
	}
	
}