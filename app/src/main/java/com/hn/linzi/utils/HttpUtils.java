package com.hn.linzi.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class HttpUtils {
	static String end = "\r\n";
	static String twoHyphens = "--";
	static String boundary = "*****";

	public static InputStream getStreamFromURL(String imageURL) {
		InputStream in = null;
		try {
			URL url = new URL(imageURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			in = connection.getInputStream();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;

	}

	public static String uploadPic(String urlpath, String uploadpath) {

		File file = new File(uploadpath);
		if (file.exists()) {
			try {
				URL url = new URL(urlpath);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				/* 允许Input、Output，不使用Cache */
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false);
				/* 设置传送的method=POST */
				con.setRequestMethod("POST");
				/* setRequestProperty */
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setRequestProperty("Charset", "UTF-8");
				con.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				/* 设置DataOutputStream */
				DataOutputStream ds = new DataOutputStream(
						con.getOutputStream());
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("Content-Disposition: form-data;name=\"file\";filename=\""
						+ "upload.jpg" + "\"" + end);
				ds.writeBytes(end);
				/* 取得文件的FileInputStream */
				FileInputStream fStream = new FileInputStream(uploadpath);
				/* 设置每次写入1024bytes */
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				int length = -1;
				/* 从文件读取数据至缓冲区 */
				while ((length = fStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream中 */
					ds.write(buffer, 0, length);
				}
				ds.writeBytes(end);
				ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
				/* close streams */
				fStream.close();
				ds.flush();
				/* 取得Response内容 */
				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				/* 将Response显示于Dialog */
				System.out.println("上传成功");
				/* 关闭DataOutputStream */
				ds.close();
				return b.toString();
			}

			catch (Exception e) {
				System.out.println("上传失败");
			}

		}
		return null;
	}

	// public static String uploadPic(String urlpath,
	// HashMap<String, String> paramString,
	// HashMap<String, File> paramFiles) {
	// // String urlpath = getString(R.string.url_neiwang) +
	// // getString(R.string.url_huodong_uploadpic) + "accesstoken=" +
	// // sp.getString("Token", "");
	// try {
	// System.out.println(urlpath);
	// URL url = new URL(urlpath);
	// HttpURLConnection con = (HttpURLConnection) url.openConnection();
	// /* 允许Input、Output，不使用Cache */
	// con.setDoInput(true);
	// con.setDoOutput(true);
	// con.setUseCaches(false);
	// /* 设置传送的method=POST */
	// con.setRequestMethod("POST");
	// /* setRequestProperty */
	// con.setRequestProperty("Connection", "Keep-Alive");
	// con.setRequestProperty("Charset", "UTF-8");
	// con.setRequestProperty("Content-Type",
	// "multipart/form-data;boundary=" + boundary);
	// /* 设置DataOutputStream */
	// DataOutputStream ds = new DataOutputStream(con.getOutputStream());
	//
	// /* 把普通参数写到服务器 */
	// // if (null != paramString && paramString.size() > 0) {
	// // writeStringParam(ds, paramString);
	// // }
	// /* 把文件参数写到服务器 */
	// if (null != paramFiles && paramFiles.size() > 0) {
	// writeFileParam(ds, paramFiles);
	// }
	// ds.writeBytes(end);
	// ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
	//
	// ds.flush();
	// /* 取得Response内容 */
	//
	//
	// System.out.println("ResponseCode=" + con.getResponseCode());
	// InputStream is = con.getInputStream();
	//
	// int ch;
	// StringBuffer b = new StringBuffer();
	// while ((ch = is.read()) != -1) {
	// b.append((char) ch);
	// }
	// System.out.println("上传成功");
	// /*
	// * // 关闭DataOutputStream
	// */
	// ds.close();
	// return b.toString();
	// } catch (Exception e) {
	// e.printStackTrace();
	// System.out.println("上传失败");
	// }
	// return null;
	// }

	/**
	 * 把字节输入流转换成字符串
	 * 
	 * @return
	 */
	private String InputStremtoString(InputStream is) throws Exception {
		int ch;
		StringBuffer b = new StringBuffer();
		byte[] bt = new byte[1024];
		while ((ch = is.read(bt)) != -1) {
			b.append(new String(bt, 0, ch, "UTF-8"));
		}

		return b.toString();
	}

	private static void writeFileParam(DataOutputStream ds,
			HashMap<String, File> paramFiles) throws Exception {
		// TODO Auto-generated method stub
		Set<String> keySet = paramFiles.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			File value = paramFiles.get(name);
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data;name=\"file\";filename=\""
					+ "uploadpic.jpg" + "\"" + end);

			// ds.writeBytes("Content-Disposition: form-data; name=\"" + name
			// + "\"; filename=\"" + URLEncoder.encode(filename) + "\""
			// + end);
			ds.writeBytes("Content-Type: " + getContentType(value) + end);
			ds.writeBytes(end);
			ds.write(getBytes(value));
			ds.writeBytes(end);
		}

	}

	private static void writeStringParam(DataOutputStream ds,
			HashMap<String, String> paramString) throws Exception {
		// TODO Auto-generated method stub
		Set<String> keySet = paramString.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String value = paramString.get(name);
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name
					+ "\"" + end);
			ds.writeBytes(end);
			ds.writeBytes(URLEncoder.encode(value) + end);
		}
	}

	// 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
	private static String getContentType(File f) throws Exception {
		return "image/jpg";
	}

	// 把文件转换成字节数组
	private static byte[] getBytes(File f) throws Exception {
		FileInputStream in = new FileInputStream(f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int n;
		while ((n = in.read(b)) != -1) {
			out.write(b, 0, n);
		}
		in.close();
		return out.toByteArray();
	}
}
