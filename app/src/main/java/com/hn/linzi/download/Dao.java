package com.hn.linzi.download;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * 涓�涓笟鍔＄被
 */
public class Dao {  
	private static Dao dao=null;
	private Context context; 
	private  Dao(Context context) { 
		this.context=context;
	}
	public static  Dao getInstance(Context context){
		if(dao==null){
			dao=new Dao(context); 
		}
		return dao;
	}
	public  SQLiteDatabase getConnection() {
		SQLiteDatabase sqliteDatabase = null;
		try { 
			sqliteDatabase= new DBHelper(context).getReadableDatabase();
		} catch (Exception e) {  
		}
		return sqliteDatabase;
	}

	/**
	 * 鏌ョ湅鏁版嵁搴撲腑鏄惁鏈夋暟鎹�
	 */
	public synchronized boolean isHasInfors(String urlstr) {
		SQLiteDatabase database = getConnection();
		int count = -1;
		Cursor cursor = null;
		try {
			String sql = "select count(*)  from download_info where url=?";
			cursor = database.rawQuery(sql, new String[] { urlstr });
			if (cursor.moveToFirst()) {
				count = cursor.getInt(0);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
			if (null != cursor) {
				cursor.close();
			}
		}
		return count == 0;
	}

	/**
	 * 淇濆瓨 涓嬭浇鐨勫叿浣撲俊鎭�
	 */
	public synchronized void saveInfos(List<DownloadInfo> infos) {
		SQLiteDatabase database = getConnection();
		try {
			for (DownloadInfo info : infos) {
				String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,url) values (?,?,?,?,?)";
				Object[] bindArgs = { info.getThreadId(), info.getStartPos(),
						info.getEndPos(), info.getCompeleteSize(),
						info.getUrl() };
				database.execSQL(sql, bindArgs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}

	/**
	 * 寰楀埌涓嬭浇鍏蜂綋淇℃伅
	 */
	public synchronized List<DownloadInfo> getInfos(String urlstr) {
		List<DownloadInfo> list = new ArrayList<DownloadInfo>();
		SQLiteDatabase database = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select thread_id, start_pos, end_pos,compelete_size,url from download_info where url=?";
			cursor = database.rawQuery(sql, new String[] { urlstr });
			while (cursor.moveToNext()) {
				DownloadInfo info = new DownloadInfo(cursor.getInt(0),
						cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
						cursor.getString(4));
				list.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
			if (null != cursor) {
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * 鏇存柊鏁版嵁搴撲腑鐨勪笅杞戒俊鎭�
	 */
	public synchronized void updataInfos(int threadId, int compeleteSize, String urlstr) {
		SQLiteDatabase database = getConnection();
		try {
			String sql = "update download_info set compelete_size=? where thread_id=? and url=?";
			Object[] bindArgs = { compeleteSize, threadId, urlstr };
			database.execSQL(sql, bindArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}

	/**
	 * 涓嬭浇瀹屾垚鍚庡垹闄ゆ暟鎹簱涓殑鏁版嵁
	 */
	public synchronized void delete(String url) {
		SQLiteDatabase database = getConnection();
		try {
			database.delete("download_info", "url=?", new String[] { url });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}
}
