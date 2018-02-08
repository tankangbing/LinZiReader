package com.hn.linzi.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.hn.linzi.utils.ZiLiaoJiaDBHelper;

public class ZiLiaoJiaData {
	public List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();
	
	private ZiLiaoJiaDBHelper ziLiaoJiaDBHelper;
	private Context context;
	private SharedPreferences sp;
	
	public ZiLiaoJiaData(Context context) {
		super();
		this.context = context;
		ziLiaoJiaDBHelper = new ZiLiaoJiaDBHelper(context);
		sp = context.getSharedPreferences("SP", context.MODE_PRIVATE);
		init();
	}

	private void init() {
		
		Cursor cursor = ziLiaoJiaDBHelper.select(null, "username=?", new String[]{sp.getString("UserName", "")});
		while (cursor.moveToNext()) {
			String item_name = cursor.getString(cursor.getColumnIndex("item_name"));
			String item_type = cursor.getString(cursor.getColumnIndex("item_type"));
			String item_url = cursor.getString(cursor.getColumnIndex("item_url"));
			
			IMG_DESCRIPTIONS.add(new Data(item_type, item_name, item_url, cursor.getString(cursor.getColumnIndex("item_path")),cursor.getString(cursor.getColumnIndex("item_dlflag"))));
		}
	}

	public static final class Data {

		public final String type;//类型
		public final String name;//名字
		public final String url;//下载链接
		public final String path;//路径
		public final String dlflag;//是否下载完
		public Data(String type, String name, String url, String path,
				String dlflag) {
			super();
			this.type = type;
			this.name = name;
			this.url = url;
			this.path = path;
			this.dlflag = dlflag;
		}
		
	}
}
