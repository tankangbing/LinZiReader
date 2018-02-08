package com.hn.linzi.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KeChengDBHelper extends SQLiteOpenHelper {
	private SQLiteDatabase db = null;

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
	
	public KeChengDBHelper(Context context) {
		super(context, "fzydCKData.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		String sql = "create table localKeCheng(kecheng_id integer PRIMARY KEY AUTOINCREMENT, kecheng_name text,"
				+ " kecheng_author text, kecheng_imgurl text, kecheng_url text, kecheng_cat text, username text)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS localKeCheng";
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
				.query("localKeCheng", need, tiaojian, zhi, null, null, "kecheng_id desc");//"book_id desc"
		return cursor;
	}

	/**
	 * 添加
	 * @param bookname 	书名
	 * @param author	作者
	 * @param imgurl	图片地址
	 * @param pub	出版社
	 * @param updatetime	更新时间
	 * @param pubtime	出版时间
	 * @param url	pdf地址
	 * @param path	保存路径
	 * @param dlflag	是否下载完成
	 * @return
	 */
	public long insert(String name, String author, String imgurl, String url, String cat, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("kecheng_name", name);
		cv.put("kecheng_author", author);
		cv.put("kecheng_imgurl", imgurl);
		cv.put("kecheng_url", url);
		cv.put("kecheng_cat", cat);
		cv.put("username", username);
		long row = db.insert("localKeCheng", null, cv);
		return row;
	}

	/**
	 * 删除
	 * @param id	图书的主键ID
	 */
	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "kecheng_id" + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete("localKeCheng", where, whereValue);
	}

	/**
	 * 修改
	 * @param id
	 * @param bookname
	 * @param author
	 * @param imgurl
	 * @param pub
	 * @param updatetime
	 * @param pubtime
	 * @param url
	 * @param path
	 * @param dlflag
	 */
	public void update(int id, String bookname, String author, String imgurl, String url, String cat, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "kecheng_id" + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put("kecheng_name", bookname);
		cv.put("kecheng_author", author);
		cv.put("kecheng_imgurl", imgurl);
		cv.put("kecheng_url", url);
		cv.put("kecheng_cat	", cat);
		cv.put("username", username);
		db.update("localKeCheng", cv, where, whereValue);
	}
}
