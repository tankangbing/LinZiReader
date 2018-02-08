package com.hn.linzi.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.hn.linzi.adapter.ActivityCommentAdapter;
import com.hn.linzi.data.ActivityCommentData;
import com.hn.linzi.utils.ParseJson;
import com.hn.linzi.utils.PhotoUtil;
import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.views.XListView;
import com.hn.linzi.views.XListView.IXListViewListener;
import com.hn.linzi.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HuoDongDetailsActivity extends BaseActivity {
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private String photoPath;
	private String srcPath;
	private String uploadPicUrl;
	private String responseurl;
	private boolean focusflag;
	private Context context;

	// private String token;
	private String ActivityCode;// 活动返回码
	private SharedPreferences sp;
	private String url;
	private Map<String, String> DetailsData;
	private ActivityCommentData commentData;
	private ActivityCommentAdapter commentAdapter;
	private int page = 1;

	private Button back;
	private ImageView xuefenbg;
	private ImageView fengmian;
	private TextView huodong_name;
	private TextView huodong_author;
	private TextView huodong_credit;
	private TextView huodong_peopleCount;
	private TextView huodong_signup;
	private TextView huodong_tag1;
	private TextView begintime;
	private TextView endtime;
	private ImageButton huodong_signin;
	private ImageButton huodong_shoucang;
	private TextView huodong_access;
	private TextView huodong_detail;
	private ImageButton orderbutton;
	private XListView commentList;
	private EditText huodong_myComment;
	private ImageView no_comment;
	private ImageButton huodong_myCommentImg1;
	private ImageButton huodong_myCommentImg2;
	private ImageButton huodong_myCommentImg3;
	private ImageButton huodong_myCommentImg4;
	private ImageButton huodong_myCommentImg5;
	private ImageButton[] myCommentImgViews;// 评论imageview 数组
	private ImageButton huodong_sendComment;
	private ArrayList<String> commentImgPath;// 压缩后的地址
	private ArrayList<String> commentImgDatas;// 相册拍照返回的地址

	private final static int SELECT_PICTURE = 1;
	private final static int SELECT_CAMER = 2;

	public final static int PIC_BROWER = 11;

	public final static int GetData = 111;
	public final static int GetComment = 222;
	public final static int GetMoreComment = 333;
	public final static int REFRESHComment = 444;
	public final static int OnSendFail = 555;
	public final static int GetUpLoadResult = 666;
	public final static int OnSendComment = 777;
	public final static int OnDeleteComment = 888;
	public final static int NoComment = 999;

	private String commentState = "new";

	public String getCommentState() {
		return commentState;
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HuoDongDetailsActivity.GetData:
				setDetailsData();
				getCommentData();
				break;
			case HuoDongDetailsActivity.GetComment:
				setCommentDate();
				// findViewById(R.id.huodong_detail_scrollview).scrollTo(0, 0);
				break;
			case HuoDongDetailsActivity.GetMoreComment:
				commentAdapter.notifyDataSetChanged();
				onLoad();
				break;
			case HuoDongDetailsActivity.REFRESHComment:
				setCommentDate();
				onLoad();
				break;
			case HuoDongDetailsActivity.OnSendFail:
//				if (ActivityCode.equals("1000")) {
//					Toast.makeText(context, "成功",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(context, "失败",
//							Toast.LENGTH_SHORT).show();
//				}
				Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
				break;
			case HuoDongDetailsActivity.GetUpLoadResult:
				onUpLoadComplete();
				break;
			case HuoDongDetailsActivity.OnSendComment:
				Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
				commentList.setVisibility(View.VISIBLE);
				no_comment.setVisibility(View.GONE);
				onSendComment();
				break;
			case HuoDongDetailsActivity.OnDeleteComment:
				OnDeleteComment();
				break;
			case HuoDongDetailsActivity.NoComment:
				commentList.setVisibility(View.GONE);
				no_comment = (ImageView) findViewById(R.id.huodong_no_comment);
				no_comment.setVisibility(View.VISIBLE);
				no_comment.setImageResource(R.drawable.no_comment);
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// setContentView(R.layout.huodongcontent);
		setContentView(R.layout.huodong_detail);
		context = this;
		commentImgPath = new ArrayList<String>();
		commentImgDatas = new ArrayList<String>();
		String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		photoPath = Environment.getExternalStorageDirectory().toString()
				+ File.separator + "ZQJYimage/" + name + ".png";
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		commentData = new ActivityCommentData();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		// token = sp.getString("Token", "");
		uploadPicUrl = getString(R.string.url_neiwang)
				+ getString(R.string.url_huodong_uploadpic) + "accesstoken="
				+ sp.getString("Token", "");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		url = bundle.getString("url");
		DetailsData = new HashMap<String, String>();
		xuefenbg = (ImageView) findViewById(R.id.xuefenbg);
		fengmian = (ImageView) findViewById(R.id.huodong_detail_fengmian);
		huodong_name = (TextView) findViewById(R.id.huodong_detail_name);
		begintime = (TextView) findViewById(R.id.huodong_detail_starttime);
		endtime = (TextView) findViewById(R.id.huodong_detail_endtime);
		huodong_author = (TextView) findViewById(R.id.huodong_detail_author);
		huodong_tag1 = (TextView) findViewById(R.id.huodong_detail_tag1);
		huodong_credit = (TextView) findViewById(R.id.huodong_detail_credit);
		huodong_peopleCount = (TextView) findViewById(R.id.huodong_detail_peopleCount);
		huodong_detail = (TextView) findViewById(R.id.huodong_detail_detail);
		huodong_signup = (TextView) findViewById(R.id.huodong_detail_signup);
		huodong_access = (TextView) findViewById(R.id.huodong_detail_access);
		huodong_shoucang = (ImageButton) findViewById(R.id.huodong_detail_shoucang);
		huodong_myComment = (EditText) findViewById(R.id.huodong_detail_myComment);
		huodong_myComment.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				v.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_UP:
					v.getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}
				return false;
			}
		});
		commentList = (XListView) findViewById(R.id.huodong_detail_commentList);
		commentList.setPullLoadEnable(true);
		commentList.setPullRefreshEnable(true);
		commentList.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				RefreshCommentData();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				GetMoreComment();
			}
		});

		orderbutton = (ImageButton) findViewById(R.id.huodong_detail_order);
		orderbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (commentState.equals("new")) {
					commentState = "old";
					orderbutton.setBackgroundResource(R.drawable.jiudaoxin);
					RefreshCommentData();
				} else {
					commentState = "new";
					orderbutton.setBackgroundResource(R.drawable.xindaojiu);
					RefreshCommentData();
				}
			}
		});
		back = (Button) findViewById(R.id.huodong_detail_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		huodong_myCommentImg1 = (ImageButton) findViewById(R.id.huodong_detail_myCommentImg1);
		huodong_myCommentImg2 = (ImageButton) findViewById(R.id.huodong_detail_myCommentImg2);
		huodong_myCommentImg3 = (ImageButton) findViewById(R.id.huodong_detail_myCommentImg3);
		huodong_myCommentImg4 = (ImageButton) findViewById(R.id.huodong_detail_myCommentImg4);
		huodong_myCommentImg5 = (ImageButton) findViewById(R.id.huodong_detail_myCommentImg5);
		myCommentImgViews = new ImageButton[] { huodong_myCommentImg1,
				huodong_myCommentImg2, huodong_myCommentImg3,
				huodong_myCommentImg4, huodong_myCommentImg5 };
		for (int i = 0; i < myCommentImgViews.length; i++) {
			myCommentImgViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// CharSequence[] items = { "相机", "本地相册" };
					// new AlertDialog.Builder(HuoDongDetailsActivity.this)
					// .setTitle("选择图片来源")
					// .setItems(items, new DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface dialog,
					// int which) {
					// if (which == SELECT_PICTURE) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "选择图片"),
							SELECT_PICTURE);
					// } else {
					// // 指定拍照
					// Intent intent = new
					// Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// // 加载路径
					// Uri uri = Uri.fromFile(new File(photoPath));
					// // 指定存储路径，这样就可以保存原图了
					// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					// // 拍照返回图片
					// startActivityForResult(intent, SELECT_CAMER);
					// }
					// }
					// }).create().show();
				}
			});
		}

		huodong_sendComment = (ImageButton) findViewById(R.id.huodong_detail_sendComment);
		huodong_sendComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (huodong_myComment.getText().toString() == null
						|| huodong_myComment.getText().toString().equals("")) {
					Toast.makeText(context, "评论内容不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					String sendCommentUrl = getString(R.string.url_huodong_sendcomment);

					OkHttpClient mOkHttpClient = new OkHttpClient();
					MultipartBuilder builder = new MultipartBuilder();
					builder.type(MultipartBuilder.FORM);
					builder.addPart(Headers.of("Content-Disposition",
							"form-data; name=\"n\""), RequestBody.create(null,
							sp.getString("UserName", "")));
					builder.addPart(Headers.of("Content-Disposition",
							"form-data; name=\"d\""), RequestBody.create(null,
							sp.getString("KEY", "")));
					builder.addPart(Headers.of("Content-Disposition",
							"form-data; name=\"actid\""), RequestBody.create(
							null, DetailsData.get("id")));
					builder.addPart(Headers.of("Content-Disposition",
							"form-data; name=\"content\""), RequestBody.create(
							null, huodong_myComment.getText().toString()));

					for (int i = 0; i < commentImgPath.size(); i++) {
						File file = new File(commentImgPath.get(i));
						RequestBody fileBody = RequestBody.create(
								MediaType.parse("application/octet-stream"),
								file);

						builder.addPart(Headers.of("Content-Disposition",
								"form-data; name=\"img" + (i + 1)
										+ "\";filename=\"upload.jpg\""),
								fileBody);
					}

					RequestBody requestBody = builder.build();

					com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
							.url(sendCommentUrl)
							.post(requestBody).build();
					huodong_sendComment.setClickable(false);
					Toast.makeText(context, "正在发送...", Toast.LENGTH_SHORT).show();
					Call call = mOkHttpClient.newCall(request);
					call.enqueue(new Callback()
					{

						@Override
						public void onFailure(com.squareup.okhttp.Request request,
								IOException e) {
							// TODO Auto-generated method stub
							handler.sendEmptyMessage(HuoDongDetailsActivity.OnSendFail);
							huodong_sendComment.setClickable(true);
						}

						@Override
						public void onResponse(Response response)
								throws IOException {
							// TODO Auto-generated method stub
							//返回OK就是成功
							if (response.message().indexOf("OK") > -1) {
//								Looper.prepare(); 
								handler.sendEmptyMessage(HuoDongDetailsActivity.OnSendComment);
//								Looper.loop();
							}else{
								handler.sendEmptyMessage(HuoDongDetailsActivity.OnSendFail);
							}
							huodong_sendComment.setClickable(true);
						}
					});

					// RequestQueue queue = Volley
					// .newRequestQueue(context);
					// StringRequest request = new
					// StringRequest(Request.Method.POST,
					// sendCommentUrl, new Listener<String>() {
					// @Override
					// public void onResponse(String response) {
					// // TODO Auto-generated method stub
					// System.out.println(response);
					// ParseJson parseJson = new ParseJson();
					// try {
					// ActivityCode = parseJson
					// .parseActivityCode(response);
					// handler.sendEmptyMessage(HuoDongDetailsActivity.GetActivityCode);
					// handler.sendEmptyMessage(HuoDongDetailsActivity.OnSendComment);
					// } catch (Exception e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
					//
					// }, new ErrorListener() {
					//
					// @Override
					// public void onErrorResponse(VolleyError error) {
					// // TODO Auto-generated method stub
					//
					// }
					// }) {
					// @Override
					// protected Map getParams() {
					// // 在这里设置需要post的参数
					// String images = "[";
					// for (int i = 0; i < commentImgPath.size(); i++) {
					// if(i != 0){
					// images = images + ",";
					// }
					// images = images + '"' + commentImgPath.get(i) + '"';
					// }
					// images = images + "]";
					// System.out.println("images=" + images);
					// System.out.println(huodong_myComment.getText().toString());
					// Map<String, String> params = new HashMap<String,
					// String>();
					// params.put("content",
					// huodong_myComment.getText().toString());
					// params.put("images", images);
					//
					// return params;
					// }
					// };
					// queue.add(request);
				}
			}
		});
		// huodong_myCommentImg1.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// CharSequence[] items = { "相机", "本地相册" };
		// new AlertDialog.Builder(HuoDongDetailsActivity.this)
		// .setTitle("选择图片来源")
		// .setItems(items, new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int which) {
		// if (which == SELECT_PICTURE) {
		// Intent intent = new Intent(
		// Intent.ACTION_GET_CONTENT);
		// intent.addCategory(Intent.CATEGORY_OPENABLE);
		// intent.setType("image/*");
		// startActivityForResult(Intent
		// .createChooser(intent, "选择图片"),
		// SELECT_PICTURE);
		// } else {
		// // 指定拍照
		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// // 加载路径
		// Uri uri = Uri.fromFile(new File(photoPath));
		// // 指定存储路径，这样就可以保存原图了
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// // 拍照返回图片
		// startActivityForResult(intent, SELECT_CAMER);
		// }
		// }
		// }).create().show();
		// }
		// });

		getDetailsData();
		super.onCreate(savedInstanceState);
	}

	private void OnDeleteComment() {
		RefreshCommentData();
	}

	private void onSendComment() {
		// TODO Auto-generated method stub
		huodong_myComment.setText("");

		for (int i = 0; i < myCommentImgViews.length; i++) {
			myCommentImgViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "选择图片"),
							SELECT_PICTURE);
				}
			});
			myCommentImgViews[i].setImageResource(R.drawable.charutupian);
			if (i > 0) {
				myCommentImgViews[i].setVisibility(View.GONE);
			}
		}
		commentImgPath.clear();
		commentImgDatas.clear();
		RefreshCommentData();
	}

	protected void GetMoreComment() {
		// TODO Auto-generated method stub
		page++;
		final String getComment_url;
		if (commentState.equals("new")) {
			getComment_url = getString(R.string.url_huodong_comment) + "&n="
					+ sp.getString("UserName", "") + "&d="
					+ sp.getString("KEY", "") + "&actid="
					+ DetailsData.get("id") + "&size=5" + "&page=" + page;
		} else {
			getComment_url = getString(R.string.url_huodong_comment) + "&n="
					+ sp.getString("UserName", "") + "&d="
					+ sp.getString("KEY", "") + "&actid="
					+ DetailsData.get("id") + "&size=5" + "&page=" + page
					+ "&sort=" + "acs";
		}
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(getComment_url,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);
						ParseJson parseJson = new ParseJson();
						try {
							if (response.indexOf("\"data\":[]") != -1) {
								page--;
								Toast.makeText(context,
										"暂无更多数据", Toast.LENGTH_SHORT).show();
								onLoad();
							} else {
								parseJson.parseCommentList(response,
										commentData.Comment_Data);
								handler.sendEmptyMessage(HuoDongDetailsActivity.GetMoreComment);
							}
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

	protected void RefreshCommentData() {
		// TODO Auto-generated method stub
		page = 1;
		final String getComment_url;
		if (commentState.equals("new")) {
			getComment_url = getString(R.string.url_huodong_comment) + "&n="
					+ sp.getString("UserName", "") + "&d="
					+ sp.getString("KEY", "") + "&actid="
					+ DetailsData.get("id") + "&size=5" + "&page=" + page;
		} else {
			getComment_url = getString(R.string.url_huodong_comment) + "&n="
					+ sp.getString("UserName", "") + "&d="
					+ sp.getString("KEY", "") + "&actid="
					+ DetailsData.get("id") + "&size=5" + "&page=" + page
					+ "&sort=" + "acs";
		}
		System.out.println(getComment_url);
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(getComment_url,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);
						ParseJson parseJson = new ParseJson();
						commentData.Comment_Data.clear();
						try {
							parseJson.parseCommentList(response,
									commentData.Comment_Data);
							handler.sendEmptyMessage(HuoDongDetailsActivity.REFRESHComment);
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

	private void onLoad() {
		commentList.stopRefresh();
		commentList.stopLoadMore();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日    HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = formatter.format(curDate);
		commentList.setRefreshTime(time);
	}

	private void setCommentDate() {
		// TODO Auto-generated method stub
		commentAdapter = new ActivityCommentAdapter(context,
				commentData, this, DetailsData.get("id"));
		commentList.setAdapter(commentAdapter);

	}

	private void getCommentData() {
		// TODO Auto-generated method stub
		final String getComment_url;
		if (commentState.equals("new")) {
			getComment_url = getString(R.string.url_huodong_comment) + "&n="
					+ sp.getString("UserName", "") + "&d="
					+ sp.getString("KEY", "") + "&actid="
					+ DetailsData.get("id") + "&size=5" + "&page=" + page;
		} else {
			getComment_url = getString(R.string.url_huodong_comment) + "&n="
					+ sp.getString("UserName", "") + "&d="
					+ sp.getString("KEY", "") + "&actid="
					+ DetailsData.get("id") + "&size=5" + "&page=" + page
					+ "&sort=" + "acs";
		}
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(getComment_url,
				new Listener<String>() {
					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						System.out.println(response);
						ParseJson parseJson = new ParseJson();
						try {
							parseJson.parseCommentList(response,
									commentData.Comment_Data);
							if (commentData.Comment_Data.size() == 0) {
								handler.sendEmptyMessage(HuoDongDetailsActivity.NoComment);
							} else {
								handler.sendEmptyMessage(HuoDongDetailsActivity.GetComment);
							}
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

	private boolean IsTime1BeforeTime2(String time1, String time2) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(time1));
			c2.setTime(df.parse(time2));
		} catch (Exception e) {
			// TODO: handle exception
		}
		int result = c1.compareTo(c2);
		if (result >= 0) {
			// time1在time2后
			return false;
		} else {
			// time1在time2前
			return true;
		}
	}

	private void setDetailsData() {
		AnimateFirstDisplayListener aDisplayListener = new AnimateFirstDisplayListener();
		imageLoader.displayImage(DetailsData.get("cover"), fengmian, options,
				aDisplayListener);

		huodong_name.setText(DetailsData.get("title"));
		if (!DetailsData.get("class1").equals("")) {
			huodong_tag1.setVisibility(View.VISIBLE);
			huodong_tag1.setText(DetailsData.get("class1"));
		}
		begintime.setText(DetailsData.get("dtBegin") + " - "
				+ DetailsData.get("dtEnd"));
		endtime.setText("报名截止时间：" + DetailsData.get("dtEnrollEnd"));
		huodong_author.setText(DetailsData.get("unit"));

		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String nowtime = df.format(curDate);

		if (IsTime1BeforeTime2(nowtime, DetailsData.get("dtEnrollEnd"))) {
			// 在报名截止前
			if (DetailsData.get("registflag").equals("false")) {
				// 还没报名
				huodong_signup.setText("报名");
				huodong_signup.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Dialog dialog = new AlertDialog.Builder(HuoDongDetailsActivity.this)
			            .setTitle("报名")
			            .setMessage("确定报名该活动？")
			            .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
			                	String url = "http://data.iego.net:85/act/actMember.ashx?actid="
										+ DetailsData.get("id")
										+ "&type=regist&"
										+ "&n="
										+ sp.getString("UserName", "")
										+ "&d="
										+ sp.getString("KEY", "");
								RequestQueue queue = Volley
										.newRequestQueue(context);
								StringRequest request = new StringRequest(url,
										new Listener<String>() {
											@Override
											public void onResponse(String response) {
												// TODO Auto-generated method stub
												System.out.println(response);
												if (response.indexOf("regist成功") > -1) {
													Toast.makeText(
															context,
															"报名成功", Toast.LENGTH_SHORT)
															.show();
													huodong_signup.setText("已报名");
													huodong_signup.setBackgroundColor(Color
															.parseColor("#9daeba"));
													huodong_signup
															.setOnClickListener(null);
													getDetailsData();
												} else {
													Toast.makeText(
															context,
															"报名失败", Toast.LENGTH_SHORT)
															.show();
												}
											}

										}, new ErrorListener() {

											@Override
											public void onErrorResponse(
													VolleyError error) {
												// TODO Auto-generated method stub
												Toast.makeText(context,
														"请求出错，请重试", Toast.LENGTH_SHORT)
														.show();
											}
										});
								queue.add(request);
			                }
			            })//设置确定按钮
			            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
			                }
			            })//设置取消按钮
			            .create();
						dialog.show();
					}
				});
			} else if (DetailsData.get("registflag").equals("true")) {
				// 已经报名
				huodong_signup.setText("已报名");
				huodong_signup.setBackgroundColor(Color.parseColor("#9daeba"));
				huodong_signup.setOnClickListener(null);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(curDate);
				calendar.add(java.util.Calendar.HOUR_OF_DAY, 2);
				String After2Hour = df.format(calendar.getTime());
				if (!IsTime1BeforeTime2(After2Hour, DetailsData.get("dtBegin"))) {
					huodong_signup.setText("未签到");
					huodong_signup.setBackgroundColor(Color
							.parseColor("#CC79C5A1"));
					huodong_signup.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(context,
									"请去现场找相关负责人进行签到吧", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}
			}
		} else {
			// 在报名截止后
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(df.parse(DetailsData.get("dtBegin")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			calendar.add(java.util.Calendar.HOUR_OF_DAY, -2);
			String Before2Hour = df.format(calendar.getTime());
			if (IsTime1BeforeTime2(Before2Hour, nowtime)
					&& IsTime1BeforeTime2(nowtime, DetailsData.get("dtEnd"))) {
				if (DetailsData.get("signflag").equals("false")) {
					huodong_signup.setText("未签到");
					huodong_signup.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(context,
									"请去现场找相关负责人进行签到吧", Toast.LENGTH_SHORT)
									.show();
						}
					});
				} else if (DetailsData.get("signflag").equals("true")) {
					huodong_signup.setText("已签到");
					huodong_signup.setBackgroundColor(Color
							.parseColor("#9daeba"));
					huodong_signup.setOnClickListener(null);
				}

			} else if (IsTime1BeforeTime2(DetailsData.get("dtEnd"), nowtime)) {
				System.out.println(DetailsData.get("dtEnd"));
				System.out.println(nowtime);
				System.out.println(IsTime1BeforeTime2(DetailsData.get("dtEnd"),
						nowtime));
				if (DetailsData.get("signflag").equals("true")
						&& DetailsData.get("rateflag").equals("false")) {
					huodong_signup.setText("待评价");
					huodong_signup.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							CharSequence[] items = { "好评", "中评", "差评", "取消" };
							new AlertDialog.Builder(HuoDongDetailsActivity.this)
									.setTitle("评价活动")
									.setItems(
											items,
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int which) {
													String score = "";
													switch (which) {
													case 0:
														score = "10";
														break;
													case 1:
														score = "0";
														break;
													case 2:
														score = "-10";
														break;
													case 3:

														break;
													}
													if (!score.equals("")) {
														String pingjiaUrl = getString(R.string.url_huodong_act)
																+ "actid="
																+ DetailsData
																		.get("id")
																+ "&type=score"
																+ "&score="
																+ score
																+ "&n="
																+ sp.getString(
																		"UserName",
																		"")
																+ "&d="
																+ sp.getString(
																		"KEY",
																		"");
														RequestQueue queue = Volley
																.newRequestQueue(context);
														StringRequest request = new StringRequest(
																pingjiaUrl,
																new Listener<String>() {
																	@Override
																	public void onResponse(
																			String response) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		System.out
																				.println(response);
																		if (response
																				.indexOf("score成功") > -1) {
																			Toast.makeText(
																					context,
																					"评价成功",
																					Toast.LENGTH_SHORT)
																					.show();
																			huodong_shoucang
																					.setImageResource(R.drawable.yishoucang);
																		} else if (response
																				.indexOf("score重复") > -1) {
																			Toast.makeText(
																					context,
																					"你已经评价过该活动了",
																					Toast.LENGTH_SHORT)
																					.show();
																			huodong_shoucang
																					.setImageResource(R.drawable.shoucang);
																		}
																		huodong_signup
																				.setText("已评价");
																		huodong_signup
																				.setBackgroundColor(Color
																						.parseColor("#9daeba"));
																		huodong_signup
																				.setOnClickListener(null);
																		getDetailsData();
																	}

																},
																new ErrorListener() {

																	@Override
																	public void onErrorResponse(
																			VolleyError error) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		Toast.makeText(
																				context,
																				"请求出错，请重试",
																				Toast.LENGTH_SHORT)
																				.show();
																	}
																});
														queue.add(request);
													}
												}
											}).create().show();
						}
					});
				} else if (DetailsData.get("signflag").equals("true")
						&& DetailsData.get("rateflag").equals("true")) {
					huodong_signup.setText("已评价");
					huodong_signup.setBackgroundColor(Color
							.parseColor("#9daeba"));
					huodong_signup.setOnClickListener(null);
				} else {
					huodong_signup.setText("已结束");
					huodong_signup.setBackgroundColor(Color
							.parseColor("#9daeba"));
					huodong_signup.setOnClickListener(null);
				}
			}
		}

		if (DetailsData.get("credit").equals("0")) {
			xuefenbg.setVisibility(View.GONE);
			huodong_credit.setText("");
		} else {
			xuefenbg.setVisibility(View.VISIBLE);
			String credit = DetailsData.get("credit");
			if (DetailsData.get("credit").length() == 1) {
				credit = DetailsData.get("credit") + ".0";
			}
			if (DetailsData.get("credit").length() == 2) {
				credit = DetailsData.get("credit") + " ";
			}
			credit = credit + "学分";
			huodong_credit.setText(credit);
		}
		if (DetailsData.get("limit").equals("")) {
			huodong_peopleCount.setText(DetailsData.get("registerNum") + "人报名");
		} else {
			huodong_peopleCount.setText(DetailsData.get("registerNum")
					+ "人报名        " + DetailsData.get("limit") + "人限制");
		}
		huodong_access.setText("好评：" + DetailsData.get("rate1") + "    中评："
				+ DetailsData.get("rate2") + "    差评："
				+ DetailsData.get("rate3"));
		huodong_detail.setText(DetailsData.get("intro"));
		huodong_detail.setOnClickListener(new OnClickListener() {
			Boolean flag = true;

			@Override
			public void onClick(View v) {
				if (flag) {
					flag = false;
					huodong_detail.setMaxLines(Integer.MAX_VALUE);
					huodong_detail.requestLayout();
				} else {
					flag = true;
					huodong_detail.setMaxLines(5);
					huodong_detail.requestLayout();
				}
			}
		});
		if (DetailsData.get("focusflag").equals("false")) {
			huodong_shoucang.setImageResource(R.drawable.shoucang);
			focusflag = false;
		} else {
			huodong_shoucang.setImageResource(R.drawable.yishoucang);
			focusflag = true;
		}
		huodong_shoucang.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String focusUrl = "";
				if (!focusflag) {
					focusUrl = getString(R.string.url_huodong_act) + "actid="
							+ DetailsData.get("id") + "&type=focus" + "&n="
							+ sp.getString("UserName", "") + "&d="
							+ sp.getString("KEY", "");
				} else {
					focusUrl = getString(R.string.url_huodong_unfocus)
							+ "actid=" + DetailsData.get("id") + "&n="
							+ sp.getString("UserName", "") + "&d="
							+ sp.getString("KEY", "");
				}
				RequestQueue queue = Volley
						.newRequestQueue(context);
				StringRequest request = new StringRequest(focusUrl,
						new Listener<String>() {
							@Override
							public void onResponse(String response) {
								// TODO Auto-generated method stub
								System.out.println(response);
								if (response.indexOf("focus成功") > -1) {
									Toast.makeText(context,
											"关注成功", Toast.LENGTH_SHORT).show();
									huodong_shoucang
											.setImageResource(R.drawable.yishoucang);
									focusflag = true;
									Intent intent = new Intent();
									intent.setAction("action.refresh");
									sendBroadcast(intent);
								} else if (response.indexOf("focus删除成功") > -1) {
									Toast.makeText(context,
											"取消关注成功", Toast.LENGTH_SHORT)
											.show();
									huodong_shoucang
											.setImageResource(R.drawable.shoucang);
									focusflag = false;
									Intent intent = new Intent();
									intent.setAction("action.refresh");
									sendBroadcast(intent);
								}
							}

						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub
								Toast.makeText(context,
										"请求出错，请重试", Toast.LENGTH_SHORT).show();
							}
						});
				queue.add(request);
			}
		});
		huodong_signin = (ImageButton) findViewById(R.id.huodong_detail_signin);
		if (DetailsData.get("n").equals(sp.getString("UserName", ""))) {
			huodong_signin.setVisibility(View.VISIBLE);
		}
		huodong_signin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] qr_checkin = new String[] { DetailsData.get("signImg") };
				imageBrower(1, qr_checkin, "LOOK");
			}
		});
	}

	private void getDetailsData() {
		// TODO Auto-generated method stub
		RequestQueue queue = Volley.newRequestQueue(this);
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				ParseJson parseJson = new ParseJson();
				try {
					parseJson.parseHuoDongDetail(response, DetailsData,
							sp.getString("UserName", ""));
					handler.sendEmptyMessage(HuoDongDetailsActivity.GetData);
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

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("requestCode" + requestCode);
		System.out.println("resultCode" + resultCode);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SELECT_CAMER:
				System.out.println("照相");
				Uri uri = Uri.fromFile(new File(photoPath));
				final File file1 = scal(uri, commentImgDatas.size() + "");
				imageLoader.displayImage(uri.toString(),
						myCommentImgViews[commentImgPath.size()], options);
				// new Thread(){
				// @Override
				// public void run() {
				// String result = HttpUtils.uploadPic(uploadPicUrl,
				// file1.getPath());
				// ParseJson parseJson = new ParseJson();
				// try {
				// responseurl = parseJson.parseOnUpLoad(result);
				// handler.sendEmptyMessage(HuoDongDetailsActivity.GetUpLoadResult);
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// super.run();
				// }
				// }.start();
				commentImgPath.add(file1.getPath());
				commentImgDatas.add(uri.toString());
				handler.sendEmptyMessage(HuoDongDetailsActivity.GetUpLoadResult);
				break;
			case SELECT_PICTURE:
				uri = data.getData();
				ContentResolver cr = this.getContentResolver();
				Cursor c = cr.query(uri, null, null, null, null);
				c.moveToFirst();
				// 这是获取的图片保存在sdcard中的位置
				srcPath = c.getString(c.getColumnIndex("_data"));
				final File file2 = scal(Uri.fromFile(new File(srcPath)),
						commentImgDatas.size() + "");
				imageLoader.displayImage(uri.toString(),
						myCommentImgViews[commentImgPath.size()], options);
				System.out.println(srcPath + "----------保存路径2");
				// new Thread(){
				// @Override
				// public void run() {
				// String result = HttpUtils.uploadPic(uploadPicUrl,
				// file2.getPath());
				// ParseJson parseJson = new ParseJson();
				// try {
				// responseurl = parseJson.parseOnUpLoad(result);
				// handler.sendEmptyMessage(HuoDongDetailsActivity.GetUpLoadResult);
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// super.run();
				// }
				// }.start();
				commentImgPath.add(file2.getPath());
				commentImgDatas.add(uri.toString());
				handler.sendEmptyMessage(HuoDongDetailsActivity.GetUpLoadResult);
				break;
			case PIC_BROWER:
				int pos = data.getIntExtra("position", 0);
				commentImgDatas.remove(pos);
				commentImgPath.remove(pos);
				for (int i = pos; i < commentImgDatas.size(); i++) {
					imageLoader.displayImage(commentImgDatas.get(i),
							myCommentImgViews[i], options);
				}
				myCommentImgViews[commentImgDatas.size()]
						.setImageResource(R.drawable.charutupian);
				myCommentImgViews[commentImgDatas.size()]
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										Intent.ACTION_GET_CONTENT);
								intent.addCategory(Intent.CATEGORY_OPENABLE);
								intent.setType("image/*");
								startActivityForResult(
										Intent.createChooser(intent, "选择图片"),
										SELECT_PICTURE);
							}
						});
				if (commentImgDatas.size() + 1 < 5) {
					myCommentImgViews[commentImgDatas.size() + 1]
							.setVisibility(View.GONE);
				}
				break;
			}

		} else {
			// Toast.makeText(context, "请重新选择图片",
			// Toast.LENGTH_SHORT).show();
		}

	}

	private void onUpLoadComplete() {
		if (myCommentImgViews.length > commentImgDatas.size()) {
			myCommentImgViews[commentImgDatas.size()]
					.setVisibility(View.VISIBLE);
		}
//		commentImgPath.add(responseurl);
		for (int i = 0; i < commentImgDatas.size(); i++) {
			final int pos = i;
			myCommentImgViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String[] array = (String[]) commentImgDatas
							.toArray(new String[0]);
					imageBrower(pos, array, "SELECT");
				}
			});
		}

	}

	private void imageBrower(int position, String[] urls, String type) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		intent.putExtra(ImagePagerActivity.TYPE, type);
		startActivityForResult(intent, PIC_BROWER);
	}

	public static File scal(Uri fileUri, String name) {
		String path = fileUri.getPath();
		File outputFile = new File(path);
		long fileSize = outputFile.length();
		final long fileMaxSize = 200 * 1024;
		if (fileSize >= fileMaxSize) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int height = options.outHeight;
			int width = options.outWidth;

			double scale = Math.sqrt((float) fileSize / fileMaxSize);
			options.outHeight = (int) (height / scale);
			options.outWidth = (int) (width / scale);
			options.inSampleSize = (int) (scale + 0.5);
			options.inJustDecodeBounds = false;

			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			outputFile = new File(PhotoUtil.createImageFile(name).getPath());
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(outputFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			} else {
				File tempFile = outputFile;
				outputFile = new File(PhotoUtil.createImageFile(name).getPath());
				PhotoUtil.copyFileUsingFileChannels(tempFile, outputFile);
			}

		}
		return outputFile;

	}
}
