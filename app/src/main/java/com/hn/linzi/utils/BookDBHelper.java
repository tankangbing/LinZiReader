package com.hn.linzi.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookDBHelper extends SQLiteOpenHelper {
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
	
	public BookDBHelper(Context context) {
		super(context, "fzydData.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
		String sql = "create table localSHUJIA(book_id integer PRIMARY KEY AUTOINCREMENT, book_name text,"
				+ " book_author text, book_imgurl text, book_pub text, book_updatetime text,"
				+ " book_pubtime text, book_url text, book_path text, book_dlflag text, book_restartflag text,"
				+ " username text, book_type text, book_realid text)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS localSHUJIA";
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
				.query("localSHUJIA", need, tiaojian, zhi, null, null, "book_id desc");//"book_id desc"
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
	public long insert(String bookname, String author, String imgurl, String pub, String updatetime,
			String pubtime, String url, String path, String dlflag, String restartflag, String username
			, String book_type, String book_realid) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("book_name", bookname);
		cv.put("book_author", author);
		cv.put("book_imgurl", imgurl);
		cv.put("book_pub", pub);
		cv.put("book_updatetime", updatetime);
		cv.put("book_pubtime", pubtime);
		cv.put("book_url", url);
		cv.put("book_path", path);
		cv.put("book_dlflag", dlflag);
		cv.put("book_restartflag", restartflag);
		cv.put("username", username);
		cv.put("book_type", book_type);
		cv.put("book_realid", book_realid);
		long row = db.insert("localSHUJIA", null, cv);
		return row;
	}

	/**
	 * 删除
	 * @param id	图书的主键ID
	 */	
	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "book_id" + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete("localSHUJIA", where, whereValue);
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
	public void update(int id, String bookname, String author, String imgurl, String pub, String updatetime,
			String pubtime, String url, String path, String dlflag, String restartflag, String username) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "book_id" + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put("book_name", bookname);
		cv.put("book_author", author);
		cv.put("book_imgurl", imgurl);
		cv.put("book_pub", pub);
		cv.put("book_updatetime", updatetime);
		cv.put("book_pubtime", pubtime);
		cv.put("book_url", url);
		cv.put("book_path", path);
		cv.put("book_dlflag", dlflag);
		cv.put("book_restartflag", restartflag);
		cv.put("username", username);
		db.update("localSHUJIA", cv, where, whereValue);
	}
}
