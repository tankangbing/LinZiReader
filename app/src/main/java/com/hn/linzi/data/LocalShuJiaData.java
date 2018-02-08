package com.hn.linzi.data;

import java.util.ArrayList;
import java.util.List;

import com.hn.linzi.utils.BookDBHelper;
import com.hn.linzi.utils.LastReadDBHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

public class LocalShuJiaData {
	public List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();

	private Context context;
	private BookDBHelper bookDBHelper;
	private LastReadDBHelper lastReadDBHelper;

	public LocalShuJiaData(Context context) {
		super();
		this.context = context;
		bookDBHelper = new BookDBHelper(context);
		lastReadDBHelper = new LastReadDBHelper(context);
		init();
	}

	private void init() {
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> author = new ArrayList<String>();
		ArrayList<String> imgurl = new ArrayList<String>();
		ArrayList<String> pub = new ArrayList<String>();
		ArrayList<String> type = new ArrayList<String>();
		ArrayList<String> updatetime = new ArrayList<String>();
		ArrayList<String> url = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		
		int num = 0;
		SharedPreferences sp = context.getSharedPreferences("SP", context.MODE_PRIVATE);
		System.out.println("username=========" + sp.getString("UserName", ""));
		Cursor last = lastReadDBHelper.select(new String[]{"book_name", "book_path", "book_imgurl"}, "username=?", new String[]{sp.getString("UserName", "")});
		while (last.moveToNext()){
			num++;
			if(last.getString(last.getColumnIndex("book_name")).equals("")){
				name.add("");
				imgurl.add("");
				path.add("");
			}else {
				System.out.println(last.getString(last.getColumnIndex("book_name")));
				name.add(last.getString(last.getColumnIndex("book_name")));
				imgurl.add(last.getString(last.getColumnIndex("book_imgurl")));
				path.add(last.getString(last.getColumnIndex("book_path")));
				
			}
		}
		
		Cursor data = bookDBHelper.select(new String[]{"book_name", "book_imgurl", "book_path", "book_id"}, "username=?", new String[]{sp.getString("UserName", "")});
		while (data.moveToNext()) {
			num++;
			name.add(data.getString(data.getColumnIndex("book_name")));
			imgurl.add(data.getString(data.getColumnIndex("book_imgurl")));
			path.add(data.getString(data.getColumnIndex("book_path")));
			
			if(data.isLast() && num != 6){
				for(int i=num+1 ; i<=6 ; i++){
		    		name.add("");
//				    author.add("");
				    imgurl.add("");
//				    pub.add("");
//				    type.add("");
//				    updatetime.add("");
//				    url.add("");
				    path.add("");
		    	}
				IMG_DESCRIPTIONS.add(new Data(name.get(0), imgurl.get(0), path.get(0),
		    			name.get(1), imgurl.get(1), path.get(1),
		    			name.get(2), imgurl.get(2), path.get(2),
		    			name.get(3), imgurl.get(3), path.get(3),
		    			name.get(4), imgurl.get(4), path.get(4),
		    			name.get(5), imgurl.get(5), path.get(5)));
				num = 0;
				name.clear();
		    	author.clear();
		    	imgurl.clear();
		    	pub.clear();
		    	type.clear();
		    	updatetime.clear();
		    	url.clear();
		    	path.clear();
			}
			if(num == 6){
				IMG_DESCRIPTIONS.add(new Data(name.get(0), imgurl.get(0), path.get(0),
		    			name.get(1), imgurl.get(1), path.get(1),
		    			name.get(2), imgurl.get(2), path.get(2),
		    			name.get(3), imgurl.get(3), path.get(3),
		    			name.get(4), imgurl.get(4), path.get(4),
		    			name.get(5), imgurl.get(5), path.get(5)));
				num = 0;
				name.clear();
		    	author.clear();
		    	imgurl.clear();
		    	pub.clear();
		    	type.clear();
		    	updatetime.clear();
		    	url.clear();
		    	path.clear();
			}
		}
	}
	

	public static final class Data {

		public final String title1;// 标题
		public final String imageFilePath1;// 封面路径 
		public final String bookFilePath1;
		public final String title2;
		public final String imageFilePath2;
		public final String bookFilePath2;
		public final String title3;
		public final String imageFilePath3;
		public final String bookFilePath3;
		public final String title4;
		public final String imageFilePath4;
		public final String bookFilePath4;
		public final String title5;
		public final String imageFilePath5;
		public final String bookFilePath5;
		public final String title6;
		public final String imageFilePath6;
		public final String bookFilePath6;

		public Data(String title1, String imageFilePath1,
				String bookFilePath1, String title2, String imageFilePath2,
				String bookFilePath2, String title3, String imageFilePath3,
				String bookFilePath3, String title4, String imageFilePath4,
				String bookFilePath4, String title5, String imageFilePath5,
				String bookFilePath5, String title6, String imageFilePath6,
				String bookFilePath6) {
			super();
			this.title1 = title1;
			this.imageFilePath1 = imageFilePath1;
			this.bookFilePath1 = bookFilePath1;
			this.title2 = title2;
			this.imageFilePath2 = imageFilePath2;
			this.bookFilePath2 = bookFilePath2;
			this.title3 = title3;
			this.imageFilePath3 = imageFilePath3;
			this.bookFilePath3 = bookFilePath3;
			this.title4 = title4;
			this.imageFilePath4 = imageFilePath4;
			this.bookFilePath4 = bookFilePath4;
			this.title5 = title5;
			this.imageFilePath5 = imageFilePath5;
			this.bookFilePath5 = bookFilePath5;
			this.title6 = title6;
			this.imageFilePath6 = imageFilePath6;
			this.bookFilePath6 = bookFilePath6;
		}

	}
}
