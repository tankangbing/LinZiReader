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
import com.ta.util.netstate.TANetWorkUtil;
import com.hn.epub.Turnmain3;
import com.hn.linzi.R;
import com.hn.linzi.utils.BookDBHelper;
import com.hn.linzi.utils.LastReadDBHelper;
import com.hn.linzi.utils.MyTools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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

public class DownloadListAdapter extends BaseAdapter
{

	private Context mContext;
	private ArrayList<HashMap<Integer, String>> dataList;
	private DownloadManager downloadManager;
	private ListView downloadList;
	private final BookDBHelper bookDBHelper;
	private LastReadDBHelper lastReadDBHelper;
	private boolean barFlag;
	private SharedPreferences sp;

	public DownloadListAdapter(Context context, ListView listView)
	{
		mContext = context;
		this.downloadList = listView;
		sp = mContext.getSharedPreferences("SP",
				mContext.MODE_PRIVATE);
		dataList = new ArrayList<HashMap<Integer, String>>();
		bookDBHelper = new BookDBHelper(context);
		lastReadDBHelper = new LastReadDBHelper(context);
//		String path = "";
//		if(MyTools.getExtSDCardPaths().size() > 0){
//			path = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/";
//		}else {
//			path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/";
//		}
		String path = context.getExternalFilesDir("BOOKS").getPath() + "/";//不能换
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
				Cursor cursor = bookDBHelper.select(new String[]{"book_name"}, "book_url=? and username=?", new String[]{url, sp.getString("UserName", "")});
				cursor.moveToNext();
				String name = cursor.getString(cursor.getColumnIndex("book_name"));
				cursor.close();
				// TODO Auto-generated method stub
				super.onLoading(url, totalSize, currentSize, speed);
				long downloadPercent = currentSize * 100 / totalSize;
				View taskListItem = downloadList.findViewWithTag(url);
				ViewHolder viewHolder = new ViewHolder(taskListItem, name);
				viewHolder.setData(url, speed + "kbs" + "|"
						+ totalSize + "|" + currentSize,
						downloadPercent + "");
				TALogger.d(DownloadListAdapter.this, "speed" + speed + "kbps"
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
		final Cursor cursor = bookDBHelper.select(null, "book_url=? and username=?",new String[]{url, sp.getString("UserName", "")});
		cursor.moveToNext();
		
		File file = new File(path);
		System.out.println(file.getName());
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String nowtime = df.format(curDate);
		
		if(path.indexOf(".") >= 0){
			System.out.println("path");
			path = SDCard + sp.getString("UserName", "")+ "_" + nowtime +"_" + cursor.getString(cursor.getColumnIndex("book_realid"));
			System.out.println(path);
			path = path + "." + cursor.getString(12);
			System.out.println(path);
		}
		file.renameTo(new File(path));
		file = new File(path);
		System.out.println(file.getName());
		
		System.out.println(path);
		SharedPreferences sp = mContext.getSharedPreferences("SP",
				mContext.MODE_PRIVATE);
		bookDBHelper.update(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), path,
				"true", "false", sp.getString("UserName", ""));
		Toast.makeText(mContext, cursor.getString(1) + "下载完成", Toast.LENGTH_SHORT).show();
		cursor.close();
//		Intent intent = new Intent();
//		intent.setClass(mContext, Exit.class);
//		mContext.startActivity(intent);
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
		Cursor cursor = bookDBHelper.select(new String[]{"book_name"}, "book_url=? and username=?", new String[]{url, sp.getString("UserName", "")});
		cursor.moveToNext();
		final String name = cursor.getString(cursor.getColumnIndex("book_name"));
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
		                	Cursor cursor = bookDBHelper.select(null, "book_url=? and username=?", new String[]{url, sp.getString("UserName", "")});
		    				cursor.moveToNext();
		    				bookDBHelper.delete(cursor.getInt(0));
		    				String name = cursor.getString(cursor.getColumnIndex("book_name"));
		    				downloadManager.deleteHandler(url);
		    				cursor.close();
		    				
		    				LastReadDBHelper lastReadDBHelper = new LastReadDBHelper(mContext);
		    				Cursor item1 = lastReadDBHelper.select(new String[]{"book_name", "book_imgurl", "book_path"}, "last_id=? and username=?", new String[]{"1", sp.getString("UserName", "")});
		    				Cursor item2 = lastReadDBHelper.select(new String[]{"book_name", "book_imgurl", "book_path"}, "last_id=? and username=?", new String[]{"2", sp.getString("UserName", "")});
		    				Cursor item3 = lastReadDBHelper.select(new String[]{"book_name", "book_imgurl", "book_path"}, "last_id=? and username=?", new String[]{"3", sp.getString("UserName", "")});
		    				item1.moveToNext();
		    				item2.moveToNext();
		    				item3.moveToNext();
		    				String name1 = item1.getString(item1.getColumnIndex("book_name"));
		    				String img1 = item1.getString(item1.getColumnIndex("book_imgurl"));
		    				String pdf1 = item1.getString(item1.getColumnIndex("book_path"));
		    				
		    				String name2 = item2.getString(item2.getColumnIndex("book_name"));
		    				String img2 = item2.getString(item2.getColumnIndex("book_imgurl"));
		    				String pdf2 = item2.getString(item2.getColumnIndex("book_path"));
		    				
		    				String name3 = item3.getString(item3.getColumnIndex("book_name"));
		    				String img3 = item3.getString(item3.getColumnIndex("book_imgurl"));
		    				String pdf3 = item3.getString(item3.getColumnIndex("book_path"));
		    				
		    				if(name.equals(name1)){
		    					lastReadDBHelper.update(1, name2, img2, pdf2, sp.getString("UserName", ""));
		    					lastReadDBHelper.update(2, name3, img3, pdf3, sp.getString("UserName", ""));
		    					lastReadDBHelper.update(3, "", "", "", sp.getString("UserName", ""));
		    				}
		    				
		    				if(name.equals(name2)){
		    					lastReadDBHelper.update(2, name3, img3, pdf3, sp.getString("UserName", ""));
		    					lastReadDBHelper.update(3, "", "", "", sp.getString("UserName", ""));
		    				}
		    				
		    				if(name.equals(name3)){
		    					lastReadDBHelper.update(3, "", "", "", sp.getString("UserName", ""));
		    				}
		    				
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
		Cursor cursor = bookDBHelper.select(
				null,
				"book_name=? and username=?",
				new String[] { name,
						sp.getString("UserName", "") });
		cursor.moveToNext();
		if (cursor.getString(12).equals("pdf")) {
			Intent intent = getPdfFileIntent(cursor.getString(cursor.getColumnIndex("book_path")));
			mContext.startActivity(intent);
		} else if (cursor.getString(12).equals("epub")) {
			if(TANetWorkUtil.isNetworkAvailable(mContext)){
				System.out.println("有网络");
				Intent intent = new Intent();
				intent.setClass(mContext,
						Turnmain3.class);
				intent.putExtra("bookurl", cursor.getString(8));
				mContext.startActivity(intent);
			}else {
				System.out.println("没网络");
				Toast.makeText(mContext, "无法进行网络认证，请检查网络后重试", Toast.LENGTH_SHORT).show();
			}
		}
		lastReadDBHelper.update(1, name,
				cursor.getString(cursor.getColumnIndex("book_imgurl")),
				cursor.getString(cursor.getColumnIndex("book_path")),
				sp.getString("UserName", ""));
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
	                	Cursor cursor = bookDBHelper.select(null, "book_url=? and username=?", new String[]{url, sp.getString("UserName", "")});
	    				cursor.moveToNext();
	    				bookDBHelper.delete(cursor.getInt(0));
	    				String name = cursor.getString(cursor.getColumnIndex("book_name"));
	    				downloadManager.deleteHandler(url);
	    				cursor.close();
	    				
	    				LastReadDBHelper lastReadDBHelper = new LastReadDBHelper(mContext);
	    				Cursor item1 = lastReadDBHelper.select(new String[]{"book_name", "book_imgurl", "book_path"}, "last_id=? and username=?", new String[]{"1", sp.getString("UserName", "")});
	    				Cursor item2 = lastReadDBHelper.select(new String[]{"book_name", "book_imgurl", "book_path"}, "last_id=? and username=?", new String[]{"2", sp.getString("UserName", "")});
	    				Cursor item3 = lastReadDBHelper.select(new String[]{"book_name", "book_imgurl", "book_path"}, "last_id=? and username=?", new String[]{"3", sp.getString("UserName", "")});
	    				item1.moveToNext();
	    				item2.moveToNext();
	    				item3.moveToNext();
	    				String name1 = item1.getString(item1.getColumnIndex("book_name"));
	    				String img1 = item1.getString(item1.getColumnIndex("book_imgurl"));
	    				String pdf1 = item1.getString(item1.getColumnIndex("book_path"));
	    				
	    				String name2 = item2.getString(item2.getColumnIndex("book_name"));
	    				String img2 = item2.getString(item2.getColumnIndex("book_imgurl"));
	    				String pdf2 = item2.getString(item2.getColumnIndex("book_path"));
	    				
	    				String name3 = item3.getString(item3.getColumnIndex("book_name"));
	    				String img3 = item3.getString(item3.getColumnIndex("book_imgurl"));
	    				String pdf3 = item3.getString(item3.getColumnIndex("book_path"));
	    				
	    				if(name.equals(name1)){
	    					lastReadDBHelper.update(1, name2, img2, pdf2, sp.getString("UserName", ""));
	    					lastReadDBHelper.update(2, name3, img3, pdf3, sp.getString("UserName", ""));
	    					lastReadDBHelper.update(3, "", "", "", sp.getString("UserName", ""));
	    				}
	    				
	    				if(name.equals(name2)){
	    					lastReadDBHelper.update(2, name3, img3, pdf3, sp.getString("UserName", ""));
	    					lastReadDBHelper.update(3, "", "", "", sp.getString("UserName", ""));
	    				}
	    				
	    				if(name.equals(name3)){
	    					lastReadDBHelper.update(3, "", "", "", sp.getString("UserName", ""));
	    				}
	    				
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