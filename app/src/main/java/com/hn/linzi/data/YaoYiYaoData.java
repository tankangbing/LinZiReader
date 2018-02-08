package com.hn.linzi.data;

import java.util.ArrayList;
import java.util.List;



public class YaoYiYaoData {
	public static final List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();
	
	
//	static {
//		YaoYiYaoData.IMG_DESCRIPTIONS.add(new YaoYiYaoData.Data(
//				"","书架的图片地址", "作者1", "简介1"));
//
//		
//
//	}
	
	
	public static final class Data {

		public final String type;//类型
		public final String name;//名字
		public final String url;//下载链接
		public final String imgurl;//图书封面
		public final String author;//更新时间
		public final String pub;
		public final String updatetime;
		public final String book_type;
		public final String book_id;
		public Data(String type, String name, String url, String imgurl,
				String author, String pub, String updatetime, String book_type,
				String book_id) {
			super();
			this.type = type;
			this.name = name;
			this.url = url;
			this.imgurl = imgurl;
			this.author = author;
			this.pub = pub;
			this.updatetime = updatetime;
			this.book_type = book_type;
			this.book_id = book_id;
		}
	}
}
