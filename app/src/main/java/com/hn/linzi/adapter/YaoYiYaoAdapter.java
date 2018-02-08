package com.hn.linzi.adapter;

import com.aphidmobile.utils.UI;
import com.hn.linzi.R;
import com.hn.linzi.activity.DownloadManageActivity;
import com.hn.linzi.activity.NetActivity;
import com.hn.linzi.activity.PublicClassContentActivity;
import com.hn.linzi.data.YaoYiYaoData;
import com.hn.linzi.data.YaoYiYaoData.Data;
import com.hn.linzi.utils.BookDBHelper;
import com.hn.linzi.utils.MyTools;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class YaoYiYaoAdapter extends BaseAdapter {

	private Context context;

	private LayoutInflater inflater;
	private TelephonyManager tm; 
	private YaoYiYaoData yaoData;
	
	private BookDBHelper bookDBHelper;
//	private MyHandler mHandler;

	public YaoYiYaoAdapter(Context context, YaoYiYaoData data) {
		super();
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.yaoData = data;
		this.tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		bookDBHelper = new BookDBHelper(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return yaoData.IMG_DESCRIPTIONS.size();
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

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View layout = convertView;
		if (convertView == null) {
			layout = inflater.inflate(R.layout.yaoyiyao_list, null);
//			if(position % 2 == 0){
//				layout.setBackgroundColor(R.color.gainsboro);
//			}
		}
		
		final Data data = yaoData.IMG_DESCRIPTIONS.get(position);
		
		UI.<TextView>findViewById(layout, R.id.yaoyiyao_name).setText(data.name);
		if(data.type.equals("BOOK")){
			UI.<TextView>findViewById(layout, R.id.yaoyiyao_type).setText("电子图书");
		}
		if(data.type.equals("LIBRARY")){
			UI.<TextView>findViewById(layout, R.id.yaoyiyao_type).setText("馆藏图书");
		}
		if(data.type.equals("VIDEO")){
			UI.<TextView>findViewById(layout, R.id.yaoyiyao_type).setText("课程视频");
		}
		
		UI.<LinearLayout>findViewById(layout, R.id.yaoyiyao_bg).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final SharedPreferences sp = context.getSharedPreferences("SP",
						context.MODE_PRIVATE);
				if(data.type.equals("BOOK")){
					Dialog dialog = new AlertDialog.Builder(context)
//		            .setIcon(R.drawable.buttons_bg20)
		            .setTitle(R.string.dialog_download)
		            .setMessage("是否下载" + data.name)
		            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	//开启线程下载图书
		                	Cursor cursor = bookDBHelper.select(null, "book_name=? and username=?", new String[]{data.name, sp.getString("UserName", "")});
		                	if(cursor.moveToNext()){
		                		Toast.makeText(context, "已有该图书" + data.name, Toast.LENGTH_SHORT).show();
		                	}else{
		                		Toast.makeText(context, "开始下载" + data.name, Toast.LENGTH_SHORT).show();
		                		String path = "";
		                		if(MyTools.getExtSDCardPaths().size() > 0){
		                			path = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/";
		                		}else {
		                			path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/";
		                		}
//		                		File DL = context.getExternalFilesDir("BOOKS");
//		            			String path = DL.getPath() + "/" + sp.getString("UserName", "") + "/";
		                		if (data.book_type.equals("epub")) {
		            				path = path + sp.getString("UserName", "")+ "_"
		            						+ data.name + ".epub";
		            			} else if (data.book_type.equals("pdf")) {
		            				path = path + sp.getString("UserName", "")+ "_"
		            						+ data.name + ".pdf";
		            			}
		                		
								String url = data.url + "&n=" + sp.getString("UserName", "") +
								"&d=" +sp.getString("KEY","" ) + "&code=" + sp.getString("code", "88888888")
								+ "&imei=" +tm.getDeviceId();
								System.out.println(url);
//								DownloadFile downloadFile = new DownloadFile(context);
//								downloadFile.downloadPDF(url, data.name);
								bookDBHelper.insert(data.name, data.author, data.imgurl, data.pub, data.updatetime, "", url, path, "false", "false", sp.getString("UserName", ""), data.book_type, data.book_id);
								Intent intent = new Intent();
								intent.setClass(context, DownloadManageActivity.class);
								context.startActivity(intent);
		                	}
							

		                }
		            })//设置确定按钮
		            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                }
		            })//设置取消按钮
		            .create();
					dialog.show();
				}else if (data.type.equals("VIDEO")) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", "http://data.iego.net:88/m/lessonInfoJson.aspx?n=" + sp.getString("UserName", "") +
							"&d=" +sp.getString("KEY","" ) + "&lesson_id=" + data.book_id);
					intent.putExtras(bundle);
					intent.setClass(context, PublicClassContentActivity.class);
					context.startActivity(intent);
				}else {
					String url = data.url + "&n=" + sp.getString("UserName", "")
					+ "&d=" + sp.getString("KEY","" )
					+ "&code=" + sp.getString("code","88888888" )
					+ "&imei=" + tm.getDeviceId();
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("url", url);
					System.out.println(url);
					intent.putExtras(bundle);
					intent.setClass(context, NetActivity.class);
					context.startActivity(intent);
				}
			}
		});
		return layout;
	}
	
	private String makeJSON() {
		// TODO Auto-generated method stub
	
		return null;
	}
}
