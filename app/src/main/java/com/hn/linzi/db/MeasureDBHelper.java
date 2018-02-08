package com.hn.linzi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MeasureDBHelper extends SQLiteOpenHelper {
	Context context1;
	public MeasureDBHelper(Context context) {
		super(context, MeasureListDB.DB_NAME, null, MeasureListDB.VERSION);
		 context1 = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(MeasureListDB.MeasureList.CREATE_TABLE_SQL);
//		Toast.makeText(context1, "创建数据库成功",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
