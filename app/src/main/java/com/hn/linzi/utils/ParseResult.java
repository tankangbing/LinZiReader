package com.hn.linzi.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hn.linzi.activity.BookXiangxiActivity;
import com.hn.linzi.activity.KeChengXiangxiActivity;
import com.hn.linzi.activity.NetActivity;
import com.hn.linzi.activity.ZiLiaoJiaActivity;
import com.hn.linzi.activity.ZiLiaoJiaManageActivity;
import com.hn.linzi.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class ParseResult {
	private String result;
	private String[] erweima;
	private SharedPreferences sp;
	private TelephonyManager tm;
	private Context context;
	private String token;
	private String[] newS;
	private String ActivityCode;

	boolean canceled = false;
	String path = "";
	
	
	
	public final static int GetToken = 111;
	public final static int GetActivityCode = 222;

	public ParseResult(String result, Context context) {
		super();
		this.result = result;
		this.context = context;
		this.sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE);
		this.tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if(MyTools.getExtSDCardPaths().size() > 0){
			path = MyTools.getExtSDCardPaths().get(0) + "/Android/data/com.wr.reader2/files/BOOKS/";
		}else {
			path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/Android/data/com.wr.reader2/files/BOOKS/";
		}
	}

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GetToken:
				if (newS[0].equals("CHECKIN")) {
					checkin();
				} else if (newS[0].equals("SIGNUP")) {
					signup();
				}
				break;
			case GetActivityCode:
				if (ActivityCode.equals("1000")) {
					if (newS[0].equals("CHECKIN")) {
						Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT)
								.show();
					} else if (newS[0].equals("SIGNUP")) {
						Toast.makeText(context, "报名成功", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		};
	};

	public void parse() throws Exception {
		System.out.println(result);
		// String newResult = new String(Base64.decode(result, Base64.DEFAULT));
		erweima = result.split("\\|");
		System.out.println(erweima[0]);
		if (erweima[0].indexOf("BOOK") > -1) {
			// 图书二维码
			System.out.println("parseBOOK");
			String code = erweima[1];
			SharedPreferences sp = context.getSharedPreferences("SP",
					context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("code", code);
			editor.commit();
			System.out.println("解析图书1");
			parseBOOK();
		} else if (erweima[0].indexOf("VIDEO") > -1) {
			// 课程二维码
			String code = erweima[1];
			SharedPreferences sp = context.getSharedPreferences("SP",
					context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("code", code);
			editor.commit();
			parseKECHENG();
		} else if (erweima[0].indexOf("LIBRARY") > -1) {
			// 资料夹？
			String code = erweima[1];
			SharedPreferences sp = context.getSharedPreferences("SP",
					context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("code", code);
			editor.commit();
			parseZILIAOJIA();
		} else if (erweima[0].indexOf("CULTURE") > -1) {
			parseCulture();
		} else if (erweima[0]
				.equals("ttp://data.iego.net:85/help/getapp.aspx?")) {
			String code = erweima[1];
			SharedPreferences sp = context.getSharedPreferences("SP",
					context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("code", code);
			editor.commit();
			parseHuoDong();
		} else if (erweima[0].indexOf("type=regist") > -1) {
			// 活动报名
			Dialog dialog = new AlertDialog.Builder(context)
					.setTitle("报名")
					.setMessage("确定报名该活动？")
					.setPositiveButton(R.string.dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									registHuoDng();
								}
							})// 设置确定按钮
					.setNegativeButton(R.string.dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							})// 设置取消按钮
					.create();
			dialog.show();
		} else if (erweima[0].indexOf("type=sign") > -1) {
			// 活动签到
			signHuoDong();
		} else if (erweima[0].indexOf("jobMember.ashx?") > -1) {
			// 职场关注
			focusZhiChang();
		}  else {

		}
	}

	private void focusZhiChang() {
		// TODO Auto-generated method stub
		String url = erweima[0] + "&n=" + sp.getString("UserName", "") + "&d="
				+ sp.getString("KEY", "");
		RequestQueue queue = Volley.newRequestQueue(context);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				if (response.indexOf("成功") > -1) {
					Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "收藏失败", Toast.LENGTH_SHORT).show();
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "请求出错，请重试", Toast.LENGTH_SHORT).show();
			}
		});
		queue.add(request);
	}

	private void signHuoDong() {
		// TODO Auto-generated method stub
		String url = erweima[0] + "&n=" + sp.getString("UserName", "") + "&d="
				+ sp.getString("KEY", "");
		RequestQueue queue = Volley.newRequestQueue(context);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				if (response.indexOf("sign成功") > -1) {
					Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "签到失败", Toast.LENGTH_SHORT).show();
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "请求出错，请重试", Toast.LENGTH_SHORT).show();
			}
		});
		queue.add(request);
	}

	private void registHuoDng() {
		// TODO Auto-generated method stub
		String url = erweima[0] + "&n=" + sp.getString("UserName", "") + "&d="
				+ sp.getString("KEY", "");
		RequestQueue queue = Volley.newRequestQueue(context);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				if (response.indexOf("regist成功") > -1) {
					Toast.makeText(context, "报名成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "报名失败", Toast.LENGTH_SHORT).show();
				}
			}

		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "请求出错，请重试", Toast.LENGTH_SHORT).show();
			}
		});
		queue.add(request);
	}

	private void parseCulture() {
		// TODO Auto-generated method stub
		final String title = erweima[1];
		final String url = erweima[2];
		String s[] = url.split("\\.");
		final String filepath = path + title + "." + s[s.length-1];
		System.out.println("filepath=" + filepath);
		final ZiLiaoJiaDBHelper ziLiaoJiaDBHelper = new ZiLiaoJiaDBHelper(context);

		Cursor cursor = ziLiaoJiaDBHelper.select(new String[] { "item_id" },
				"item_name = ?", new String[] { title });
		if (cursor.moveToNext()) {
			System.out.println("已有该图书");
			Toast.makeText(context, "资料夹已有该文件", Toast.LENGTH_SHORT).show();
		} else {
			Dialog dialog = new AlertDialog.Builder(context)
					.setTitle("下载文件")
					.setMessage("是否下载该文件")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									ziLiaoJiaDBHelper.insert(title, "Culture", url, filepath, "false", "false", sp.getString("UserName", ""));
									System.out.println("增加文件");

									Intent intent = new Intent();
									intent.setClass(context, ZiLiaoJiaManageActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									context.startActivity(intent);
								}
							})// 设置确定按钮
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									
								}
							})// 设置取消按钮
					.create();
			dialog.show();
		}
	}

	private void parseHuoDong() {
		// TODO Auto-generated method stub
		if (erweima[2].indexOf("actjoin") >= 0) {
			System.out.println("11111");

			Dialog dialog = new AlertDialog.Builder(context)
					.setTitle("报名")
					.setMessage("是否报名该活动")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String url = erweima[2] + "&n="
											+ sp.getString("UserName", "")
											+ "&d=" + sp.getString("KEY", "")
											+ "&code=" + erweima[1] + "&imei="
											+ tm.getDeviceId();
									System.out.println("活动：" + url);
									Intent intent = new Intent();
									Bundle bundle = new Bundle();
									bundle.putString("url", url);
									intent.putExtras(bundle);
									intent.setClass(context, NetActivity.class);
									context.startActivity(intent);
									Toast.makeText(context, "报名成功",
											Toast.LENGTH_SHORT).show();
								}
							})// 设置确定按钮
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							})// 设置取消按钮
					.create();
			dialog.show();
			System.out.println("22222");
		} else if (erweima[2].indexOf("actsign") >= 0) {
			String url = erweima[2] + "&n=" + sp.getString("UserName", "")
					+ "&d=" + sp.getString("KEY", "") + "&code=" + erweima[1]
					+ "&imei=" + tm.getDeviceId();
			System.out.println("活动：" + url);
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			intent.putExtras(bundle);
			intent.setClass(context, NetActivity.class);
			context.startActivity(intent);
		}

	}

	private void parseZILIAOJIA() {
		String code = erweima[1];
		String bookid = erweima[2];
		String title = erweima[3];
		String url = erweima[4];
		String contentUri = "http://data.iego.net:85/interlib/detail8.aspx?"
				+ "n=" + sp.getString("UserName", "") + "&d="
				+ sp.getString("KEY", "") + "&code=" + code + "&imei="
				+ tm.getDeviceId() + "&id=" + bookid;
		// URL curl = new URL(contentURL);
		// HttpURLConnection conn = (HttpURLConnection) curl
		// .openConnection();
		// conn.setRequestMethod("GET");
		// conn.setReadTimeout(5000);
		// if (conn.getResponseCode() == 200) {
		//
		// }

		ZiLiaoJiaDBHelper ziLiaoJiaDBHelper = new ZiLiaoJiaDBHelper(context);

		Cursor cursor = ziLiaoJiaDBHelper.select(new String[] { "item_id" },
				"item_name = ?", new String[] { title });
		if (cursor.moveToNext()) {
			System.out.println("已有该图书");
			Toast.makeText(context, "资料夹已有该图书", Toast.LENGTH_SHORT).show();
		} else {
			ziLiaoJiaDBHelper.insert(title, "Library", contentUri, sp.getString("UserName", ""));
			System.out.println("增加馆藏图书");

			Intent intent = new Intent();
			intent.setClass(context, ZiLiaoJiaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
		// Uri uri = Uri.parse(contentUri);
		// Intent it = new Intent(Intent.ACTION_VIEW, uri);
		// context.startActivity(it);
	}

	private void parseKECHENG() {
		String code = erweima[1];
		String lessonid = erweima[2];
		String title = erweima[3];
		String catalog = erweima[4];
		String url = erweima[5];
		String contentUri = "http://data.iego.net:88/m/coursedetail8.aspx?"
		// + "n=" + sp.getString("UserName", "")
		// + "&d=" + sp.getString("KEY","" )
		// + "&code=" + code
		// + "&imei=" + tm.getDeviceId()
				+ "&lesson_id=" + lessonid;
		System.out.println(contentUri);
		Bundle bundle = new Bundle();
		Intent intent = new Intent();
		bundle.putString("imgurl",
				"http://data.iego.net:88/m/cover.aspx?cover=" + lessonid
						+ ".jpg");
		bundle.putString("title", title);
		bundle.putString("type", catalog);
		bundle.putString("contentUri", contentUri);
		intent.putExtras(bundle);
		intent.setClass(context, KeChengXiangxiActivity.class);
		context.startActivity(intent);

		// Uri uri = Uri.parse(contentUri);
		// Intent it = new Intent(Intent.ACTION_VIEW, uri);
		// context.startActivity(it);
	}

	private void parseBOOK() throws Exception {
		System.out.println("解析图书2");
		String code = erweima[1];
		String libbookid = erweima[2];
		String title = erweima[3];
		final String contentURL = "http://data.iego.net:85/book/infoJson8.aspx?"
				+ "n="
				+ sp.getString("UserName", "")
				+ "&d="
				+ sp.getString("KEY", "")
				+ "&imei="
				+ tm.getDeviceId()
				+ "&code=" + code + "&id=" + libbookid;
		final String id = libbookid.substring(libbookid.indexOf("*") + 1,
				libbookid.length());
		System.out.println(contentURL);

		new Thread() {
			public void run() {
				URL curl;
				try {
					curl = new URL(contentURL);
					HttpURLConnection conn = (HttpURLConnection) curl
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream();
						ParseJson parseJson = new ParseJson();
						ArrayList<String> bookData = parseJson
								.parseBookErWeiMa(inStream);
						System.out.println("startBookXiangxiActivity");
						Bundle bundle = new Bundle();
						bundle.putStringArrayList("bookData", bookData);
						bundle.putString("id", id);
						Intent intent = new Intent(context,
								BookXiangxiActivity.class);
						intent.putExtras(bundle);
						context.startActivity(intent);
						System.out.println("open");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();

	}

	private void checkin() {
		String checkinUrl = newS[1] + "?accesstoken=" + token;
		RequestQueue queue = Volley.newRequestQueue(context);
		StringRequest request = new StringRequest(Request.Method.POST,
				checkinUrl, new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);
						ParseJson parseJson = new ParseJson();
						try {
							ActivityCode = parseJson
									.parseActivityCode(response);
							handler.sendEmptyMessage(ParseResult.GetActivityCode);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
		queue.add(request);
	}

	private void signup() {
		String checkinUrl = newS[1] + "?accesstoken=" + token;
		RequestQueue queue = Volley.newRequestQueue(context);
		StringRequest request = new StringRequest(Request.Method.POST,
				checkinUrl, new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);
						ParseJson parseJson = new ParseJson();
						try {
							ActivityCode = parseJson
									.parseActivityCode(response);
							handler.sendEmptyMessage(ParseResult.GetActivityCode);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				});
		queue.add(request);
	}
	
	public boolean downloadfile(final String _url, final String _path,
			final String _name) {
		try {
			URL url = new URL(_url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
//			int length = conn.getContentLength();
			InputStream is = conn.getInputStream();

			File file = new File(_path);
			if (!file.exists()) {
				file.mkdirs();
			}
			String apkFile = _path + _name;
			File ApkFile = new File(apkFile);
			if (ApkFile.exists()) {
				ApkFile.delete();
			}
			FileOutputStream fos = new FileOutputStream(ApkFile);

			byte buf[] = new byte[1024];
			do {
				int numread = is.read(buf);

				if (numread <= 0) {

					// 下载完了，cancelled也要设置
					canceled = true;
					System.out.println("download OK");
					
					break;
				}
				fos.write(buf, 0, numread);
			} while (!canceled);// 点击取消就停止下载.
			fos.close();
			is.close();
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			canceled = true;
		} catch (IOException e) {
			
			e.printStackTrace();
			canceled = true;
		}

		return false;
	};
}
