package com.hn.linzi.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.hn.linzi.views.BaseActivity;
import com.hn.linzi.R;

public class CustomMenuActivity extends BaseActivity implements OnClickListener {
	
	private Button back;
	private CheckBox zhongwai;
	private CheckBox gongkai;
	private CheckBox huodong;
//	private CheckBox zhichang;
	private TextView btn_ok;
	private SharedPreferences sp;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_menu);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		context = this;
		sp = getSharedPreferences("SP", MODE_PRIVATE);
		back = (Button) findViewById(R.id.custommenu_back);
		zhongwai = (CheckBox) findViewById(R.id.checkbox_zhongwai);
		gongkai = (CheckBox) findViewById(R.id.checkbox_gongkai);
		huodong = (CheckBox) findViewById(R.id.checkbox_huodong);
//		zhichang = (CheckBox) findViewById(R.id.checkbox_zhichang);
		btn_ok = (TextView) findViewById(R.id.custommenu_ok);
		
		back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.custommenu_back:
			finish();
			break;
		case R.id.custommenu_ok:
			//1书架	2课程	3摇一摇	4扫一扫	5资料夹	6中外	7公开	8特色资源
			String menuKey = "1-2-3-4-5";
			if (zhongwai.isChecked()) {
				menuKey = menuKey + "-6";
			}
			if (gongkai.isChecked()) {
				menuKey = menuKey + "-7";
			}
			if (huodong.isChecked()) {
				menuKey = menuKey + "-8";
			}
//			if (zhichang.isChecked()) {
//				menuKey = menuKey + "-9";
//			}
			Editor editor = sp.edit();
			editor.putString("menuKey", menuKey);
			editor.putBoolean("menuChange", true);
			editor.commit();
			finish();
			Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
