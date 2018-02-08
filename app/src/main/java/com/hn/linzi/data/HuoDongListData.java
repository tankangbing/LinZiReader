package com.hn.linzi.data;

import java.util.ArrayList;
import java.util.List;

public class HuoDongListData {
	
	public HuoDongListData() {
	}

	public List<Data> List_Data = new ArrayList<Data>();
	
	public static final class Data {
		
		public final String title1;// 标题
		public final String cover1;// 封面路径
		public final String unit1;//单位
		public final String time1;//时间
		public final String id1;//活动ID
		public final String credit1;//学分
		public final String signupnum1;//报名人数
		public Data(String title1, String cover1, String unit1, String time1,
				String id1, String credit1, String signupnum1) {
			super();
			this.title1 = title1;
			this.cover1 = cover1;
			this.unit1 = unit1;
			this.time1 = time1;
			this.id1 = id1;
			this.credit1 = credit1;
			this.signupnum1 = signupnum1;
		}
		
		
		
	}
}
