package com.hn.linzi.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ZiLiaoJiaDBHelper extends SQLiteOpenHelper{
private SQLiteDatabase db = null;
	
	public ZiLiaoJiaDBHelper(Context context) {
		super(context, "ziliaojia.db", null, 1);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		final SQLiteDatabase db;
		if(this.db != null){
			db = this.db;
		} else {
			db = super.getWritableDatabase();
		}
		return db;
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		String sql = "create table ziliaojia(item_id integer PRIMARY KEY AUTOINCREMENT, item_name text, item_type text, item_url text"
				+ ", item_path text, item_dlflag text, item_restartflag text, username text)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS ziliaojia";
		db.execSQL(sql);
		onCreate(db);
	}
	/**
	 * 查询
	 * @param need 需要查询的
	 * @param tiaojian 条件
	 * @param zhi 条件的值
	 * @return
	 */
	public Cursor select(String[] need, String tiaojian, String[] zhi) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query("ziliaojia", need, tiaojian, zhi, null, null, null);
		return cursor;
	}

	/**
	 * 添加馆藏图书
	 * @param name	名字
	 * @param type	类型
	 * @param url 链接
	 * @param username 当前用户账号（学号）
	 * @return
	 */
	public long insert(String name, String type, String url, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("item_name", name);
		cv.put("item_type", type);
		cv.put("item_url", url);
		cv.put("username", username);
		cv.put("item_dlflag", "true");
		long row = db.insert("ziliaojia", null, cv);
		return row;
	}
	
	/**
	 * 添加校园文化下载
	 * @param name 名字
	 * @param type 类型
	 * @param url 链接
	 * @param path 保存路径
	 * @param dlflag 下载完成标识
	 * @param restartflag 重新下载标识
	 * @param username 当前用户账号（学号）
	 * @return
	 */
	public long insert(String name, String type, String url, String path, String dlflag, String restartflag, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("item_name", name);
		cv.put("item_type", type);
		cv.put("item_url", url);
		cv.put("item_path", path);
		cv.put("item_dlflag", dlflag);
		cv.put("item_restartflag", restartflag);
		cv.put("username", username);
		long row = db.insert("ziliaojia", null, cv);
		return row;
	}

	/**
	 * 删除
	 * @param id	图书的主键ID
	 */
	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "item_id" + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete("ziliaojia", where, whereValue);
	}

	/**
	 * 修改
	 * @param id id
	 * @param name 名字
	 * @param type 类型
	 * @param url 链接
	 * @param path 保存路径
	 * @param dlflag 下载完成标识
	 * @param restartflag 重新下载标识
	 * @param username 当前用户账号（学号）
	 */
	public void update(int id, String name, String type, String url, String path, String dlflag, String restartflag, String username) {
		 
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "item_id" + " = ?";
		String[] whereValue = { Integer.toString(id) };
		
		ContentValues cv = new ContentValues();
		cv.put("item_name", name);
		cv.put("item_type", type);
		cv.put("item_url", url);
		cv.put("item_path", path);
		cv.put("item_dlflag", dlflag);
		cv.put("item_restartflag", restartflag);
		cv.put("username", username);
		db.update("ziliaojia", cv, where, whereValue);
	}
}
