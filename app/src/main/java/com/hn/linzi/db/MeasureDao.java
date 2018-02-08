package com.hn.linzi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hn.linzi.data.MeasureInfo;

import java.util.ArrayList;
import java.util.List;


public class MeasureDao {

	private MeasureDBHelper mDbHelper;

	public MeasureDao(Context context){
		mDbHelper = new MeasureDBHelper(context);
	}
	// 增删改查的方法
	// 增
	public boolean add(String day, String time, int high_handed, int low_handed , int pules , String remark){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(MeasureListDB.MeasureList.DAY, day);
		values.put(MeasureListDB.MeasureList.TIME, time);
		values.put(MeasureListDB.MeasureList.HIGH_HANDED, high_handed);
		values.put(MeasureListDB.MeasureList.LOW_HANDED, low_handed);
		values.put(MeasureListDB.MeasureList.PULSE, pules);
		values.put(MeasureListDB.MeasureList.REMARK, remark);
		long insert=db.insert(MeasureListDB.MeasureList.TABLE_NAME, null, values);
		db.close();
		return insert!=-1;
	}
	
	// 删除
	public boolean delete(String phone){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int delete=db.delete(MeasureListDB.MeasureList.TABLE_NAME, "phone=?", new String[]{phone});// delete from blacklist where phone=?
		db.close();
		return delete!=0;
	}
	
	// 改
	public boolean update(String phone, int type){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(MeasureListDB.MeasureList.PULSE, type);
		int update=db.update(MeasureListDB.MeasureList.TABLE_NAME, values, "phone=?", new String[]{phone});// update blacklist set type=? where phone=?
		db.close();
		return update!=0;
	}
	
	// 查询所有的信息
	public List<MeasureInfo> queryAllMeasureInfos(){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select _id,day,time ,high_handed,low_handed,pules,remark from MeasureList", null);
		List<MeasureInfo> infos=new ArrayList<MeasureInfo>();
		if(cursor!=null){
			while(cursor.moveToNext()){
				MeasureInfo measureInfo=new MeasureInfo();
                measureInfo._id=cursor.getInt(0);
                measureInfo.day=cursor.getString(1);
                measureInfo.time=cursor.getString(2);
                measureInfo.high_handed = cursor.getInt(3);
                measureInfo.low_handed = cursor.getInt(4);
                measureInfo.pules = cursor.getInt(5);
                measureInfo.remark=cursor.getString(6);
				infos.add(measureInfo);
			}
			cursor.close();
		}
		db.close();
		return infos;
	}

	public List<MeasureInfo> AllMeasureInfos(){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from MeasureList order by _id desc", null);
		List<MeasureInfo> infos=new ArrayList<MeasureInfo>();
		if(cursor!=null){
			while(cursor.moveToNext()){
				MeasureInfo measureInfo=new MeasureInfo();
				measureInfo._id=cursor.getInt(0);
				measureInfo.day=cursor.getString(1);
				measureInfo.time=cursor.getString(2);
				measureInfo.high_handed = cursor.getInt(3);
				measureInfo.low_handed = cursor.getInt(4);
				measureInfo.pules = cursor.getInt(5);
				measureInfo.remark=cursor.getString(6);
				infos.add(measureInfo);
			}
			cursor.close();
		}
		db.close();
		return infos;
	}

	public List<MeasureInfo> queryPartBlackInfos(int offset, int count){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from MeasureList order by _id desc limit ?,? ", new String[]{String.valueOf(offset), String.valueOf(count)});
		List<MeasureInfo> infos=new ArrayList<MeasureInfo>();
		if(cursor!=null){
			while(cursor.moveToNext()){
				MeasureInfo measureInfo=new MeasureInfo();
				measureInfo._id=cursor.getInt(0);
				measureInfo.day=cursor.getString(1);
				measureInfo.time=cursor.getString(2);
				measureInfo.high_handed = cursor.getInt(3);
				measureInfo.low_handed = cursor.getInt(4);
				measureInfo.pules = cursor.getInt(5);
				measureInfo.remark=cursor.getString(6);
				infos.add(measureInfo);
			}
			cursor.close();
		}
		db.close();
		return infos;
	}

	// 查询部分的信息
	public List<MeasureInfo> queryDayMeasureInfos(String day){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select _id,day,time ,high_handed,low_handed,pules,remark from MeasureList where day= ? order by _id desc", new String[]{day});
		List<MeasureInfo> infosDay=new ArrayList<MeasureInfo>();
		if(cursor!=null){
			while(cursor.moveToNext()){
				MeasureInfo measureInfo=new MeasureInfo();
				measureInfo._id=cursor.getInt(0);
				measureInfo.day=cursor.getString(1);
				measureInfo.time=cursor.getString(2);
				measureInfo.high_handed = cursor.getInt(3);
				measureInfo.low_handed = cursor.getInt(4);
				measureInfo.pules = cursor.getInt(5);
				measureInfo.remark=cursor.getString(6);
				infosDay.add(measureInfo);
			}
			cursor.close();
		}
		db.close();
		return infosDay;
	}
	// 查询部分的信息
	public List<MeasureInfo> DayMeasureInfos(String day){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from MeasureList where day= ? ", new String[]{day});
		List<MeasureInfo> infosDay=new ArrayList<MeasureInfo>();
		if(cursor!=null){
			while(cursor.moveToNext()){
				MeasureInfo measureInfo=new MeasureInfo();
				measureInfo._id=cursor.getInt(0);
				measureInfo.day=cursor.getString(1);
				measureInfo.time=cursor.getString(2);
				measureInfo.high_handed = cursor.getInt(3);
				measureInfo.low_handed = cursor.getInt(4);
				measureInfo.pules = cursor.getInt(5);
				measureInfo.remark=cursor.getString(6);
				infosDay.add(measureInfo);
			}
			cursor.close();
		}
		db.close();
		return infosDay;
	}
	
	// 清除所有
	public void deleteAll(){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.execSQL("delete from MeasureList");
		db.close();
	}

	
}
