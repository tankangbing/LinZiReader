package com.hn.linzi.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LastWatchDBHelper extends SQLiteOpenHelper{
	private SQLiteDatabase db = null;
	
	public LastWatchDBHelper(Context context) {
		super(context, "lastWatch.db", null, 1);
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
		String sql = "create table lastWatch(lastw_id integer PRIMARY KEY AUTOINCREMENT, lastw_name text,"
				+ " lastw_url text, lastw_imgurl text, lastw_cat text)";
		db.execSQL(sql);
		insert("", "", "", "");
		insert("", "", "", "");
		insert("", "", "", "");
		insert("", "", "", "");
		insert("", "", "", "");
		insert("", "", "", "");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS lastWatch";
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
				.query("lastWatch", need, tiaojian, zhi, null, null, null);
		return cursor;
	}

	/**
	 * 添加
	 * @param bookname 	书名
	 * @return
	 */
	private long insert(String name, String url, String imgurl, String cat) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put("lastw_name", name);
		cv.put("lastw_url", url);
		cv.put("lastw_imgurl", imgurl);
		cv.put("lastw_cat", cat);
		long row = db.insert("lastWatch", null, cv);
		return row;
	}

	/**
	 * 删除
	 * @param id	图书的主键ID
	 */
	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "lastw_id" + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete("lastWatch", where, whereValue);
	}

	/**
	 * 修改
	 * @param id
	 * @param bookname
	 */
	public void update(int id, String name, String url, String imgurl, String cat) {
		if(id == 1){
			Cursor item1 = select(new String[]{"lastw_name", "lastw_url", "lastw_imgurl", "lastw_cat"}, "lastw_id = ?", new String[]{"1"});
			Cursor item2 = select(new String[]{"lastw_name", "lastw_url", "lastw_imgurl", "lastw_cat"}, "lastw_id = ?", new String[]{"2"});
			Cursor item3 = select(new String[]{"lastw_name", "lastw_url", "lastw_imgurl", "lastw_cat"}, "lastw_id = ?", new String[]{"3"});
			Cursor item4 = select(new String[]{"lastw_name", "lastw_url", "lastw_imgurl", "lastw_cat"}, "lastw_id = ?", new String[]{"4"});
			Cursor item5 = select(new String[]{"lastw_name", "lastw_url", "lastw_imgurl", "lastw_cat"}, "lastw_id = ?", new String[]{"5"});
			Cursor item6 = select(new String[]{"lastw_name", "lastw_url", "lastw_imgurl", "lastw_cat"}, "lastw_id = ?", new String[]{"6"});
			item1.moveToNext();
			item2.moveToNext();
			item3.moveToNext();
			item4.moveToNext();
			item5.moveToNext();
			item6.moveToNext();
			String name1 = item1.getString(item1.getColumnIndex("lastw_name"));
			String url1 = item1.getString(item1.getColumnIndex("lastw_url"));
			String imgurl1 = item1.getString(item1.getColumnIndex("lastw_imgurl"));
			String cat1 = item1.getString(item1.getColumnIndex("lastw_cat"));
			
			String name2 = item2.getString(item2.getColumnIndex("lastw_name"));
			String url2 = item2.getString(item2.getColumnIndex("lastw_url"));
			String imgurl2 = item2.getString(item2.getColumnIndex("lastw_imgurl"));
			String cat2 = item2.getString(item2.getColumnIndex("lastw_cat"));
			
			String name3 = item3.getString(item3.getColumnIndex("lastw_name"));
			String url3 = item3.getString(item3.getColumnIndex("lastw_url"));
			String imgurl3 = item3.getString(item3.getColumnIndex("lastw_imgurl"));
			String cat3 = item3.getString(item3.getColumnIndex("lastw_cat"));
			
			String name4 = item4.getString(item4.getColumnIndex("lastw_name"));
			String url4 = item4.getString(item4.getColumnIndex("lastw_url"));
			String imgurl4 = item4.getString(item4.getColumnIndex("lastw_imgurl"));
			String cat4 = item4.getString(item4.getColumnIndex("lastw_cat"));
			
			String name5 = item5.getString(item5.getColumnIndex("lastw_name"));
			String url5 = item5.getString(item5.getColumnIndex("lastw_url"));
			String imgurl5 = item5.getString(item5.getColumnIndex("lastw_imgurl"));
			String cat5 = item5.getString(item5.getColumnIndex("lastw_cat"));
			
			String name6 = item6.getString(item6.getColumnIndex("lastw_name"));
			String url6 = item6.getString(item6.getColumnIndex("lastw_url"));
			String imgurl6 = item6.getString(item6.getColumnIndex("lastw_imgurl"));
			String cat6 = item6.getString(item6.getColumnIndex("lastw_cat"));
			
			if (name.equals(name2)) {
				update(2, name1, url1, imgurl1, cat1);
			}
			if (name.equals(name3)) {
				update(2, name1, url1, imgurl1, cat1);
				update(3, name2, url2, imgurl2, cat2);	
			}
			if (name.equals(name4)) {
				update(2, name1, url1, imgurl1, cat1);
				update(3, name2, url2, imgurl2, cat2);
				update(4, name3, url3, imgurl3, cat3);
			}
			if (name.equals(name5)) {
				update(2, name1, url1, imgurl1, cat1);
				update(3, name2, url2, imgurl2, cat2);	
				update(4, name3, url3, imgurl3, cat3);
				update(5, name4, url4, imgurl4, cat4);
			}
			if (name.equals(name6)) {
				update(2, name1, url1, imgurl1, cat1);
				update(3, name2, url2, imgurl2, cat2);
				update(4, name3, url3, imgurl3, cat3);
				update(5, name4, url4, imgurl4, cat4);
				update(6, name5, url5, imgurl5, cat5);
			}
			if (!name.equals(name1) && !name.equals(name2) && !name.equals(name3) && !name.equals(name4) && !name.equals(name5) && !name.equals(name6)){
				update(2, name1, url1, imgurl1, cat1);
				update(3, name2, url2, imgurl2, cat2);
				update(4, name3, url3, imgurl3, cat3);
				update(5, name4, url4, imgurl4, cat4);
				update(6, name5, url5, imgurl5, cat5);
			}
			
		}
		 
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "last_id" + " = ?";
		String[] whereValue = { Integer.toString(id) };
		
		ContentValues cv = new ContentValues();
		cv.put("lastw_name", name);
		cv.put("lastw_url", url);
		cv.put("lastw_imgurl", imgurl);
		cv.put("lastw_cat", cat);
		db.update("lastWatch", cv, where, whereValue);
	}
}
