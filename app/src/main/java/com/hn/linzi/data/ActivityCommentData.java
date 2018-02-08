package com.hn.linzi.data;

import java.util.ArrayList;
import java.util.List;

public class ActivityCommentData {
	
	public ActivityCommentData() {
	}

	public List<Data> Comment_Data = new ArrayList<Data>();
	
	public static final class Data {
		public final int total;//总数
		public final String content;//内容
		public final String image1;//图片
		public final String image2;
		public final String image3;
		public final String image4;
		public final String image5;
		public final String deleteable;//可否删除
		public final String created_at;//时间
		public final String id;//评论ID
		public final String account;//账号
		public final String name;//名字
		public Data(int total, String content, String image1, String image2,
				String image3, String image4, String image5, String deleteable,
				String created_at, String id, String account, String name) {
			super();
			this.total = total;
			this.content = content;
			this.image1 = image1;
			this.image2 = image2;
			this.image3 = image3;
			this.image4 = image4;
			this.image5 = image5;
			this.deleteable = deleteable;
			this.created_at = created_at;
			this.id = id;
			this.account = account;
			this.name = name;
		}
		
	}
}
