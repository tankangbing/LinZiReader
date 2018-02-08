package com.hn.linzi.data;

import java.util.ArrayList;
import java.util.List;

public class ShuJiaData {
	public static final List<Data> IMG_DESCRIPTIONS = new ArrayList<Data>();

	static {
		ShuJiaData.IMG_DESCRIPTIONS.add(new ShuJiaData.Data(
				"标题1","书架的图片地址", "作者1", "简介1",
				"标题2","书架的图片地址", "作者2", "简介2",
				"标题3","书架的图片地址", "作者3", "简介3"));

		ShuJiaData.IMG_DESCRIPTIONS.add(new ShuJiaData.Data(
				"标题4","书架的图片地址", "作者4", "简介4",
				"标题5","书架的图片地址", "作者5", "简介5",
				"标题6","书架的图片地址", "作者6", "简介6"));
		
		ShuJiaData.IMG_DESCRIPTIONS.add(new ShuJiaData.Data(
				"标题7","书架的图片地址", "作者7", "简介7",
				"标题8","书架的图片地址", "作者8", "简介8",
				"标题9","书架的图片地址", "作者9", "简介9"));

	}

	public static final class Data {

		public final String title1;//标题
		public final String imageFilePath1;//封面路径
		public final String author1;//作者
		public final String introduction1;//介绍
		public final String title2;
		public final String imageFilePath2;
		public final String author2;
		public final String introduction2;
		public final String title3;
		public final String imageFilePath3;
		public final String author3;
		public final String introduction3;
		
		private Data(String title1, String imageFilePath1, String author1,
				String introduction1, String title2, String imageFilePath2,
				String author2, String introduction2, String title3,
				String imageFilePath3, String author3, String introduction3) {
			super();
			this.title1 = title1;
			this.imageFilePath1 = imageFilePath1;
			this.author1 = author1;
			this.introduction1 = introduction1;
			this.title2 = title2;
			this.imageFilePath2 = imageFilePath2;
			this.author2 = author2;
			this.introduction2 = introduction2;
			this.title3 = title3;
			this.imageFilePath3 = imageFilePath3;
			this.author3 = author3;
			this.introduction3 = introduction3;
		}

	}
}
