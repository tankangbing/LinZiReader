package com.hn.linzi.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hn.linzi.R;
import com.hn.linzi.utils.ParseJson;
import com.hn.linzi.views.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class XiuGaiPassword extends BaseActivity implements OnClickListener {

	private EditText password1;
	private EditText password2;
	private Button btn;
	private TelephonyManager tm;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiugai_password);
		password1 = (EditText) findViewById(R.id.chongshe1);
		password2 = (EditText) findViewById(R.id.chongshe2);
		btn = (Button) findViewById(R.id.chongshequeding);
		btn.setOnClickListener(this);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chongshequeding:
			String pw1 = password1.getText().toString();
			String pw2 = password2.getText().toString();
			if (pw1.equals(pw2) && !pw1.equals("")) {
				Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
				Matcher matcher = pattern.matcher(password1.getText()
						.toString());
				if (!matcher.matches()) {
					Toast.makeText(this, "格式不正确", Toast.LENGTH_SHORT).show();
				} else {
					// ChuShiHuaPassword();
					SharedPreferences sp = getSharedPreferences("SP",
							MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("PassWord", password1.getText()
							.toString());
					editor.putString("First", "no");
					editor.putString("DengLu", "yes");
					editor.commit();
					
					Intent intent = new Intent();
					intent.setClass(this, MainMenuActivity.class);
					startActivity(intent);
				}
			} else {
				Toast.makeText(this, "两次输入的密码不相同或为空", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	public void ChuShiHuaPassword() {
		new Thread() {

			@Override
			public void run() {
				try {
					SharedPreferences sp = getSharedPreferences("SP",
							MODE_PRIVATE);
					Editor editor = sp.edit();
					String content = "http://data.iego.net:85/users/pwd1.aspx?";
					String imei = tm.getDeviceId();

					content = content + "imei=" + imei + "&n="
							+ sp.getString("UserName", "") + "&realname="
							+ sp.getString("RealName", "") + "&unit="
							+ sp.getString("DanWei", "") + "&userpwd="
							+ password1.getText().toString();
					URL url = new URL(content);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					if (conn.getResponseCode() == 200) {
						InputStream inStream = conn.getInputStream();
						ParseJson parseJson = new ParseJson();
						String result = parseJson
								.parseChuShiHuaPassword(inStream);
						if (result.equals("0")) {
							editor.putString("PassWord", password1.getText()
									.toString());
							editor.putString("First", "no");
							editor.putString("DengLu", "yes");
							editor.commit();

							Intent intent = new Intent();
							intent.setClass(context, XiuGaiPassword.class);
							context.startActivity(intent);
						} else {
							Toast.makeText(context, "密码初始化失败",
									Toast.LENGTH_LONG).show();
						}

					}
					super.run();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}.start();
	}

}
