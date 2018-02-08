package com.hn.linzi.download;
 
 import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
/**
 * 寤虹珛涓�涓暟鎹簱甯姪绫�
 */
public class DBHelper extends SQLiteOpenHelper {
	//download.db-->鏁版嵁搴撳悕
	public DBHelper(Context context) {
	    super(context, "download.db", null, 1);
	}
	
	/**
	 * 鍦╠ownload.db鏁版嵁搴撲笅鍒涘缓涓�涓猟ownload_info琛ㄥ瓨鍌ㄤ笅杞戒俊鎭�
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL("create table download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer, "
	            + "start_pos integer, end_pos integer, compelete_size integer,url char)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
