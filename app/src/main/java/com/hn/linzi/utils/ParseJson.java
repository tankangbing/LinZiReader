package com.hn.linzi.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hn.linzi.activity.PublicClassContentActivity.UrlData;
import com.hn.linzi.data.KeChengData;
import com.hn.linzi.data.YaoYiYaoData;
import com.hn.linzi.data.KeChengData.Data;

public class ParseJson {

	public String parseLogin(InputStream inStream) throws Exception {
		String LoginResult;
		byte[] data = StreamTool.read(inStream);// 从输入流中读取数据
		String json = new String(data);// 转化为字符串，例如[{xxx,xx,x},{xxx,xx,x}]
		JSONObject jsonObject = new JSONObject(json);
		LoginResult = jsonObject.getString("status");
		return LoginResult;
	}

	public String parseChuShiHuaPassword(InputStream inStream) throws Exception {
		String Result;
		byte[] data = StreamTool.read(inStream);
		String json = new String(data);
		JSONObject jsonObject = new JSONObject(json);
		Result = jsonObject.getString("status");
		return Result;
	}

	public ArrayList<String> parseBookErWeiMa(InputStream inStream)
			throws Exception {
		ArrayList<String> bookData = new ArrayList<String>();
		byte[] data = StreamTool.read(inStream);
		String json = new String(data);
		System.out.println(json);
		JSONObject jsonObject = new JSONObject(json);

		if (jsonObject.getString("status").equals("0")) {
			if(jsonObject.has("epuburl")){
				bookData.add(jsonObject.getString("bookname"));
				bookData.add(jsonObject.getString("author"));
				bookData.add(jsonObject.getString("imgurl"));
				bookData.add(jsonObject.getString("pub"));
				bookData.add(jsonObject.getString("pubtime"));
				bookData.add(jsonObject.getString("updatetime"));
				bookData.add(jsonObject.getString("epuburl"));
				bookData.add("epub");
			}else if (jsonObject.has("pdfurl")) {
				bookData.add(jsonObject.getString("bookname"));
				bookData.add(jsonObject.getString("author"));
				bookData.add(jsonObject.getString("imgurl"));
				bookData.add(jsonObject.getString("pub"));
				bookData.add(jsonObject.getString("pubtime"));
				bookData.add(jsonObject.getString("updatetime"));
				bookData.add(jsonObject.getString("pdfurl"));
				bookData.add("pdf");
			}
		}
		return bookData;
	}

	public YaoYiYaoData parseYaoYiYao(byte[] data) throws Exception {

		String json = new String(data);
		JSONObject jsonObject = new JSONObject(json);
		YaoYiYaoData yaoData = new YaoYiYaoData();
		yaoData.IMG_DESCRIPTIONS.clear();

		if (jsonObject.getString("status").equals("0")) {
			String count = jsonObject.getString("count");
			JSONArray datas = jsonObject.getJSONArray("datas");
			for (int i = 0; i < datas.length(); i++) {
				String type = datas.getJSONObject(i).getString("type");
				String code = datas.getJSONObject(i).getString("code");
				String name = "";
				String author = "";
				String url = "";
				String id = "";
				String imgurl = "";
				String pub = "";
				String pubtime = "";
				String updatetime = "";
				String book_type = "";
				if (type.equals("BOOK")) {
					name = datas.getJSONObject(i).getString("name");
					author = datas.getJSONObject(i).getString("author");
					if(datas.getJSONObject(i).has("epuburl")){
						url = datas.getJSONObject(i).getString("epuburl");
						book_type = "epub";
					}else if (datas.getJSONObject(i).has("pdfurl")) {
						url = datas.getJSONObject(i).getString("pdfurl");
						book_type = "pdf";
					}
					id = datas.getJSONObject(i).getString("bookid");
					imgurl = datas.getJSONObject(i).getString("imgurl");
					pub = datas.getJSONObject(i).getString("pub");
					pubtime = datas.getJSONObject(i).getString("pubtime");
					updatetime = datas.getJSONObject(i).getString("updatetime");
				} else {
					name = datas.getJSONObject(i).getString("name");
					url = datas.getJSONObject(i).getString("url");
					id = datas.getJSONObject(i).getString("id");
				}
				yaoData.IMG_DESCRIPTIONS.add(new YaoYiYaoData.Data(type, name,
						url, imgurl, author, pub, updatetime, book_type, id));
			}

		}

		return yaoData;
	}

	public void parseKeChengList(InputStream inStream, List<Data> list)
			throws Exception {
		byte[] data = StreamTool.read(inStream);
//		System.out.println(new String(data,"GB2312"));
		String json = new String(data,"GB2312");
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.getString("status").equals("0")) {
			String count = jsonObject.getString("count");
			JSONArray datas = jsonObject.getJSONArray("datas");

			String title1 = "";// 标题
			String imageFilePath1 = "";// 封面路径
			String cat1 = "";// 分类W
			String url1 = "";// 课程链接
			String title2 = "";
			String imageFilePath2 = "";
			String cat2 = "";
			String url2 = "";
			String title3 = "";
			String imageFilePath3 = "";
			String cat3 = "";
			String url3 = "";
			String title4 = "";
			String imageFilePath4 = "";
			String cat4 = "";
			String url4 = "";
			String title5 = "";
			String imageFilePath5 = "";
			String cat5 = "";
			String url5 = "";
			String title6 = "";
			String imageFilePath6 = "";
			String cat6 = "";
			String url6 = "";

			for (int i = 1; i <= datas.length(); i++) {
				switch (i % 6) {
				case 1:
					title1 = datas.getJSONObject(i-1).getString("name");
					imageFilePath1 = datas.getJSONObject(i-1).getString("imgurl");
					cat1 = datas.getJSONObject(i-1).getString("cat");
					url1 = datas.getJSONObject(i-1).getString("url");
					break;
				case 2:
					title2 = datas.getJSONObject(i-1).getString("name");
					imageFilePath2 = datas.getJSONObject(i-1).getString("imgurl");
					cat2 = datas.getJSONObject(i-1).getString("cat");
					url2 = datas.getJSONObject(i-1).getString("url");
					break;
				case 3:
					title3 = datas.getJSONObject(i-1).getString("name");
					imageFilePath3 = datas.getJSONObject(i-1).getString("imgurl");
					cat3 = datas.getJSONObject(i-1).getString("cat");
					url3 = datas.getJSONObject(i-1).getString("url");
					break;
				case 4:
					title4 = datas.getJSONObject(i-1).getString("name");
					imageFilePath4 = datas.getJSONObject(i-1).getString("imgurl");
					cat4 = datas.getJSONObject(i-1).getString("cat");
					url4 = datas.getJSONObject(i-1).getString("url");
					break;
				case 5:
					title5 = datas.getJSONObject(i-1).getString("name");
					imageFilePath5 = datas.getJSONObject(i-1).getString("imgurl");
					cat5 = datas.getJSONObject(i-1).getString("cat");
					url5 = datas.getJSONObject(i-1).getString("url");
					break;
				case 0:
					title6 = datas.getJSONObject(i-1).getString("name");
					imageFilePath6 = datas.getJSONObject(i-1).getString("imgurl");
					cat6 = datas.getJSONObject(i-1).getString("cat");
					url6 = datas.getJSONObject(i-1).getString("url");
					break;
				}

				if (i % 6 == 0 || i == datas.length()) {
					list.add(new KeChengData.Data(title1, imageFilePath1, cat1,
							url1, title2, imageFilePath2, cat2, url2, title3,
							imageFilePath3, cat3, url3, title4, imageFilePath4,
							cat4, url4, title5, imageFilePath5, cat5, url5,
							title6, imageFilePath6, cat6, url6));
					title1 = "";// 标题
					imageFilePath1 = "";// 封面路径
					cat1 = "";// 分类
					url1 = "";// 课程链接
					title2 = "";
					imageFilePath2 = "";
					cat2 = "";
					url2 = "";
					title3 = "";
					imageFilePath3 = "";
					cat3 = "";
					url3 = "";
					title4 = "";
					imageFilePath4 = "";
					cat4 = "";
					url4 = "";
					title5 = "";
					imageFilePath5 = "";
					cat5 = "";
					url5 = "";
					title6 = "";
					imageFilePath6 = "";
					cat6 = "";
					url6 = "";
				}
			}
		}
		System.out.println("解析完成");
	}

	public void parseXiaoxiList(InputStream inStream, List<com.hn.linzi.data.XiaoXiData.Data> list) throws Exception {
		byte[] data = StreamTool.read(inStream);
		String json = new String(data,"GB2312");
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.getString("status").equals("0")) {
			String count = jsonObject.getString("count");
			JSONArray datas = jsonObject.getJSONArray("datas");
			for (int i = 0; i < datas.length(); i++) {
				System.out.println("i================" + i);
				String id = datas.getJSONObject(i).getString("id");
				String parentId = datas.getJSONObject(i).getString("parentId");
				String send = datas.getJSONObject(i).getString("send");
				String to = datas.getJSONObject(i).getString("to");
				String type = datas.getJSONObject(i).getString("type");
				String isread = datas.getJSONObject(i).getString("isread");
				String title = datas.getJSONObject(i).getString("title");
				String dt = datas.getJSONObject(i).getString("dt");
				
				list.add(new com.hn.linzi.data.XiaoXiData.Data(id, parentId, send, to, type, isread, title, dt));
			}
		}
	}

	public String parseXiaoxiContent(InputStream inStream) throws Exception {
		byte[] data = StreamTool.read(inStream);
		String json = new String(data,"GB2312");
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.getString("status").equals("0")) {
			return jsonObject.getString("content");
		}
		return "";
	}

	public ArrayList<HashMap<String, Object>> parseClassicBooks(InputStream inStream) throws Exception {
		ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();
		byte[] data = StreamTool.read(inStream);
		String json = new String(data);
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("datas");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ItemName", "全部");
		item.add(map);  
		for (int i = 0; i < jsonArray.length(); i++) {
			String s = jsonArray.getJSONObject(i).getString("class");
			map = new HashMap<String, Object>();
			map.put("ItemName", s);
			item.add(map);  
		}
		return item;
	}

	public void parseClassicBookList(InputStream inStream,
			List<com.hn.linzi.data.ClassicBookListData.Data> iMG_DESCRIPTIONS) throws Exception {
		byte[] data = StreamTool.read(inStream);
		String json = new String(data);
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.getString("status").equals("0")) {
			JSONArray jsonArray = jsonObject.getJSONArray("datas");
			System.out.println(jsonArray.length());
			List<String> title = new ArrayList<String>();
			List<String> imgurl = new ArrayList<String>();
			List<String> bookid = new ArrayList<String>();
			List<String> author = new ArrayList<String>();
			int num = 0;

			for (int i = 0; i < jsonArray.length(); i++) {
				num++;
				title.add(jsonArray.getJSONObject(i).getString("bookname"));
				imgurl.add(jsonArray.getJSONObject(i).getString("imgurl"));
				author.add(jsonArray.getJSONObject(i).getString("author"));
				bookid.add(jsonArray.getJSONObject(i).getString("bookid"));
				
				if(i+1 == jsonArray.length()){
					for (int j = num; j <= 6; j++) {
						title.add("");
						imgurl.add("");
						author.add("");
						bookid.add("");
					}
					iMG_DESCRIPTIONS.add(new com.hn.linzi.data.ClassicBookListData.Data(
							title.get(0), imgurl.get(0), bookid.get(0), author.get(0),
							title.get(1), imgurl.get(1), bookid.get(1), author.get(1),
							title.get(2), imgurl.get(2), bookid.get(2), author.get(2),
							title.get(3), imgurl.get(3), bookid.get(3), author.get(3),
							title.get(4), imgurl.get(4), bookid.get(4), author.get(4),
							title.get(5), imgurl.get(5), bookid.get(5), author.get(5)));
					title.clear();
					imgurl.clear();
					bookid.clear();
					author.clear();
					num = 0;
				}
				
				if(num == 6){
					iMG_DESCRIPTIONS.add(new com.hn.linzi.data.ClassicBookListData.Data(
							title.get(0), imgurl.get(0), bookid.get(0), author.get(0),
							title.get(1), imgurl.get(1), bookid.get(1), author.get(1),
							title.get(2), imgurl.get(2), bookid.get(2), author.get(2),
							title.get(3), imgurl.get(3), bookid.get(3), author.get(3),
							title.get(4), imgurl.get(4), bookid.get(4), author.get(4),
							title.get(5), imgurl.get(5), bookid.get(5), author.get(5)));
					title.clear();
					imgurl.clear();
					bookid.clear();
					author.clear();
					num = 0;
				}
			}
		}
	}

	public ArrayList<String> parseClassicBookContent(String response) throws Exception {
		ArrayList<String> data = new ArrayList<String>();
		
		JSONObject jsonObject = new JSONObject(response);
		System.out.println("pdfurl?" + jsonObject.has("pdfurl"));
		System.out.println("epuburl?" + jsonObject.has("epuburl"));
		if (jsonObject.getString("status").equals("0")) {
			if(jsonObject.has("epuburl")){
				data.add(jsonObject.getString("imgurl"));
				data.add(jsonObject.getString("bookname"));
				data.add(jsonObject.getString("author"));
				data.add(jsonObject.getString("pubtime"));
				data.add(jsonObject.getString("pub"));
				data.add(jsonObject.getString("intro"));
				data.add(jsonObject.getString("dir"));
				data.add(jsonObject.getString("epuburl"));
				data.add("epub");
				data.add(jsonObject.getString("bookid"));
			}else if(jsonObject.has("pdfurl")){
				data.add(jsonObject.getString("imgurl"));
				data.add(jsonObject.getString("bookname"));
				data.add(jsonObject.getString("author"));
				data.add(jsonObject.getString("pubtime"));
				data.add(jsonObject.getString("pub"));
				data.add(jsonObject.getString("intro"));
				data.add(jsonObject.getString("dir"));
				data.add(jsonObject.getString("pdfurl"));
				data.add("pdf");
				data.add(jsonObject.getString("bookid"));
			} 
		}
		
		return data;
	}

	public void parseNewBooksList(InputStream inStream,
			List<com.hn.linzi.data.NewBooksListData.Data> iMG_DESCRIPTIONS) throws Exception {
		byte[] data = StreamTool.read(inStream);
		String json = new String(data);
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.getString("status").equals("0")) {
			JSONArray jsonArray = jsonObject.getJSONArray("datas");
			System.out.println(jsonArray.length());
			List<String> title = new ArrayList<String>();
			List<String> imgurl = new ArrayList<String>();
			List<String> bookid = new ArrayList<String>();
			List<String> author = new ArrayList<String>();
			int num = 0;

			for (int i = 0; i < jsonArray.length(); i++) {
				num++;
				title.add(jsonArray.getJSONObject(i).getString("bookname"));
				imgurl.add(jsonArray.getJSONObject(i).getString("imgurl"));
				author.add(jsonArray.getJSONObject(i).getString("author"));
				bookid.add(jsonArray.getJSONObject(i).getString("bookid"));
				
				if(i+1 == jsonArray.length()){
					for (int j = num; j <= 6; j++) {
						title.add("");
						imgurl.add("");
						author.add("");
						bookid.add("");
					}
					iMG_DESCRIPTIONS.add(new com.hn.linzi.data.NewBooksListData.Data(
							title.get(0), imgurl.get(0), bookid.get(0), author.get(0),
							title.get(1), imgurl.get(1), bookid.get(1), author.get(1),
							title.get(2), imgurl.get(2), bookid.get(2), author.get(2),
							title.get(3), imgurl.get(3), bookid.get(3), author.get(3),
							title.get(4), imgurl.get(4), bookid.get(4), author.get(4),
							title.get(5), imgurl.get(5), bookid.get(5), author.get(5)));
					title.clear();
					imgurl.clear();
					bookid.clear();
					author.clear();
					num = 0;
				}
				
				if(num == 6){
					iMG_DESCRIPTIONS.add(new com.hn.linzi.data.NewBooksListData.Data(
							title.get(0), imgurl.get(0), bookid.get(0), author.get(0),
							title.get(1), imgurl.get(1), bookid.get(1), author.get(1),
							title.get(2), imgurl.get(2), bookid.get(2), author.get(2),
							title.get(3), imgurl.get(3), bookid.get(3), author.get(3),
							title.get(4), imgurl.get(4), bookid.get(4), author.get(4),
							title.get(5), imgurl.get(5), bookid.get(5), author.get(5)));
					title.clear();
					imgurl.clear();
					bookid.clear();
					author.clear();
					num = 0;
				}
			}
		}
	}
	
	public ArrayList<HashMap<String, Object>> parsePublicClass(InputStream inStream) throws Exception {
		ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();
		byte[] data = StreamTool.read(inStream);
		String json = new String(data, "GB2312");
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("datas");
		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("ItemName", "全部");
//		item.add(map);  
		for (int i = 0; i < jsonArray.length(); i++) {
			String s = jsonArray.getJSONObject(i).getString("class");
			map = new HashMap<String, Object>();
			map.put("ItemName", s);
			item.add(map);  
		}
		return item;
	}

	public void parsePublicClassList(InputStream inStream,
			List<com.hn.linzi.data.PublicClassListData.Data> iMG_DESCRIPTIONS) throws Exception {
		byte[] data = StreamTool.read(inStream);
		String json = new String(data, "GB2312");
		JSONObject jsonObject = new JSONObject(json);
		if (jsonObject.getString("status").equals("0")) {
			JSONArray jsonArray = jsonObject.getJSONArray("datas");
			System.out.println(jsonArray.length());
			List<String> title = new ArrayList<String>();
			List<String> imgurl = new ArrayList<String>();
			List<String> id = new ArrayList<String>();
			List<String> author = new ArrayList<String>();
			List<String> cat = new ArrayList<String>();
			int num = 0;

			for (int i = 0; i < jsonArray.length(); i++) {
				num++;
				title.add(jsonArray.getJSONObject(i).getString("name"));
				imgurl.add(jsonArray.getJSONObject(i).getString("imgurl"));
				author.add(jsonArray.getJSONObject(i).getString("chief"));
				id.add(jsonArray.getJSONObject(i).getString("id"));
				cat.add(jsonArray.getJSONObject(i).getString("cat"));
				
				if(i+1 == jsonArray.length()){
					for (int j = num; j <= 6; j++) {
						title.add("");
						imgurl.add("");
						author.add("");
						id.add("");
						cat.add("");
					}
					iMG_DESCRIPTIONS.add(new com.hn.linzi.data.PublicClassListData.Data(
							title.get(0), imgurl.get(0), id.get(0), author.get(0), cat.get(0),
							title.get(1), imgurl.get(1), id.get(1), author.get(1), cat.get(1),
							title.get(2), imgurl.get(2), id.get(2), author.get(2), cat.get(2),
							title.get(3), imgurl.get(3), id.get(3), author.get(3), cat.get(3),
							title.get(4), imgurl.get(4), id.get(4), author.get(4), cat.get(4),
							title.get(5), imgurl.get(5), id.get(5), author.get(5), cat.get(5)));
					title.clear();
					imgurl.clear();
					id.clear();
					author.clear();
					cat.clear();
					num = 0;
				}
				
				if(num == 6){
					iMG_DESCRIPTIONS.add(new com.hn.linzi.data.PublicClassListData.Data(
							title.get(0), imgurl.get(0), id.get(0), author.get(0), cat.get(0),
							title.get(1), imgurl.get(1), id.get(1), author.get(1), cat.get(1),
							title.get(2), imgurl.get(2), id.get(2), author.get(2), cat.get(2),
							title.get(3), imgurl.get(3), id.get(3), author.get(3), cat.get(3),
							title.get(4), imgurl.get(4), id.get(4), author.get(4), cat.get(4),
							title.get(5), imgurl.get(5), id.get(5), author.get(5), cat.get(5)));
					title.clear();
					imgurl.clear();
					id.clear();
					author.clear();
					cat.clear();
					num = 0;
				}
			}
		}
	}

	public ArrayList<String> parseKeChengContent(String response) throws Exception {
		ArrayList<String> data = new ArrayList<String>();
		JSONObject jsonObject = new JSONObject(response);
		if (jsonObject.getString("status").equals("0")) {
			data.add(jsonObject.getString("imgurl"));
			data.add(jsonObject.getString("name"));
			data.add(jsonObject.getString("chief"));
			data.add(jsonObject.getString("cat"));			
			data.add(jsonObject.getJSONArray("chapters").getJSONObject(0).getString("name"));
			data.add(jsonObject.getString("id"));
		}
		return data;
	}

	public ArrayList<String> parseKechengFather(String response) throws Exception {
		ArrayList<String> arrayList = new ArrayList<String>();
		JSONObject jsonObject = new JSONObject(response);
		if (jsonObject.getString("status").equals("0")) {
			JSONArray jsonArray = jsonObject.getJSONArray("chapters");
			for (int i = 0; i < jsonArray.length(); i++) {
				arrayList.add(jsonArray.getJSONObject(i).getString("name"));
			}
		}
		return arrayList;
	}
	
	public List<UrlData> parseKechengChild(String response, int num) throws Exception {
		List<UrlData> arrayList = new ArrayList<UrlData>() ;
		JSONObject jsonObject = new JSONObject(response);
		if (jsonObject.getString("status").equals("0")) {
			JSONArray jsonArray = jsonObject.getJSONArray("chapters").getJSONObject(num).getJSONArray("datas");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				UrlData urlData = new UrlData(object.getString("name"), object.getString("url"));
				arrayList.add(urlData);
			}
		}
		return arrayList;
	}
	
	public ArrayList<String> parseHuoDongToken(String response) throws Exception{
		ArrayList<String> arrayList = new ArrayList<String>();
		JSONObject jsonObject = new JSONObject(response);
		arrayList.add(jsonObject.getString("token"));
		arrayList.add(jsonObject.getString("expires"));
		arrayList.add(jsonObject.getString("rank"));
		return arrayList;
	}
	
	public void parseHuoDongList(String response, List<com.hn.linzi.data.HuoDongListData.Data> listdata) throws Exception{
		JSONObject object = new JSONObject(response);
		JSONArray jsonArray = object.getJSONArray("data");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			listdata.add(new com.hn.linzi.data.HuoDongListData.Data(jsonObject.getString("title"),
					jsonObject.getString("cover"), jsonObject.getString("unit"),
					jsonObject.getString("dtBegin"), jsonObject.getString("id"),
					jsonObject.getString("credit"), jsonObject.getString("registCount")));
		}
	}
	
	public void parseHuoDongDetail(String response, Map<String, String> data, String user) throws Exception {
		JSONObject jsonObject = new JSONObject(response);
		String rateflag = "false";
		String signflag = "false";
		String registflag = "false";
		String focusflag = "false";
		data.put("id", jsonObject.getString("id"));
		data.put("unit", jsonObject.getString("unit"));
		data.put("class1", jsonObject.getString("class1"));
		data.put("n", jsonObject.getString("n"));
		data.put("userName", jsonObject.getString("userName"));
		data.put("realName", jsonObject.getString("realName"));
		data.put("registImg", jsonObject.getString("registImg"));
		data.put("signImg", jsonObject.getString("signImg"));
		data.put("title", jsonObject.getString("title"));
		data.put("cover", jsonObject.getString("cover"));
		data.put("grade", jsonObject.getString("grade"));
		data.put("credit", jsonObject.getString("credit"));
		data.put("limit", jsonObject.getString("limit"));
		data.put("dtBegin", jsonObject.getString("dtBegin"));
		data.put("dtEnd", jsonObject.getString("dtEnd"));
		data.put("dtEnrollEnd", jsonObject.getString("dtEnrollEnd"));
		data.put("location", jsonObject.getString("location"));
		data.put("intro", jsonObject.getString("intro"));
		JSONArray focuslist = jsonObject.getJSONArray("focus");
		for (int i = 0; i < focuslist.length(); i++) {
			if (focuslist.getString(i).equals(user)) {
				focusflag = "true";
				break;
			}
		}
		JSONArray registerlist = jsonObject.getJSONArray("register");
		for (int i = 0; i < registerlist.length(); i++) {
			if (registerlist.getString(i).equals(user)) {
				registflag = "true";
				break;
			}
		}
		data.put("registerNum", registerlist.length()+ "");
		JSONArray rateList = jsonObject.getJSONArray("rateList");
		int rate1 = 0,rate2 = 0,rate3 = 0;
		for (int i = 0; i < rateList.length(); i++) {
			JSONObject rate = rateList.getJSONObject(i);
			if (rate.getString("rater").equals(user)) {
				rateflag = "true";
			}
			if(rate.getString("score").equals("10")){
				rate1++;
			}else if(rate.getString("score").equals("0")){
				rate2++;
			}else if(rate.getString("score").equals("-10")){
				rate3++;
			}
		}
		data.put("rate1", rate1 + "");//好评
		data.put("rate2", rate2 + "");//中评
		data.put("rate3", rate3 + "");//差评
		JSONArray signerList = jsonObject.getJSONArray("signer");
		for (int i = 0; i < signerList.length(); i++) {
			if (signerList.getString(i).equals(user)) {
				signflag = "true";
				break;
			}
		}
		data.put("registflag", registflag);//是否报名
		data.put("signflag", signflag);//是否签到
		data.put("rateflag", rateflag);//是否评价
		data.put("focusflag", focusflag);//是否关注
	}
	
	public String parseActivityCode(String response) throws Exception {
		JSONObject jsonObject = new JSONObject(response);
		return jsonObject.getString("code");
	}
	
	public void parseCommentList(String response, List<com.hn.linzi.data.ActivityCommentData.Data> listdata) throws Exception{
		JSONObject comment = new JSONObject(response);
		int total = comment.getInt("count");
		JSONArray jsonArray = comment.getJSONArray("data");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String content = jsonObject.getString("content");
			String image1 = "",image2 = "",image3 = "",image4 = "",image5 = "";
			if(jsonObject.getJSONArray("imageList").length() != 0){
				JSONArray images = jsonObject.getJSONArray("imageList");
				for (int j = 0; j < images.length(); j++) {
					switch (j) {
					case 0:
						image1 = images.getString(j);
						break;
					case 1:
						image2 = images.getString(j);
						break;
					case 2:
						image3 = images.getString(j);
						break;
					case 3:
						image4 = images.getString(j);
						break;
					case 4:
						image5 = images.getString(j);
						break;
					}
				}
			}
			String deleteable = "";
			String created_at = jsonObject.getString("dt");
			String id = jsonObject.getString("id");
//			JSONObject author = jsonObject.getJSONObject("userName");
			String account = jsonObject.getString("n");
			String name = jsonObject.getString("userName");
			listdata.add(new com.hn.linzi.data.ActivityCommentData.Data(
					total, content, image1, image2, image3, image4, image5, deleteable, created_at, id, account, name));
		}
	}
	
	public String parseOnUpLoad(String result) throws Exception{
		JSONObject url = new JSONObject(result);
		return url.getString("url");
	}

	public void parseZhiChangList(String response,
			List<com.hn.linzi.data.ZhiChangListData.Data> list_Data) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject(response);
		JSONArray jsonArray = jsonObject.getJSONArray("data");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject data = jsonArray.getJSONObject(i);
			list_Data.add(new com.hn.linzi.data.ZhiChangListData.Data(data.getString("position"),
					data.getString("company"), data.getString("cover"),
					data.getString("limit"), data.getString("dt"),
					data.getString("pay"), data.getString("id")));
		}
	}

	public void parseZhiChangDetail(String response,
			Map<String, String> detailsData, String user) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject(response);
		String focusflag = "false";
		detailsData.put("id", jsonObject.getString("id"));
		detailsData.put("unit", jsonObject.getString("unit"));
		detailsData.put("userName", jsonObject.getString("userName"));
		detailsData.put("realName", jsonObject.getString("realName"));
		detailsData.put("position", jsonObject.getString("position"));
		detailsData.put("cover", jsonObject.getString("cover"));
		detailsData.put("type", jsonObject.getString("type"));
		detailsData.put("company", jsonObject.getString("company"));
		detailsData.put("pay", jsonObject.getString("pay"));
		detailsData.put("limit", jsonObject.getString("limit"));
		detailsData.put("intro", jsonObject.getString("intro"));
		detailsData.put("focusImg", jsonObject.getString("focusImg"));
		detailsData.put("dt", jsonObject.getString("dt"));
		JSONArray jsonArray = jsonObject.getJSONArray("focus");
		for (int i = 0; i < jsonArray.length(); i++) {
			if (user.equals(jsonArray.getString(i))) {
				focusflag = "true";
				break;
			}
		}
		detailsData.put("focusflag", focusflag);
	}
}
